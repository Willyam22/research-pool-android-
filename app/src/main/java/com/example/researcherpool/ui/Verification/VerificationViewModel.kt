package com.example.researcherpool.ui.Verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.researcherpool.api.response.DetailItem
import com.example.researcherpool.api.response.DetailPartResponse
import com.example.researcherpool.api.response.PostResponse
import com.example.researcherpool.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerificationViewModel: ViewModel() {
    private val _detailVerif =MutableLiveData<List<DetailItem>?>()
    val detailVerif : MutableLiveData<List<DetailItem>?> get() = _detailVerif

    private val _isError = MutableLiveData<Boolean>()
    val isError:MutableLiveData<Boolean> = _isError

    private val _isErroru = MutableLiveData<Boolean>()
    val isErroru:MutableLiveData<Boolean> = _isErroru

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading:MutableLiveData<Boolean> = _isLoading

    private val _Msg = MutableLiveData<String?>()
    val Msg: MutableLiveData<String?> = _Msg

    private val _Status = MutableLiveData<String?>()
    val Status : MutableLiveData<String?> = _Status


    fun getDetailPart(id:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailPart(id)
        client.enqueue(object: Callback<DetailPartResponse>{
            override fun onResponse(
                call: Call<DetailPartResponse>,
                response: Response<DetailPartResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody !== null){
                        _detailVerif.value = responseBody.result as List<DetailItem>?
                        _Status.value = responseBody.result?.get(0)?.status
                        _isError.value = false
                    }else{
                        _isError.value = true
                        _Msg.value = "response is null"
                    }
                }else{
                    _isError.value = true
                    _Msg.value = "failed to catch response"
                }
            }

            override fun onFailure(call: Call<DetailPartResponse>, t: Throwable) {
                _isError.value = true
                _Msg.value = "failed to connect to the server"
            }

        })
    }

    fun verifyparticipant(id:String, stat:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().verifyPart(id, stat)
        client.enqueue(object :Callback<PostResponse>{
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        _isErroru.value = false
                        _Msg.value = responseBody.msg
                    }else{
                        _isErroru.value = true
                        _Msg.value = "response is null"
                    }
                }else{
                    _isErroru.value = true
                    _Msg.value = "failed to catch response"
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                _isErroru.value = true
                _Msg.value = "failed to connect to the server"
            }

        })
    }
}