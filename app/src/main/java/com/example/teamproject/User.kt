package com.example.teamproject

import java.util.ArrayList

class User(
    private var name: String,
    var id: String,
    var password: String,
    val startStock: ArrayList<Stock>,
    val currentStock: ArrayList<Stock>,
    val stockChange: ArrayList<Int>
) {
    var stockNumber = 0
        private set
    var changeNum = 0
        set(temp) {
            stockNumber = temp
        }

    fun getname(): String {
        return name
    }

    fun setname(s: String) {
        name = s
    }

    fun setStock(temp: Stock) {
        startStock[stockNumber] = temp
        currentStock[stockNumber] = temp
        stockNumber++
    }

    fun setChange(temp: Int) {
        stockChange[changeNum++] = temp
    }

    fun setstockNumber(temp: Int) {
        stockNumber = temp
    }

}