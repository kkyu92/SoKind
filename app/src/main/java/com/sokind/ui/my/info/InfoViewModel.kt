package com.sokind.ui.my.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    private val repository: MemberRepository
) : BaseViewModel() {
    private val _profile: MutableLiveData<Boolean> = MutableLiveData()
    val profile: LiveData<Boolean> = _profile
    private val _code: MutableLiveData<String> = MutableLiveData()
    val code: LiveData<String> = _code
    private val _extras: MutableLiveData<Boolean> = MutableLiveData()
    val extras: LiveData<Boolean> = _extras

    private val _isLogout: MutableLiveData<Boolean> = MutableLiveData()
    val isLogout: LiveData<Boolean> = _isLogout

    init {

    }

    fun profileUpdate(file: MultipartBody.Part) {
        compositeDisposable.add(
            repository
                .changeProfile(file)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _profile.postValue(true)
                }, {
                    _profile.postValue(false)
                    it.printStackTrace()
                })
        )
    }

    fun sendEmail(email: String) {
        compositeDisposable.add(
            repository
                .sendEmail(email)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _code.postValue(it.number)
                }, { it.printStackTrace() })
        )
    }

    fun extraUpdate(event: Int, email: Int, app: Int) {
        compositeDisposable.add(
            repository
                .changeExtra(event, email, app)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _extras.postValue(true)
                }, {
                    _extras.postValue(false)
                    it.printStackTrace()
                })
        )
    }

    fun logout() {
        compositeDisposable.add(
            repository
                .logout()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _isLogout.postValue(true)
                }, { it.printStackTrace() })
        )
    }
}