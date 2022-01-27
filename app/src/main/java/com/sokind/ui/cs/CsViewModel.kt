package com.sokind.ui.cs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.local.user.UserEntity
import com.sokind.data.remote.edu.EduList
import com.sokind.data.remote.member.MemberInfo
import com.sokind.data.repository.edu.EduRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class CsViewModel @Inject constructor(
    private val eduRepository: EduRepository
) : BaseViewModel() {
    private val _eduList: MutableLiveData<EduList> = MutableLiveData()
    val eduList: LiveData<EduList> = _eduList
    private val _getMe: MutableLiveData<UserEntity> = MutableLiveData()
    val getMe: LiveData<UserEntity> = _getMe

    init {
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

    fun getMe() {
        compositeDisposable.add(
            eduRepository
                .getMe()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _getMe.postValue(it)
                }, { it.printStackTrace() })
        )
    }
}