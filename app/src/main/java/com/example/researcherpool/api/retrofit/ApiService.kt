package com.example.researcherpool.api.retrofit

import com.example.researcherpool.api.response.CountResponse
import com.example.researcherpool.api.response.DetailPartResponse
import com.example.researcherpool.api.response.DetailResponse
import com.example.researcherpool.api.response.LoginResponse
import com.example.researcherpool.api.response.MyFormResponse
import com.example.researcherpool.api.response.MyParticipantResponse
import com.example.researcherpool.api.response.PostResponse
import com.example.researcherpool.api.response.ResearchParticipantResponse
import com.example.researcherpool.api.response.UpdateResponse
import com.example.researcherpool.api.response.UploadParticipantResponse
import com.example.researcherpool.api.response.UserListResponse
import com.example.researcherpool.api.response.VerifPartResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/login")
    fun Login(
        @Field("email") email:String,
        @Field("password") password:String,
    ):Call<LoginResponse>

    @GET("api/myform/{email}")
    fun getMyForm(
        @Path("email") email: String
    ):Call<MyFormResponse>

    @FormUrlEncoded
    @POST("api/postforum")
    fun PostForum(
        @Field("title") title: String,
        @Field("description") description :String,
        @Field("link") link :String,
        @Field("email") email:String
    ): Call<PostResponse>

    @GET("api/mydetail/{id}")
    fun getMyDetail(
        @Path("id") id: String
    ):Call<DetailResponse>

    @FormUrlEncoded
    @POST("api/updateform/{id}")
    fun updateForm(
        @Path("id") id : String,
        @Field("title") title :String,
        @Field("description") description :String,
        @Field("link") link: String,
        @Field("status") status: String
    ):Call<UpdateResponse>

    @GET("api/userform")
    fun getUserList():Call<UserListResponse>

    @Multipart
    @POST("api/postparticipant")
    fun uploadParticipant(
        @Part("id_pool") idPool:RequestBody,
        @Part("email_user") emailUser:RequestBody,
        @Part file : MultipartBody.Part
    ):Call<UploadParticipantResponse>

    @GET("api/getmypart/{email}")
    fun getMyParticipation(
        @Path("email") email: String
    ):Call<MyParticipantResponse>

    @GET("api/getresearchpart/{email}")
    fun getMyPartR(
        @Path("email") email:String
    ):Call<ResearchParticipantResponse>

    @FormUrlEncoded
    @POST("api/verifypart")
    fun verifyPart(
        @Field("id") id:String,
        @Field("stat") stat:String
    ):Call<PostResponse>

    @GET("api/getdetailpart/{id}")
    fun getDetailPart(
        @Path("id") id :String
    ):Call<DetailPartResponse>

    @FormUrlEncoded
    @POST("api/register")
    fun regis(
        @Field("email") email :String,
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("type") type:String
    ):Call<PostResponse>

    @GET("api/verifres/{email}")
    fun verifres(
        @Path("email") email :String
    ):Call<PostResponse>

    @GET("api/getcount/{email}")
    fun getCount(
        @Path("email")email :String
    ):Call<CountResponse>

    @GET("api/veripart/{id}/{email}")
    fun getVeripart(
        @Path("id") id:String,
        @Path("email") email:String
    ):Call<VerifPartResponse>
}