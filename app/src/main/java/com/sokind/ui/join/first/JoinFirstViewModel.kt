package com.sokind.ui.join.first

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sokind.data.repository.test.TestRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class JoinFirstViewModel @Inject constructor(
    private val repository: TestRepository
) : BaseViewModel() {

    private val _autoText: MutableLiveData<List<String>> = MutableLiveData()
    val autoText: LiveData<List<String>> get() = _autoText

    private val autoTextBehaviorSubject = BehaviorSubject.create<String>()

    init {
        compositeDisposable.add(
            autoTextBehaviorSubject
                .subscribeOn(Schedulers.newThread())
                .throttleLast(1500, TimeUnit.MILLISECONDS)
                .concatMap {
                    Timber.e("get words : $it")
                    repository.test().toObservable()
                }
                .subscribe({
                    Timber.e("test ${it.results}")
                    _autoText.postValue(it.results)
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
}