package com.example.researcherpool.ui.PostForm

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.researcherpool.api.response.PostResponse
import com.example.researcherpool.api.retrofit.ApiConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostFormViewModel:ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> =_isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError :MutableLiveData<Boolean> = _isError

    private val _Msg = MutableLiveData<String>()
    val Msg:MutableLiveData<String> = _Msg

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty:MutableLiveData<Boolean> = _isEmpty

    fun postForum(Title: String, description:String, link:String, email:String){
        if(Title.isEmpty() || description.isEmpty() || link.isEmpty() || email.isEmpty()){
            _isEmpty.value = true
        }else{
            _isEmpty.value = false
            _isLoading.value = true
            val client = ApiConfig.getApiService().PostForum(Title, description, link, email)
            client.enqueue(object: Callback<PostResponse>{
                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    if(response.isSuccessful){
                        val responseBody = response.body()
                        if(responseBody != null){
                            _isLoading.value = false
                            _isError.value = false
                            _Msg.value = "Succeed Posted"
                        }else{
                            _isLoading.value = false
                            if(_isError.value == null){
                                _isError.value = false
                            }
                            _Msg.value = "Response Empty"
                        }
                    }else{
                        if(_isError.value == null){
                            _isError.value = false
                        }
                        _isLoading.value = false
                        val errorBody =(response.errorBody() as ResponseBody).string()
                        _Msg.value = errorBody
                    }
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = true
                    _Msg.value = "cannot connect to the server"
                }

            })
        }
    }

}