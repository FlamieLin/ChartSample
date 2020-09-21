package com.tdec.chartsample.ui.hiChart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.highsoft.highcharts.common.hichartsclasses.HIAnimationOptionsObject
import com.highsoft.highcharts.common.hichartsclasses.HIPlotOptions
import com.highsoft.highcharts.common.hichartsclasses.HISeries
import com.tdec.chartsample.databinding.HichartsFragmentBinding
import kotlinx.android.synthetic.main.hicharts_fragment.view.*

class HichartsFragment : Fragment() {

    private val viewModel: HichartsViewModel by viewModels()
    private lateinit var binding: HichartsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return HichartsFragmentBinding.inflate(inflater, container, false).also { binding ->
            this.binding = binding
            binding.viewModel = viewModel
            binding.lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.options.observe(viewLifecycleOwner, Observer { options ->
            if (options != null) {
                binding.hiChartView.options = options
                val plotOptions = HIPlotOptions()
                val series = HISeries()
                val animationObject = HIAnimationOptionsObject()
                animationObject.duration = 0
                series.animation = animationObject
                plotOptions.series = series
                binding.hiChartView.options.plotOptions = plotOptions
            }
        })

        viewModel.yData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val options = binding.hiChartView.options
                val categories = options.xAxis[0].categories.also { categories ->
                    val x = categories.removeAt(0)
                    categories += x

                }
                val series = options.series[0].also { hiSeries ->
                    val yData = hiSeries.data
                    yData.removeAt(0)
                    yData += it
                }
                series.animate(false)

                binding.hiChartView.hiChartView.options.xAxis[0].categories = categories
                binding.hiChartView.hiChartView.options.series[0].update(series)
            }
        })

        viewModel.setOptions()

        viewModel.startGetData()
    }
}