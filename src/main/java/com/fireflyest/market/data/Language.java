package com.fireflyest.market.data;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Language {

    private final FileConfiguration lang;

    public static List<String>HELP = new ArrayList<>();
    public static String VERSION;
    public static String TITLE;
    public static String PLAYER_COMMAND;
    public static String RELOADING;
    public static String RELOADED;

    public static String PLUGIN_NAME;
    public static String NOT_ENOUGH_MONEY;
    public static String NOT_ENOUGH_ITEM;
    public static String NOT_ENOUGH_SPACE;
    public static String NOT_PERMISSION;
    public static String SELL_ITEM;
    public static String BUY_ITEM;
    public static String BUY_ERROR;
    public static String TYPE_ERROR;
    public static String CANCEL_ITEM;
    public static String CANCEL_ERROR;
    public static String AUCTION_ITEM;
    public static String AUCTION_FINISH;
    public static String COMMAND_ERROR;
    public static String AFFAIR_FINISH;
    public static String SIGN_FINISH;
    public static String SIGN_ERROR;
    public static String USER_ERROR;
    public static String DATA_NULL;
    public static String HAS_MAIL;
    public static String LIMIT_MAIL;

    public static String MARKET_MAIN_NICK;
    public static String MARKET_MAIL_NICK;
    public static String MARKET_MINE_NICK;
    public static String MARKET_CLASSIFY_NICK;
    public static String MARKET_MENU_NICK;

    public Language(FileConfiguration lang){
        this.lang = lang;
        this.setUp();
    }

    private void setUp(){
        VERSION = lang.getString("Version");
        HELP = lang.getStringList("Help");
        TITLE = this.parseColor(lang.getString("Title"));
        PLAYER_COMMAND = TITLE + this.parseColor(lang.getString("PlayerCommand"));
        RELOADING = TITLE + this.parseColor(lang.getString("Reloading"));
        RELOADED = TITLE + this.parseColor(lang.getString("Reloaded"));

        PLUGIN_NAME = this.parseColor(lang.getString("PluginName"));

        NOT_ENOUGH_MONEY = TITLE + this.parseColor(lang.getString("NotEnoughMoney"));
        NOT_ENOUGH_ITEM = TITLE + this.parseColor(lang.getString("NotEnoughItem"));
        NOT_ENOUGH_SPACE = TITLE + this.parseColor(lang.getString("NotEnoughSpace"));
        NOT_PERMISSION = TITLE + this.parseColor(lang.getString("NotPermission"));
        SELL_ITEM = TITLE + this.parseColor(lang.getString("SellItem"));
        BUY_ITEM = TITLE + this.parseColor(lang.getString("BuyItem"));
        BUY_ERROR = TITLE + this.parseColor(lang.getString("BuyError"));
        TYPE_ERROR = TITLE + this.parseColor(lang.getString("TypeError"));
        CANCEL_ITEM = TITLE + this.parseColor(lang.getString("CancelItem"));
        CANCEL_ERROR = TITLE + this.parseColor(lang.getString("CancelError"));
        AUCTION_ITEM = TITLE + this.parseColor(lang.getString("AuctionItem"));
        AUCTION_FINISH = TITLE + this.parseColor(lang.getString("AuctionFinish"));
        COMMAND_ERROR = TITLE + this.parseColor(lang.getString("CommandError"));
        AFFAIR_FINISH = TITLE + this.parseColor(lang.getString("AffairFinish"));
        SIGN_FINISH = TITLE + this.parseColor(lang.getString("SignFinish"));
        SIGN_ERROR = TITLE + this.parseColor(lang.getString("SignError"));
        USER_ERROR = TITLE + this.parseColor(lang.getString("UserError"));
        DATA_NULL = TITLE + this.parseColor(lang.getString("DataNull"));
        HAS_MAIL = TITLE + this.parseColor(lang.getString("HasMail"));
        LIMIT_MAIL = TITLE + this.parseColor(lang.getString("LimitMail"));

        MARKET_MAIN_NICK = this.parseColor(lang.getString("MarketMainNick"));
        MARKET_MAIL_NICK = this.parseColor(lang.getString("MarketMailNick"));
        MARKET_MINE_NICK = this.parseColor(lang.getString("MarketMineNick"));
        MARKET_CLASSIFY_NICK = this.parseColor(lang.getString("MarketClassifyNick"));
        MARKET_MENU_NICK = this.parseColor(lang.getString("MarketMenuNick"));
    }

    private String parseColor(String str){
        if(null == str)return " ";
        return str.replace("&", "§");
    }

}
