package com.fireflyest.market.core;

import com.cryptomorin.xseries.XMaterial;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ItemUtils;
import com.fireflyest.market.util.SerializeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fireflyest
 * 2021/4/16 21:54
 */

public class MarketClassify implements MarketPage<Sale> {

    private static final MarketClassify marketClassify = new MarketClassify();

    private Inventory page;
    private Inventory empty;

    public static MarketClassify getInstance() {
        return marketClassify;
    }

    private static final Map<String ,List<Inventory>> pagesMap = new HashMap<String ,List<Inventory>>(){
        {
            put("edible", new ArrayList<>());
            put("item", new ArrayList<>());
            put("block", new ArrayList<>());
            put("record", new ArrayList<>());
            put("burnable", new ArrayList<>());
            put("interactable", new ArrayList<>());
            put("solid", new ArrayList<>());
            put("equip", new ArrayList<>());
            put("knowledge", new ArrayList<>());
        }
    };
    private static final Map<String, List<Sale>> data = new HashMap<String, List<Sale>>(){
        {
            put("edible", new ArrayList<>());
            put("item", new ArrayList<>());
            put("block", new ArrayList<>());
            put("record", new ArrayList<>());
            put("burnable", new ArrayList<>());
            put("interactable", new ArrayList<>());
            put("solid", new ArrayList<>());
            put("equip", new ArrayList<>());
            put("knowledge", new ArrayList<>());
        }
    };

    private final Material exp = XMaterial.EXPERIENCE_BOTTLE.parseMaterial();

    @Override
    public void initPages(List<Sale> list) {
        for (Sale sale : list) {
            this.addItem(sale);
        }
        empty = Bukkit.createInventory(null, 9,Language.PLUGIN_NAME + " §9该分类无商品");
        empty.setItem(0, MarketItem.MARKET);
        empty.setItem(7, MarketItem.CLASSIFY);
        empty.setItem(8, MarketItem.CLOSE);

        page = Bukkit.createInventory(null, 27,Language.PLUGIN_NAME + " §9分类");
        page.setItem(0, MarketItem.EDIBLE);
        page.setItem(1, MarketItem.ITEM);
        page.setItem(2, MarketItem.BLOCK);
        page.setItem(3, MarketItem.RECORD);
        page.setItem(4, MarketItem.BURNABLE);
        page.setItem(5, MarketItem.INTERACTABLE);
        page.setItem(6, MarketItem.SOLID);
        page.setItem(7, MarketItem.EQUIP);
        page.setItem(8, MarketItem.KNOWLEDGE);
        page.setItem(18, MarketItem.MARKET);
        page.setItem(19, MarketItem.POINT);
        page.setItem(20, MarketItem.ADMIN);
        page.setItem(26, MarketItem.CLOSE);
        for (int i = 9; i < 18; i++) {
            page.setItem(i, MarketItem.BLANK);
        }
    }

    @Override
    public void notifyItemChange(Sale sale) {
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        Material material = item.getType();
        if (material.isEdible()){
            this.notifyItemChange(sale, "edible");
        }
        if(material.isItem()){
            this.notifyItemChange(sale, "item");
        }
        if(material.isBlock()){
            this.notifyItemChange(sale, "block");
        }
        if(material.isRecord()){
            this.notifyItemChange(sale, "record");
        }
        if(material.isBurnable() || material.isFlammable() || material.isFuel()){
            this.notifyItemChange(sale, "burnable");
        }
        if(material.isInteractable()){
            this.notifyItemChange(sale, "interactable");
        }
        if(material.isSolid()){
            this.notifyItemChange(sale, "solid");
        }
        if (material.getMaxDurability() > 0){
            this.notifyItemChange(sale, "equip");
        }
        if(material.name().contains("BOOK") || material.name().contains("POTION") || material.equals(exp)){
            this.notifyItemChange(sale, "knowledge");
        }
    }

    private void notifyItemChange(Sale sale, String type){
        // 计算位置
        List<Inventory> pages = pagesMap.get(type);
        List<Sale> mData = data.get(type);
        int Index = mData.indexOf(sale);
        int pageIndex = Index / 45;
        int itemIndex = Index % 45;

        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        ItemUtils.loreSaleItem(item, sale);
        pages.get(pageIndex).setItem(itemIndex, item);
    }

    @Override
    public void addItem(Sale sale) {
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        Material material = item.getType();
        if (material.isEdible()){
            this.addItem(sale, "edible", item.clone());
        }
        if(material.isItem()){
            this.addItem(sale, "item", item.clone());
        }
        if(material.isBlock()){
            this.addItem(sale, "block", item.clone());
        }
        if(material.isRecord()){
            this.addItem(sale, "record", item.clone());
        }
        if(material.isBurnable() || material.isFlammable() || material.isFuel()){
            this.addItem(sale, "burnable", item.clone());
        }
        if(material.isInteractable()){
            this.addItem(sale, "interactable", item.clone());
        }
        if(material.isSolid()){
            this.addItem(sale, "solid", item.clone());
        }
        if (material.getMaxDurability() > 0){
            this.addItem(sale, "equip", item.clone());
        }
        if(material.name().contains("BOOK") || material.name().contains("POTION") || material.equals(exp)){
            this.addItem(sale, "knowledge", item.clone());
        }
    }

    private void addItem(Sale sale, String type, ItemStack item){
        List<Sale> mData = data.get(type);
        List<Inventory> pages = pagesMap.get(type);
        mData.add(sale);

        // 计算物品位置
        int index = mData.indexOf(sale);
        int pageIndex = index / 45;
        int itemIndex = index % 45;

        // 判断是否够放
        if(pages.size() - 1 < pageIndex){
            int p = (pages.size() + 1);
            Inventory inv = Bukkit.createInventory(null, 54, Language.PLUGIN_NAME + " §9" + this.convertToCn(type)  + Language.MARKET_MINE_NICK + " §7#§8" + p);
            pages.add(inv);
            this.loadPage(inv, p, type);
        }

        // 放置物品
        ItemUtils.loreSaleItem(item, sale);
        Inventory inv = pages.get(pageIndex);
        inv.setItem(itemIndex, item);

    }

    @Override
    public void removeItem(Sale sale) {
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        Material material = item.getType();
        if (material.isEdible()){
            this.removeItem(sale, "edible");
        }
        if(material.isItem()){
            this.removeItem(sale, "item");
        }
        if(material.isBlock()){
            this.removeItem(sale, "block");
        }
        if(material.isRecord()){
            this.removeItem(sale, "record");
        }
        if(material.isBurnable() || material.isFlammable() || material.isFuel()){
            this.removeItem(sale, "burnable");
        }
        if(material.isInteractable()){
            this.removeItem(sale, "interactable");
        }
        if(material.isSolid()){
            this.removeItem(sale, "solid");
        }
        if (material.getMaxDurability() > 0){
            this.removeItem(sale, "equip");
        }
        if(material.name().contains("BOOK") || material.name().contains("POTION") || material.equals(exp)){
            this.removeItem(sale, "knowledge");
        }
    }

    private void removeItem(Sale sale, String type){
        // 计算位置
        List<Inventory> pages = pagesMap.get(type);
        List<Sale> mData = data.get(type);
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

    /**
     * 获取页面
     * @param str 分类名 页码
     * @return 箱子
     */
    @Override
    public Inventory getPage(String str) {
        if ("".equals(str)) {
            return page;
        }
        String[] strings = str.split(" ");
        String name = strings[0];
        int index = Integer.parseInt(strings[1])-1;
        //判断玩家是否有箱子列表
        if(!pagesMap.containsKey(name)){
            return Bukkit.createInventory(null, 54, " §7#§8 无此分类");
        }
        // 最大页码
        int max = pagesMap.get(name).size()-1;
        if(max < 0){
            return empty;
        }
        return index > max ? pagesMap.get(name).get(max) : pagesMap.get(name).get(index);
    }

    @Override
    public void loadPage(Inventory inv, int page) {
        inv.setItem(45, MarketItem.MARKET);
        inv.setItem(46, MarketItem.MAIL);
        inv.setItem(52, MarketItem.CLASSIFY);
        inv.setItem(53, MarketItem.CLOSE);
    }

    public void loadPage(Inventory inv, int page, String type){
        this.loadPage(inv, page);
        inv.setItem(49, MarketItem.getInstance().getPageItem("classify:" + type, page));
    }

    @Override
    public void loadPage(Inventory inv, Sale sale) {
    }

    private String convertToCn(String type){
        switch (type){
            case "edible":
                return "食物";
            case "item":
                return "物品";
            case "block":
                return "方块";
            case "record":
                return "唱片";
            case "burnable":
                return "可燃物";
            case "interactable":
                return "功能方块";
            case "solid":
                return "土壤";
            case "equip":
                return "装备";
            case "knowledge":
                return "知识";
            default:
                return "";
        }
    }

}
