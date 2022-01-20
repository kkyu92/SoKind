package com.sokind.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.base.BaseNetworkCallResult
import com.sokind.data.remote.member.login.LoginRequest
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MemberRepository
) : BaseViewModel() {
    private val _loginResult: MutableLiveData<Boolean> = MutableLiveData()
    val loginResult: LiveData<Boolean> get() = _loginResult

    fun doLoginRequest(id: String, password: String) {
        compositeDisposable.add(
            repository
                .login(LoginRequest(id, password))
                .andThen(Single.just(BaseNetworkCallResult<Unit>()))
                .onErrorReturn{ BaseNetworkCallResult(throwable = it) }
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate{ hideProgress() }
                .subscribe({
                    _loginResult.value = true
                }, {
                    it.printStackTrace()
                    _loginResult.value = false
                })
        )
    }
}