package com.sokind.ui.join.first

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.data.remote.member.join.EnterpriseList
import com.sokind.data.remote.member.join.Position
import com.sokind.data.remote.member.join.Store
import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class JoinFirstViewModel @Inject constructor(
    private val repository: MemberRepository
) : BaseViewModel() {
    private val _autoText: MutableLiveData<EnterpriseList> = MutableLiveData()
    val autoText: LiveData<EnterpriseList> = _autoText

    private val _storeList: MutableLiveData<List<Store>> = MutableLiveData()
    val storeList: LiveData<List<Store>> = _storeList

    private val _positionList: MutableLiveData<List<Position>> = MutableLiveData()
    val positionList: LiveData<List<Position>> = _positionList

    private val autoTextBehaviorSubject = BehaviorSubject.create<String>()

    init {
        compositeDisposable.add(
            autoTextBehaviorSubject
                .subscribeOn(Schedulers.newThread())
                .throttleLast(1000, TimeUnit.MILLISECONDS)
                .concatMap {
                    Timber.e("get words : $it")
                    repository.searchEnterpriseList(it).toObservable()
                }
                .subscribe({
                    Timber.e("list\n ${it.data}")
                    _autoText.postValue(it)
                }, {
                    Timber.e("error $it")
                }, {
                    Timber.e("complete")
                })
        )
    }

    fun searchEnterprise(words: CharSequence) {
        autoTextBehaviorSubject.onNext(words.toString())
    }

    fun getStoreList(code: String, key: Int) {
        compositeDisposable.add(
            repository
                .getEnterpriseInfo(code, key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _storeList.postValue(it.storeInfo)
                    _positionList.postValue(it.positionInfo)
                }, { it.printStackTrace() })
        )
    }
}