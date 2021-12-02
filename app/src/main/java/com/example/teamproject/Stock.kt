package com.example.teamproject

import java.io.Serializable

class Stock :Serializable{
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
        stockCode = code
        stockName = name
        stockPrice = price
        stockNum = num
    }

}