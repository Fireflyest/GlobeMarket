package com.fireflyest.market.core;

import com.fireflyest.market.bean.Sale;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * @author Fireflyest
 * 2021/5/6 17:37
 */

public class MarketAdmin implements MarketPage<Sale>{

    @Override
    public void initPages(List<Sale> list) {

    }

    @Override
    public void notifyItemChange(Sale sale) {

    }

    @Override
    public void addItem(Sale sale) {

    }

    @Override
    public void removeItem(Sale sale) {

    }

    @Override
    public Inventory getPage(String str) {
        return null;
    }

    @Override
    public void loadPage(Inventory inv, int page) {

    }

    @Override
    public void loadPage(Inventory inv, Sale sale) {

    }

}
