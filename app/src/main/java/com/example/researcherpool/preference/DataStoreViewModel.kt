package com.example.researcherpool.preference

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DataStoreViewModel(private val pref:UserPreference): ViewModel() {
    fun getEmail(): LiveData<String>{
        return pref.getEmail().asLiveData()
    }

    fun setEmail(email:String){
        viewModelScope.launch {
            pref.setEmail(email)
        }
    }

    fun getUsername():LiveData<String>{
        return pref.getUsername().asLiveData()
    }

    fun clearData(){
        viewModelScope.launch {
            pref.clear()
        }
    }

    fun setUsername(username:String){
        viewModelScope.launch {
            pref.setUsername(username)
        }
    }

    fun getType() : LiveData<String>{
        return pref.getType().asLiveData()
    }

    fun setType(type:String){
        viewModelScope.launch {
            pref.setType(type)
        }
    }
}