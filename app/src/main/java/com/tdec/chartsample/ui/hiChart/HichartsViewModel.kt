package com.tdec.chartsample.ui.hiChart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.highsoft.highcharts.common.hichartsclasses.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class HichartsViewModel : ViewModel() {

    private val _options = MutableLiveData<HIOptions>()
    val options: LiveData<HIOptions> = _options

    private val _yData = MutableLiveData<Float?>()
    val yData: LiveData<Float?> = _yData

    private val _xData = MutableLiveData<Float?>()
    val xData: LiveData<Float?> = _xData

    fun setOptions() {
        val options = HIOptions()
        val chart = HIChart()
        chart.animation = HIAnimationOptionsObject().also {
            it.duration = 0
        }
        options.chart = chart
        val title = HITitle()
        title.text = "测试图表"
        options.title = title
        val xAxis = HIXAxis()
        val categories = ArrayList(listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"))
        xAxis.categories = categories
        options.xAxis = ArrayList(Collections.singletonList(xAxis))
        val series = HILine()
        series.data = ArrayList(listOf(49.9, 71.5, 106.4, 129.2, 144, 176, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4))
        options.series = ArrayList(Collections.singletonList(series))
        options.credits = HICredits().also {
            it.enabled = false
        }
        options.chart.animation = null

        _options.value = options
    }

    fun startGetData() {
        viewModelScope.launch(Dispatchers.IO) {
            while (this.isActive) {
                setData(Random().nextFloat() * 100)
                delay(1000)
            }
        }
    }

    private suspend fun setData(data: Float) {
        withContext(Dispatchers.Main) {
            _yData.value = data
            _yData.value = null
        }
    }
}