package com.sokind.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.edu.EduList
import com.sokind.data.remote.member.MemberInfo
import com.sokind.data.remote.member.login.RefreshRequest
import com.sokind.data.repository.edu.EduRepository
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val eduRepository: EduRepository
) : BaseViewModel() {
    private val _getMe: MutableLiveData<MemberInfo> = MutableLiveData()
    val getMe: LiveData<MemberInfo> = _getMe

    private val _eduList: MutableLiveData<EduList> = MutableLiveData()
    val eduList: LiveData<EduList> = _eduList

    // subject 사용

    init {
        compositeDisposable.add(
            memberRepository
                .getMe()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _getMe.postValue(it)
                }, { it.printStackTrace() })
        )
    }

    fun saveUser(memberInfo: MemberInfo) {
        compositeDisposable.add(
            memberRepository
                .saveUser(memberInfo)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    Timber.e("save success!")
                }, { it.printStackTrace() })
        )
    }

    fun getEdu() {
        compositeDisposable.add(
            eduRepository
                .getEdu()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _eduList.postValue(it)
                }, { it.printStackTrace() })
        )
    }
}