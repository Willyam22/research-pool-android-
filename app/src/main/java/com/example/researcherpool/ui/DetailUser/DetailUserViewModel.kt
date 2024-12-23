package com.example.researcherpool.ui.DetailUser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.researcherpool.api.response.DetailResponse
import com.example.researcherpool.api.response.ItemDetail
import com.example.researcherpool.api.response.VerifPartResponse
import com.example.researcherpool.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel: ViewModel() {
    private val _myDetail= MutableLiveData<List<ItemDetail>?>()
    val myDetail : MutableLiveData<List<ItemDetail>?> get() = _myDetail

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    private val _isLoading  = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    private val _Msg = MutableLiveData<String>()
    val Msg:MutableLiveData<String> = _Msg

    private val _photo = MutableLiveData<String?>()
    val photo : MutableLiveData<String?> = _photo

    private val _ispart =MutableLiveData<Boolean>()
    val ispart:MutableLiveData<Boolean> = _ispart

    private val _status = MutableLiveData<String?>()
    val status : MutableLiveData<String?> = _status

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
                            _Msg.value = "response null"
                        }
                    }

                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                if(_isError.value == null){
                    _isError.value = true
                    _Msg.value = "Failed to connect server"
                }
            }

        })
    }

    fun getVerif(id:String,email:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getVeripart(id, email)
        client.enqueue(object : Callback<VerifPartResponse>{
            override fun onResponse(
                call: Call<VerifPartResponse>,
                response: Response<VerifPartResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        _ispart.value = true
                        val path = responseBody.photo
                        val name = path?.substringAfterLast("\\")
                        val finalname = name?.substringBeforeLast(".")
                        _photo.value =finalname
                        _status.value = responseBody.status
                    }
                }else{
                    _isLoading.value = false
                    _ispart.value = false
                }
            }

            override fun onFailure(call: Call<VerifPartResponse>, t: Throwable) {
                _isLoading.value = false
                _ispart.value = false
            }

        })
    }
}