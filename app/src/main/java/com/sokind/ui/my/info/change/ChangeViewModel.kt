package com.sokind.ui.my.info.change

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.member.change.EmailRequest
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ChangeViewModel @Inject constructor(
    private val repository: MemberRepository
) : BaseViewModel() {
    private val _code: MutableLiveData<String> = MutableLiveData()
    val code: LiveData<String> = _code
    private val _isEmailChange: MutableLiveData<Boolean> = MutableLiveData()
    val isEmailChange: LiveData<Boolean> = _isEmailChange
    private val _isPwChange: MutableLiveData<Boolean> = MutableLiveData()
    val isPwChange: LiveData<Boolean> = _isPwChange

    init {

    }

    fun sendEmail(email: String) {
        compositeDisposable.add(
            repository
                .sendEmail(email)
                .doOnSubscribe { showLottieProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideLottieProgress() }
                .subscribe({
                    _code.postValue(it.number)
                }, { it.printStackTrace() })
        )
    }


    fun changeEmail(newEmail: String) {
        compositeDisposable.add(
            repository
                .changeEmail(newEmail)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _isEmailChange.postValue(true)
                }, {
                    _isEmailChange.postValue(false)
                    it.printStackTrace()
                })
        )
    }

    fun changePw(pw: String, newPw: String) {
        compositeDisposable.add(
            repository
                .changePw(pw, newPw)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _isPwChange.postValue(true)
                }, {
                    _isPwChange.postValue(false)
                    it.printStackTrace()
                })
        )
    }
}