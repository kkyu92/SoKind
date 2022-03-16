package com.sokind.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.base.ErrorEntity
import com.sokind.data.remote.base.transformCompletableToSingleDefault
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
    private val _loginResult: MutableLiveData<Boolean> = MutableLiveData()
    val loginResult: LiveData<Boolean> = _loginResult
    private val _isSecession: MutableLiveData<String> = MutableLiveData()
    val isSecession: LiveData<String> = _isSecession

    fun doLoginRequest(id: String, password: String) {
        compositeDisposable.add(
            repository
                .login(LoginRequest(id, password))
                .transformCompletableToSingleDefault()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    if (it.throwable != null) {
                        when (it.throwable) {
                            is ErrorEntity.SecessionLoginRequest -> {
                                _isSecession.postValue(Constants.SECESSION_LOGIN_REQUEST)
                            }
                            is ErrorEntity.SecessionLogin -> {
                                _isSecession.postValue(Constants.SECESSION_LOGIN)
                            }
                            is ErrorEntity.FailLogin -> {
                                _loginResult.postValue(false)
                            }
                        }
                    } else {
                        _loginResult.postValue(true)
                    }
                }, {
                    it.printStackTrace()
                    _loginResult.postValue(false)
                })
        )
    }
}