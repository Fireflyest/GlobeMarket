package com.fireflyest.market;

import com.fireflyest.market.command.MarketCommand;
import com.fireflyest.market.command.MarketTab;
import com.fireflyest.market.core.MarketHandler;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketStatistic;
import com.fireflyest.market.data.DataManager;
import com.fireflyest.market.listener.PlayerEventListener;
import com.fireflyest.market.util.YamlUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.permission.Permission;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@Plugin(name="GlobeMarket", version="2.0-SNAPSHOT")
@Author(value = "Fireflyest")
@Description("环球市场插件，作者QQ746969484(备注来意)")
@Website("https://www.mcbbs.net/thread-1090611-1-1.html")

@Command(name = "market", usage = "/market <help>", desc = "主指令")
@Command(name = "globemarket")

@Permission(name = "market.statistic", desc = "查询数据", defaultValue = PermissionDefault.OP)
@Permission(name = "market.sign", desc = "一键签收所有物品", defaultValue = PermissionDefault.OP)
@Permission(name = "market.create", desc = "创建牌子", defaultValue = PermissionDefault.OP)
@Permission(name = "market.vip", desc = "会员", defaultValue = PermissionDefault.OP)

@Permission(name = "market.send", desc = "邮寄物品", defaultValue = PermissionDefault.TRUE)
@Permission(name = "market.sell", desc = "出售物品", defaultValue = PermissionDefault.TRUE)
@Permission(name = "market.point", desc = "出售点券物品", defaultValue = PermissionDefault.TRUE)
@Permission(name = "market.auction", desc = "拍卖物品", defaultValue = PermissionDefault.TRUE)
@Permission(name = "market.discount", desc = "折扣物品", defaultValue = PermissionDefault.TRUE)
@Permission(name = "market.reprice", desc = "修改价格", defaultValue = PermissionDefault.TRUE)
@Permission(name = "market.other", desc = "打开个人商店", defaultValue = PermissionDefault.TRUE)

@ApiVersion(ApiVersion.Target.v1_13)
public class GlobeMarket extends JavaPlugin {

    /*
    系统商店
    点券支持
    商品税收
    邮箱交费
    排行榜
     */

    @Override
    public void onDisable() {
        MarketHandler.getInstance().stop();
        Bukkit.getScheduler().cancelTasks(this);
    }

    @Override
    public void onEnable() {

        // 加载配置
        YamlUtils.iniYamlUtils(this);

        // 加载物品
        MarketItem.getInstance().init();

        // 加载数据
        DataManager.getInstance().loadData();

        // 加载市场
        MarketManager.getInstance().init();

        // 任务运行
        MarketHandler.getInstance().createTaskHandler(this);

        // 统计
        MarketStatistic.getInstance().init();

        //注册事件
        this.getServer().getPluginManager().registerEvents( new PlayerEventListener(this), this);

        //注册指令
        PluginCommand command = this.getCommand("market");
        if(command!=null){
            command.setExecutor(new MarketCommand());
            command.setTabCompleter(new MarketTab());
        }

    }

}
