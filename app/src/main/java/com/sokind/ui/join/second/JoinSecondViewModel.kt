package com.sokind.ui.join.second

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class JoinSecondViewModel @Inject constructor(
    private val repository: MemberRepository
) : BaseViewModel() {
    private val _emailCode: MutableLiveData<String> = MutableLiveData()
    val emailCode: LiveData<String> = _emailCode

    private val _idCheck: MutableLiveData<Boolean> = MutableLiveData()
    val idCheck: LiveData<Boolean> = _idCheck

    init {

    }

    fun sendEmail(email: String) {
        compositeDisposable.add(
            repository
                .sendEmail(email)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _emailCode.postValue(it.number)
                }, { it.printStackTrace() })
        )
    }

    fun checkId(id: String) {
        compositeDisposable.add(
            repository
                .checkId(id)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _idCheck.postValue(true)
                }, {
                    _idCheck.postValue(false)
                    it.printStackTrace()
                })
        )
    }
}