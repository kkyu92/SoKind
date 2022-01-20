package com.sokind.ui.join.fourth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.member.join.JoinInfo
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class JoinFourthViewModel @Inject constructor(
    private val repository: MemberRepository
) : BaseViewModel() {
    private val _signFinish: MutableLiveData<Boolean> = MutableLiveData()
    val signFinish: LiveData<Boolean> = _signFinish

    init {

    }

    fun signUp(joinInfo: JoinInfo) {
        compositeDisposable.add(
            repository
                .signUp(joinInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _signFinish.value = true
                }, {
                    _signFinish.value = false
                    it.printStackTrace()
                })
        )
    }
}