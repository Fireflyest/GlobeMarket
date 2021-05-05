package com.fireflyest.market.core;

import com.cryptomorin.xseries.XMaterial;
import com.fireflyest.market.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MarketItem {

    private final static MarketItem marketItem = new MarketItem();

    public static MarketItem getInstance() {
        return marketItem;
    }

    public static ItemStack MINE;
    public static ItemStack MARKET;
    public static ItemStack DATA;
    public static ItemStack CLASSIFY;
    public static ItemStack SIGN;
    public static ItemStack MAIL;
    public static ItemStack MENU;
    public static ItemStack CLOSE;
    public static ItemStack BLANK;
    public static ItemStack BOOK;
    public static ItemStack CANCEL;
    public static ItemStack ADD;
    public static ItemStack PAGE;
    public static ItemStack ERROR;

    public static ItemStack EDIBLE;
    public static ItemStack ITEM;
    public static ItemStack BLOCK;
    public static ItemStack RECORD;
    public static ItemStack BURNABLE;
    public static ItemStack INTERACTABLE;
    public static ItemStack SOLID;
    public static ItemStack EQUIP;
    public static ItemStack KNOWLEDGE;

    private MarketItem(){
    }

    public void init(){

        Material mine = XMaterial.CHEST.parseMaterial();
        if(null != mine){
            MINE = new ItemStack(mine);
            ItemUtils.setDisplayName(MINE, "§3§l我的");
            ItemUtils.addLore(MINE, "§f点击打开个人");
            ItemUtils.setItemValue(MINE, "mine");
        }

        Material market = XMaterial.ENDER_CHEST.parseMaterial();
        if(null != market){
            MARKET = new ItemStack(market);
            ItemUtils.setDisplayName(MARKET, "§3§l市场");
            ItemUtils.addLore(MARKET, "§f点击回到总市场");
            ItemUtils.setItemValue(MARKET, " ");
        }

        Material data = XMaterial.BOOK.parseMaterial();
        if(null != data){
            DATA = new ItemStack(data);
            ItemUtils.setDisplayName(DATA, "§3§l统计");
            ItemUtils.addLore(DATA, "§f点击查看");
            ItemUtils.setItemValue(DATA, "data");
        }

        Material classify = XMaterial.ENDER_EYE.parseMaterial();
        if(null != classify){
            CLASSIFY = new ItemStack(classify);
            ItemUtils.setDisplayName(CLASSIFY, "§3§l分类");
            ItemUtils.addLore(CLASSIFY, "§f点击查看所有分类");
            ItemUtils.setItemValue(CLASSIFY, "classify");
        }

        Material mail = XMaterial.JUKEBOX.parseMaterial();
        if(null != mail){
            MAIL = new ItemStack(mail);
            ItemUtils.setDisplayName(MAIL, "§3§l邮箱");
            ItemUtils.addLore(MAIL, "§f点击打开");
            ItemUtils.setItemValue(MAIL, "mail");
        }

        Material menu = XMaterial.CLOCK.parseMaterial();
        if(null != menu){
            MENU = new ItemStack(menu);
            ItemUtils.setDisplayName(MENU, "§3§l菜单");
            ItemUtils.addLore(MENU, "§f点击打开");
            ItemUtils.setItemValue(MENU, "menu");
        }

        Material clean = XMaterial.FEATHER.parseMaterial();
        if(null != clean){
            SIGN = new ItemStack(clean);
            ItemUtils.setDisplayName(SIGN, "§3§l签收");
            ItemUtils.addLore(SIGN, "§f全部签收");
            ItemUtils.setItemValue(SIGN, "sign");
        }

        Material close = XMaterial.BARRIER.parseMaterial();
        if(null != close){
            CLOSE = new ItemStack(close);
            ItemUtils.setDisplayName(CLOSE, "§c§l关闭");
            ItemUtils.addLore(CLOSE, "§f点击关闭");
            ItemUtils.setItemValue(CLOSE, "close");
        }

        Material blank = XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial();
        if(null != blank){
            BLANK = new ItemStack(blank);
            ItemUtils.setDisplayName(BLANK, " ");
        }

        Material book = XMaterial.WRITABLE_BOOK.parseMaterial();
        if(null != book){
            BOOK = new ItemStack(book);
        }

        Material cancel = XMaterial.HOPPER.parseMaterial();
        if(null != cancel){
            CANCEL = new ItemStack(cancel);
        }

        Material add = XMaterial.GOLD_NUGGET.parseMaterial();
        if(null != add){
            ADD = new ItemStack(add);
        }

        Material page = XMaterial.PAPER.parseMaterial();
        if(null != page){
            PAGE = new ItemStack(page);
            ItemUtils.setDisplayName(PAGE, "§3§l换页");
            ItemUtils.addLore(PAGE, "§f左右键");
        }

        Material error = XMaterial.BARRIER.parseMaterial();
        if(null != error){
            ERROR = new ItemStack(error);
            ItemUtils.setDisplayName(ERROR, "§c§l错误");
            ItemUtils.addLore(ERROR, "§f按钮错误");
            ItemUtils.setItemValue(ERROR, "help");
        }


//      "edible", "item", "block", "record", "burnable", "interactable", "occluding", "solid", "equip", "knowledge"
        Material edible = XMaterial.CARROT.parseMaterial();
        if(null != edible){
            EDIBLE = new ItemStack(edible);
            ItemUtils.setDisplayName(EDIBLE, "§3§l食物");
            ItemUtils.addLore(EDIBLE, "§f吃货专区");
            ItemUtils.setItemValue(EDIBLE, "classify edible");
        }

        Material item = XMaterial.STICK.parseMaterial();
        if(null != item){
            ITEM = new ItemStack(item);
            ItemUtils.setDisplayName(ITEM, "§3§l物品");
            ItemUtils.addLore(ITEM, "§f杂七杂八的东西");
            ItemUtils.setItemValue(ITEM, "classify item");
        }

        Material block = XMaterial.QUARTZ_BLOCK.parseMaterial();
        if(null != block){
            BLOCK = new ItemStack(block);
            ItemUtils.setDisplayName(BLOCK, "§3§l方块");
            ItemUtils.addLore(BLOCK, "§f搭积木吗");
            ItemUtils.setItemValue(BLOCK, "classify block");
        }

        Material record = XMaterial.MUSIC_DISC_WARD.parseMaterial();
        if(null != record){
            RECORD = new ItemStack(record);
            ItemUtils.setDisplayName(RECORD, "§3§l唱片");
            ItemUtils.addLore(RECORD, "§f来首音乐吧");
            ItemUtils.setItemValue(RECORD, "classify record");
        }

        Material burnable = XMaterial.COAL.parseMaterial();
        if(null != burnable){
            BURNABLE = new ItemStack(burnable);
            ItemUtils.setDisplayName(BURNABLE, "§3§l可燃物");
            ItemUtils.addLore(BURNABLE, "§f燃烧吧，烈焰");
            ItemUtils.setItemValue(BURNABLE, "classify burnable");
        }

        Material interactable = XMaterial.FURNACE.parseMaterial();
        if(null != interactable){
            INTERACTABLE = new ItemStack(interactable);
            ItemUtils.setDisplayName(INTERACTABLE, "§3§l功能方块");
            ItemUtils.addLore(INTERACTABLE, "§f似乎是魔法在运作");
            ItemUtils.setItemValue(INTERACTABLE, "classify interactable");
        }

        Material solid = XMaterial.GRASS_BLOCK.parseMaterial();
        if(null != solid){
            SOLID = new ItemStack(solid);
            ItemUtils.setDisplayName(SOLID, "§3§l土壤");
            ItemUtils.addLore(SOLID, "§f万物生长所必须");
            ItemUtils.setItemValue(SOLID, "classify solid");
        }

        Material equip = XMaterial.GOLDEN_SWORD.parseMaterial();
        if(null != equip){
            EQUIP = new ItemStack(equip);
            ItemUtils.setDisplayName(EQUIP, "§3§l装备");
            ItemUtils.addLore(EQUIP, "§f要生存只能战斗");
            ItemUtils.setItemValue(EQUIP, "classify equip");
        }

        Material knowledge = XMaterial.BOOKSHELF.parseMaterial();
        if(null != knowledge){
            KNOWLEDGE = new ItemStack(knowledge);
            ItemUtils.setDisplayName(KNOWLEDGE, "§3§l知识");
            ItemUtils.addLore(KNOWLEDGE, "§f知识就是力量");
            ItemUtils.setItemValue(KNOWLEDGE, "classify knowledge");
        }

    }

    public ItemStack getPageItem(int i){
        return getPageItem("main", i);
    }

    public ItemStack getPageItem(String type, int i){
        ItemStack tmp = PAGE.clone();
        tmp.setAmount(i);
        ItemUtils.addLore(tmp, "§7<§e"+(i)+"§7>§0");
        ItemUtils.setItemValue(tmp, String.format("page %s %d", type, i) );
        return tmp;
    }

}
