package com.tdec.chartsample.ui.hiChart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
            }
        })

        viewModel.data.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val data =binding.hiChartView.options.series[0].data
                data.removeAt(0)
                data += it
                binding.hiChartView.hiChartView.options.series[0].data = data
            }
        })

        viewModel.setOptions()

        viewModel.startGetData()
    }
}