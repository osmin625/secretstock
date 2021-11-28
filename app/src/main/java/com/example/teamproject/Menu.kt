package com.example.teamproject

import android.app.AlertDialog
import android.app.TabActivity
import android.content.Context
import android.icu.util.ULocale.getName
import android.os.Bundle
import android.util.Log
import android.view.Gravity
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


@Suppress("deprecation")
class Menu : TabActivity() {


    lateinit var stockAddBtn: Button
    lateinit var dlgStockName: EditText
    lateinit var dlgStockPrice: EditText
    lateinit var dlgStockCount: EditText
    lateinit var toastText: TextView
    lateinit var dialogView: View
    lateinit var stockListView: ListView
    lateinit var temp: Stock
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        var intent = intent
        var user = intent.getSerializableExtra("user") as User
//        Toast.makeText(this, "${user.getStock()}.", Toast.LENGTH_SHORT).show()
        var stockList = ArrayList<Stock>()
        stockList = user.getStock()
        var stockAddBtn = findViewById<Button>(R.id.stockAddBtn)
        stockListView = findViewById<ListView>(R.id.stockList) as ListView
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stockList)
        stockListView.adapter = adapter
        var barChart: LineChart = findViewById(R.id.barChart)
        val entries = ArrayList<Entry>()
        entries.add(Entry(1.2f, 20.0f))
        entries.add(Entry(2.2f, 70.0f))
        entries.add(Entry(3.2f, 30.0f))
        entries.add(Entry(4.2f, 90.0f))
        entries.add(Entry(5.2f, 70.0f))




        var tabHost = this.tabHost

        var tabSpecChart = tabHost.newTabSpec("Chart").setIndicator("차트")
        tabSpecChart.setContent(R.id.Chart)
        tabHost.addTab(tabSpecChart)

        var tabSpecStock = tabHost.newTabSpec("Stock").setIndicator("주식")
        tabSpecStock.setContent(R.id.Stock)
        tabHost.addTab(tabSpecStock)

        var tabSpecWallpaper = tabHost.newTabSpec("Wallpaper").setIndicator("배경")
        tabSpecWallpaper.setContent(R.id.Wallpaper)
        tabHost.addTab(tabSpecWallpaper)

        var tabSpecSetting = tabHost.newTabSpec("Setting").setIndicator("설정")
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
                axisMaximum = 101f //100 위치에 선을 그리기 위해 101f로 맥시멈값 설정
                axisMinimum = 0f // 최소값 0
                granularity = 50f // 50 단위마다 선을 그리려고 설정.
                setDrawLabels(true) // 값 적는거 허용 (0, 50, 100)
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


//        stockListView.setOnClickListener{parent, view, position, id ->
//            Toast.makeText(applicationContext, stockList[position, to])
//        }

        stockAddBtn.setOnClickListener {
            dialogView = View.inflate(this@Menu, R.layout.addstock, null)
            var dlg = AlertDialog.Builder(this@Menu)
            dlg.setTitle("주식 정보 입력")
            dlg.setView(dialogView)
            dlgStockName = dialogView.findViewById<EditText>(R.id.StockName)
            dlgStockPrice = dialogView.findViewById<EditText>(R.id.StockPrice)
            dlgStockCount = dialogView.findViewById<EditText>(R.id.StockCount)
            dlg.setPositiveButton("확인") { dialog, which ->
                //var toast1 = Toast(this@Menu)
                //toastText.text = "주식 추가 완료"
                stockList.add(temp)
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
        }


    }


    inner class MyXAxisFormatter : ValueFormatter() {
        private val days = arrayOf("mon", "tue", "wed", "thu", "fri")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt() - 1) ?: value.toString()
        }
    }
}

private fun <E> ArrayList<E>.add(element: EditText?) {

}
