package com.tdec.chartsample.ui.hiChartWeb

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tdec.chartsample.R
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.random.Random

class HIChartWebViewModel : ViewModel() {

    val chartUrl = "file:///android_asset/HighCharts/html/highcharts.html"

    private val _chartJavascript: MutableLiveData<String> = MutableLiveData("")
    val chartJavascript: LiveData<String> = _chartJavascript

    fun createChart() {
        _chartJavascript.value = "javascript:createChart()"
        _chartJavascript.value = ""
    }

    private val job = Job()
    fun startSetChartData() {
        viewModelScope.launch(Dispatchers.IO + job) {
            while (this.isActive) {
                setChartData(getXData(), getChartData(chartLinkedList1), getChartData(chartLinkedList2))
                delay(10000)
            }
        }
    }

    private suspend fun setChartData(xData: Array<String>, valueData1: IntArray, valueData2: IntArray) {
        val json = JSONObject()
        json.put("xAxis", JSONObject().put("data", JSONArray(xData)))
        json.put(
            "series",
            JSONArray().put(JSONObject().put("data", JSONArray(valueData1)))
                .put(JSONObject().put("data", JSONArray(valueData2)))
        )

        withContext(Dispatchers.Main) {
            _chartJavascript.value = "javascript:setData(${json})"
            _chartJavascript.value = ""
        }
    }

    private val xLinkedList = LinkedList<String>().also {
        it.addAll(arrayOf("苹果", "香蕉", "橙子", "西瓜", "菠萝", "葡萄", "芒果"))
    }

    private fun getXData(): Array<String> {
        return xLinkedList.apply {
            val type = removeFirst()
            add(type)
        }.toTypedArray()
    }

    private val chartLinkedList1 = LinkedList<Int>().also {
        it.addAll(arrayOf(1, 0, 4, 2, 3, 9, 1))
    }
    private val chartLinkedList2 = LinkedList<Int>().also {
        it.addAll(arrayOf(5, 7, 3, 2, 2, 4, 12))
    }

    private fun getChartData(chartLinkedList: LinkedList<Int>): IntArray {
        return chartLinkedList.apply {
            removeFirst()
            add(Random.nextInt(0, 20))
        }.toIntArray()
    }

    fun valueCallBack(id: Int, result: String) {
        when (id) {
            R.id.hiChartView -> {
                if (result.isNotEmpty()) Log.i("hiChart返回", result)
            }
        }
    }
}