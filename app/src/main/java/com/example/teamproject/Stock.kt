package com.example.teamproject

class Stock {
    var stockCode: String
    var stockName: String
    private var stockPrice: Int
    private var stockNum: Int

    constructor() {
        stockCode = ""
        stockName = ""
        stockPrice = 0
        stockNum = 0
    }

    constructor(code: String, name: String, price: Int, num: Int) {
        stockCode = code
        stockName = name
        stockPrice = price
        stockNum = num
    }
    fun setStockNum(temp : Int){
        stockNum = temp
    }
    fun setStockPrice(temp : Int){
        stockPrice = temp
    }
}