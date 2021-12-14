package com.sokind.ui.join.first

import android.text.Html
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.focusChanges
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
    private lateinit var departmentAdapter: SpinnerAdapter
    private lateinit var positionAdapter: SpinnerAdapter
    private val listOfDepartment = ArrayList<String>()
    private val listOfPosition = ArrayList<String>()

    override fun init() {
        setBinding()
        setSpinner()

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

    private fun setSpinner() {
        val departments = resources.getStringArray(R.array.department_test)
        for (i in departments.indices) {
            val department = departments[i]
            listOfDepartment.add(department)
        }
        departmentAdapter =
            SpinnerAdapter(requireContext(), R.layout.item_enterprise_list, listOfDepartment)
        binding.spEnterpriseDepartment.adapter = departmentAdapter

        val positions = resources.getStringArray(R.array.position_test)
        for (i in positions.indices) {
            val position = positions[i]
            listOfPosition.add(position)
        }
        positionAdapter =
            SpinnerAdapter(requireContext(), R.layout.item_enterprise_list, listOfPosition)
        binding.spEnterprisePosition.adapter = positionAdapter
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
                    llContainerCode.visibility = View.VISIBLE
                    etEnterpriseCodeInput.requestFocus()
                }
            })

            // endIcon event
            tiLayout.setEndIconOnClickListener {
                autoSearchView.text.clear()
                etEnterpriseCodeInput.text.clear()
                searchAdapter.setClear()
                rvEnterprise.visibility = View.VISIBLE
                llContainerCode.visibility = View.GONE
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
                    if (it.isNotEmpty()) {
                        llContainerMore.visibility = View.VISIBLE
                    } else {
                        llContainerMore.visibility = View.GONE
                    }
                }, { it.printStackTrace() })

            // change focus title
            autoSearchView
                .focusChanges()
                .subscribe({
                    titleFocus(tvEnterpriseTitle, it)
                }, { it.printStackTrace() })
            etEnterpriseCodeInput
                .focusChanges()
                .subscribe({
                    titleFocus(tvEnterpriseCode, it)
                }, { it.printStackTrace() })
            spEnterpriseDepartment
                .focusChanges()
                .subscribe({
                    titleFocus(tvEnterpriseDepartment, it)
                }, { it.printStackTrace() })
            spEnterprisePosition
                .focusChanges()
                .subscribe({
                    titleFocus(tvEnterprisePosition, it)
                }, { it.printStackTrace() })

            // clicks
            spEnterpriseDepartment.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    TODO("Not yet implemented")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
            ivBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().popBackStack()
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
        fun newInstance() = JoinFirstFragment()
    }
}