package com.fireflyest.market.listener;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.*;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.DataManager;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ChatUtils;
import com.fireflyest.market.util.ConvertUtils;
import com.fireflyest.market.util.ItemUtils;
import com.fireflyest.market.util.TimeUtils;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PlayerEventListener implements Listener {

    private final DataManager dataManager;

    private final MarketMain marketMain;
    private final MarketMail marketMail;
    private final MarketAffair marketAffair;
    private final MarketMine marketMine;
    private final MarketClassify marketClassify;
    private final JavaPlugin plugin;
    private final Sound clickSound;
    private final Sound cancelSound;

    public PlayerEventListener(JavaPlugin plugin){
        this.plugin = plugin;

        dataManager = DataManager.getInstance();

        marketMain = MarketMain.getInstance();
        marketMail = MarketMail.getInstance();
        marketAffair = MarketAffair.getInstance();
        marketMine = MarketMine.getInstance();
        marketClassify = MarketClassify.getInstance();
        clickSound = XSound.BLOCK_STONE_BUTTON_CLICK_OFF.parseSound();
        cancelSound = XSound.BLOCK_ANVIL_PLACE.parseSound();

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        User user = dataManager.getUser(name);
        if (user == null) {
            user = new User(name, player.getUniqueId().toString(), 100, 0.0, 0, false, TimeUtils.getDate(), 0);
            dataManager.insert(user);
            dataManager.addUser(user);
        }

        int mailAmount = marketMail.getMailAmount(name);
        if (mailAmount > 0){
            player.sendMessage(Language.HAS_MAIL);
            ChatUtils.sendCommandButton(player, "打开邮箱", "点击打开邮箱界面", "/market mail");
            if (!Config.LIMIT_MAIL) return;
            if (mailAmount > Config.LIMIT_MAIL_NUM - 5){
                player.sendMessage(Language.LIMIT_MAIL);
            }
            if(mailAmount > Config.LIMIT_MAIL_NUM){
                marketAffair.affairSignAll(player);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(!event.getView().getTitle().contains(Language.PLUGIN_NAME)) return;
        event.setCancelled(true);

        this.performClick(event);

    }

    /**
     * 玩家容器点击事件
     * @param event 事件
     */
    private void performClick(InventoryClickEvent event){
        ItemStack item = event.getCurrentItem();
        if(item == null) return;

        Player player = null;
        if(event.getWhoClicked() instanceof Player) player = (Player)event.getWhoClicked();
        if (player == null) {
            return;
        }
        String value = ItemUtils.getItemValue(item);
        if("".equals(value))return;

        if (event.isShiftClick()){ // 下架
            if (value.contains("affair")){
                marketAffair.affairCancel(player, ConvertUtils.parseInt(value.split(" ")[1]));
                player.playSound(player.getLocation(), cancelSound, 1F, 1F);
            }
        }else {
            if(value.contains("page")){ // 翻页
                String[] strings = value.split(" ");
                int page = Integer.parseInt(strings[2]);
                if (event.isLeftClick()){
                    page--;
                }else if (event.isRightClick()){
                    page++;
                }
                if (page < 1)page = 1;
                player.closeInventory();
                switch (strings[1]){
                    case "main":
                        player.openInventory(marketMain.getPage(String.valueOf(page)));
                        break;
                    case "mail":
                        player.openInventory(marketMail.getPage(String.format("%s %d", player.getName(), page)));
                        break;
                    case "mine":
                        player.openInventory(marketMine.getPage(String.format("%s %d", player.getName(), page)));
                        break;
                    default:
                        if(strings[1].contains("classify")){
                            player.openInventory(marketClassify.getPage(String.format("%s %d", strings[1].split(":")[1], page)));
                        }
                }
            }else { // 其他按钮
                if (Config.DEBUG) System.out.println(player.getName() + " -> " + "market "+ value);
                player.performCommand("market "+ value);
                if (clickSound != null) {
                    player.playSound(player.getLocation(), clickSound, 1F, 1F);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){

        if(event.hasItem()){
            ItemStack item = event.getItem();
            if(item == null)return;
            if(!item.getType().equals(XMaterial.WRITABLE_BOOK.parseMaterial()))return;
            String value = ItemUtils.getItemValue(item);
            if(value.contains("#"))item.setAmount(0);
        }

        if(event.hasBlock()){
            Block block = event.getClickedBlock();
            if(block == null)return;
            if(!block.getType().name().contains("SIGN"))return;
            Sign sign = (Sign)block.getState();

            if(sign.getLine(0).contains("GlobeMarket")){
                Player player = event.getPlayer();

                if(player.isSneaking()){
                    player.performCommand("market quick");
                }else {
                    player.performCommand("market");
                }
            }
        }

    }

    @EventHandler
    public void onSignChange(SignChangeEvent event){
        if(!event.getPlayer().hasPermission("market.create"))return;
        if("market".equalsIgnoreCase(event.getLine(0))){
            event.setLine(0, Language.PLUGIN_NAME);
        }
    }

}
