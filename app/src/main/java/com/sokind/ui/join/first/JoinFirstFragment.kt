package com.sokind.ui.join.first

import android.text.Html
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.textChanges
import com.sokind.R
import com.sokind.databinding.FragmentJoinFirstBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class JoinFirstFragment : BaseFragment<FragmentJoinFirstBinding>(R.layout.fragment_join_first) {
    private val viewModel by viewModels<JoinFirstViewModel>()

    private lateinit var searchAdapter: SearchAdapter

    override fun init() {
        setBinding()

        viewModel.autoText.observe(viewLifecycleOwner, {
            val searchWord = binding.autoSearchView.text.toString()

            if (it.isEmpty()) {
                binding.rvEnterprise.visibility = View.GONE
                binding.tvNoEnterprise.text = Html.fromHtml(
                    String.format(
                        getString(R.string.no_enterprise_content_1),
                        searchWord
                    )
                )
                binding.llNoEnterpriseResult.visibility = View.VISIBLE
            } else {
                searchAdapter.setData(it, searchWord)
            }
        })
    }

    private fun setBinding() {
        searchAdapter = SearchAdapter()

        binding.apply {
            rvEnterprise.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvEnterprise.adapter = searchAdapter
            searchAdapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
                override fun onItemClick(v: View, enterprise: String, pos: Int) {
                    autoSearchView.setText(enterprise)
                    rvEnterprise.visibility = View.GONE
                    etEnterpriseCodeInput.visibility = View.VISIBLE
                    etEnterpriseCodeInput.requestFocus()
                }
            })

            ivBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().popBackStack()
                }, { it.printStackTrace() })

            // endIcon event
            tiLayout.setEndIconOnClickListener {
                autoSearchView.text.clear()
                searchAdapter.setClear()
                hideKeyboard()
            }

            // enterprise autoSearch event
            autoSearchView
                .textChanges()
                .subscribe({
                    if (it.isNotEmpty()) {
                        if (Constants.validateKo(it.toString())) {
                            viewModel.searchEnterprise(it)
                        }
                    } else {
                        searchAdapter.setClear()
                    }
                }, { it.printStackTrace() })

            // enterprise enter code
            etEnterpriseCodeInput
                .textChanges()
                .subscribe({
                    btNext.isEnabled = it.isNotEmpty()
                }, { it.printStackTrace() })

            btNext
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().navigate(R.id.action_joinFirstFragment_to_joinSecondFragment)
                }, { it.printStackTrace() })
        }
    }

    companion object {
//        fun newInstance() = JoinFirstFragment()
    }
}