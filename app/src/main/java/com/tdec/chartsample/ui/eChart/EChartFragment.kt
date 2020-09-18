package com.tdec.chartsample.ui.eChart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tdec.chartsample.databinding.EChartFragmentBinding

class EChartFragment : Fragment() {

    private val viewModel: EChartViewModel by viewModels()

    private lateinit var binding: EChartFragmentBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return EChartFragmentBinding.inflate(inflater, container, false).also { binding ->
            this.binding = binding
            binding.viewModel = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
            binding.echart.also { chart ->
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
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel

        viewModel.chartJavascript.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) {
                binding.echart.evaluateJavascript(it) { result ->
                    viewModel.valueCallBack(binding.echart.id, result)
                }
            }
        })
    }

}