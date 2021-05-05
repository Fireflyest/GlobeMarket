package com.fireflyest.market.core;

import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ItemUtils;
import com.fireflyest.market.util.SerializeUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fireflyest
 * 2021/4/12 18:39
 */

public class MarketMine implements MarketPage<Sale>{

    private static final Map<String, List<Inventory>> pagesMap = new HashMap<>();
    private static final Map<String, List<Sale>> data = new HashMap<>();

    private static final MarketMine mine = new MarketMine();

    public static MarketMine getInstance() {
        return mine;
    }


    @Override
    public void initPages(List<Sale> list) {
        for (Sale sale : list) {
            this.addItem(sale);
        }
    }

    @Override
    public void notifyItemChange(Sale sale) {
        String name = sale.getOwner();

        // 计算位置
        List<Inventory> pages = pagesMap.get(name);
        List<Sale> mData = data.get(name);
        int Index = mData.indexOf(sale);
        int pageIndex = Index / 45;
        int itemIndex = Index % 45;

        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        ItemUtils.loreSaleItem(item, sale);
        pages.get(pageIndex).setItem(itemIndex, item);
    }

    @Override
    public void addItem(Sale sale) {
        String name = sale.getOwner();
        // 判断是否有创建
        if (!pagesMap.containsKey(name)){
            // 放物品
            List<Inventory> pages = new ArrayList<>();
            Inventory inv = Bukkit.createInventory(null, 54, Language.PLUGIN_NAME + " §9" + name  + Language.MARKET_MINE_NICK + " §7#§8" + 1);
            pages.add(inv);
            pagesMap.put(name, pages);
            this.loadPage(inv, 1);
        }
        if (!data.containsKey(name)) {
            // 新建数据列表
            data.put(name, new ArrayList<>());
        }

        List<Sale> mData = data.get(name);
        List<Inventory> pages = pagesMap.get(name);
        mData.add(sale);

        // 计算物品位置
        int index = mData.indexOf(sale);
        int pageIndex = index / 45;
        int itemIndex = index % 45;

        // 判断是否够放
        if(pages.size() - 1 < pageIndex){
            int p = (pages.size() + 1);
            Inventory inv = Bukkit.createInventory(null, 54, Language.PLUGIN_NAME + " §9" + name  + Language.MARKET_MINE_NICK + " §7#§8" + p);
            pages.add(inv);
            this.loadPage(inv, p);
        }

        // 放置物品
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        ItemUtils.loreSaleItem(item, sale);
        Inventory inv = pages.get(pageIndex);
        inv.setItem(itemIndex, item);
    }

    @Override
    public void removeItem(Sale sale) {
        String name = sale.getOwner();

        // 计算位置
        List<Inventory> pages = pagesMap.get(name);
        List<Sale> mData = data.get(name);
        int Index = mData.indexOf(sale);
        int pageIndex = Index / 45;
        int itemIndex = Index % 45;

        int lastIndex = -1;
        int dataSize = mData.size();
        for (int i = pageIndex; i < pages.size(); i++) {
            // 获取当前箱子
            Inventory inv = pages.get(i);
            if (inv.firstEmpty() == 0) break;
            for (int j = (i == pageIndex ? itemIndex : 0); j < 45; j++) {
                // 获取
                int k = (i * 45 + j) + 1;
                if (k == mData.size()){
                    lastIndex = j;
                    break;
                }
                // 放置物品
                Sale s = mData.get(k);
                ItemStack item = SerializeUtil.deserialize(s.getStack(), s.getMeta());
                ItemUtils.loreSaleItem(item, s);
                inv.setItem(j, item);
            }
        }
        mData.remove(sale);
        // 删除最后一个物品
        pages.get((dataSize-1)/45).setItem(lastIndex, null);
    }

    @Override
    public Inventory getPage(String str) {
        String[] strings = str.split(" ");
        String name = strings[0];
        int index = Integer.parseInt(strings[1])-1;
        //判断玩家是否有箱子列表
        if(!pagesMap.containsKey(name)){
            List<Inventory> page = new ArrayList<>();
            page.add(Bukkit.createInventory(null, 54, Language.PLUGIN_NAME + " §9" + name  + Language.MARKET_MINE_NICK + " §7#§8" + 1));
            pagesMap.put(name, page);
            this.loadPage(page.get(0), 1);
        }
        if (!data.containsKey(name)) {
            // 新建数据列表
            data.put(name, new ArrayList<>());
        }

        // 最大页码
        int max = pagesMap.get(name).size()-1;
        return index > max ? pagesMap.get(name).get(max) : pagesMap.get(name).get(index);
    }

    @Override
    public void loadPage(Inventory inv, int page) {
        inv.setItem(45, MarketItem.MARKET);
        inv.setItem(46, MarketItem.MAIL);
        inv.setItem(49, MarketItem.getInstance().getPageItem("mine", page));
        inv.setItem(52, MarketItem.DATA);
        inv.setItem(53, MarketItem.CLOSE);
    }

    @Override
    public void loadPage(Inventory inv, Sale sale) {

    }

    public List<Sale> getPlayerSales(String name){
        return data.get(name);
    }

}
