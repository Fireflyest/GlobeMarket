package com.fireflyest.market.core;

import com.fireflyest.market.bean.Mail;
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
 * 2021/3/31 17:16
 */

public class MarketMail implements MarketPage<Mail>{

    private static final Map<String, List<Inventory>> pagesMap = new HashMap<>();
    private static final Map<String, List<Mail>> data = new HashMap<>();

    private static final MarketMail mail = new MarketMail();

    public static MarketMail getInstance() {
        return mail;
    }

    @Override
    public void initPages(List<Mail> list) {
        for (Mail mail : list) {
            this.addItem(mail);
        }
    }

    @Override
    public void notifyItemChange(Mail mail) {
    }

    @Override
    public void addItem(Mail mail) {
        String name = mail.getOwner();
        // 判断是否有创建
        if (!pagesMap.containsKey(name)){
            // 放物品
            List<Inventory> pages = new ArrayList<>();
            Inventory inv = Bukkit.createInventory(null, 54, Language.PLUGIN_NAME + " §7#§8" + 1);
            pages.add(inv);
            pagesMap.put(name, pages);
            this.loadPage(inv, 1);
        }
        if (!data.containsKey(name)) {
            // 新建数据列表
            data.put(name, new ArrayList<>());
        }

        List<Mail> mData = data.get(name);
        List<Inventory> pages = pagesMap.get(name);
        mData.add(mail);

        // 计算物品位置
        int index = mData.indexOf(mail);
        int pageIndex = index / 45;
        int itemIndex = index % 45;

        // 判断是否够放
        if(pages.size() - 1 < pageIndex){
            int p = (pages.size() + 1);
            Inventory inv = Bukkit.createInventory(null, 54, Language.PLUGIN_NAME + Language.MARKET_MAIL_NICK + " §7#§8" + p);
            pages.add(inv);
            this.loadPage(inv, p);
        }

        // 放置物品
        ItemStack item = SerializeUtil.deserialize(mail.getStack(), mail.getMeta());
        ItemUtils.loreMailItem(item, mail);
        Inventory inv = pages.get(pageIndex);
        inv.setItem(itemIndex, item);
    }

    @Override
    public void removeItem(Mail mail) {
        String name = mail.getOwner();

        // 计算位置
        List<Inventory> pages = pagesMap.get(name);
        List<Mail> mData = data.get(name);
        int Index = mData.indexOf(mail);
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
                Mail m = mData.get(k);
                ItemStack item = SerializeUtil.deserialize(m.getStack(), m.getMeta());
                ItemUtils.loreMailItem(item, m);
                inv.setItem(j, item);
            }
        }
        mData.remove(mail);
        // 删除最后一个物品
        pages.get((dataSize-1)/45).setItem(lastIndex, null);

    }

    /**
     *
     * @param str 游戏名 页码
     * @return 页面
     */
    @Override
    public Inventory getPage(String str) {
        String[] strings = str.split(" ");
        String name = strings[0];
        int index = Integer.parseInt(strings[1])-1;
        //判断玩家是否有箱子列表
        if(!pagesMap.containsKey(name)){
            List<Inventory> page = new ArrayList<>();
            page.add(Bukkit.createInventory(null, 54, Language.PLUGIN_NAME + Language.MARKET_MAIL_NICK + " §7#§8" + 1));
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
        inv.setItem(46, MarketItem.SIGN);
        inv.setItem(49, MarketItem.getInstance().getPageItem("mail", page));
        inv.setItem(52, MarketItem.DATA);
        inv.setItem(53, MarketItem.CLOSE);
    }

    @Override
    public void loadPage(Inventory inv, Mail mail) {}

    /**
     * 判断是否有邮件
     * @param name 名称
     * @return 是否有邮件
     */
    public int getMailAmount(String name){
        if(!data.containsKey(name)) return 0;
        return data.get(name).size();
    }

}
