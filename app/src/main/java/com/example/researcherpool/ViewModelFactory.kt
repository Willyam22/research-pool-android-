package com.example.researcherpool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.researcherpool.preference.DataStoreViewModel
import com.example.researcherpool.preference.UserPreference

class ViewModelFactory(private val pref:UserPreference): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DataStoreViewModel::class.java)) {
            return DataStoreViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}