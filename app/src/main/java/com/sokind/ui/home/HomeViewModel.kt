package com.sokind.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.member.MemberInfo
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val repository: MemberRepository
) : BaseViewModel() {
    private val _getMe: MutableLiveData<MemberInfo> = MutableLiveData()
    val getMe: LiveData<MemberInfo> = _getMe

    init {
        compositeDisposable.add(
            repository
                .getMe()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _getMe.postValue(it)
                }, { it.printStackTrace() })
        )
    }

    fun getEduList() {

    }

}