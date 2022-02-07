package com.sokind.ui.guide.tabs.manual

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.guide.ManualResponse
import com.sokind.data.repository.guide.GuideRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class ManualViewModel @Inject constructor(
    repository: GuideRepository
): BaseViewModel() {
    private val _manualList: MutableLiveData<ManualResponse> = MutableLiveData()
    val manualList: LiveData<ManualResponse> = _manualList

    init {
        compositeDisposable.add(
            repository
                .getManual()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _manualList.postValue(it)
                }, { it.printStackTrace() })
        )
    }
}