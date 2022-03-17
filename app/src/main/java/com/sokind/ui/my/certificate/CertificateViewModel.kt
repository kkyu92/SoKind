package com.sokind.ui.my.certificate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.member.MemberInfo
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class CertificateViewModel @Inject constructor(
    private val repository: MemberRepository
) : BaseViewModel() {
    private val _certificateResult: MutableLiveData<Boolean> = MutableLiveData()
    val certificateResult: LiveData<Boolean> = _certificateResult

    init {

    }

    fun checkCertificate() {
        compositeDisposable.add(
            repository
                .checkCertificate()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _certificateResult.postValue(true)
                }, {
                    _certificateResult.postValue(false)
                    it.printStackTrace()
                })
        )
    }
}