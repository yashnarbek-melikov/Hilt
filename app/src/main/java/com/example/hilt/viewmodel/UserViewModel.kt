package com.example.hilt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hilt.database.entity.UserEntity
import com.example.hilt.repository.UserRepository
import com.example.hilt.utils.NetworkHelper
import com.example.hilt.utils.UserResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository, private val networkHelper: NetworkHelper) : ViewModel() {

    fun getGithubUsers(): StateFlow<UserResource> {
        val stateFlow = MutableStateFlow<UserResource>(UserResource.Loading)
        viewModelScope.launch {
            if(networkHelper.isNetworkConnected()) {
                val flow = userRepository.getUsers()
                flow.catch {
                    stateFlow.emit(UserResource.Error(it.message ?: ""))
                }.collect { it ->
                    if(it.isSuccessful) {
                        val body = it.body()
                        val list = ArrayList<UserEntity>()
                        body?.forEach { user ->
                            list.add(UserEntity(user.id, user.login, user.avatar_url))
                        }
                        userRepository.addUsers(list)
                        stateFlow.emit(UserResource.Success(list))
                    } else {
                        stateFlow.emit(UserResource.Error(it.message()))
                    }
                }
            } else {
                userRepository.getDbUsers().collect {
                    if(it.isNotEmpty()) {
                        stateFlow.emit(UserResource.Success(it))
                    } else {
                        stateFlow.emit(UserResource.Error("No internet connection"))
                    }
                }
            }
        }
        return stateFlow
    }
}