package com.fireflyest.market.command;

import com.cryptomorin.xseries.XMaterial;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.*;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.DataManager;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ConvertUtils;
import com.fireflyest.market.util.YamlUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MarketCommand implements CommandExecutor {

    private boolean enable = false;

    private DataManager dataManager;

    private MarketMain marketMain;
    private MarketMail marketmail;
    private MarketAffair marketAffair;
    private MarketMine marketMine;
    private MarketClassify marketClassify;
    private MarketStatistic marketStatistic;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!label.equalsIgnoreCase("market")) return true;
        if (!enable) {
            dataManager = DataManager.getInstance();

            marketMain = MarketMain.getInstance();
            marketAffair = MarketAffair.getInstance();
            marketmail = MarketMail.getInstance();
            marketMine = MarketMine.getInstance();
            marketClassify = MarketClassify.getInstance();
            marketStatistic = MarketStatistic.getInstance();
            enable = true;
        }
        switch (args.length){
            case 1:
                this.executeCommand(sender, args[0]);
                break;
            case 2:
                this.executeCommand(sender, args[0], args[1]);
                break;
            case 3:
                this.executeCommand(sender, args[0], args[1], args[2]);
                break;
            default:
                this.executeCommand(sender);
                break;
        }
        return true;
    }


    private void executeCommand(CommandSender sender){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if (player != null) {
            player.openInventory(marketMain.getPage("1"));
        }else {
            sender.sendMessage(Language.PLAYER_COMMAND);
        }
    }

    /**
     * 无参指令
     * @param sender 发送者
     * @param var1 指令类型
     */
    private void executeCommand(CommandSender sender, String var1){
        Player player = (sender instanceof Player)? (Player)sender : null;
        switch (var1){
            case "help":
                for(String msg : Language.HELP){
                    sender.sendMessage(msg.replace("&", "§"));
                }
                break;
            case "reload":
                if(!sender.isOp())return;
                sender.sendMessage(Language.RELOADING);
                YamlUtils.loadConfig();
                sender.sendMessage(Language.RELOADED);
                break;
            case "test":
                if (player != null && Config.DEBUG){
                    for (int i = 0; i < 60; i++) {
                        player.performCommand(String.format( "market sell %d 1", i));
                    }
                }
                break;
            case "mine":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                player.openInventory(marketMine.getPage(String.format("%s %d", player.getName(), 1)));
                break;
            case "close":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                player.closeInventory();
                break;
            case "data":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                marketStatistic.statisticData(player);
                break;
            case "mail":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                player.openInventory(marketmail.getPage(String.format("%s %d", player.getName(), 1)));
                break;
            case "statistic":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                if(!player.hasPermission("market.statistic")){
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                marketStatistic.statisticMarket(player);
                break;
            case "classify":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                if(!Config.ITEM_CLASSIFY){
                    player.sendMessage(Language.COMMAND_ERROR);
                    return;
                }
                player.openInventory(marketClassify.getPage(""));
                break;
            case "sign":
                marketAffair.affairSignAll(player);
                break;
            default:
                sender.sendMessage(Language.COMMAND_ERROR);
        }
    }

    /**
     * 单参数指令
     * @param sender 发送者
     * @param var1 指令类型
     * @param var2 参数
     */
    private void executeCommand(CommandSender sender, String var1, String var2){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        switch (var1){
            case "point":
                // TODO: 2021/5/5 point
                sender.sendMessage("功能未完成");
                break;
            case "sell":
            case "auction":
            case "send":
                String var3 = String.valueOf(player.getInventory().getItemInMainHand().getAmount());
                this.executeCommand(sender, var1, var2, var3);
                break;
            case "classify":
                if(!Config.ITEM_CLASSIFY){
                    player.sendMessage(Language.COMMAND_ERROR);
                    return;
                }
                player.openInventory(marketClassify.getPage(String.format("%s %d", var2, 1)));
                break;
            case "affair":
                player.openInventory(marketAffair.getPage(var2));
                break;
            case "buy":
                marketAffair.affairBuy(player, ConvertUtils.parseInt(var2), 0);
                break;
            case "sign":
                marketAffair.affairSign(player, ConvertUtils.parseInt(var2));
                break;
            case "cancel":
                marketAffair.affairCancel(player, ConvertUtils.parseInt(var2));
                player.closeInventory();
                break;
            case "finish":
                marketAffair.affairFinish(player, ConvertUtils.parseInt(var2));
                break;
            case "statistic":
                if(!player.hasPermission("market.statistic")){
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                int target = ConvertUtils.parseInt(var2);
                if(target == 0){
                    marketStatistic.statisticPlayer(player, var2);
                }else {
                    marketStatistic.statisticSale(player, target);
                }
                break;
            case "other":
                if(!player.hasPermission("market.other")){
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                player.openInventory(marketMine.getPage(String.format("%s %d", var2, 1)));
                break;
            default:
                sender.sendMessage(Language.COMMAND_ERROR);
        }
    }

    /**
     * 双参数指令
     * @param sender 发送者
     * @param var1 指令类型
     * @param var2 第一个参数
     * @param var3 第二个参数
     */
    private void executeCommand(CommandSender sender, String var1, String var2, String var3){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        switch (var1){
            case "send":
                int sendAmount = ConvertUtils.parseInt(var3);
                if(sendAmount <= 0 || sendAmount >64){
                    player.sendMessage(Language.COMMAND_ERROR);
                    return;
                }
                ItemStack sendItem = player.getInventory().getItemInMainHand();
                int sendHas = sendItem.getAmount();
                if(sendAmount > sendHas){
                    player.sendMessage(Language.NOT_ENOUGH_ITEM);
                    return;
                }else {
                    ItemStack saleItem = sendItem.clone();
                    saleItem.setAmount(sendAmount);
                    sendItem.setAmount(sendHas - sendAmount);
                    marketAffair.affairSend(var2, saleItem);
                }
                break;
            case "add":
                // 拍卖加价
                int id = ConvertUtils.parseInt(var2);
                int add = ConvertUtils.parseInt(var3);
                if(add <= 0){
                    player.sendMessage(Language.COMMAND_ERROR);
                    return;
                }
                marketAffair.affairAuction(player, id, add);
                break;
            case "discount":
                if(!player.hasPermission("market.discount")){
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                marketAffair.affairDiscount(player, ConvertUtils.parseInt(var2), ConvertUtils.parseInt(var3));
                player.sendMessage(Language.TITLE+ String.format("商品成功打§3%s§f折", var3));
                break;
            case "point":
                // TODO: 2021/5/5 point
                sender.sendMessage("功能未完成");
                break;
            case "sell":
            case "auction":
                int price = ConvertUtils.parseInt(var2), amount = ConvertUtils.parseInt(var3);
                if(price <= 0 || price > Config.MAX_PRICE){
                    player.sendMessage(Language.COMMAND_ERROR+" §3物品价格设置错误§f！");
                    return;
                }
                if(amount <= 0 || amount > 64){
                    player.sendMessage(Language.COMMAND_ERROR+" §3物品数量设置错误§f！");
                    return;
                }
                ItemStack item = player.getInventory().getItemInMainHand();
                // 判断最大数量
                if(Config.LIMIT_AMOUNT){
                    User user = dataManager.getUser(player.getName());
                    int limit = player.hasPermission("market.vip") ? Config.LIMIT_AMOUNT_NUM_VIP : Config.LIMIT_AMOUNT_NUM;
                    if(user.getSelling() >= limit){
                        player.sendMessage(Language.NOT_ENOUGH_SPACE);
                        return;
                    }
                }
                //判断违规上架
                if(Config.LIMIT_LORE){
                    ItemMeta meta = item.getItemMeta();
                    if(null != meta){
                        List<String> lores = meta.getLore();
                        if(null != lores){
                            for(String lore:Config.LIMIT_LORE_LIST){
                                if(lores.contains(lore)){
                                    player.sendMessage(Language.TYPE_ERROR);
                                    return;
                                }
                            }
                        }
                    }
                }
                // 禁止交易记录
                if (item.getType().equals(XMaterial.WRITTEN_BOOK.parseMaterial())){
                    player.sendMessage(Language.TYPE_ERROR);
                    return;
                }
                int has = item.getAmount();
                if(amount > has){
                    player.sendMessage(Language.NOT_ENOUGH_ITEM);
                    return;
                }else {
                    ItemStack saleItem = item.clone();
                    saleItem.setAmount(amount);
                    if(var1.equals("sell") && sender.hasPermission("market.sell")){
                        marketAffair.affairSell(player.getName(), false, false, price, saleItem);
                        item.setAmount(has - amount);
                        player.sendMessage(Language.SELL_ITEM);
                        return;
                    }else if(var1.equals("auction") && sender.hasPermission("market.auction")){
                        marketAffair.affairSell(player.getName(), true, false, price, saleItem);
                        item.setAmount(has - amount);
                        player.sendMessage(Language.SELL_ITEM);
                        return;
                    }else if(var1.equals("point") && sender.hasPermission("market.point")){
                        marketAffair.affairSell(player.getName(), false, true, price, saleItem);
                        item.setAmount(has - amount);
                        player.sendMessage(Language.SELL_ITEM);
                        return;
                    } else {
                        player.sendMessage(Language.NOT_PERMISSION);
                    }
                }
                break;
            case "reprice":
                if(!player.hasPermission("market.reprice")){
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                marketAffair.affairReprice(player, ConvertUtils.parseInt(var2), ConvertUtils.parseInt(var3));
                player.sendMessage(Language.TITLE+ String.format("商品修改价格为 §3%s§f", var3));
                break;
            case "buy":
                marketAffair.affairBuy(player, ConvertUtils.parseInt(var2), ConvertUtils.parseInt(var3));
                break;
            default:
                sender.sendMessage(Language.COMMAND_ERROR);
        }
    }

}
