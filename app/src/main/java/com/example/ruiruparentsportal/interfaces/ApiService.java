package com.example.ruiruparentsportal.interfaces;

import com.example.ruiruparentsportal.response.ParentResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("login.php")
    Call<ParentResponse> loginParent(
            @Field("token") String token,
            @Field("phone") String phone,
            @Field("token") String name,
            @Field("token") String email,
            @Field("password") String password


    );

    @FormUrlEncoded
    @POST("register.php")
    Call<ParentResponse> registerParent(
            @Field("token") String token,
            @Field("name") String name,
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("password") String password
    );
}
