package com.example.myapplication.entity;

import java.io.Serializable;

public class Goods implements Serializable {
    private Integer goodsId;
    private String goodsName;
    private String goodsPrice;
    private String goodsDescription;
    private Integer goodsStock;
    private String goodsIcon;
    private Integer goodsType;
    private String goodsTag;

    public Goods() {
    }

    public Goods(Integer goodsId, String goodsName, String goodsPrice, String goodsDescription, Integer goodsStock, String goodsIcon, Integer goodsType, String goodsTag) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsDescription = goodsDescription;
        this.goodsStock = goodsStock;
        this.goodsIcon = goodsIcon;
        this.goodsType = goodsType;
        this.goodsTag = goodsTag;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsDescription() {
        return goodsDescription;
    }

    public void setGoodsDescription(String goodsDescription) {
        this.goodsDescription = goodsDescription;
    }

    public Integer getGoodsStock() {
        return goodsStock;
    }

    public void setGoodsStock(Integer goodsStock) {
        this.goodsStock = goodsStock;
    }

    public String getGoodsIcon() {
        return goodsIcon;
    }

    public void setGoodsIcon(String goodsIcon) {
        this.goodsIcon = goodsIcon;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }
}
