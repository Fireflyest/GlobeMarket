package com.fireflyest.market.util;

import com.cryptomorin.xseries.XEnchantment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

public class SerializeUtil {

    private SerializeUtil(){
    }

    public static String serializeItemStack(ItemStack itemStack) {
        Gson stack = new Gson();
        ItemStack item = itemStack.clone();
        item.setItemMeta(null);
        return stack.toJson(item.serialize());
    }

    public static String serializeItemMeta(ItemStack itemStack){
        Gson meta = new Gson();
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta == null ? "" : meta.toJson(itemMeta.serialize());
    }

    public static ItemStack deserialize(String itemStack, String itemMeta) {
        Gson gson = new Gson();
        Map<String, Object> itemMap;
        Map<String, Object> metaMap;
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        ItemStack item;
        itemMap = gson.fromJson(itemStack, type);
        item = ItemStack.deserialize(itemMap);
        if(!"".equals(itemMeta)){
            metaMap = gson.fromJson(itemMeta, type);
            item.setItemMeta(deserializeItemMeta(metaMap));
        }
        return item;
    }

    public static ItemMeta deserializeItemMeta(Map<String, Object> map) {
        String version = Bukkit.getVersion();
        String v = version.substring(version.indexOf(".")+1, version.lastIndexOf("."));
        ItemMeta meta = null;
        Class<?> clazz = null;
        int r = 1;
        while (clazz == null && r < 9){
            try {
                clazz = Class.forName(
                        String.format("org.bukkit.craftbukkit.v1_%s_R%d.inventory.CraftMetaItem$SerializableMeta", v, r));
            } catch (ClassNotFoundException ignore) {}
            r ++;
        }
        if (clazz == null){
            return null;
        }
        try {
            Method deserialize = clazz.getMethod("deserialize", Map.class);
            meta = (ItemMeta) deserialize.invoke(null, map);

            String string;
            if (map.containsKey("enchants")){
                string = map.get("enchants").toString().replace("{", "").replace("}", "").replace("\"", "");
                addEnchantments(meta, string.split(","));
            }else if (map.containsKey("stored-enchants")){
                string = map.get("stored-enchants").toString().replace("{", "").replace("}", "").replace("\"", "");
                addEnchantments(meta, string.split(","));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return meta;
    }

    private static void addEnchantments(ItemMeta meta, String[] enchantments){
        for (String s : enchantments) {
            String[] split = s.split("=");
            if (split.length > 0) {
                Enchantment enchantment = XEnchantment.valueOf(split[0]).parseEnchantment();
                int level = (int) Float.parseFloat(split[1]);
                if (enchantment != null) meta.addEnchant(enchantment, level, true);
            }
        }
    }

}
