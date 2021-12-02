package com.example.teamproject

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
        var print_price : TextView = findViewById<TextView>(R.id.buyprice)
        var button: Button = findViewById<Button>(R.id.article)
        var modify : Button = findViewById<Button>(R.id.modify)

        var name = intent.getStringExtra("name")
        var code = intent.getStringExtra("code")
        var price = intent.getStringExtra("price")?.toInt()
        var count = intent.getStringExtra("count")?.toInt()

        stockname.setText(name)
        stockNum.setText(count.toString()+"개")
        print_price.setText(price.toString()+"원")
        //기사 크롤링
        val url = "https://m.stock.naver.com/index.html#/domestic/stock/"+code+"/news/title"
        button.setOnClickListener{
            val openUrl = Intent(Intent.ACTION_VIEW)
            openUrl.data = Uri.parse(url)
            startActivity(openUrl)
        }

        //val doc = Jsoup.connect(url).get()
        //val title = doc.title()
        //val links = doc.select("a[href]")
        //필요한 자료만 - 오늘의 스포츠 NoW 목록을 가져오자. HTML  태그등을 분석해서 무엇을 가져 올지 결정해야 한다.
        //val headline = doc.select("div.NewsList_listNews__1X8w8 li div a div")

        }
    }