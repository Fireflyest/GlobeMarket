package com.fireflyest.market.util;

import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Fireflyest
 * Yaml数据管理
 */
public class YamlUtils {

    private static JavaPlugin plugin;
    private static final Map<String, Map<String, File>> data = new HashMap<>();
    private static Config config;

//    private static File dataFolder = new File("F:\\Java\\Plugins\\GlobeMarket\\src\\main\\java\\com\\fireflyest\\market\\util\\test");
    private static File dataFolder;

    private YamlUtils(){
    }

    /**
     * 初始化
     * @param javaPlugin 插件class
     */
    public static void iniYamlUtils(JavaPlugin javaPlugin){
        plugin = javaPlugin;
        dataFolder = javaPlugin.getDataFolder();
        loadConfig();
    }

    /**
     * 添加/加载一个yml文件
     * @param table 文件夹
     * @param ymlName 不带后缀文件名
     * @return FileConfiguration
     */
    public static FileConfiguration setup(String table, String ymlName) {
        File file = new File(dataFolder+"/"+table, ymlName+".yml");
        if (!file.exists()) {
            try {
                boolean mkdirs = file.getParentFile().mkdirs();
                boolean create =  file.createNewFile();
                if (Config.DEBUG){
                    Bukkit.getServer().getLogger().severe(mkdirs + "and" + create + ymlName+".yml");
                }
                if("".equals(table))plugin.saveResource(ymlName+".yml", true);
            } catch (IOException e) {
                e.printStackTrace();
                Bukkit.getServer().getLogger().severe(String.format("无法创建文件 %s!", ymlName+".yml"));
            }
        }

        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        if(!"".equals(table))data.get(table).put(ymlName, file);
        return yml;
    }


    /**
     * 获取玩家的数据
     * @param name 玩家游戏名
     * @return FileConfiguration
     */
    public static FileConfiguration getPlayerData(String table, String name){
        if(!data.get(table).containsKey(name))setup(table, name);
        return YamlConfiguration.loadConfiguration(data.get(table).get(name));
    }

    /**
     * @return table的所有yml
     */
    public static Set<String> getPlayerDataKeys(String table){
        return data.get(table).keySet();
    }

    public static void deletePlayerData(String table, String name){
        File file = new File(dataFolder+"/"+table, name+".yml");
        boolean delete = file.delete();
        if (Config.DEBUG){
            Bukkit.getServer().getLogger().severe(delete + name+".yml");
        }
    }

    /**
     * 写入玩家数据
     * @param name 玩家游戏名
     * @param key 数据键值
     * @param value 数据值
     */
    public static void setPlayerData(String table, String name, String key, Object value){
        data.computeIfAbsent(table, k -> new HashMap<>());
        if(!data.get(table).containsKey(name)) setup(table, name);
        FileConfiguration configuration = getPlayerData(table, name);
        configuration.set(key, value);
        try {
            configuration.save(data.get(table).get(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean containsData(String table, String name){
        addTable(table);
        return data.get(table).containsKey(name);
    }

    public static void addTable(String table){
        if(!data.containsKey(table)){
            Map<String, File>tableMap = new HashMap<>();
            data.put(table, tableMap);
        }
        File file = new File(dataFolder+"/"+table);
        if(file.getParentFile().mkdirs()){
            setup(table, "###");
        }else {
            String[]list = file.list();
            if(list == null)return;
            for(String f : list){
                if(f.contains("###"))continue;
                setup(table, f.replace(".yml", ""));
            }
        }
    }

    /**
     * 保存配置数据
     * @param key 据键值
     * @param value 数据值
     */
    public static void setConfigData(String key, Object value) {
        config.setKey(key, value);
        File file = new File(dataFolder, "config.yml");

        try {
            config.getConfig().save(file);
        } catch (IOException e) {
            Bukkit.getServer().getLogger().severe(String.format("无法保存数据 %s!", "config.yml"));
        }
    }

    /**
     * 加载配置文件
     */
    public static void loadConfig() {
        if (config != null) {
            plugin.reloadConfig();
        }
        config = new Config(setup("", "config"));
        new Language(setup("", "language"));
    }

    /**
     * 更新配置文件
     */
    public static void upDateConfig(){
        plugin.saveResource("config.yml", true);
        plugin.saveResource("language.yml", true);
    }

}
