package com.sokind.ui.my.info.inquiry.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.repository.common.CommonRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class PostInquiryViewModel @Inject constructor(
    private val repository: CommonRepository
) : BaseViewModel() {
    private val _postInquiry: MutableLiveData<Boolean> = MutableLiveData()
    val postInquiry: LiveData<Boolean> = _postInquiry

    init {

    }

    fun postInquiry(type: String, title: String, contents: String) {
        compositeDisposable.add(
            repository
                .postInquiry(type, title, contents)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _postInquiry.postValue(true)
                }, {
                    _postInquiry.postValue(false)
                    it.printStackTrace()
                })
        )
    }
}