package com.fireflyest.market.core;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Fireflyest
 * 2021/3/27 21:43
 */

public class MarketAffair implements MarketPage<Sale>, MarketInteract{

    private final Map<Integer, Inventory> affairs = new HashMap<>();

    private MarketAffair(){}

    private MarketHandler marketHandler;

    private static final MarketAffair marketAffair = new MarketAffair();

    private Inventory empty;

    public static MarketAffair getInstance() {
        return marketAffair;
    }

    @Override
    public void initPages(List<Sale> list) {

        marketHandler = MarketHandler.getInstance();

        for (Sale sale : list) {
            this.addItem(sale);
        }

        empty = Bukkit.createInventory(null, 9,Language.PLUGIN_NAME + " §9商品不存在");
        empty.setItem(0, MarketItem.MARKET);
        empty.setItem(7, MarketItem.CLASSIFY);
        empty.setItem(8, MarketItem.CLOSE);
    }

    @Override
    public void notifyItemChange(Sale sale) {
        Inventory inv = affairs.get(sale.getId());
        loadAffairButton(inv, sale);
    }

    @Override
    public void addItem(Sale sale) {
        Inventory inv = Bukkit.createInventory(null, 27, Language.PLUGIN_NAME+" §9§l" + sale.getInfo());
        affairs.put(sale.getId(), inv);
        this.loadPage(inv, sale);
    }

    @Override
    public void removeItem(Sale sale) {
        Inventory inventory = affairs.get(sale.getId());
        inventory.setItem(10, null);
        inventory.setItem(13, null);
        inventory.setItem(14, null);
        inventory.setItem(15, null);
        inventory.setItem(17, null);
        affairs.remove(sale.getId());
    }

    @Override
    public Inventory getPage(String str) {
        return affairs.getOrDefault(ConvertUtils.parseInt(str), empty);
    }

    @Override
    public void loadPage(Inventory inv, int page) {
    }

    @Override
    public void loadPage(Inventory inv, Sale sale) {
        this.loadNormalButton(inv, sale);
        this.loadAffairButton(inv, sale);
    }

    private void loadNormalButton(Inventory inv, Sale sale){
        inv.setItem(0, MarketItem.BLANK);
        inv.setItem(1, MarketItem.BLANK);
        inv.setItem(2, MarketItem.BLANK);
        inv.setItem(9, MarketItem.BLANK);
        inv.setItem(11, MarketItem.BLANK);
        inv.setItem(18, MarketItem.BLANK);
        inv.setItem(19, MarketItem.BLANK);
        inv.setItem(20, MarketItem.BLANK);

        inv.setItem(26, MarketItem.MARKET.clone());

        ItemStack other = new ItemStack(MarketItem.MINE.getType());
        ItemUtils.setDisplayName(other, "§e§l个人商店");
        ItemUtils.addLore(other, "§f点击查看该商品主人的商店");
        ItemUtils.setItemValue(other, "other "+sale.getOwner());
        inv.setItem(8, other);
    }

    private void loadAffairButton(Inventory inv, Sale sale){
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        inv.setItem(10, item);

        ItemStack cancel;
        if(sale.isAuction() && sale.getPrice() != sale.getCost()){
            cancel = MarketItem.BOOK.clone();
            ItemUtils.setDisplayName(cancel, "§e§l成交");
            ItemUtils.addLore(cancel, "§3§l售价§7:§f "+sale.getCost());
            ItemUtils.setItemValue(cancel, "finish "+sale.getId());
        }else {
            cancel = MarketItem.CANCEL.clone();
            ItemUtils.setDisplayName(cancel, "§c§l下架");
            ItemUtils.addLore(cancel, "§3§l卖家§7:§f "+sale.getOwner());
            ItemUtils.addLore(cancel, "§3§l上架于§7:§f "+ TimeUtils.getTime(sale.getAppear()));
            ItemUtils.setItemValue(cancel, "cancel "+sale.getId());
        }
        inv.setItem(17, cancel);

        // 添加交易操作按钮
        if(sale.isAuction()){
            ItemStack add1 = MarketItem.ADD_NUGGET.clone();
            ItemUtils.setDisplayName(add1, "§e§l加价");
            ItemUtils.addLore(add1, "§3§l增加§7:§f 10");
            ItemUtils.addLore(add1, "§3§l当前§7:§f "+sale.getCost());
            ItemUtils.setItemValue(add1, "add "+sale.getId()+" 10");
            inv.setItem(13, add1);

            ItemStack add2 = MarketItem.ADD_INGOT.clone();
            ItemUtils.setDisplayName(add2, "§e§l加价");
            ItemUtils.addLore(add2, "§3§l增加§7:§f 100");
            ItemUtils.addLore(add2, "§3§l当前§7:§f "+sale.getCost());
            ItemUtils.setItemValue(add2, "add "+sale.getId()+" 100");
            inv.setItem(14, add2);

            ItemStack add3 = MarketItem.ADD_BLOCK.clone();
            ItemUtils.setDisplayName(add3, "§e§l加价");
            ItemUtils.addLore(add3, "§3§l增加§7:§f 1000");
            ItemUtils.addLore(add3, "§3§l当前§7:§f "+sale.getCost());
            ItemUtils.setItemValue(add3, "add "+sale.getId()+" 1000");
            inv.setItem(15, add3);
        }else {
            int amount = item.getAmount();
            ItemStack buy1 = MarketItem.BOOK.clone();
            buy1.setAmount(1);
            ItemUtils.setDisplayName(buy1, "§e§l单件");
            ItemUtils.addLore(buy1, "§3§l价格§7:§f "+ ConvertUtils.formatDouble(sale.getCost()/amount));
            ItemUtils.setItemValue(buy1, "buy "+sale.getId()+" 1");
            inv.setItem(13, buy1);
            if(amount > 8){
                ItemStack buy2 = MarketItem.BOOK.clone();
                buy2.setAmount(8);
                ItemUtils.setDisplayName(buy2, "§e§l部分");
                ItemUtils.addLore(buy2, "§3§l价格§7:§f "+ ConvertUtils.formatDouble(sale.getCost()/amount * 8));
                ItemUtils.setItemValue(buy2, "buy "+sale.getId()+" 8");
                inv.setItem(14, buy2);
            }
            ItemStack buy3 = MarketItem.BOOK.clone();
            buy3.setAmount(amount);
            ItemUtils.setDisplayName(buy3, "§e§l一口价");
            ItemUtils.addLore(buy3, "§3§l价格§7:§f "+ ConvertUtils.formatDouble(sale.getCost()));
            ItemUtils.setItemValue(buy3, "buy "+sale.getId());
            inv.setItem(15, buy3);
        }
    }


    @Override
    public void affairBuy(Player player, int id, int amount) {
        marketHandler.obtainBuyTask(MarketTask.BUY, player, id, amount).sendToTarget();
    }

    @Override
    public void affairCancel(Player player, int id) {
        marketHandler.obtainTask(MarketTask.CANCEL, player, id).sendToTarget();
    }

    @Override
    public void affairAuction(Player player, int id, int add) {
        marketHandler.obtainBuyTask(MarketTask.AUCTION, player, id, add).sendToTarget();
    }

    @Override
    public void affairSell(String seller, boolean auction, boolean point, double price, ItemStack item) {
        marketHandler.obtainSellTask(seller, auction, point, price, item).sendToTarget();
    }

    @Override
    public void affairSign(Player player, int id) {
        marketHandler.obtainTask(MarketTask.SIGN, player, id).sendToTarget();
    }

    @Override
    public void affairSend(String to, ItemStack item) {
        marketHandler.obtainMailTask(to, item).sendToTarget();
    }

    @Override
    public void affairFinish(Player player, int id) {
        marketHandler.obtainTask(MarketTask.FINISH, player, id).sendToTarget();
    }

    @Override
    public void affairDiscount(Player player, int id, int discount) {
        marketHandler.obtainDiscountTask(player, id, discount).sendToTarget();
    }

    @Override
    public void affairReprice(Player player, int id, double price) {
        marketHandler.obtainRepriceTask(player, id, price).sendToTarget();
    }

    @Override
    public void affairSignAll(Player player) {
        marketHandler.obtainSignTask(player).sendToTarget();
    }
}
