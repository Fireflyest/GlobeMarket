package com.fireflyest.market.data;

import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.bean.Note;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.util.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Fireflyest
 * 2021/5/3 22:42
 */

public class DataManager {

    private static DataDriver driver;

    private static final DataManager dataManager = new DataManager();

    public static DataManager getInstance() {
        return dataManager;
    }

    private final List<User> users = new ArrayList<>();
    private final List<Sale> sales = new ArrayList<>();
    private final List<Mail> mails = new ArrayList<>();
    private final List<Note> notes = new ArrayList<>();

    private Note todayNote;

    private DataManager(){
    }

    public void loadData(){
        //初始化数据连接
        driver = Config.SQL ?
                new JdbcDriver(Config.URL, Config.USER, Config.PASSWORD) :
                new YamlDriver();

//        driver = new YamlDriver();
//        driver = new JdbcDriver("jdbc:mysql://localhost:3306/mc_market?useSSL=false&serverTimezone=UTC", "root", "123456");

        // 初始化数据库
        driver.initTable(User.class);
        driver.initTable(Sale.class);
        driver.initTable(Mail.class);
        driver.initTable(Note.class);

        // 初始化商店数据
        for (Object o : driver.queryList(User.class)) users.add((User) o);
        for (Object o : driver.queryList(Sale.class)) sales.add((Sale) o);
        for (Object o : driver.queryList(Mail.class)) mails.add((Mail) o);
        for (Object o : driver.queryList(Note.class)) notes.add((Note) o);
        Collections.sort(sales);
        Collections.sort(mails);

        // 初始化当天统计数据
        String day = TimeUtils.getTimeToday();
        for (Note note : notes) {
            if (!note.getDay().equals(day)) break;
            todayNote = note;
            break;
        }
        if (todayNote == null) {
            this.createNote(day);
        }

    }

    public List<Sale> getSales() {
        return sales;
    }

    public List<Mail> getMails() {
        return mails;
    }

    public User getUser(String name){
        User user = null;
        for (User u : users) {
            if (u.getName().equals(name)) user = u;
        }
        return user;
    }

    public void addUser(User user){
        users.add(user);
    }

    public Note getTodayNote(){
        String day = TimeUtils.getTimeToday();
        if (!todayNote.getDay().equals(day)){
            this.createNote(day);
        }
        return todayNote;
    }

    public void createNote(String day){
        todayNote = new Note(day, 0, 0, 0);
        this.insert(todayNote);
    }

    public int insert(Object obj){
        return driver.insert(obj);
    }

    public void delete(Object obj) {
        driver.delete(obj);
    }

    public void update(Object obj) {
        driver.update(obj);
    }

//    @Test
    public void test(){
        this.loadData();
        driver.initTable(User.class);
        driver.initTable(Sale.class);

        driver.queryList(Sale.class).forEach(sale ->{
            System.out.println("sale = " + sale.toString());
        });

//        s(Sale.class);

//        Sale sale = new Sale(0, "ccccdada", "sfsa", "sadsadsad", 1646466, "Fuck", 5615, 6516, 16, "info", false , false);
//        driver.insert(sale);
//        driver.update(sale);

//        Sale sale = ((Sale) driver.query(Sale.class, "id", 26));
//        System.out.println("sale = " + sale.toString());

//        System.out.println("driver.contain() = " + driver.contain(Sale.class, "id", 15));

//        driver.delete(Sale.class, "id", 24);

//        User user = new User();
//        user.setUuid("safsad");
//        user.setAmount(12);
//        user.setBlack(false);
//        user.setMoney(1548);
//        user.setCredit(161);
//        user.setSelling(1616);
//        user.setName("Fuck");
//        driver.insert(user);
    }



}
