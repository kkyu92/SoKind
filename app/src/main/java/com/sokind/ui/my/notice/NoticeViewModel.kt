package com.sokind.ui.my.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.common.NoticeResponse
import com.sokind.data.repository.common.CommonRepository
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class NoticeViewModel @Inject constructor(
    repository: CommonRepository
) : BaseViewModel() {
    private val _getNotice: MutableLiveData<NoticeResponse> = MutableLiveData()
    val getNotice: LiveData<NoticeResponse> = _getNotice

    init {
        compositeDisposable.add(
            repository
                .getNotice()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _getNotice.postValue(it)
                }, { it.printStackTrace() })
        )
    }
}