package com.sokind.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.member.login.LoginRequest
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import com.sokind.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MemberRepository
) : BaseViewModel() {
    private val _loginResult: MutableLiveData<Int> = MutableLiveData()
    val loginResult: LiveData<Int> = _loginResult

    fun doLoginRequest(id: String, password: String) {
        compositeDisposable.add(
            repository
                .login(LoginRequest(id, password))
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({ data ->
                        when (data) {
                            0 -> _loginResult.postValue(Constants.SECESSION_LOGIN)
                            1 -> _loginResult.postValue(Constants.SUCCESS_LOGIN)
                            2 -> _loginResult.postValue(Constants.SECESSION_LOGIN_REQUEST)
                        }
                }, {
                    it.printStackTrace()
                    _loginResult.postValue(Constants.FAIL_LOGIN)
                })
        )
    }
}