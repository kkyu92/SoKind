package com.sokind.ui.edu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.local.user.UserEntity
import com.sokind.data.remote.edu.NextEdu
import com.sokind.data.remote.edu.StartEdu
import com.sokind.data.repository.edu.EduRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import okhttp3.MultipartBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EduViewModel @Inject constructor(
    private val repository: EduRepository
) : BaseViewModel() {
    private val _getStartEdu: MutableLiveData<StartEdu> = MutableLiveData()
    val getStartEdu: LiveData<StartEdu> = _getStartEdu
    private val _updateEdu: MutableLiveData<NextEdu> = MutableLiveData()
    val updateEdu: LiveData<NextEdu> = _updateEdu
    private val _user: MutableLiveData<UserEntity> = MutableLiveData()
    val user: LiveData<UserEntity> = _user

    init {
        getUser()
    }

    fun startEdu(key: Int, type: Int) {
        compositeDisposable.add(
            repository
                .startEdu(key, type)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    Timber.e("startEdu : $it")
                    _getStartEdu.postValue(it)
                }, {
                    it.printStackTrace()
                    Timber.e("error : $it")
                })
        )
    }

    fun updateEdu(file: MultipartBody.Part, eduKey: Int, eduType: Int) {
        compositeDisposable.add(
            repository
                .putEdu(file, eduKey, eduType)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _updateEdu.postValue(it)
                }, { it.printStackTrace() })
        )
    }

    fun getUser() {
        compositeDisposable.add(
            repository
                .getMe()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _user.postValue(it)
                }, { it.printStackTrace() })
        )
    }
}