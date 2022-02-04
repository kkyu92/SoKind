package com.sokind.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class BoardingViewModel @Inject constructor(
    memberRepository: MemberRepository
) : BaseViewModel() {
    private val _name: MutableLiveData<String> = MutableLiveData()
    var name: LiveData<String> = _name

    init {
        compositeDisposable.add(
            memberRepository
                .getMe()
                .doOnSubscribe { showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate { hideProgress() }
                .subscribe({
                    _name.postValue(it.memberName)
                }, { it.printStackTrace() })
        )
    }
}