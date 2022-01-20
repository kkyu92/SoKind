package com.sokind.ui.join.third

import com.sokind.data.repository.member.MemberRepository
import com.sokind.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JoinThirdViewModel @Inject constructor(
    private val repository: MemberRepository
) : BaseViewModel() {

}