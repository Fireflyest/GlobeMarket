package com.fireflyest.market.core;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ItemUtils;
import com.fireflyest.market.util.SerializeUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fireflyest
 * 2021/3/26 12:56
 */

public class MarketMain implements MarketPage<Sale>{

    private final List<Inventory> pages = new ArrayList<>();
    private List<Sale> sales;

    private final static MarketMain market = new MarketMain();
    public static MarketMain getInstance() {
        return market;
    }

    private MarketMain(){
    }

    @Override
    public void initPages(List<Sale> list){
        this.sales = list;
        Inventory inv = Bukkit.createInventory(null, 54, Language.PLUGIN_NAME + Language.MARKET_MAIN_NICK + " §7#§8" + 1);
        pages.add(inv);
        this.loadPage(inv, 1);
        for (Sale sale : list) {
            this.addItem(sale);
        }
    }

    @Override
    public void notifyItemChange(Sale sale) {
        // 物品序号
        int itemIndex = sales.indexOf(sale);
        // 正顺序排位
        int rightIndex = sales.size() - 1 - itemIndex;
        // 所在页面
        int pageIndex = rightIndex/45;
        Inventory inv = pages.get(pageIndex);

        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        ItemUtils.loreSaleItem(item, sale);
        // 覆盖物品
        inv.setItem(rightIndex%45, item);
    }

    @Override
    public void addItem(Sale sale) {
        // 判断箱子是否足够
        int needInv = sales.size() / 45 + 1;
        if(pages.size() < needInv){
            Inventory inv = Bukkit.createInventory(null, 54, Language.PLUGIN_NAME + Language.MARKET_MAIN_NICK + " §7#§8" + needInv);
            pages.add(inv);
            this.loadPage(inv, needInv);
        }

        // 遍历箱子放置物品
        for (int i = 0; i < pages.size(); i++) {
            // 获取当前箱子
            int salesSize = sales.size();
            Inventory inv = pages.get(i);
            for (int j = 0; j < 45; j++) {
                int index = salesSize -1 - j - i * 45;
                if (index < 0) break;
                Sale s = sales.get(index);
                ItemStack item = SerializeUtil.deserialize(s.getStack(), s.getMeta());
                ItemUtils.loreSaleItem(item, s);
                inv.setItem(j, item);
            }
        }
    }

    @Override
    public void removeItem(Sale sale) {
        int itemIndex = sales.indexOf(sale);
        // 正顺序排位
        int rightIndex = sales.size() - 1 - itemIndex;
        // 页码
        int pageIndex = rightIndex/45;
        // 刷新起始位置
        int startIndex = rightIndex%45;

        int lastIndex = -1;
        int salesSize = sales.size();
        for (int i = pageIndex; i < pages.size(); i++) {
            // 获取当前箱子
            Inventory inv = pages.get(i);
            if (inv.firstEmpty() == 0) break;
            for (int j = (i == pageIndex ? startIndex : 0); j < 45; j++) {
                // 获取被删除的前一个物品
                int index = (salesSize -1 - j - i * 45) - 1;
                if (index < 0) {
                    lastIndex = j;
                    break;
                }
                Sale s = sales.get(index);
                ItemStack item = SerializeUtil.deserialize(s.getStack(), s.getMeta());
                ItemUtils.loreSaleItem(item, s);
                inv.setItem(j, item);
            }
        }
        // 删除最后一个物品
        pages.get((salesSize-1)/45).setItem(lastIndex, null);
    }

    /**
     *
     * @param str 页码
     * @return 页面
     */
    @Override
    public Inventory getPage(String str) {
        int index = Integer.parseInt(str)-1;
        // 最大页码
        int max = pages.size()-1;
        return max >= index ? pages.get(index) : pages.get(max);
    }

    @Override
    public void loadPage(Inventory inv, int page) {
        inv.setItem(45, MarketItem.MINE);
        inv.setItem(46, MarketItem.MAIL);
        inv.setItem(49, MarketItem.getInstance().getPageItem(page));
        inv.setItem(52, Config.ITEM_CLASSIFY ? MarketItem.CLASSIFY : MarketItem.DATA);
        inv.setItem(53, MarketItem.CLOSE);
    }

    @Override
    public void loadPage(Inventory inv, Sale sale) {}
}
