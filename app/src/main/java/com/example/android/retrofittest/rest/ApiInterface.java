package com.example.android.retrofittest.rest;


import com.example.android.retrofittest.model.ResultCost;
import com.example.android.retrofittest.model.ResultPatient;
import com.example.android.retrofittest.model.ResultSession;
import com.example.android.retrofittest.model.ResultState;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiInterface {

    @GET("patients")
    Call<ResultPatient> getAllPatient();

    @FormUrlEncoded
    @POST("createPatient")
    Call<ResultState> createPatient(@Field("name") String name,
                                    @Field("phone") String phone,
                                    @Field("periodontal") String periodontal,
                                    @Field("systemic") String systemic

    );

    @GET("patient/{id}")
    Call<ResultSession> getSessionByUserID(@Path("id") int userID);

    @GET("costs/{id}")
    Call<ResultCost> getTotalCost (@Path("id") int userID);

    @FormUrlEncoded
    @POST("createsession")
    Call<ResultState> createSession(@Field("session_date") String sessionDate,
                                    @Field("details") String details,
                                    @Field("cost") int cost,
                                    @Field("patient_id") int pat_id);
}
