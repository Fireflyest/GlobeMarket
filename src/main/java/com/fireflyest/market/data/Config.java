package com.fireflyest.market.data;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public FileConfiguration getConfig() {
        return config;
    }

    private final FileConfiguration config;

    public static boolean DEBUG;

    public static String VERSION;
    public static boolean SQL;
    public static String URL;
    public static String USER;
    public static String PASSWORD;
    public static int LIMIT_TIME;
    public static boolean LIMIT_LORE;
    public static boolean LIMIT_AMOUNT;
    public static int LIMIT_AMOUNT_NUM;
    public static int LIMIT_AMOUNT_NUM_VIP;
    public static int MAX_PRICE;
    public static boolean SELL_BROADCAST;
    public static boolean LIMIT_MAIL;
    public static int LIMIT_MAIL_NUM;
    public static List<String> LIMIT_LORE_LIST;

    public static boolean ITEM_CLASSIFY;

    public Config(FileConfiguration config){
        this.config = config;
        this.setUp();
    }

    private void setUp(){
        DEBUG = config.getBoolean("Debug", false);

        VERSION = config.getString("Version");
        SQL = config.getBoolean("Sql");
        URL = config.getString("Url");
        USER = config.getString("User");
        PASSWORD = config.getString("Password");
        LIMIT_TIME = config.getInt("LimitTime");

        LIMIT_LORE = config.getBoolean("LimitLore", false);
        if(LIMIT_LORE){
            LIMIT_LORE_LIST = config.getStringList("LimitLoreList");
        }else {
            LIMIT_LORE_LIST = new ArrayList<>();
        }

        LIMIT_AMOUNT = config.getBoolean("LimitAmount", false);
        if(LIMIT_AMOUNT){
            LIMIT_AMOUNT_NUM = config.getInt("LimitAmountNum", 30);
            LIMIT_AMOUNT_NUM_VIP = config.getInt("LimitAmountNumVIP", 50);
        }else {
            LIMIT_AMOUNT_NUM = 999;
        }

        MAX_PRICE = config.getInt("MaxPrice", 9999999);

        SELL_BROADCAST = config.getBoolean("SellBroadcast", false);

        LIMIT_MAIL = config.getBoolean("LimitMail", false);
        LIMIT_MAIL_NUM = config.getInt("LimitMailNum", 30);

        ITEM_CLASSIFY = config.getBoolean("ItemClassify", false);
    }

    public void setKey(String key, Object value) {
        config.set(key, value);
    }

}
