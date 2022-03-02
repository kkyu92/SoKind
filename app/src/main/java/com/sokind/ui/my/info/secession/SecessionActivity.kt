package com.sokind.ui.my.info.secession

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.checkedChanges
import com.jakewharton.rxbinding4.widget.itemSelections
import com.jakewharton.rxbinding4.widget.textChanges
import com.sokind.R
import com.sokind.databinding.ActivitySecessionBinding
import com.sokind.ui.MainActivity
import com.sokind.ui.base.BaseActivity
import com.sokind.util.Constants
import com.sokind.util.adapter.SpinnerAdapter
import com.sokind.util.dialog.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function3
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SecessionActivity : BaseActivity<ActivitySecessionBinding>(R.layout.activity_secession) {
    private val viewModel by viewModels<SecessionViewModel>()
    private var selectedReason: Int = 0

    override fun init() {
        setBinding()
        setViewModel()
    }

    private fun setBinding() {
        binding.apply {
            val reasonList: MutableList<String> =
                resources.getStringArray(R.array.secession_reason).toMutableList()
            val reasonAdapter =
                SpinnerAdapter(this@SecessionActivity, R.layout.item_enterprise_list, reasonList)
            spReason.adapter = reasonAdapter

            btBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    onBackPressed()
                }, { it.printStackTrace() })

            btSecession
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val dialog = BottomSheetDialog.newInstance(
                        Constants.SIMPLE_DIALOG,
                        getString(R.string.dialog_secession),
                        "",
                        itemClick = { okClick ->
                            if (okClick) {
                                if (selectedReason == 5) {
                                    viewModel.secession(etReason.text.toString())
                                } else {
                                    viewModel.secession(reasonList[selectedReason])
                                }
                            }
                        }
                    ) as BottomSheetDialog
                    dialog.show(supportFragmentManager, dialog.tag)
                }, { it.printStackTrace() })

            compositeDisposable.add(
                Observable
                    .combineLatest(
                        spReason.itemSelections(),
                        cbSecession.checkedChanges(),
                        etReason.textChanges(),
                        Function3 { reasonSelect: Int, isChecked: Boolean, textReason: CharSequence ->
                            selectedReason = reasonSelect
                            if (reasonSelect == 5) {
                                etReason.visibility = View.VISIBLE
                                return@Function3 textReason.toString().isNotEmpty() && isChecked
                            } else {
                                etReason.visibility = View.GONE
                                return@Function3 reasonSelect != 0 && isChecked
                            }
                        }
                    )
                    .subscribe({ select ->
                        btSecession.isEnabled = select
                    }, { it.printStackTrace() })
            )
        }
    }

    private fun setViewModel() {
        viewModel.apply {
            isSecession.observe(this@SecessionActivity, {
                if (it) {
                    val intent = Intent(this@SecessionActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            })
        }
    }
}