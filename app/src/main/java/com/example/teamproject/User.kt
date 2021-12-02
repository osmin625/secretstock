package com.example.teamproject

import org.jsoup.select.Evaluator
import java.util.ArrayList
import java.io.Serializable

class User(
    private var name: String,
    var id: String,
    var password: String,
    val startStock: ArrayList<Stock>,
    val currentStock: ArrayList<Stock>,
    val stockChange: ArrayList<Int>,
    val date : ArrayList<String>,
    private var stockNumber : Int,
    private var changeNum : Int
):Serializable{
    fun getname(): String {
        return name
    }
    fun getStock(): ArrayList<Stock>{
        return currentStock
    }
    fun getstockNumber() : Int{
        return stockNumber
    }
    fun getChangeNum() : Int{
        return changeNum
    }

    fun setChange(temp: Int) {
        stockChange[changeNum++] = temp
    }
    fun setstockNumber(temp: Int) {
        stockNumber = temp
    }

}