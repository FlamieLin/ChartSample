package com.tdec.chartsample.ui.eChart

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

class EChartViewModel : ViewModel() {

    var isInitFinish = false

    val chartUrl = "file:///android_asset/ECharts/html/ECharts.html"

    private val _chartJavascript = MutableLiveData("")
    val chartJavascript: LiveData<String> = _chartJavascript

    fun valueCallBack(id: Int, result: String) {
        when (id) {
            R.id.echart -> {
                if (result.isNotEmpty()) Log.i("eChart返回", result)
            }
        }
    }

    fun createChart() {
        _chartJavascript.value = "javascript:createChart()"
        _chartJavascript.value = ""
    }

    private lateinit var job: CompletableJob
    fun startSetChartData() {
        job = Job()
        viewModelScope.launch(Dispatchers.IO + job) {
            while (this.isActive) {
                if (isInitFinish) {
                    setChartData(getXData(), getChartData())
                }
                delay(1000)
            }
        }
    }

    fun stopSetChartData() {
        if (this::job.isInitialized && job.isActive) {
            job.cancel()
        }
    }

    private suspend fun setChartData(xData: IntArray, valueData: IntArray) {
        val json = JSONObject()
        json.put("xAxis", JSONObject().put("data", JSONArray(xData)))
        json.put("series", JSONArray().put(JSONObject().put("data", JSONArray(valueData))))

        withContext(Dispatchers.Main) {
            _chartJavascript.value = "javascript:setData(${json})"
            _chartJavascript.value = ""
        }
    }

    private val xLinkedList = LinkedList<Int>().also {
        it.addAll(arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20))
    }

    private fun getXData(): IntArray {
        return xLinkedList.apply {
            val num = removeFirst() + 20
            add(num)
        }.toIntArray()
    }

    private val chartLinkedList = LinkedList<Int>().also {
        it.addAll(arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))
    }

    private fun getChartData(): IntArray {
        return chartLinkedList.apply {
            removeFirst()
            add(Random.nextInt(1, 2000))
        }.toIntArray()
    }
}