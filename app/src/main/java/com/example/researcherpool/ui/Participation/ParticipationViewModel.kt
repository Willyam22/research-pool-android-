package com.example.researcherpool.ui.Participation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.researcherpool.api.response.CountItem
import com.example.researcherpool.api.response.CountResponse
import com.example.researcherpool.api.response.MyParticipantResponse
import com.example.researcherpool.api.response.ParticipateItem
import com.example.researcherpool.api.retrofit.ApiConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParticipationViewModel : ViewModel() {

    private val _MyParticipation = MutableLiveData<List<ParticipateItem>?>()
    val MyParticipation : MutableLiveData<List<ParticipateItem>?> get() = _MyParticipation

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> = _isError

    private val _Msge = MutableLiveData<String>()
    val Msge:MutableLiveData<String> = _Msge

    private val _MsgC = MutableLiveData<String>()
    val MsgC:MutableLiveData<String> = _MsgC

    private val _isErrorC = MutableLiveData<Boolean>()
    val isErrorC :MutableLiveData<Boolean> = _isErrorC

    private val _listCount = MutableLiveData<List<CountItem>?>()
    val listCount : MutableLiveData<List<CountItem>?> get()=_listCount

    fun getPart(email:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getMyParticipation(email)
        client.enqueue(object:Callback<MyParticipantResponse>{
            override fun onResponse(
                call: Call<MyParticipantResponse>,
                response: Response<MyParticipantResponse>
            ) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        _MyParticipation.value = responseBody.result as List<ParticipateItem>?
                        _isError.value = false
                    }
                    else{
                        _isError.value = true
                        _Msge.value = "isEmpty"
                    }
                }else{
                    _isError.value = true
                    _Msge.value = "failed to catch response"
                }
            }

            override fun onFailure(call: Call<MyParticipantResponse>, t: Throwable) {
                _isError.value = true
                _Msge.value = "failed to connect the server"
            }

        })
    }

    fun getCount (Email:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getCount(Email)
        client.enqueue(object :Callback<CountResponse>{
            override fun onResponse(call: Call<CountResponse>, response: Response<CountResponse>) {
                if(response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if(responseBody != null){
                        _isErrorC.value = false
                        _listCount.value = responseBody.result as List<CountItem>?
                    }
                }else{
                    if(_isErrorC.value == false || _isErrorC == null){
                        _isErrorC.value = true
                    }
                    _MsgC.value = (response.errorBody() as ResponseBody).string()
                }
            }

            override fun onFailure(call: Call<CountResponse>, t: Throwable) {
                if(_isErrorC.value == false || _isErrorC == null){
                    _isErrorC.value = true
                }
                _MsgC.value = "onFailure ${t.message}"
            }

        })
    }
}