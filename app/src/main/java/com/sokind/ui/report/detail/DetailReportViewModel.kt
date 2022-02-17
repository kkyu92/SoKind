package com.sokind.ui.report.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.local.user.UserEntity
import com.sokind.data.remote.report.ReportDetail
import com.sokind.data.repository.report.ReportRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class DetailReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : BaseViewModel() {
    private val _detailReport: MutableLiveData<ReportDetail> = MutableLiveData()
    val detailReport: LiveData<ReportDetail> = _detailReport
    private val _getMe: MutableLiveData<UserEntity> = MutableLiveData()
    val getMe: LiveData<UserEntity> = _getMe

    init {
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

    fun getReportDetail(key: Int, type: Int) {
        compositeDisposable.add(
            reportRepository
                .getReportDetail(key, type)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _detailReport.postValue(it)
                }, { it.printStackTrace() })
        )
    }
}