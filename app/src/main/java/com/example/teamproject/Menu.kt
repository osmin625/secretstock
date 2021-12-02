package com.example.teamproject

import android.app.AlertDialog
import android.app.TabActivity
import android.content.Context
import android.content.Intent
import android.icu.util.ULocale.getName
import android.os.Bundle
import android.preference.PreferenceActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TabHost.TabSpec
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.components.XAxis
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.items.view.*
import kotlin.collections.HashMap as HashMap


@Suppress("deprecation")
class Menu : TabActivity() {
    lateinit var stockAddBtn: Button
    lateinit var dlgStockName: EditText
    lateinit var dlgStockPrice: EditText
    lateinit var dlgStockCount: EditText
    lateinit var toastText: TextView
    lateinit var dialogView: View
    lateinit var stockListView: ListView
    lateinit var tempStock: Stock
    lateinit var stockSearchBtn: Button
    lateinit var resultStockCodeDB:TextView
    lateinit var resultStockNameDB:TextView
    lateinit var stockList : ArrayList<Stock>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        var intent = intent
        var user = intent.getSerializableExtra("user") as User
        var stockAddBtn = findViewById<Button>(R.id.stockAddBtn)
        var barChart: LineChart = findViewById(R.id.barChart)
        val entries = ArrayList<Entry>()
        var newstockList = mutableListOf<Listviewitem>()
        var nameText : TextView = findViewById(R.id.nameText)
        var stockchangelist = ArrayList<Int>()
        var stockchangenum : Int

        stockchangelist = user.stockChange
        stockchangenum = user.getChangeNum()
        stockList = user.getStock()
        tempStock = Stock()


        var stockList = mutableListOf<Stock>()
        tempStock = Stock()
        stockListView = findViewById<ListView>(R.id.stockList) as ListView
        var adapter = ListViewAdapter(newstockList)


        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference
        for (i in 0..user.getstockNumber() - 1){
            myRef.child("user").child(user.id).child("currentStock").child(i.toString()).get().addOnSuccessListener {
                Log.i("firebase", "Got value ${it.value}")
                val stockName = it.child("stockName").value.toString()
                val stockCode = it.child("stockCode").value.toString()
                val stockPrice = Integer.parseInt(it.child("stockPrice").value.toString())
                val stockNum = Integer.parseInt(it.child("stockNum").value.toString())
                stockList.add(Stock(
                    stockCode,stockName,stockPrice,stockNum
                ))
                if(i == user.getstockNumber() - 1){
                }
                newstockList.add(Listviewitem(stockName, stockPrice, stockNum))
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        }

        stockListView.adapter = adapter

        stockListView.setOnItemClickListener { adapterView, view, i, l ->
            var intent = Intent(this, Article::class.java)
            intent.putExtra("name", stockList[i].stockName)
            intent.putExtra("code", stockList[i].stockCode)
            intent.putExtra("price", stockList[i].stockPrice.toString())
            intent.putExtra("count", stockList[i].stockNum.toString())
            startActivity(intent)
        }
        nameText.text = "안녕하세요," +user.getname()+ "님"

        var j : Float
        j = 1.2f
        for (i in 0..stockchangenum-1)
        {
            entries.add(Entry(j,stockchangelist[i].toFloat()))
            Log.i("stockchangeList", "Got value ${stockchangelist[i]}")
            j = j+1
        }
//        entries.add(Entry(1.2f, 20.0f))
//        entries.add(Entry(2.2f, 70.0f))
//        entries.add(Entry(3.2f, 30.0f))
//        entries.add(Entry(4.2f, 90.0f))
//        entries.add(Entry(5.2f, 70.0f))




        var tabHost = this.tabHost

        var tabSpecChart = tabHost.newTabSpec("Home").setIndicator("",resources.getDrawable(R.drawable.home_selector))
        tabSpecChart.setContent(R.id.Home)
        tabHost.addTab(tabSpecChart)

        var tabSpecStock = tabHost.newTabSpec("Stock").setIndicator("",resources.getDrawable(R.drawable.stock_selector))
        tabSpecStock.setContent(R.id.Stock)
        tabHost.addTab(tabSpecStock)

        var tabSpecWallpaper = tabHost.newTabSpec("Wallpaper").setIndicator("",resources.getDrawable(R.drawable.wallpaper_selector))
        tabSpecWallpaper.setContent(R.id.Wallpaper)
        tabHost.addTab(tabSpecWallpaper)

        var tabSpecSetting = tabHost.newTabSpec("Setting").setIndicator("",resources.getDrawable(R.drawable.settings_selector))
        tabSpecSetting.setContent(R.id.Setting)
        tabHost.addTab(tabSpecSetting)

        //var tabSpecTemp = tabHost.newTabSpec("temp").setIndicator("temp")
        //tabSpecTemp.setContent(R.id.temp)
        //tabHost.addTab(tabSpecTemp)

        tabHost.currentTab = 0


        barChart.run {
            description.isEnabled = false // 차트 옆에 별도로 표기되는 description을 안보이게 설정 (false)
            setMaxVisibleValueCount(5) // 최대 보이는 그래프 개수를 5개로 지정
            setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            //setDrawLineShadow(false) //그래프의 그림자
            setDrawGridBackground(false)//격자구조 넣을건지

            axisLeft.run { //왼쪽 축. 즉 Y방향 축을 뜻한다.
                axisMaximum = (stockchangelist[0]*2).toFloat() //100 위치에 선을 그리기 위해 101f로 맥시멈값 설정
                axisMinimum =  0f// 최소값 0
                granularity = 50f // 50 단위마다 선을 그리려고 설정.
                setDrawLabels(false) // 값 적는거 허용 (0, 50, 100)
                //setDrawGridLines(true) //격자 라인 활용
                setDrawAxisLine(false) // 축 그리기 설정
                setDrawZeroLine(false)
                setDrawGridLines(false)
                axisLineColor = ContextCompat.getColor(
                    context,
                    R.color.design_default_color_secondary_variant
                ) // 축 색깔 설정
                gridColor = ContextCompat.getColor(
                    context,
                    R.color.design_default_color_on_secondary
                ) // 축 아닌 격자 색깔 설정
                textColor = ContextCompat.getColor(
                    context,
                    R.color.design_default_color_primary_dark
                ) // 라벨 텍스트 컬러 설정
                textSize = 30f //라벨 텍스트 크기
            }
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                granularity = 1f // 1 단위만큼 간격 두기
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = ContextCompat.getColor(
                    context,
                    R.color.design_default_color_primary_dark
                ) //라벨 색상
                textSize = 12f // 텍스트 크기
                valueFormatter = MyXAxisFormatter() // X축 라벨값(밑에 표시되는 글자) 바꿔주기 위해 설정
            }
            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
            animateY(1000) // 밑에서부터 올라오는 애니매이션 적용
            legend.isEnabled = false //차트 범례 설정
        }

        var set = LineDataSet(entries, "DataSet") // 데이터셋 초기화
        set.color = ContextCompat.getColor(
            applicationContext!!,
            R.color.design_default_color_primary_dark
        ) // 바 그래프 색 설정

        val dataSet: ArrayList<ILineDataSet> = ArrayList()
        dataSet.add(set)
        val data = LineData(dataSet)
        //data. = 0.3f //막대 너비 설정
        barChart.run {
            this.data = data //차트의 데이터를 data로 설정해줌.
            //setFitBars(true)
            invalidate()
        }

//        stockListView.setOnClickListener{parent, view, position, id ->
//            Toast.makeText(applicationContext, stockList[position, to])
//        }

        stockAddBtn.setOnClickListener {
            var flag = 0
            var StockCode = ""
            dialogView = View.inflate(this@Menu, R.layout.addstock, null)
            stockSearchBtn = dialogView.findViewById<Button>(R.id.stockSearchBtn)
            var dlg = AlertDialog.Builder(this@Menu)
            dlg.setTitle("주식 정보 입력")
            dlg.setView(dialogView)
            dlgStockName = dialogView.findViewById<EditText>(R.id.StockName)
            dlgStockPrice = dialogView.findViewById<EditText>(R.id.StockPrice)
            dlgStockCount = dialogView.findViewById<EditText>(R.id.StockCount)
            dlg.setPositiveButton("확인") { dialog, which ->
                //var toast1 = Toast(this@Menu)
                //toastText.text = "주식 추가 완료"
                if(flag == 1) {
                    val database = FirebaseDatabase.getInstance()
                    var stockNum : Int
                    var stockSum = Integer.parseInt(dlgStockCount.text.toString()) * Integer.parseInt(
                            dlgStockPrice.text.toString())
                    tempStock = Stock(
                        StockCode,
                        dlgStockName.text.toString(),
                        stockSum,
                        Integer.parseInt(dlgStockCount.text.toString())
                    )

                    stockNum = user.getstockNumber()
                    var userRef= database.getReference()
                    userRef.child("user").child(user.id).child("startStock").child(stockNum.toString()).setValue(tempStock)
                    userRef.child("user").child(user.id).child("currentStock").child(stockNum.toString()).setValue(tempStock)
                    stockNum += 1
                    user.setstockNumber(stockNum)
                    userRef.child("user").child(user.id).child("stockNumber").setValue(stockNum)
                    //Toast.makeText(this, "${tempStock.stockName}이/가 입력되었습니다.",Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "${tempStock.stockName}이/가 입력되었습니다.",Toast.LENGTH_SHORT).show()
                    newstockList.add(Listviewitem(tempStock.stockName,tempStock.stockPrice,tempStock.stockNum))

                    adapter.notifyDataSetChanged()

                }
                else{
                    Toast.makeText(this, "주식정보를 입력하세요.",Toast.LENGTH_SHORT).show()
                }
            //adapter.notifyDataSetChanged()
                //toast1.setGravity(Gravity.CENTER, 0, -800)
                //toast1.show()
            }
            dlg.setNegativeButton("취소") { dialog, which ->
                //var toast2 = Toast(this@Menu)
                //toastText.text = "취소"
                //toast2.setGravity(Gravity.CENTER, 0, -800)
                //toast2.show()
            }
            dlg.show()
            stockSearchBtn.setOnClickListener {
                val database = FirebaseDatabase.getInstance()
                val myRef = database.reference
                var tempStockName = dlgStockName.text.toString()
                resultStockCodeDB = dialogView.findViewById(R.id.StockCodeDB)
                resultStockNameDB = dialogView.findViewById(R.id.StockNameDB)
                myRef.child("stock").child(tempStockName).get().addOnSuccessListener {
                    Log.i("firebase", "Got value ${it.value}")
                    tempStock.stockCode = it.value.toString()
                    tempStock.stockName = it.key.toString()
                    resultStockNameDB.text = tempStock.stockName
                    resultStockCodeDB.text = tempStock.stockCode
                    StockCode = tempStock.stockCode
                    flag = 1
                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }
            }
        }
    }

    inner class ListViewAdapter(private val items: MutableList<Listviewitem>): BaseAdapter() {
        override fun getCount(): Int = items.size
        override fun getItem(position: Int): Listviewitem = items[position]
        override fun getItemId(position: Int): Long = position.toLong()
        override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
            var convertView = view
            if (convertView == null) convertView = LayoutInflater.from(parent?.context).inflate(R.layout.items, parent, false)
            val item: Listviewitem = items[position]
            convertView!!.text1.text = item.name
            convertView.text2.text = item.price.toString()
            convertView.text3.text = item.num.toString()
            return convertView
        }
    }

    inner class MyXAxisFormatter : ValueFormatter() {
        private val days = arrayOf("mon", "tue", "wed", "thu", "fri")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt() - 1) ?: value.toString()
        }
    }
    inner class Setting : PreferenceActivity() {}
}

private fun <E> ArrayList<E>.add(element: EditText?) {

}
