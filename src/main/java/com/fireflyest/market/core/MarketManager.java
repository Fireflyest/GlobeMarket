package com.fireflyest.market.core;

import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.bean.Note;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.ListIterator;

/**
 * @author Fireflyest
 * 2021/3/27 19:31
 */

public class MarketManager {

    private List<Sale> sales;
    private List<Mail> mails;

    private DataManager dataManager;

    private MarketMain marketMain;
    private MarketMail marketMail;
    private MarketAffair marketAffair;
    private MarketMine marketMine;
    private MarketClassify marketClassify;

    private MarketManager(){}

    private final static MarketManager DATA_MANAGER = new MarketManager();

    public static MarketManager getInstance() {
        return DATA_MANAGER;
    }

    public void init(){

        dataManager = DataManager.getInstance();

        sales = dataManager.getSales();
        mails = dataManager.getMails();

        marketMain = MarketMain.getInstance();
        marketAffair = MarketAffair.getInstance();
        marketMail = MarketMail.getInstance();
        marketMine = MarketMine.getInstance();
        marketClassify = MarketClassify.getInstance();

        marketMain.initPages(sales);
        marketAffair.initPages(sales);
        marketMine.initPages(sales);
        marketClassify.initPages(sales);

        marketMail.initPages(mails);
    }

    public Sale getSale(int id){
        for (Sale s : sales) {
            if (s.getId() == id) return s;
        }
        return null;
    }

    public Mail getMail(int id){
        for (Mail m : mails) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    public List<Mail> getMails() {
        return mails;
    }

    public void addSale(Sale sale){
        long start = System.currentTimeMillis();
        if (Config.DEBUG){
            System.out.println("------------------------------------");
            System.out.println("Thread=" + Thread.currentThread().getName());
            System.out.println("添加商品");
        }

        int id = dataManager.insert(sale);
        sale.setId(id);
        sales.add(sale);
        marketMain.addItem(sale);
        marketMine.addItem(sale);
        marketClassify.addItem(sale);
        marketAffair.addItem(sale);

        if (Config.DEBUG){
            long end = System.currentTimeMillis();
            System.out.println("耗时=" + (end - start));
            System.out.println("------------------------------------");
        }
    }


    public void removeSale(Sale sale){
        marketMain.removeItem(sale);
        marketMine.removeItem(sale);
        marketClassify.removeItem(sale);
        marketAffair.removeItem(sale);
        sales.remove(sale);
        dataManager.delete(sale);
    }

    public void updateSale(Sale sale){
        marketMain.notifyItemChange(sale);
        marketMine.notifyItemChange(sale);
        marketClassify.notifyItemChange(sale);
        marketAffair.notifyItemChange(sale);
        dataManager.update(sale);
    }

    public void addMail(Mail mail){
        int id = dataManager.insert(mail);
        mail.setId(id);
        mails.add(mail);
        marketMail.addItem(mail);
    }

    public void removeMail(Mail mail){
        // 更新统计数据
        Note note = dataManager.getTodayNote();
        note.setAmount(note.getAmount() + 1);
        note.setMoney(note.getMoney() + mail.getPrice());
        if (mail.getPrice() > note.getMax()) note.setMax(mail.getPrice());
        //删除邮件
        marketMail.removeItem(mail);
        mails.remove(mail);
        dataManager.delete(mail);
    }

    public void updateMail(Mail mail){
        marketMail.notifyItemChange(mail);
        dataManager.update(mail);
    }

}
