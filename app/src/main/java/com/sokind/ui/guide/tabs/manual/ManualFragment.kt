package com.sokind.ui.guide.tabs.manual

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sokind.R
import com.sokind.data.remote.guide.Manual
import com.sokind.databinding.FragmentManualBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.OnManualItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Completable
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ManualFragment : BaseFragment<FragmentManualBinding>(R.layout.fragment_manual) {
    private val viewModel by viewModels<ManualViewModel>()

    private lateinit var manualAdapter: ManualAdapter

    override fun init() {
        setBinding()
        setViewModel()
    }

    private fun setBinding() {
        binding.apply {

        }
    }

    private fun setViewModel() {
        viewModel.apply {
            manualList.observe(viewLifecycleOwner, {
                setData(it.manualList)
            })
        }
    }

    private fun setData(manualList: List<Manual>) {
        binding.apply {
            manualAdapter = ManualAdapter()
            manualAdapter.manualList = manualList
            rvManual.setHasFixedSize(true)
            rvManual.layoutManager = LinearLayoutManager(requireContext())
            rvManual.adapter = manualAdapter

            manualAdapter.setOnItemClickListener(object : OnManualItemClickListener {
                override fun onManualItemClick(pos: Int) {
                    compositeDisposable.add(
                        Completable.complete()
                            .delay(200, TimeUnit.MILLISECONDS)
                            .subscribe({
                                rvManual.smoothScrollToPosition(pos)
                            }, { it.printStackTrace() })
                    )
                }
            })
        }
    }

    companion object {
        fun newInstance() = ManualFragment()
    }
}