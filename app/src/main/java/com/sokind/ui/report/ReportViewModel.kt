package com.sokind.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.local.user.UserEntity
import com.sokind.data.remote.base.ErrorEntity
import com.sokind.data.remote.base.wrappingAPICallResult
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
    private val _report: MutableLiveData<ReportResponse?> = MutableLiveData()
    val report: LiveData<ReportResponse?> = _report
    private val _getMe: MutableLiveData<UserEntity> = MutableLiveData()
    val getMe: LiveData<UserEntity> = _getMe

    init {
        getReport()
        getMe()
    }

    fun getReport() {
        compositeDisposable.add(
            reportRepository
                .getReport()
                .wrappingAPICallResult()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    if (it.throwable != null) {
                        if (it.throwable is ErrorEntity.NoReportData) {
                            _report.postValue(null)
                        }
                    } else {
                        _report.postValue(it.result)
                    }
                }, { it.printStackTrace() })
        )
    }

    private fun getMe() {
        compositeDisposable.add(
            reportRepository
                .getMe()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _getMe.postValue(it)
                }, { it.printStackTrace() })
        )
    }
}