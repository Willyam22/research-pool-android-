package com.example.researcherpool.ui.UpdateForm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.researcherpool.api.response.DetailResponse
import com.example.researcherpool.api.response.ItemDetail
import com.example.researcherpool.api.response.UpdateResponse
import com.example.researcherpool.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePostViewModel:ViewModel() {
    private val _myDetail=MutableLiveData<List<ItemDetail>?>()
    val myDetail : MutableLiveData<List<ItemDetail>?> get() = _myDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading :MutableLiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    private val _Msge = MutableLiveData<String?>()
    val Msge : MutableLiveData<String?> = _Msge

    private val _Status = MutableLiveData<String>()
    val Status:MutableLiveData<String> = _Status

    private val _isErroru = MutableLiveData<Boolean>()
    val isErroru:MutableLiveData<Boolean> = _isErroru

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty:MutableLiveData<Boolean> = _isEmpty

    fun setStatus(stat:String){
        _Status.value = stat
    }


    fun getDetail(id: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getMyDetail(id)
        client.enqueue(object :Callback<DetailResponse>{
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value=false
                    val responseBody = response.body()
                    if(responseBody != null){
                        _isError.value = false
                        _myDetail.value = responseBody.result as List<ItemDetail>?
                    }else{
                        if(_isError.value == null){
                            _isError.value = true
                            _Msge.value = "response null"
                        }
                    }

                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                if(_isError.value == null){
                    _isError.value = true
                    _Msge.value = "Failed to connect server"
                }
            }

        })
    }

    fun updateDetail(id: String, title: String, description: String, link: String, status: String){
        if(title.isEmpty() || description.isEmpty() || link.isEmpty() || status.isEmpty()){
            _isEmpty.value = true
        }else{
            _isEmpty.value = false
            _isLoading.value = true
            val client = ApiConfig.getApiService().updateForm(id,title,description,link,status)
            client.enqueue(object:Callback<UpdateResponse>{
                override fun onResponse(
                    call: Call<UpdateResponse>,
                    response: Response<UpdateResponse>
                ) {
                    if(response.isSuccessful){
                        _isLoading.value = false
                        val responseBody = response.body()
                        if(responseBody != null){
                            _isErroru.value = false
                            _Msge.value = responseBody.msg
                        }else{
                            if(_isErroru.value == null){
                                _isErroru.value = true
                            }
                            _Msge.value = "response is null"
                        }
                    }else{
                        if(_isErroru.value == null){
                            _isErroru.value = true
                        }
                        _Msge.value = "Failed on response"
                    }
                }

                override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                    if(_isErroru.value == null){
                        _isErroru.value = true
                    }
                    _Msge.value = "Failed on connection"
                }

            })
        }

    }
}