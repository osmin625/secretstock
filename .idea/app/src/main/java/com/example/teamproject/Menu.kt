package com.example.teamproject

import android.app.AlertDialog
import android.app.TabActivity
import android.content.Context
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
        var nameText : TextView = findViewById(R.id.nameText)
        var newstockList = mutableListOf<Listviewitem>()
        var stockChangeList : ArrayList<Int>
        var stockChangeNum = user.getChangeNum()
        stockChangeList = user.stockChange
        stockList = user.getStock()
        tempStock = Stock()
        stockListView = findViewById<ListView>(R.id.stockList) as ListView
        var adapter = ListViewAdapter(newstockList)
        for(i in stockList){
            newstockList.add(Listviewitem(i.stockName, i.stockPrice / i.stockNum, i.stockNum))
        }
        stockListView.adapter = adapter
        nameText.text = "???????????????," +user.getname()+ "???"
        var j : Float
        j = 1.2f
        for (i in 0..stockChangeNum-1)
        {
            entries.add(Entry(j,stockChangeList[i].toFloat()))
            Log.i("stockchangeList", "Got value ${stockChangeList[i]}")
            j = j+1
        }




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
            description.isEnabled = false // ?????? ?????? ????????? ???????????? description??? ???????????? ?????? (false)
            setMaxVisibleValueCount(5) // ?????? ????????? ????????? ????????? 5?????? ??????
            setPinchZoom(false) // ?????????(?????????????????? ?????? ??? ???????????????) ??????
            //setDrawLineShadow(false) //???????????? ?????????
            setDrawGridBackground(false)//???????????? ????????????

            axisLeft.run { //?????? ???. ??? Y?????? ?????? ?????????.
                axisMaximum = (stockChangeList[0] * 2).toFloat() //100 ????????? ?????? ????????? ?????? 101f??? ???????????? ??????
                axisMinimum = 0f // ????????? 0
                granularity = 50f // 50 ???????????? ?????? ???????????? ??????.
                setDrawLabels(false) // ??? ????????? ?????? (0, 50, 100)
                //setDrawGridLines(true) //?????? ?????? ??????
                setDrawAxisLine(false) // ??? ????????? ??????
                setDrawZeroLine(false)
                setDrawGridLines(false)
                axisLineColor = ContextCompat.getColor(
                    context,
                    R.color.design_default_color_secondary_variant
                ) // ??? ?????? ??????
                gridColor = ContextCompat.getColor(
                    context,
                    R.color.design_default_color_on_secondary
                ) // ??? ?????? ?????? ?????? ??????
                textColor = ContextCompat.getColor(
                    context,
                    R.color.design_default_color_primary_dark
                ) // ?????? ????????? ?????? ??????
                textSize = 13f //?????? ????????? ??????
            }
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM //X?????? ??????????????? ??????.
                granularity = 1f // 1 ???????????? ?????? ??????
                setDrawAxisLine(true) // ??? ??????
                setDrawGridLines(false) // ??????
                textColor = ContextCompat.getColor(
                    context,
                    R.color.design_default_color_primary_dark
                ) //?????? ??????
                textSize = 12f // ????????? ??????
                valueFormatter = MyXAxisFormatter() // X??? ?????????(?????? ???????????? ??????) ???????????? ?????? ??????
            }
            axisRight.isEnabled = false // ????????? Y?????? ???????????? ??????.
            setTouchEnabled(false) // ????????? ???????????? ?????? ???????????? ??????
            animateY(1000) // ??????????????? ???????????? ??????????????? ??????
            legend.isEnabled = false //?????? ?????? ??????
        }

        var set = LineDataSet(entries, "DataSet") // ???????????? ?????????
        set.color = ContextCompat.getColor(
            applicationContext!!,
            R.color.design_default_color_primary_dark
        ) // ??? ????????? ??? ??????

        val dataSet: ArrayList<ILineDataSet> = ArrayList()
        dataSet.add(set)
        val data = LineData(dataSet)
        //data. = 0.3f //?????? ?????? ??????
        barChart.run {
            this.data = data //????????? ???????????? data??? ????????????.
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
            dlg.setTitle("?????? ?????? ??????")
            dlg.setView(dialogView)
            dlgStockName = dialogView.findViewById<EditText>(R.id.StockName)
            dlgStockPrice = dialogView.findViewById<EditText>(R.id.StockPrice)
            dlgStockCount = dialogView.findViewById<EditText>(R.id.StockCount)
            dlg.setPositiveButton("??????") { dialog, which ->
                //var toast1 = Toast(this@Menu)
                //toastText.text = "?????? ?????? ??????"
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
                    //Toast.makeText(this, "${tempStock.stockName}???/??? ?????????????????????.",Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "${tempStock.stockName}???/??? ?????????????????????.",Toast.LENGTH_SHORT).show()
                    newstockList.add(Listviewitem(dlgStockName.text.toString(), stockSum / Integer.parseInt(dlgStockCount.text.toString()), Integer.parseInt(dlgStockCount.text.toString())))
                    adapter.notifyDataSetChanged()

                }
                else if(flag == 2){
                    Toast.makeText(this, "?????? ???????????? ???????????????.",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "??????????????? ???????????????.",Toast.LENGTH_SHORT).show()
                }
            //adapter.notifyDataSetChanged()
                //toast1.setGravity(Gravity.CENTER, 0, -800)
                //toast1.show()
            }
            dlg.setNegativeButton("??????") { dialog, which ->
                //var toast2 = Toast(this@Menu)
                //toastText.text = "??????"
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
                    for(i in stockList){
                        if(i.stockCode == StockCode) {
                            flag = 2
                        }
                    }
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
