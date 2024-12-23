package com.example.researcherpool.ui.FormUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.researcherpool.api.response.UserItem
import com.example.researcherpool.api.response.UserListResponse
import com.example.researcherpool.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormUserViewModel : ViewModel() {

    private val _formUser = MutableLiveData<List<UserItem>?>()
    val formUser: MutableLiveData<List<UserItem>?> get() = _formUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> =_isLoading

    private val _Msg = MutableLiveData<String?>()
    val Msg : MutableLiveData<String?> = _Msg

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    fun getUserList(){
        _isLoading.value =true
        val client = ApiConfig.getApiService().getUserList()
        client.enqueue(object: Callback<UserListResponse>{
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value  = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        _isError.value = false
                        _formUser.value = responseBody.result as List<UserItem>?
                        _Msg.value = responseBody.msg
                    }else{
                        _isError.value = true
                        _Msg.value = "response is null"
                    }
                }else{
                    _isError.value = true
                    _Msg.value = "response failed to success"
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                _isError.value = true
                _Msg.value = "failed to connect the server"
            }

        })
    }
}