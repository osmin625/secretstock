package com.example.teamproject;

import java.io.Serializable;

public class Stock {
    private String code;
    private String name;
    private int price;
    private int num;

    public Stock() {
        this.code = "";
        this.name = "";
        this.price = 0;
        this.num = 0;
    }
    public Stock(String code, String name, int price, int num){
        this.code = code;
        this.name = name;
        this.price = price;
        this.num = num;
    }
    public String getStockName(){
        return this.name;
    }
    public String getStockCode(){
        return this.code;
    }
    public int getStockPrice(){
        return this.price;
    }
    public int getStockNum(){
        return this.num;
    }
    public void setStockName(String s){
        this.name = s;
    }
    public void setStockCode(String k){
        this.code = k;
    }
    public void setStockPrice(int k){
        this.price = k;
    }
    public void setStockNum(int k){
        this.num = k;
    }
}
