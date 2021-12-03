package com.example.teamproject

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jsoup.Jsoup

class Article : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stock_detail)
        var stockname : TextView = findViewById<TextView>(R.id.name)
        var stockNum : TextView = findViewById<TextView>(R.id.count)
        var button: Button = findViewById<Button>(R.id.article)
        var modify : Button = findViewById<Button>(R.id.modify)
        var buyPrice : TextView = findViewById(R.id.buyprice)
        var currentPrice : TextView = findViewById(R.id.currentprice)
        var benefit : TextView = findViewById(R.id.benefit)
        var btnDel : Button = findViewById(R.id.btnStockDel)
        var intent = intent
        var currentStock = intent.getSerializableExtra("currentStock") as Stock
        var startStock = intent.getSerializableExtra("startStock") as Stock
        var buy = startStock.stockPrice / startStock.stockNum
        var current = currentStock.stockPrice / currentStock.stockNum
        var stockBenefit = (current - buy) * currentStock.stockNum
        var btnClose : ImageButton = findViewById(R.id.dtCloseBtn)
        var index = 0
        index = intent.getIntExtra("index", 0)
        stockname.setText(currentStock.stockName)
        stockNum.setText(currentStock.stockNum.toString()+"개")
        buyPrice.text = buy.toString()
        currentPrice.text = current.toString()
        benefit.text = stockBenefit.toString()
        //기사 크롤링
        val url = "https://m.stock.naver.com/index.html#/domestic/stock/"+currentStock.stockCode+"/news/title"
        button.setOnClickListener{
            val openUrl = Intent(Intent.ACTION_VIEW)
            openUrl.data = Uri.parse(url)
            startActivity(openUrl)
        }
        btnDel.setOnClickListener {
            var outIntent = Intent(this, Menu::class.java)
            outIntent.putExtra("index", index)
            outIntent.putExtra("sum", currentStock.stockPrice)
            Log.i("intent","${currentStock.stockPrice}")
            setResult(Activity.RESULT_OK, outIntent)
            finish()
        }
        btnClose.setOnClickListener {
            finish()
        }

        //val doc = Jsoup.connect(url).get()
        //val title = doc.title()
        //val links = doc.select("a[href]")
        //필요한 자료만 - 오늘의 스포츠 NoW 목록을 가져오자. HTML  태그등을 분석해서 무엇을 가져 올지 결정해야 한다.
        //val headline = doc.select("div.NewsList_listNews__1X8w8 li div a div")

        }
    }

