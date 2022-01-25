package com.sokind.ui.edu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.repository.edu.EduRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EduViewModel @Inject constructor(
    private val repository: EduRepository
) : BaseViewModel() {
    private val _updateEdu: MutableLiveData<String> = MutableLiveData()
    val updateEdu: LiveData<String> = _updateEdu

    init {

    }

    fun updateEdu(file: MultipartBody.Part, eduKey: Int, eduType: Int, id: Int, key: Int) {
        compositeDisposable.add(
            repository
                .putEdu(file, eduKey, eduType, id, key)
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _updateEdu.postValue(it)
                }, { it.printStackTrace() })
        )
    }
}