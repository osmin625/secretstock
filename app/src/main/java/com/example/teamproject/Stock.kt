package com.example.teamproject

class Stock {
    var stockCode: Int
    var stockName: String
    var stockPrice: Int
    var stockNum: Int

    constructor() {
        stockCode = 0
        stockName = ""
        stockPrice = 0
        stockNum = 0
    }

    constructor(code: Int, name: String, price: Int, num: Int) {
        stockCode = code
        stockName = name
        stockPrice = price
        stockNum = num
    }
}
