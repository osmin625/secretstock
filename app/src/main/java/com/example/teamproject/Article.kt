package com.example.teamproject

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jsoup.Jsoup

class Article : AppCompatActivity() {
    lateinit var dialogView : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stock_detail)
        var stockname : TextView = findViewById<TextView>(R.id.name)
        var stockNum : TextView = findViewById<TextView>(R.id.count)
        var button: Button = findViewById<Button>(R.id.article)
        var buyPrice : TextView = findViewById(R.id.buyprice)
        var currentPrice : TextView = findViewById(R.id.currentprice)
        var benefit : TextView = findViewById(R.id.benefit)
        var btnDel : Button = findViewById(R.id.btnStockDel)
        var btnMod : Button = findViewById(R.id.modify)
        var EditModify : EditText
        var EditModifyPrice : EditText
        var flag = 0 // 변경내용 있을때 1
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
            //Log.i("intent","${currentStock.stockPrice}")
            setResult(Activity.RESULT_OK, outIntent)
            finish()
        }
        btnClose.setOnClickListener { // db작업은 여기서 할거임
            if(flag == 1){
                var outIntent = Intent(this, Menu::class.java)
                outIntent.putExtra("index",index)
                outIntent.putExtra("startStock", startStock)
                outIntent.putExtra("currentStock", currentStock)
                setResult(Activity.RESULT_FIRST_USER, outIntent)
            }
            finish()
        }
        btnMod.setOnClickListener {
            dialogView = View.inflate(this@Article, R.layout.modifystock, null)
            var dlg = AlertDialog.Builder(this@Article)
            EditModify = dialogView.findViewById(R.id.EditModify)
            EditModifyPrice = dialogView.findViewById(R.id.EditModifyPrice)
            dlg.setTitle("주식 수정")
            dlg.setView(dialogView)
            dlg.setPositiveButton("수정") { dialog, which ->
                var editNum = Integer.parseInt(EditModify.text.toString())
                var editPrice = Integer.parseInt(EditModifyPrice.text.toString())
                if(editNum <=0){
                    Toast.makeText(this, "주식개수는 0보다 커야됩니다.",Toast.LENGTH_SHORT).show()
                }
                else{
                    currentStock.stockPrice += (editNum - currentStock.stockNum) * editPrice
                    currentStock.stockNum = editNum
                    if(editNum >= startStock.stockNum){
                        startStock.stockPrice += (editNum - startStock.stockNum) * editPrice
                    }
                    else{
                        startStock.stockPrice -= (startStock.stockNum - editNum) * buy
                    }
                    startStock.stockNum = editNum
                    stockNum.setText(currentStock.stockNum.toString()+"개")
                    var modBuy = startStock.stockPrice / startStock.stockNum
                    var modCurrent = currentStock.stockPrice / currentStock.stockNum
                    buyPrice.text = modBuy.toString()
                    currentPrice.text = modCurrent.toString()
                    benefit.text = ((modCurrent - modBuy) * editNum).toString()
                    flag = 1
                    Toast.makeText(this, "x버튼으로 종료해야 저장됩니다.",Toast.LENGTH_SHORT).show()
                }
            }
            dlg.setNegativeButton("취소") { dialog, which ->


            }
            dlg.show()
        }

        //val doc = Jsoup.connect(url).get()
        //val title = doc.title()
        //val links = doc.select("a[href]")
        //필요한 자료만 - 오늘의 스포츠 NoW 목록을 가져오자. HTML  태그등을 분석해서 무엇을 가져 올지 결정해야 한다.
        //val headline = doc.select("div.NewsList_listNews__1X8w8 li div a div")

    }
}

