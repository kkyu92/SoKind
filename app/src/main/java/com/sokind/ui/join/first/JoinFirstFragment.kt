package com.sokind.ui.join.first

import android.text.Html
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.focusChanges
import com.jakewharton.rxbinding4.widget.itemSelections
import com.jakewharton.rxbinding4.widget.textChanges
import com.sokind.R
import com.sokind.data.remote.member.join.Enterprise
import com.sokind.data.remote.member.join.Position
import com.sokind.data.remote.member.join.Store
import com.sokind.databinding.FragmentJoinFirstBinding
import com.sokind.ui.base.BaseFragment
import com.sokind.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class JoinFirstFragment : BaseFragment<FragmentJoinFirstBinding>(R.layout.fragment_join_first) {
    private val viewModel by viewModels<JoinFirstViewModel>()

    private var mEnterpriseKey: Int = 0
    private var mStoreKey: Int = 0
    private var mPositionKey: Int = 0
    private lateinit var mCode: String

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var storeAdapter: StoreAdapter
    private lateinit var positionAdapter: PositionAdapter
    private val storeList: MutableList<Store> = mutableListOf()
    private val positionList: MutableList<Position> = mutableListOf()

    override fun init() {
        setBinding()
        setSearch()
        setSpinner()
    }

    private fun setSearch() {
        viewModel.autoText.observe(viewLifecycleOwner, {
            val searchWord = binding.autoSearchView.text.toString()
            if (searchWord.isNotEmpty()) {
                if (it.data.isEmpty()) {
                    binding.rvEnterprise.visibility = View.GONE
                    binding.tvNoEnterprise.text = fromHtml(getString(R.string.no_enterprise_content_1, searchWord))
                    binding.llNoEnterpriseResult.visibility = View.VISIBLE
                } else {
                    binding.rvEnterprise.visibility = View.VISIBLE
                    binding.llNoEnterpriseResult.visibility = View.GONE
                    searchAdapter.setData(it.data, searchWord)
                }
            }
        })

        binding.apply {
            rvEnterprise.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvEnterprise.adapter = searchAdapter
            searchAdapter.setOnItemClickListener(object : SearchAdapter.OnItemClickListener {
                override fun onItemClick(v: View, enterprise: Enterprise, pos: Int) {
                    autoSearchView.setText(enterprise.enterpriseName)
                    autoSearchView.isEnabled = false
                    rvEnterprise.visibility = View.GONE
                    llContainerCode.visibility = View.VISIBLE
                    etEnterpriseCodeInput.requestFocus()
                    viewModel.getStoreList(enterprise.enterpriseCode, enterprise.enterpriseKey)
                    mCode = enterprise.enterpriseCode
                    mEnterpriseKey = enterprise.enterpriseKey
                }
            })

            // endIcon event
            tiLayout.setEndIconOnClickListener {
                setClear()
            }
        }
    }

    private fun setSpinner() {
        viewModel.storeList.observe(viewLifecycleOwner, {
            storeList.add(Store(0, "- 점포를 선택해주세요 -", ""))
            for (i in it.indices) {
                storeList.add(it[i])
            }
            storeAdapter =
                StoreAdapter(requireContext(), R.layout.item_enterprise_list, storeList)
            binding.spEnterpriseStore.adapter = storeAdapter
        })
        viewModel.positionList.observe(viewLifecycleOwner, {
            positionList.add(Position(0, "- 직책을 선택해주세요 -"))
            for (i in it.indices) {
                positionList.add(it[i])
            }
            positionAdapter =
                PositionAdapter(requireContext(), R.layout.item_enterprise_list, positionList)
            binding.spEnterprisePosition.adapter = positionAdapter
        })
    }

    private fun setBinding() {
        searchAdapter = SearchAdapter()

        binding.apply {
            compositeDisposable.add(
                Observable
                    .combineLatest(
                        spEnterpriseStore.itemSelections(),
                        spEnterprisePosition.itemSelections(),
                        BiFunction { storeSelect: Int, positionSelect: Int ->
                            if (storeSelect > 0 && positionSelect > 0) {
                                mStoreKey = storeList[storeSelect].storeKey
                                mPositionKey = positionList[positionSelect].positionKey
                            }
                            return@BiFunction storeSelect > 0 && positionSelect > 0
                        }
                    )
                    .subscribe({ select ->
                        btNext.isEnabled = select
                    }, { it.printStackTrace() })
            )

            // textChanges
            autoSearchView
                .textChanges()
                .subscribe({
                    if (it.isNotEmpty()) {
                        if (Constants.validateKo(it.toString()) || Constants.validateEn(it.toString())
                        ) {
                            viewModel.searchEnterprise(it)
                        }
                    } else {
                        setClear()
                    }
                }, { it.printStackTrace() })
            etEnterpriseCodeInput
                .textChanges()
                .subscribe({
                    when {
                        it.isNullOrBlank() -> {
                            tvCodeError.visibility = View.GONE
                        }
                        it.toString() == mCode -> {
                            etEnterpriseCodeInput.isEnabled = false
                            tvCodeError.visibility = View.GONE
                            llContainerMore.visibility = View.VISIBLE
                            hideKeyboard()
                        }
                        else -> {
                            tvCodeError.visibility = View.VISIBLE
                        }
                    }
                }, { it.printStackTrace() })

            // focusChanges
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

            // clicks
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
                    val action =
                        JoinFirstFragmentDirections.actionJoinFirstFragmentToJoinSecondFragment(
                            mEnterpriseKey,
                            mStoreKey,
                            mPositionKey
                        )
                    findNavController().navigate(action)
                }, { it.printStackTrace() })
        }
    }

    private fun setClear() {
        binding.apply {
            autoSearchView.text.clear()
            etEnterpriseCodeInput.text.clear()
            searchAdapter.setClear()
            storeList.clear()
            positionList.clear()
            rvEnterprise.visibility = View.VISIBLE
            llContainerCode.visibility = View.GONE
            llContainerMore.visibility = View.GONE
            mCode = ""
            autoSearchView.isEnabled = true
            etEnterpriseCodeInput.isEnabled = true
            btNext.isEnabled = false
        }
    }

    companion object {
        fun newInstance() = JoinFirstFragment()
    }
}