package com.example.android.retrofittest.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.retrofittest.R;
import com.example.android.retrofittest.Util.ConnectivityReceiver;
import com.example.android.retrofittest.Util.MyApplication;
import com.example.android.retrofittest.adapter.PatientsAdapter;
import com.example.android.retrofittest.model.Patient;
import com.example.android.retrofittest.model.ResultPatient;
import com.example.android.retrofittest.rest.ApiClient;
import com.example.android.retrofittest.rest.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListiner {

    private RecyclerView recyclerView;
    private ArrayList<Patient> mpatientLists;
    private PatientsAdapter adapter;
    private static final String NO_PATIENTS = "There is no patients please add patient";
    private static final String NO_CONNECTION = "There is no Network please check network connection";
    private TextView text_results;
    private static final String BUNDLE_RECYCLER_LAYOUT = "MainActivity.recycler.activity_main";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        text_results = (TextView) findViewById(R.id.txt_view);


        /*if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

        } else {

            savedInstanceState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
            }*/


        checkConnection();



        /*notifyAdapter();*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPatient.class);
                startActivity(intent);
            }
        });

    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showMainAction(isConnected);

    }

    private void showMainAction(boolean isConnected) {
        if (isConnected) {
            getAllPatientList();


        } else {
            showNetworkError();
        }
    }

    private void getAllPatientList() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResultPatient> connection = apiInterface.getAllPatient();
        connection.enqueue(new Callback<ResultPatient>() {
            @Override
            public void onResponse(Call<ResultPatient> call, Response<ResultPatient> response) {
                if (response.body().getPatients().size() > 0) {
                    mpatientLists = (ArrayList<Patient>) response.body().getPatients();
                    adapter = new PatientsAdapter(mpatientLists, getApplicationContext());

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else {
                    showPateintsErrpr();
                }
            }

            @Override
            public void onFailure(Call<ResultPatient> call, Throwable t) {
                Log.e("TAG", t.getMessage());
            }
        });
    }

    private void showPateintsErrpr() {
        text_results.setVisibility(View.VISIBLE);
        text_results.setText(NO_PATIENTS);
    }

    private void showNetworkError() {
        text_results.setVisibility(View.VISIBLE);
        text_results.setText(NO_CONNECTION);
    }

    @Override
    public void onNetworkConectionChanged(boolean isConnected) {
        showMainAction(isConnected);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
        getAllPatientList();
        text_results.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_item, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView(searchView);

        return true;
    }

    private void searchView(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}
 /*private void notifyAdapter() {
            Intent intent = getIntent();

            String name = intent.getStringExtra("NAME");
            String period = intent.getStringExtra("PERIOD");
            String phone = intent.getStringExtra("PHONE");
            String system = intent.getStringExtra("SYSTEMIC");
            String cost = intent.getStringExtra("COST");
            Patient patient = new Patient();
            patient.setName(name);
            patient.setPeriodontal(period);
            patient.setPhone(phone);
            patient.setCost(cost);
            patient.setSystemic(system);
            mpatientLists.add(patient);

            adapter.addItem(mpatientLists);
        }*/