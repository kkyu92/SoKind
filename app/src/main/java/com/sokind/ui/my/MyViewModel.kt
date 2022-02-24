package com.sokind.ui.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.member.MemberInfo
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: MemberRepository
) : BaseViewModel() {
    private val _info: MutableLiveData<MemberInfo> = MutableLiveData()
    val info: LiveData<MemberInfo> = _info

    init {
        getMe()
    }

    fun getMe() {
        compositeDisposable.add(
            repository
                .getMe()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _info.postValue(it)
                }, { it.printStackTrace() })
        )
    }
}