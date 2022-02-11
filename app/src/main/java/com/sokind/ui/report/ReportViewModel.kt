package com.sokind.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.report.ReportResponse
import com.sokind.data.repository.report.ReportRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : BaseViewModel() {
    private val _report: MutableLiveData<ReportResponse> = MutableLiveData()
    val report: LiveData<ReportResponse> = _report

    init {
        getReport()
    }

    fun getReport() {
        compositeDisposable.add(
            reportRepository
                .getReport()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _report.postValue(it)
                }, { it.printStackTrace() })
        )
    }
}