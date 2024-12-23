package com.example.researcherpool.ui.MyForm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.researcherpool.api.response.MyFormResponse
import com.example.researcherpool.api.response.ResultItem
import com.example.researcherpool.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFormViewModel : ViewModel() {
    private val _formMyForm = MutableLiveData<List<ResultItem>?>()
    val formMyForm: MutableLiveData<List<ResultItem>?> get() = _formMyForm

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> =_isLoading

    private val _Msg = MutableLiveData<String>()
    val Msg : MutableLiveData<String> = _Msg

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    fun getFormlist(email:String){
        _isLoading.value = true
        val client= ApiConfig.getApiService().getMyForm(email)
        client.enqueue(object:Callback<MyFormResponse>{
            override fun onResponse(
                call: Call<MyFormResponse>,
                response: Response<MyFormResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _isLoading.value = false
                        _isError.value = false
                        _formMyForm.value = responseBody.result as List<ResultItem>?
                    }else{
                        _isLoading.value = false
                        _isError.value = true
                        _Msg.value = "isNull"
                    }
                }else{
                    _isLoading.value = false
                    _isError.value = true
                    _Msg.value = "response is not success"
                }
            }

            override fun onFailure(call: Call<MyFormResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                _Msg.value = "cannot connect to the server"
            }

        })
    }
}