package com.example.teamproject;

public class User {
    private String name;
    private String Id;
    private String password;
    private Stock[] startStock;
    private Stock[] currentStock;
    private int[] stockChange;
    private int stockNumber;
    private int changeNum;

    public User(String name, String Id, String password){
        this.name = name;
        this.Id = Id;
        this.password = password;
        this.startStock = new Stock[20];
        this.currentStock = new Stock[20];
        this.stockChange = new int[365];
        this.stockNumber = 0;
        this.changeNum = 0;
    }

    public String getName() {
        return this.name;
    }
    public String getId(){
        return this.Id;
    }
    public String getPassword(){
        return this.password;
    }
    public Stock[] getStartStock(){
        return startStock;
    }
    public Stock[] getCurrentStock(){
        return currentStock;
    }
    public int[] getStockChange(){
        return stockChange;
    }
    public void setName(String s){
       this.name = s;
    }
    public void setId(String s){
        this.Id = s;
    }
    public void setPassword(String s){
        this.password = s;
    }
    public void setStock(Stock temp){
        this.startStock[stockNumber] = temp;
        this.currentStock[stockNumber] = temp;
        stockNumber++;
    }
    public void setChange(int temp){
        stockChange[changeNum++] = temp;
    }
}
