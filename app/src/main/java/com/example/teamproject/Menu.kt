package com.example.teamproject

import android.app.Activity
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
    lateinit var user : User
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
    lateinit var startList : ArrayList<Stock>
    lateinit var myMoney : TextView
    lateinit var adapter : ListViewAdapter
    lateinit var stockChangeList : ArrayList<Int>
    var total : Int = 0
    var newstockList = mutableListOf<Listviewitem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        var intent = intent
        user = intent.getSerializableExtra("user") as User
        var stockAddBtn = findViewById<Button>(R.id.stockAddBtn)
        var barChart: LineChart = findViewById(R.id.barChart)
        val entries = ArrayList<Entry>()
        var nameText: TextView = findViewById(R.id.nameText)
        var myMoney = findViewById<TextView>(R.id.myMoneyText)
        var stockChangeNum = user.getChangeNum()
        stockChangeList = user.stockChange
        total = stockChangeList[stockChangeNum-1]
        stockList = user.getStock()
        startList = user.startStock
        tempStock = Stock()
        stockListView = findViewById<ListView>(R.id.stockList) as ListView
        adapter = ListViewAdapter(newstockList)
        for(i in stockList){
            newstockList.add(Listviewitem(i.stockName, i.stockPrice / i.stockNum, i.stockNum))
        }
        stockListView.adapter = adapter
        if(stockChangeNum > 0)
        {
            myMoney.text = total.toString()+ "원"
        }
            nameText.text = "안녕하세요," +user.getname()+ "님"
        var j : Float
        j = 1.2f
        for (i in 0..stockChangeNum - 1) {
            entries.add(Entry(j, stockChangeList[i].toFloat()))
            //Log.i("stockchangeList", "Got value ${stockChangeList[i]}")
            j = j + 1
        }

        stockListView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, Article::class.java)
            intent.putExtra("currentStock", stockList[i])
            intent.putExtra("startStock", startList[i])
            intent.putExtra("index", i)
            startActivityForResult(intent,0)
        }
        var tabHost = this.tabHost

        var tabSpecChart = tabHost.newTabSpec("Home")
            .setIndicator("", resources.getDrawable(R.drawable.home_selector))
        tabSpecChart.setContent(R.id.Home)
        tabHost.addTab(tabSpecChart)

        var tabSpecStock = tabHost.newTabSpec("Stock")
            .setIndicator("", resources.getDrawable(R.drawable.stock_selector))
        tabSpecStock.setContent(R.id.Stock)
        tabHost.addTab(tabSpecStock)

        var tabSpecWallpaper = tabHost.newTabSpec("Wallpaper")
            .setIndicator("", resources.getDrawable(R.drawable.wallpaper_selector))
        tabSpecWallpaper.setContent(R.id.Wallpaper)
        tabHost.addTab(tabSpecWallpaper)

        var tabSpecSetting = tabHost.newTabSpec("Setting")
            .setIndicator("", resources.getDrawable(R.drawable.settings_selector))
        tabSpecSetting.setContent(R.id.Settings)
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
                axisMaximum = (stockChangeList[0] * 2).toFloat() //100 위치에 선을 그리기 위해 101f로 맥시멈값 설정
                axisMinimum = 0f // 최소값 0
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
                textSize = 13f //라벨 텍스트 크기
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
                    var stockNumber : Int
                    var stockSum = Integer.parseInt(dlgStockCount.text.toString()) * Integer.parseInt(
                        dlgStockPrice.text.toString())
                    tempStock = Stock(
                        StockCode,
                        dlgStockName.text.toString(),
                        stockSum,
                        Integer.parseInt(dlgStockCount.text.toString())
                    )
                    total += stockSum
                    stockList.add(tempStock)
                    startList.add(tempStock)
                    stockNumber = user.getstockNumber()
                    var userRef= database.getReference()
                    userRef.child("user").child(user.id).child("startStock").child(stockNumber.toString()).setValue(tempStock)
                    userRef.child("user").child(user.id).child("currentStock").child(stockNumber.toString()).setValue(tempStock)
                    stockNumber += 1
                    user.setstockNumber(stockNumber)
                    userRef.child("user").child(user.id).child("stockNumber").setValue(stockNumber)
                    //Toast.makeText(this, "${tempStock.stockName}이/가 입력되었습니다.",Toast.LENGTH_SHORT).show()
                    if(stockChangeNum == 0){
                        stockChangeList.add(0,stockSum)
                        stockChangeNum = 1
                        userRef.child("user").child(user.id).child("ChangeNum").setValue(stockChangeNum)
                    }
                    else{
                        for(i in 0..stockChangeNum - 1){
                            stockChangeList[i] += stockSum
                        }
                    }
                    myMoney.text = total.toString() + "원"
                    userRef.child("user").child(user.id).child("stockChange").setValue(stockChangeList)
                    Toast.makeText(this, "${tempStock.stockName}이/가 입력되었습니다.",Toast.LENGTH_SHORT).show()
                    newstockList.add(Listviewitem(dlgStockName.text.toString(), stockSum / Integer.parseInt(dlgStockCount.text.toString()), Integer.parseInt(dlgStockCount.text.toString())))
                    adapter.notifyDataSetChanged()
                }
                else if(flag == 2){
                    Toast.makeText(this, "이미 존재하는 주식입니다.",Toast.LENGTH_SHORT).show()
                }
                else if(flag == 3){
                    Toast.makeText(this, "존재하지 않는 주식입니다.",Toast.LENGTH_SHORT).show()
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
                    //Log.i("firebase", "Got value ${it.value}")
                    resultStockNameDB.text = it.key.toString()
                    resultStockCodeDB.text = it.value.toString()
                    StockCode = it.value.toString()
                    flag = 1
                    for(i in stockList){
                        Log.i("stockList", "${i.stockCode}")
                        if(i.stockCode == StockCode) {
                            Log.i("same stock", "${i.stockCode}")
                            flag = 2
                        }
                    }
                    if(StockCode.length != 6){
                        flag = 3
                    }
                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }
                Log.i("firebase", "Got value ${StockCode}")
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
    inner class Setting : PreferenceActivity() {

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            var setupArray = ArrayList<Int>(1)
            var index = data!!.getIntExtra("index", 0)
            var sum = data!!.getIntExtra("sum", 0)
            Log.i("sum", "${sum}")
            val database = FirebaseDatabase.getInstance()
            var userRef = database.reference.child("user").child(user.id)
            var stockNum = user.getstockNumber()
            var stockchangeNum = user.getChangeNum()
            stockList.removeAt(index)
            startList.removeAt(index)
            Log.i("code", "${stockList}")
            newstockList.removeAt(index)
            stockNum -= 1
            total -= sum
            myMoney = findViewById(R.id.myMoneyText)
            myMoney.text = total.toString()
            setupArray.add(total)
            user.setstockNumber(stockNum)
            user.stockChange = setupArray
            adapter.notifyDataSetChanged()
            userRef.child("startStock").setValue(startList)
            userRef.child("currentStock").setValue(stockList)
            userRef.child("stockNumber").setValue(stockNum)
            userRef.child("stockChange").setValue(setupArray)
            userRef.child("changeNum").setValue(1)
        }
    }
}

private fun <E> ArrayList<E>.add(element: EditText?) {

}
