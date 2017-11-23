package com.example.android.retrofittest.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.retrofittest.R;
import com.example.android.retrofittest.adapter.SessionAdatpter;
import com.example.android.retrofittest.model.Cost;
import com.example.android.retrofittest.model.Patient;
import com.example.android.retrofittest.model.ResultCost;
import com.example.android.retrofittest.model.ResultSession;
import com.example.android.retrofittest.model.ResultState;
import com.example.android.retrofittest.model.Session;
import com.example.android.retrofittest.rest.ApiClient;
import com.example.android.retrofittest.rest.ApiInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {


    Patient patient;
    Button galley_button, upload_button;
    private String date;
    final Context c = this;
    int total_cost = 0;
    private SessionAdatpter adatpter;
    private RecyclerView recyclerView;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    TextView periodontal_txt_view, systemic_txt_view, session_count_txt_view ,total_cost_txt_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        periodontal_txt_view = (TextView) findViewById(R.id.per_txt);
        systemic_txt_view = (TextView) findViewById(R.id.sys_txt);
        session_count_txt_view = (TextView) findViewById(R.id.session_count);
        total_cost_txt_view = (TextView) findViewById(R.id.cost_txt);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("patient")) {
            patient = getIntent().getParcelableExtra("patient");

            periodontal_txt_view.setText(patient.getPeriodontal());
            systemic_txt_view.setText(patient.getSystemic());
            // add back arrow to toolbar
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle(patient.getName());
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


        }
        getCostsLists();


        galley_button = (Button)findViewById(R.id.gallery_button);
        galley_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PatientGallery.class);
                intent.putExtra("PATIENTID",String.valueOf(patient.getId()));
                startActivity(intent);
            }
        });

        upload_button = (Button) findViewById(R.id.uplaod_button);
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UploadImages.class);
                intent.putExtra("PATIENTID",String.valueOf(patient.getId()));
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        showSessionList();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.call:
                if (checkPermission(Manifest.permission.CALL_PHONE)) {
                    String dial = "tel:" + patient.getPhone();
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                } else {
                    Toast.makeText(DetailsActivity.this, "Permission call phone denied", Toast.LENGTH_LONG).show();
                }

                return true;
            case R.id.new_session:
                showSessionDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showSessionDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(DetailsActivity.this);
        View mView1 = layoutInflater.inflate(R.layout.patient_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView1);

        final EditText sessionDescription = (EditText) mView1.findViewById(R.id.descriptionInputDialog);
        final EditText sessionCost = (EditText) mView1.findViewById(R.id.costInputDialog);
        final EditText mDisplaydate = (EditText) mView1.findViewById(R.id.dateInputDialog);
        mDisplaydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                date = month + "/" + dayOfMonth + "/" + year;
                mDisplaydate.setText(date);
            }
        };

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String details = sessionDescription.getText().toString();
                        int cost = Integer.valueOf(sessionCost.getText().toString());


                        ApiInterface apiCreeateSession = ApiClient.getClient().create(ApiInterface.class);
                        Call<ResultState> sessionConn = apiCreeateSession.createSession(date, details, cost, patient.getId());
                        sessionConn.enqueue(new Callback<ResultState>() {
                            @Override
                            public void onResponse(Call<ResultState> call, Response<ResultState> response) {
                                Toast.makeText(getApplicationContext(), "Session added successfully" , Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<ResultState> call, Throwable t) {

                            }
                        });
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    private void showDateDialog() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(DetailsActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    private void showSessionList() {
        ApiInterface apiInt = ApiClient.getClient().create(ApiInterface.class);
        Call<ResultSession> sessionConnection = apiInt.getSessionByUserID(patient.getId());

        sessionConnection.enqueue(new Callback<ResultSession>() {
            @Override
            public void onResponse(Call<ResultSession> call, Response<ResultSession> response) {
                List<Session> sessionList = response.body().getSessions();
                adatpter = new SessionAdatpter(getApplicationContext(), sessionList);
                recyclerView.setAdapter(adatpter);
                adatpter.notifyDataSetChanged();
                session_count_txt_view.setText(String.valueOf(response.body().getSessions().size()));
            }

            @Override
            public void onFailure(Call<ResultSession> call, Throwable t) {

            }
        });
    }

    private void getCostsLists(){
        ApiInterface apiInt = ApiClient.getClient().create(ApiInterface.class);
        Call<ResultCost> costsconnection = apiInt.getTotalCost(patient.getId());

        costsconnection.enqueue(new Callback<ResultCost>() {
            @Override
            public void onResponse(Call<ResultCost> call, Response<ResultCost> response) {
                ArrayList<Cost> costArrayList = (ArrayList<Cost>) response.body().getCosts();
                int size = costArrayList.size();
                for (int i = 0; i<size; i++){

                    total_cost += costArrayList.get(i).getCost();
                }
                total_cost_txt_view.setText(String.valueOf(total_cost));

            }

            @Override
            public void onFailure(Call<ResultCost> call, Throwable t) {

            }
        });

    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
