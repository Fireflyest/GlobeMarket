package com.fireflyest.market.core;

import com.fireflyest.market.bean.Item;
import org.bukkit.inventory.Inventory;

import java.util.List;

public interface MarketPage<T extends Item> {

    void initPages(List<T> list);

    // 对单一单元格进行修改
    void notifyItemChange(T t);

    // 添加一单元格
    void addItem(T t);

    // 移除一单元格
    void removeItem(T t);

    // 获取页面
    Inventory getPage(String str);

    // 加载界面
    void loadPage(Inventory inv, int page);

    // 加载界面
    void loadPage(Inventory inv, T t);

}
