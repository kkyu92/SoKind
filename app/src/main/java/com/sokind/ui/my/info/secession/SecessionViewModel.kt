package com.sokind.ui.my.info.secession

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class SecessionViewModel @Inject constructor(
    private val repository: MemberRepository
) : BaseViewModel() {
    private val _isSecession: MutableLiveData<Boolean> = MutableLiveData()
    val isSecession: LiveData<Boolean> = _isSecession

    init {

    }

    fun secession(reason: String) {
        compositeDisposable.add(
            repository
                .secession(reason)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _isSecession.postValue(true)
                }, { it.printStackTrace() })
        )
    }
}