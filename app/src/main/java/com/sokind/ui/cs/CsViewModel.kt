package com.sokind.ui.cs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.local.user.UserEntity
import com.sokind.data.remote.edu.Edu
import com.sokind.data.remote.edu.EduList
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
    private val _nextEdu: MutableLiveData<Edu> = MutableLiveData()
    var nextEdu: LiveData<Edu> = _nextEdu

    init {
        compositeDisposable.add(
            eduRepository
                .getEdu()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _eduList.postValue(it)
                    findNextEdu(it)
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

    private fun findNextEdu(eduList: EduList) {
        val baseList = eduList.baseCs
        val deepList = eduList.deepCs

        for (deep in deepList) {
            if (deep.status == 2) {
                _nextEdu.postValue(deep)
                break
            }
        }
        for (base in baseList) {
            if (base.status == 2) {
                _nextEdu.postValue(base)
                break
            }
        }
    }
}