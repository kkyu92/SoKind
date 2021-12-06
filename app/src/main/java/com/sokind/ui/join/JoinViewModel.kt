package com.sokind.ui.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(

) : BaseViewModel() {
    private val _titleInputText: MutableLiveData<String> = MutableLiveData()
    val titleInputText: LiveData<String> get() = _titleInputText

    private val _autoText: MutableLiveData<List<String>> = MutableLiveData()
    val autoText: LiveData<List<String>> get() = _autoText

    private val autoTextBehaviorSubject = BehaviorSubject.create<String>()

    init {
        compositeDisposable.add(
            autoTextBehaviorSubject
                .subscribeOn(Schedulers.newThread())
                .throttleLast(2500, TimeUnit.MILLISECONDS)
                .concatMap {
                    Timber.e("test $it")

                    repository.getMovieT itleList(it, null).toObservable()
                }
                .map { it.map { it.title.htmlToString() } }
                .subscribe({
                    Timber.e("test $it")
                    _autoText.postValue(it)
                }, {
                    Timber.e("test $it")
                }, {

                })
        )
    }

    fun onContentTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        _titleInputText.value = s.toString()
        autoTextBehaviorSubject.onNext(_titleInputText.value ?: "")
    }
}