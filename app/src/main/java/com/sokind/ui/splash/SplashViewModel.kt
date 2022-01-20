package com.sokind.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val repository: MemberRepository
) : BaseViewModel() {
    private val _isLogin = MutableLiveData<String>()
    val isLogin: LiveData<String> get() = _isLogin

    init {
        compositeDisposable.add(
            repository
                .isLogin()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _isLogin.postValue(it)
                }, { it.printStackTrace() })
        )
    }
}