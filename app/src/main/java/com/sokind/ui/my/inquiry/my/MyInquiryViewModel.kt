package com.sokind.ui.my.info.inquiry.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.common.InquiryResponse
import com.sokind.data.repository.common.CommonRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class MyInquiryViewModel @Inject constructor(
    private val repository: CommonRepository
) : BaseViewModel() {
    private val _getInquiry: MutableLiveData<InquiryResponse> = MutableLiveData()
    val getInquiry: LiveData<InquiryResponse> = _getInquiry
    private val _isDeleted: MutableLiveData<Boolean> = MutableLiveData()
    val isDeleted: LiveData<Boolean> = _isDeleted

    init {
        myInquiry()
    }

    fun myInquiry() {
        compositeDisposable.add(
            repository
                .getInquiry()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _getInquiry.postValue(it)
                }, { it.printStackTrace() })
        )
    }

    fun deleteInquiry(id: Int) {
        compositeDisposable.add(
            repository
                .deleteInquiry(id)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _isDeleted.postValue(true)
                }, {
                    _isDeleted.postValue(false)
                    it.printStackTrace()
                })
        )
    }
}