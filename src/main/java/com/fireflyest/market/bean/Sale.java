package com.fireflyest.market.bean;

/**
 * @author Fireflyest
 * 2021/3/26 12:08
 */

public class Sale extends Item{

    // 商品主人
    private String owner;

    // 购买者
    private String buyer;

    // 原始价格
    private double price;

    // 现价
    private double cost;

    // 热度
    private int heat;

    // 简介
    private String info;

    // 拍卖
    private boolean auction;

    // 货币
    private boolean point;

    public Sale() {
    }

    public Sale(int id, String stack, String meta, long create, String owner, String buyer, double price, double cost, int heat, String info, boolean auction, boolean point) {
        super(id, stack, meta, create);
        this.buyer = buyer;
        this.owner = owner;
        this.price = price;
        this.cost = cost;
        this.heat = heat;
        this.info = info;
        this.auction = auction;
        this.point = point;
    }

    public boolean isPoint() {
        return point;
    }

    public void setPoint(boolean point) {
        this.point = point;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isAuction() {
        return auction;
    }

    public void setAuction(boolean auction) {
        this.auction = auction;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "owner='" + owner + '\'' +
                ", price=" + price +
                ", cost=" + cost +
                ", heat=" + heat +
                ", info='" + info + '\'' +
                ", auction=" + auction +
                ", point=" + point +
                '}';
    }

}
