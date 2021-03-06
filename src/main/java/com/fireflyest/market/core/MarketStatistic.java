package com.fireflyest.market.core;

import com.fireflyest.market.bean.Note;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.data.DataManager;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.TimeUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MarketStatistic {

    private final static MarketStatistic marketStatistic = new MarketStatistic();

    private DataManager dataManager;
    private MarketManager marketManager;
    private MarketMine marketMine;

    public static MarketStatistic getInstance() {
        return marketStatistic;
    }

    private MarketStatistic(){
    }

    public void init(){
        dataManager = DataManager.getInstance();
        marketManager = MarketManager.getInstance();
        marketMine = MarketMine.getInstance();
    }

    public void statisticMarket(Player player){
        Note note = dataManager.getTodayNote();
        player.sendMessage("§e§m =                                             = ");
        player.sendMessage(Language.TITLE+"§f只显示一天之内的交易数据");
        player.sendMessage("§3总交易金额§7: §f"+note.getMoney());
        player.sendMessage("§3总交易数量§7: §f"+note.getAmount());
        player.sendMessage("§3最高交易金额§7: §f"+note.getMax());
        player.sendMessage("§e§m =                                             = ");
    }

    public void statisticSale(Player player, int id){
        Sale sale = marketManager.getSale(id);
        if(sale == null){
            player.sendMessage(Language.DATA_NULL);
            return;
        }
        player.sendMessage("§e§m =                                             = ");
        player.sendMessage(Language.TITLE+"§f商品信息");
        player.sendMessage("§3卖家§7: §f"+sale.getOwner());
        player.sendMessage("§3买家§7: §f"+sale.getBuyer());
        player.sendMessage("§3原价§7: §f"+sale.getPrice());
        player.sendMessage("§3现价§7: §f"+sale.getCost());
        player.sendMessage("§3拍卖§7: §f"+sale.isAuction());
        player.sendMessage("§3热度§7: §f"+sale.getHeat());
        player.sendMessage("§e§m =                                             = ");
    }

    public void statisticPlayer(Player player, String name){
        User user = dataManager.getUser(name);
        if("".equals(user.getUuid())){
            player.sendMessage(Language.USER_ERROR);
        }else {
            player.sendMessage("§e§m =                                             = ");
            player.sendMessage(Language.TITLE+"§f玩家§3"+user.getName()+"§f注册于"+ TimeUtils.getTime(user.getRegister()));
            player.sendMessage("§3信誉度§7: §f"+user.getCredit());
            player.sendMessage("§3黑名单§7: §f"+user.isBlack());
            player.sendMessage("§3在出售§7: §f"+user.getSelling());
            player.sendMessage("§3累计交易数量§7: §f"+user.getAmount());
            player.sendMessage("§3累计交易金额§7: §f"+user.getMoney());
            player.sendMessage("§f输入§3: §3/market data §f查看目前商品信息");
            player.sendMessage("§e§m =                                             = ");
        }
    }

    public void statisticData(Player player){
        List<Sale> sales = marketMine.getPlayerSales(player.getName());

        if(sales.size() == 0){
            player.sendMessage(Language.DATA_NULL);
        }else {
            player.closeInventory();
            List<Integer> list = new ArrayList<>();
            int amount = 0;
            double cost = 0;
            for(Sale sale : sales){
                cost+=sale.getCost();
                amount++;
                list.add(sale.getId());
            }
            player.sendMessage("§e§m =                                             = ");
            player.sendMessage(Language.TITLE+"§f这是你目前市场交易数据");
            player.sendMessage("§3商品总金额§7: §f"+cost);
            player.sendMessage("§3商品数量§7: §f"+amount);
            player.sendMessage("§3商品编号§7: §f"+list);
            player.sendMessage("§e§m =                                             = ");
        }
    }

}
