package com.example.teamproject

import android.app.TabActivity
import android.os.Bundle
import android.widget.TabHost
import android.widget.TabHost.TabSpec

@Suppress("deprecation")
class Menu : TabActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

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

        var tabSpecTemp = tabHost.newTabSpec("temp").setIndicator("temp")
        tabSpecTemp.setContent(R.id.temp)
        tabHost.addTab(tabSpecTemp)

        tabHost.currentTab = 0
    }
}