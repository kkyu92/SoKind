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
    repository: CommonRepository
) : BaseViewModel() {
    private val _getInquiry: MutableLiveData<InquiryResponse> = MutableLiveData()
    val getInquiry: LiveData<InquiryResponse> = _getInquiry

    init {
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
}