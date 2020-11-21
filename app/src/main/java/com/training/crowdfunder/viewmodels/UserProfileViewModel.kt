package com.training.crowdfunder.viewmodels

import androidx.lifecycle.*
import com.training.crowdfunder.data.FirebaseService
import com.training.crowdfunder.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class UserProfileViewModel(private val userId : String) : ViewModel(){
    private val _user = MutableLiveData<User>()
    var user : LiveData<User> = _user

    init {
        viewModelScope.launch {
            FirebaseService.getUserData(userId).collectLatest { _user.value = it }
        }
    }
}

class UserViewModelFactory(private val userId: String) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserProfileViewModel(userId) as T
    }
}