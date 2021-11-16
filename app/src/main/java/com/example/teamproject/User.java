package com.example.teamproject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class User {
    private String name;
    private String Id;
    private String password;
    private ArrayList<Stock> startStock;
    private ArrayList<Stock> currentStock;
    private ArrayList<Integer> stockChange;
    private int stockNumber;
    private int changeNum;

    public User(@NotNull String name, @NotNull String Id, @NotNull String password, @NotNull ArrayList<Stock> startStock, @NotNull ArrayList<Stock> currentStock, @NotNull ArrayList<Integer> stockChange) {
        this.name = name;
        this.Id = Id;
        this.password = password;
        this.startStock = startStock;
        this.currentStock = currentStock;
        this.stockChange = stockChange;
        this.stockNumber = 0;
        this.changeNum = 0;
    }

    public String getname() {
        return this.name;
    }
    public String getId(){
        return this.Id;
    }
    public String getPassword(){
        return this.password;
    }
    public ArrayList<Stock>getStartStock(){
        return startStock;
    }
    public ArrayList<Stock> getCurrentStock(){
        return currentStock;
    }
    public ArrayList<Integer> getStockChange(){
        return stockChange;
    }
    public int getStockNumber(){return stockNumber;}
    public int getChangeNum(){return changeNum;}
    public void setname(String s){
       this.name = s;
    }
    public void setId(String s){
        this.Id = s;
    }
    public void setPassword(String s){
        this.password = s;
    }
    public void setStock(Stock temp){
        this.startStock.set(stockNumber, temp);
        this.currentStock.set(stockNumber, temp);
        stockNumber++;
    }
    public void setChange(int temp){
        stockChange.set(changeNum++, temp);
    }
    public void setstockNumber(int temp){stockNumber = temp;}
    public void setChangeNum(int temp){stockNumber = temp;}
}
