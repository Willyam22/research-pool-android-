package com.example.researcherpool.ui.MainActiviy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.researcherpool.api.response.LoginResponse
import com.example.researcherpool.api.response.PostResponse
import com.example.researcherpool.api.retrofit.ApiConfig
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel:ViewModel() {
    private val _isError =MutableLiveData<Boolean>()
    val isError : MutableLiveData<Boolean> =_isError

    private val _isErrorV = MutableLiveData<Boolean>()
    val isErrorV:MutableLiveData<Boolean> = _isErrorV

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : MutableLiveData<Boolean> = _isLoading

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty:MutableLiveData<Boolean> = _isEmpty

    private val _txtMsg = MutableLiveData<String>()
    val txtMsg:MutableLiveData<String> = _txtMsg

    private val _email = MutableLiveData<String?>()
    val email: MutableLiveData<String?> = _email

    private val _username = MutableLiveData<String?>()
    val username : MutableLiveData<String?> = _username

    private val _type = MutableLiveData<String?>()
    val type : MutableLiveData<String?> = _type

    private val _isEmptyReg = MutableLiveData<Boolean>()
    val isEmptyReg :MutableLiveData<Boolean> = _isEmptyReg

    private val _isNotSame = MutableLiveData<Boolean>()
    val isNotSame:MutableLiveData<Boolean> = _isNotSame

    private val _isEmptyRes = MutableLiveData<Boolean>()
    val isEmptyRes:MutableLiveData<Boolean> = _isEmptyRes

    private val _isVerif = MutableLiveData<Boolean>()
    val isVerif:MutableLiveData<Boolean> = _isVerif

    private val _MsgV = MutableLiveData<String?>()
    val MsgV : MutableLiveData<String?> = _MsgV

    private val _isErrorReg = MutableLiveData<Boolean>()
    val isErroReg:MutableLiveData<Boolean> = _isErrorReg

    private val _MsgR = MutableLiveData<String>()
    val msgR:MutableLiveData<String> = _MsgR


    fun verifres(Email :String){
        if(Email.isEmpty() ){
            _isEmptyRes.value = true
        }else{
            _isEmptyRes.value = false
            _isLoading.value = true
            val client = ApiConfig.getApiService().verifres(Email)
            client.enqueue(object:Callback<PostResponse>{
                override fun onResponse(
                    call: Call<PostResponse>,
                    response: Response<PostResponse>
                ) {
                    if(response.isSuccessful){
                        _isLoading.value = false
                        val responseBody = response.body()
                        if(responseBody != null){
                            _isErrorV.value = false
                            _isVerif.value = true
                            _MsgV.value = responseBody.msg
                        }
                    }else{
                        if(_isErrorV.value == false || _isErrorV.value == null){
                            _isErrorV.value  =true
                        }
                        _isVerif.value = false
                        _MsgV.value = (response.errorBody() as ResponseBody).string()
                    }
                }
                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    if(_isErrorV.value == false|| _isErrorV.value == null){
                        _isErrorV.value  =true
                    }
                    _isVerif.value = false
                    _MsgV.value = "on Failure ${t.message}"
                }

            })
        }
    }

    fun login(Email :String, Password:String){
        if(Email.isEmpty() || Password.isEmpty()){
            _isEmpty.value = true
        }else{
            _isLoading.value = true
            val client = ApiConfig.getApiService().Login(Email, Password)
            client.enqueue(object:Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if(response.isSuccessful){
                        _isLoading.value = false
                        val responseBody = response.body()
                        if(responseBody != null){
                            _isError.value = false
                            _email.value = responseBody.userId?.email
                            _username.value = responseBody.userId?.username
                            _type.value = responseBody.type
                        }
                    }else{
                        if(_isError.value == null){
                            _isError.value  =true
                        }
                        _isLoading.value = false
                        val errorBody =(response.errorBody() as ResponseBody).string()
                        _txtMsg.value = errorBody
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value=false
                    if(_isError.value == null){
                        _isError.value  =true
                    }
                    _txtMsg.value = "onFailure: ${t.message}"
                }

            })
        }

    }

    fun register(Email :String, Username: String, Password:String, conPassword:String, type:String){
        if(Email.isEmpty() || Username.isEmpty() || Password.isEmpty() || conPassword.isEmpty()){
            _isEmptyReg.value = true
        }else{
            _isEmptyReg.value = false
            if(Password != conPassword){
                _isNotSame.value = true
            }else{
                _isNotSame.value = false
                _isLoading.value = true
                val client = ApiConfig.getApiService().regis(Email, Username, Password, type)
                client.enqueue(object: Callback<PostResponse>{
                    override fun onResponse(
                        call: Call<PostResponse>,
                        response: Response<PostResponse>
                    ) {
                        if(response.isSuccessful){
                            _isLoading.value = false
                            val responseBody = response.body()
                            if(responseBody!= null){

                                _isErrorReg.value = false
                                _MsgV.value = "succeed to regis as a $type"
                            }
                        }else{
                            _isLoading.value =false
                            if(_isErrorReg.value == false || _isErrorReg.value == null){
                                _isErrorReg.value = true
                            }
                            _MsgR.value = (response.errorBody() as ResponseBody).string()
                        }
                    }

                    override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                        _isLoading.value =false
                        if(_isErrorReg.value == false || _isErrorReg.value == null){
                            _isErrorReg.value = true
                        }
                        _MsgR.value = "onFailur: ${t.message}"
                    }

                })
            }
        }
    }
}