package com.example.researcherpool.ui.ParticipantR

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.researcherpool.api.response.PartR
import com.example.researcherpool.api.response.ResearchParticipantResponse
import com.example.researcherpool.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParticipantRViewModel : ViewModel() {

    private val _myPartR = MutableLiveData<List<PartR>?>()
    val myPartR : MutableLiveData<List<PartR>?> get()  = _myPartR

    private val _isError = MutableLiveData<Boolean>()
    val isError :MutableLiveData<Boolean> = _isError

    private val _Msg = MutableLiveData<String>()
    val Msg : MutableLiveData<String> = _Msg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading:MutableLiveData<Boolean> = _isLoading

    fun getMyPartR(email :String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getMyPartR(email)
        client.enqueue(object:Callback<ResearchParticipantResponse>{
            override fun onResponse(
                call: Call<ResearchParticipantResponse>,
                response: Response<ResearchParticipantResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        _isError.value = false
                        _myPartR.value = responseBody.result as List<PartR>?
                    }else{
                        _isError.value = false
                        _Msg.value = "response is null"
                    }
                }else{
                    _isError.value = false
                    _Msg.value = "Error to catch response"
                }
            }

            override fun onFailure(call: Call<ResearchParticipantResponse>, t: Throwable) {
                _isError.value = false
                _Msg.value = t.message
            }

        })
    }
}