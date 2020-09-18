package com.tdec.chartsample.application

import android.app.Application

class ChartSampleApplication: Application() {

    companion object {
        lateinit var instance: ChartSampleApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}