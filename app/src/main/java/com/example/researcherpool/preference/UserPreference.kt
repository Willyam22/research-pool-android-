package com.example.researcherpool.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.researcherpool.api.response.UserId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name = "email")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){
    private val EMAIL = stringPreferencesKey("email")
    private val TYPE = stringPreferencesKey("type")
    private val USERNAME = stringPreferencesKey("username")

    fun getEmail(): Flow<String>{
        return dataStore.data.map { preferences->
            preferences[EMAIL]?:"email"
        }
    }

    suspend fun setEmail(email:String){
        dataStore.edit { preferences->
            preferences[EMAIL] = email
        }
    }

    suspend fun clear(){
        dataStore.edit { preferences->
            preferences.clear()
        }
    }

    fun getUsername():Flow<String>{
        return dataStore.data.map{preferences->
            preferences[USERNAME]?:"username"
        }
    }

    suspend fun setUsername(username:String){
        dataStore.edit { preferences->
            preferences[USERNAME] = username
        }
    }


    fun getType(): Flow<String>{
        return dataStore.data.map { preferences->
            preferences[TYPE]?:"type"
        }
    }

    suspend fun setType(type:String){
        dataStore.edit { preferences->
            preferences[TYPE] = type
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore:DataStore<Preferences>):UserPreference{
            return INSTANCE ?: synchronized(this){
                val instance =UserPreference(dataStore)
                INSTANCE= instance
                instance
            }
        }
    }
}