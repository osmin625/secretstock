package com.example.teamproject


class Stock {
    var stockCode: String
    var stockName: String
    var stockPrice: Int
    var stockNum: Int

    constructor() {
        stockCode = ""
        stockName = ""
        stockPrice = 0
        stockNum = 0
    }

    constructor(code: String, name: String, price: Int, num: Int) {
        this.stockCode = code
        this.stockName = name
        this.stockPrice = price
        this.stockNum = num
    }

    fun getName(): String {
        return stockName
    }

}