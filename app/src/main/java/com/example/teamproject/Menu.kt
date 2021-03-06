package com.example.teamproject

import android.app.Activity
import android.app.AlertDialog
import android.app.TabActivity
import android.content.Context
import android.content.Intent
import android.icu.util.ULocale.getName
import android.os.Bundle
import android.preference.PreferenceActivity
import android.text.Layout
import android.util.Log
import android.app.WallpaperManager
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
import android.media.MediaPlayer
import com.github.mikephil.charting.charts.BarChart
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.items.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.addstock.*
import kotlinx.android.synthetic.main.addstock.view.*
import kotlin.collections.HashMap as HashMap
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.teamproject.databinding.ActivityMainBinding
import kotlin.concurrent.timer


@Suppress("deprecation")
class Menu : TabActivity() {
    lateinit var wallpaperIntent: Intent
    lateinit var naturebutton : ImageButton
    lateinit var originalbutton : ImageButton
    lateinit var notifysound : Switch
    lateinit var gallery : TextView
    lateinit var user : User
    lateinit var stockAddBtn: Button
    lateinit var logoutbtn: ImageButton
    lateinit var toastText: TextView
    lateinit var stockListView: ListView
    lateinit var tempStock: Stock
    lateinit var resultStockCodeDB:TextView
    lateinit var stockList : ArrayList<Stock>
    lateinit var startList : ArrayList<Stock>
    lateinit var myMoney : TextView
    lateinit var adapter : ListViewAdapter
    lateinit var stockChangeList : ArrayList<Int>
    lateinit var date : ArrayList<String>
    lateinit var email : TextView
    lateinit var todayRevenue : TextView
    lateinit var todayRevenuePer : TextView
    lateinit var interval : TextView
    lateinit var period : TextView

    var time : Long = 60000
    var total : Int = 0
    var startTotal : Int = 0
    var prevTotal : Int = 0
    var max : Float = 0f
    var min : Float = 1000000000f
    var newstockList = mutableListOf<Listviewitem>()
    var wallflag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        var intent = intent
        user = intent.getSerializableExtra("user") as User
        var id = intent.getSerializableExtra("id") as String
        var stockAddBtn = findViewById<Button>(R.id.stockAddBtn)
        var logoutbtn = findViewById<ImageButton>(R.id.logoutbtn)
        var barChart: LineChart = findViewById(R.id.barChart)
        val entries = ArrayList<Entry>()
        var nameText: TextView = findViewById(R.id.nameText)
        var myMoney = findViewById<TextView>(R.id.myMoneyText)
        var stockChangeNum = user.getChangeNum()
        todayRevenue = findViewById(R.id.todayRevenue)
        todayRevenuePer = findViewById(R.id.todayRevenuePercent)
        stockChangeList = user.stockChange
        total = stockChangeList[stockChangeNum-1]
        startTotal = stockChangeList[0]
        if(stockChangeNum > 2){
            prevTotal =stockChangeList[stockChangeNum - 2]
        }
        else{
            prevTotal = stockChangeList[0]
        }
        stockList = user.getStock()
        startList = user.startStock
        date = user.date
        tempStock = Stock()
        todayRevenue.text = (total - startTotal).toString() + "???"
        if(prevTotal == 0) {
            todayRevenuePer.text = "0%"
        }
        else{
            todayRevenuePer.text = String.format("%.2f",(((total - prevTotal).toFloat() * 100 / prevTotal))) + "%"
        }
        stockListView = findViewById<ListView>(R.id.stockList) as ListView
        email = findViewById(R.id.email_connect)
        email.text = user.id + "@gmail.com"
        adapter = ListViewAdapter(newstockList)
        for(i in stockList){
            newstockList.add(Listviewitem(i.stockCode, i.stockName, i.stockPrice / i.stockNum, i.stockNum))
        }
        stockListView.adapter = adapter
        if(stockChangeNum > 0)
        {
            myMoney.text = total.toString()+ "???"
        }
        nameText.text = "???????????????," +user.getname()+ "???"
        var j : Float

        j = 1.2f

        for (i in 0..stockChangeNum - 1) {
            entries.add(Entry(j, stockChangeList[i]/10000.toFloat()))
            //Log.i("stockchangeList", "Got value ${stockChangeList[i]}")
            if(max < stockChangeList[i].toFloat())
                max = stockChangeList[i].toFloat()
            if(min > stockChangeList[i].toFloat())
                min = stockChangeList[i].toFloat()
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
            description.isEnabled = false // ?????? ?????? ????????? ???????????? description??? ???????????? ?????? (false)
            setMaxVisibleValueCount(stockChangeNum-1) // ?????? ????????? ????????? ????????? 5?????? ??????
            setPinchZoom(false) // ?????????(?????????????????? ?????? ??? ???????????????) ??????
            //setDrawLineShadow(false) //???????????? ?????????
            setDrawGridBackground(false)//???????????? ????????????

            axisLeft.run { //?????? ???. ??? Y?????? ?????? ?????????.
                axisMaximum = (max/10000*1.2f) //100 ????????? ?????? ????????? ?????? 101f??? ???????????? ??????
                axisMinimum = (min/10000*0.8f) // ????????? 0
                setDrawLabels(true) // ??? ????????? ?????? (0, 50, 100)
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
                textSize = 8f //?????? ????????? ??????
            }
            xAxis.run {
                isEnabled = false
                position = XAxis.XAxisPosition.BOTTOM //X?????? ??????????????? ??????.
                granularity = 1f // 1 ???????????? ?????? ??????
                setDrawAxisLine(true) // ??? ??????
                setDrawGridLines(false) // ??????
                textColor = ContextCompat.getColor(
                    context,
                    R.color.design_default_color_primary_dark
                ) //?????? ??????
                textSize = 12f // ????????? ??????
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
        set.lineWidth = 3f

        val dataSet: ArrayList<ILineDataSet> = ArrayList()
        dataSet.add(set)
        val data = LineData(dataSet)
        //data. = 0.3f //?????? ?????? ??????
        barChart.run {
            this.data = data //????????? ???????????? data??? ????????????.
            //setFitBars(true)
            invalidate()
        }
        naturebutton = findViewById<ImageButton>(R.id.natureBtn)
        originalbutton = findViewById<ImageButton>(R.id.originalBtn)
        notifysound = findViewById<Switch>(R.id.notifySound)
        var mediaplayer : MediaPlayer = MediaPlayer.create(this, R.raw.alarm)
        val nature: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.nature)
        val original: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.original)
        val wallpaperManager = WallpaperManager.getInstance(baseContext)
        val naturewall= resources.obtainTypedArray(R.array.nature)
        val originwall = resources.obtainTypedArray(R.array.origin)
        //var revenue : Float = (((total - startTotal) * 100 / startTotal).toFloat())
        val database2 = FirebaseDatabase.getInstance()
        val myRef2 = database2.reference
        var wallchangelist = ArrayList<Int>(0)
        var wallchangenum : Int = 0

        interval = findViewById<TextView>(R.id.interval)
        period = findViewById<TextView>(R.id.period)
        interval.setOnClickListener {
            val IDialogView = LayoutInflater.from(this).inflate(R.layout.interval, null)
            var items = arrayOf("1???", "10???", "30???", "1??????", "3??????", "6??????")
            val IBuilder = AlertDialog.Builder(this)
                .setView(IDialogView)
                .setTitle("????????? ????????? ???????????????")
                .setItems(items) { dialog, which ->
                    Toast.makeText(this, "${items[which]} ???????????? ???????????????.", Toast.LENGTH_SHORT).show()
                    if (which == 0) time = 10000
                    else if (which == 1) time = 60000
                    else if (which == 2) time = 1800000
                    else if (which == 3) time = 3600000
                    else if (which == 4) time = 10800000
                    else if (which == 5) time = 21600000
                    period.setText("${items[which]}")
                    Log.d("time", time.toString()+"?????????")
                    timer(period = time)
                    {
                        var wallchangelist = ArrayList<Int>(0)
                        var wallchangenum : Int = 0
                        myRef2.child("user").child(id).get().addOnSuccessListener{
                            wallchangelist = it.child("stockChange").value as ArrayList<Int>
                            wallchangenum = Integer.parseInt(it.child("changeNum").value.toString())
                            var revenue : Float = ((wallchangelist[wallchangenum-1].toFloat() - wallchangelist[wallchangenum-2].toFloat())/wallchangelist[wallchangenum-2].toFloat()*100)
                            if(wallchangenum > 2) {
                                revenue = ((wallchangelist[wallchangenum - 1].toFloat() - wallchangelist[wallchangenum - 2].toFloat()) / wallchangelist[wallchangenum - 2].toFloat() * 100)
                            }
                            else{
                                revenue = 0f
                            }
                            Log.i("timer","${revenue}")
                            if (wallflag == 0) {
                                if (revenue >= 5f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.nature0))
                                else if (revenue >= 0.5f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.nature1))
                                else if (revenue == 0f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.nature2))
                                else if (revenue <= -5f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.nature4))
                                else if (revenue < 0f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.nature3))
                                if (notifysound.isChecked == true) mediaplayer.start()
                            }
                            else{
                                if (revenue >= 5f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.original))
                                else if (revenue >= 0.5f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.original1))
                                else if (revenue == 0f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.original2))
                                else if (revenue <= -5f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.original4))
                                else if (revenue < 0f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.original3))
                                if (notifysound.isChecked == true) mediaplayer.start()
                            }
                        }.addOnFailureListener{
                        }
                    }
                }
                .show()
        }
        naturebutton.setOnClickListener {
            Toast.makeText(this, "???????????? ????????? ??????.(??????)", Toast.LENGTH_SHORT).show()
            wallflag = 0
            myRef2.child("user").child(id).get().addOnSuccessListener{
                wallchangelist = it.child("stockChange").value as ArrayList<Int>
                wallchangenum = Integer.parseInt(it.child("changeNum").value.toString())
                var revenue : Float = ((wallchangelist[wallchangenum-1].toFloat() - wallchangelist[wallchangenum-2].toFloat())/wallchangelist[wallchangenum-2].toFloat()*100)
                if(wallchangenum > 2) {
                    revenue = ((wallchangelist[wallchangenum - 1].toFloat() - wallchangelist[wallchangenum - 2].toFloat()) / wallchangelist[wallchangenum - 2].toFloat() * 100)
                }
                else{
                    revenue = 0f
                }
                if (revenue >= 5f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.nature0))
                else if (revenue >= 0.5f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.nature1))
                else if (revenue == 0f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.nature2))
                else if (revenue <= -5f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.nature4))
                else if (revenue < 0f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.nature3))
                if (notifysound.isChecked == true) mediaplayer.start()
            }
        }
        originalbutton.setOnClickListener {
            Toast.makeText(this, "???????????? ????????? ??????.(??????)", Toast.LENGTH_SHORT).show()
            wallflag = 1
            myRef2.child("user").child(id).get().addOnSuccessListener{
                wallchangelist = it.child("stockChange").value as ArrayList<Int>
                wallchangenum = Integer.parseInt(it.child("changeNum").value.toString())
                var revenue : Float = ((wallchangelist[wallchangenum-1].toFloat() - wallchangelist[wallchangenum-2].toFloat())/wallchangelist[wallchangenum-2].toFloat()*100)
                if(wallchangenum > 2) {
                    revenue = ((wallchangelist[wallchangenum - 1].toFloat() - wallchangelist[wallchangenum - 2].toFloat()) / wallchangelist[wallchangenum - 2].toFloat() * 100)
                }
                else{
                    revenue = 0f
                }
                if (revenue >= 5f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.original))
                else if (revenue >= 0.5f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.original1))
                else if (revenue == 0f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.original2))
                else if (revenue <= -5f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.original4))
                else if (revenue < 0f) wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.original3))
                if (notifysound.isChecked == true) mediaplayer.start()
            }
        }

        // ?????????
        gallery = findViewById<TextView>(R.id.gallery)
        gallery.setOnClickListener {
            val intent = Intent(this,Gallery::class.java)
            startActivity(intent)
        }

        logoutbtn.setOnClickListener {
            Toast.makeText(this, "????????????",Toast.LENGTH_SHORT).show()
            finish()
        }

        stockAddBtn.setOnClickListener {
            var flag = 0
            var StockCode = ""
            var dlgStockName: EditText
            var dlgStockPrice: EditText
            var dlgStockCount: EditText
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.addstock, null)
            val mBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("")
            val  mAlertDialog = mBuilder.show()

            dlgStockName = mAlertDialog.StockName
            dlgStockPrice = mAlertDialog.StockPrice
            dlgStockCount = mAlertDialog.StockCount

            mDialogView.stockSearchBtn.setOnClickListener {
                val database = FirebaseDatabase.getInstance()
                val myRef = database.reference
                var tempStockName = dlgStockName.text.toString()
                if(tempStockName == ""){
                    Toast.makeText(this, "??????????????? ???????????????.",Toast.LENGTH_SHORT).show()
                }
                resultStockCodeDB = mAlertDialog.StockCodeDB
                myRef.child("stock").child(tempStockName).get().addOnSuccessListener {
                    //Log.i("firebase", "Got value ${it.value}")
                    if(tempStockName !="") {
                        resultStockCodeDB.text = it.value.toString()
                    }
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

            mDialogView.dlgAddBtn.setOnClickListener {
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
                    startTotal += stockSum
                    stockList.add(tempStock)
                    startList.add(tempStock)
                    stockNumber = user.getstockNumber()
                    var userRef= database.getReference()
                    userRef.child("user").child(user.id).child("startStock").child(stockNumber.toString()).setValue(tempStock)
                    userRef.child("user").child(user.id).child("currentStock").child(stockNumber.toString()).setValue(tempStock)
                    stockNumber += 1
                    user.setstockNumber(stockNumber)
                    userRef.child("user").child(user.id).child("stockNumber").setValue(stockNumber)
                    userRef.child("user").child(user.id).child("date").setValue(date)
                    //Toast.makeText(this, "${tempStock.stockName}???/??? ?????????????????????.",Toast.LENGTH_SHORT).show()
                    if(stockChangeNum == 1){
                        var now = Date()
                        var sFormat : SimpleDateFormat
                        sFormat = SimpleDateFormat("yyyy-MM-dd")
                        date.add(sFormat.format(now))
                        stockChangeList.add(0,stockSum)
                    }
                    else{
                        for(i in 0..stockChangeNum - 1) {
                            stockChangeList[i] += stockSum
                        }
                    }
                    myMoney.text = total.toString() + "???"
                    userRef.child("user").child(user.id).child("stockChange").setValue(stockChangeList)
                    Toast.makeText(this, "${tempStock.stockName}???/??? ?????????????????????.",Toast.LENGTH_SHORT).show()
                    prevTotal += stockSum
                    todayRevenue.text = (total - prevTotal).toString() + "???"
                    if(prevTotal == 0) {
                        todayRevenuePer.text = "0%"
                    }
                    else{
                        todayRevenuePer.text = String.format("%.2f",(((total - prevTotal).toFloat() * 100 / prevTotal))) + "%"
                    }
                    newstockList.add(Listviewitem(StockCode, dlgStockName.text.toString(), stockSum / Integer.parseInt(dlgStockCount.text.toString()), Integer.parseInt(dlgStockCount.text.toString())))

                    adapter.notifyDataSetChanged()

                    j = 1.2f
                    max = 0f
                    min = 10000000000f
                    entries.clear()

                    for (i in 0..stockChangeNum - 1) {
                        entries.add(Entry(j, stockChangeList[i]/10000.toFloat()))
                        //Log.i("stockchangeList", "Got value ${stockChangeList[i]}")
                        if(max < stockChangeList[i].toFloat())
                            max = stockChangeList[i].toFloat()
                        if(min > stockChangeList[i].toFloat())
                            min = stockChangeList[i].toFloat()
                        j = j + 1
                    }



                    barChart.run {
                        description.isEnabled = false // ?????? ?????? ????????? ???????????? description??? ???????????? ?????? (false)
                        setMaxVisibleValueCount(stockChangeNum-1) // ?????? ????????? ????????? ????????? 5?????? ??????
                        setPinchZoom(false) // ?????????(?????????????????? ?????? ??? ???????????????) ??????
                        //setDrawLineShadow(false) //???????????? ?????????
                        setDrawGridBackground(false)//???????????? ????????????

                        axisLeft.run { //?????? ???. ??? Y?????? ?????? ?????????.
                            axisMaximum = (max/10000*1.2f) //100 ????????? ?????? ????????? ?????? 101f??? ???????????? ??????
                            axisMinimum = (min/10000*0.8f) // ????????? 0
                            granularity = 20f // 50 ???????????? ?????? ???????????? ??????.
                            setDrawLabels(true) // ??? ????????? ?????? (0, 50, 100)
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
                            textSize = 8f //?????? ????????? ??????
                        }
                        xAxis.run {
                            isEnabled = false
                            position = XAxis.XAxisPosition.BOTTOM //X?????? ??????????????? ??????.
                            granularity = 1f // 1 ???????????? ?????? ??????
                            setDrawAxisLine(true) // ??? ??????
                            setDrawGridLines(false) // ??????
                            textColor = ContextCompat.getColor(
                                context,
                                R.color.design_default_color_primary_dark
                            ) //?????? ??????
                            textSize = 12f // ????????? ??????
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
                    set.lineWidth = 3f

                    val dataSet: ArrayList<ILineDataSet> = ArrayList()
                    dataSet.add(set)
                    val data = LineData(dataSet)
                    //data. = 0.3f //?????? ?????? ??????
                    barChart.run {
                        this.data = data //????????? ???????????? data??? ????????????.
                        //setFitBars(true)
                        invalidate()
                    }

                }
                else if(flag == 2){
                    Toast.makeText(this, "?????? ???????????? ???????????????.",Toast.LENGTH_SHORT).show()
                }
                else if(flag == 3){
                    Toast.makeText(this, "???????????? ?????? ???????????????.",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this, "??????????????? ???????????????.",Toast.LENGTH_SHORT).show()
                }
                mAlertDialog.dismiss()
            }

            mDialogView.dlgClose.setOnClickListener {
                mAlertDialog.dismiss()
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
            convertView!!.item_code.text = item.code
            convertView.item_name.text = item.name
            convertView.item_price.text = item.price.toString()
            convertView.item_count.text = item.num.toString()
            return convertView
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            var setupArray = ArrayList<Int>(1)
            var index = data!!.getIntExtra("index", 0)
            var sum = data!!.getIntExtra("sum", 0)
            //Log.i("sum", "${sum}")
            val database = FirebaseDatabase.getInstance()
            var userRef = database.reference.child("user").child(user.id)
            var stockNum = user.getstockNumber()
            var stockchangeNum = user.getChangeNum()
            var tempStock = Stock()
            var newDate = ArrayList<String>(1)
            var now = Date()
            var sFormat : SimpleDateFormat
            var stockName = stockList[index].stockName
            sFormat = SimpleDateFormat("yyyy-MM-dd")
            total -= sum
            startTotal -= startList[index].stockPrice
            Log.i("startSum", "${startTotal}")
            setupArray.add(startTotal)
            setupArray.add(total)
            newDate.add(date[0])
            newDate.add(sFormat.format(now))
            stockList.removeAt(index)
            startList.removeAt(index)
            //Log.i("code", "${stockList}")
            newstockList.removeAt(index)
            stockNum -= 1
            myMoney = findViewById(R.id.myMoneyText)
            myMoney.text = total.toString()
            user.setstockNumber(stockNum)
            user.stockChange = setupArray
            adapter.notifyDataSetChanged()
            if(stockNum == 0){
                startList.add(tempStock)
                stockList.add(tempStock)
            }
            userRef.child("startStock").setValue(startList)
            userRef.child("currentStock").setValue(stockList)
            userRef.child("stockNumber").setValue(stockNum)
            userRef.child("stockChange").setValue(setupArray)
            userRef.child("changeNum").setValue(2)
            userRef.child("date").setValue(newDate)
            Toast.makeText(this, "${stockName}???/??? ?????????????????????.",Toast.LENGTH_SHORT).show()
            prevTotal = startTotal
            todayRevenue.text = (total - prevTotal).toString() + "???"
            if(prevTotal == 0) {
                todayRevenuePer.text = "0%"
            }
            else{
                todayRevenuePer.text = String.format("%.2f",(((total - prevTotal).toFloat() * 100 / prevTotal))) + "%"
            }
        }
        if(resultCode == Activity.RESULT_FIRST_USER){ // ???????????????
            var index = data!!.getIntExtra("index", 0)
            var startStock = data.getSerializableExtra("startStock") as Stock
            var currentStock = data.getSerializableExtra("currentStock") as Stock
            var modStockChange = ArrayList<Int>(1)
            var modDate = ArrayList<String>(1)
            var sumStart = startStock.stockPrice - startList[index].stockPrice
            var sumCurrent = currentStock.stockPrice - stockList[index].stockPrice
            var now = Date()
            var sFormat : SimpleDateFormat
            myMoney = findViewById(R.id.myMoneyText)
            val database = FirebaseDatabase.getInstance()
            var userRef = database.reference.child("user").child(user.id)
            sFormat = SimpleDateFormat("yyyy-MM-dd")
            startList[index] = startStock
            stockList[index] = currentStock
            modStockChange.add(stockChangeList[0] + sumStart)
            modStockChange.add(stockChangeList[user.getChangeNum() - 1] + sumCurrent)
            modDate.add(date[0])
            modDate.add(sFormat.format(now))
            userRef.child("date").setValue(modDate)
            userRef.child("stockChange").setValue(modStockChange)
            userRef.child("startStock").setValue(startList)
            userRef.child("currentStock").setValue(stockList)
            userRef.child("changeNum").setValue(2)
            total += sumCurrent
            startTotal += sumStart
            myMoney.text = total.toString()
            newstockList.set(index, Listviewitem(stockList[index].stockCode,stockList[index].stockName, stockList[index].stockPrice / stockList[index].stockNum, stockList[index].stockNum))
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "${stockList[index].stockName}???/??? ?????????????????????.",Toast.LENGTH_SHORT).show()
            prevTotal = startTotal
            todayRevenue.text = (total - prevTotal).toString() + "???"
            if(prevTotal == 0) {
                todayRevenuePer.text = "0%"
            }
            else{
                todayRevenuePer.text = String.format("%.2f",(((total - prevTotal).toFloat() * 100 / prevTotal))) + "%"
            }
        }
    }
}