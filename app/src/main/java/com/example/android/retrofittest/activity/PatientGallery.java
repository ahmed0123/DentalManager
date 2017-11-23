package com.example.android.retrofittest.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.android.retrofittest.R;
import com.example.android.retrofittest.adapter.ImageAdapter;
import com.example.android.retrofittest.model.Image;
import com.example.android.retrofittest.model.ResultImages;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class PatientGallery extends AppCompatActivity {

    private RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    private ArrayList<Image> imageArrayList;
    String patient_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patient_id = extras.getString("PATIENTID");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://clinicman.000webhostapp.com/clinic/clinicManage/ImageUploading/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GalleryApi galleryApi = retrofit.create(GalleryApi.class);

        Call<ResultImages> imageConnection = galleryApi.getImageList(patient_id);

        imageConnection.enqueue(new Callback<ResultImages>() {
            @Override
            public void onResponse(Call<ResultImages> call, Response<ResultImages> response) {
                imageArrayList = (ArrayList<Image>) response.body().getImages();
                imageAdapter = new ImageAdapter(getApplicationContext(), imageArrayList);
                recyclerView.setAdapter(imageAdapter);

            }

            @Override
            public void onFailure(Call<ResultImages> call, Throwable t) {
                Log.e("ERR",t.getMessage());

            }
        });


    }

    interface GalleryApi {

        @FormUrlEncoded
        @POST("img_fetch_from_server.php")
        Call<ResultImages> getImageList(@Field("patient_id") String patientID);
    }

}
