package com.tdec.chartsample.ui.hiChartWeb

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tdec.chartsample.R
import com.tdec.chartsample.databinding.HIChartWebFragmentBinding

class HIChartWebFragment : Fragment() {

    private val viewModel: HIChartWebViewModel by viewModels()
    private lateinit var binding: HIChartWebFragmentBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return HIChartWebFragmentBinding.inflate(inflater, container, false).also { binding ->
            binding.viewModel = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
            binding.hiChart.also { chart ->
                chart.settings.javaScriptEnabled = true
                chart.loadUrl(viewModel.chartUrl)
                chart.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        viewModel.createChart()
                        viewModel.startSetChartData()
                    }
                }
            }
            this.binding = binding
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.chartJavascript.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                binding.hiChart.evaluateJavascript(it) { result ->
                    viewModel.valueCallBack(binding.hiChart.id, result)
                }
            }
        })
    }

}