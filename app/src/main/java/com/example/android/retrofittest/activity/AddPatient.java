package com.example.android.retrofittest.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.retrofittest.R;
import com.example.android.retrofittest.adapter.PatientsAdapter;
import com.example.android.retrofittest.model.ResultState;
import com.example.android.retrofittest.rest.ApiClient;
import com.example.android.retrofittest.rest.ApiInterface;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.id;
import static android.R.attr.name;

public class AddPatient extends AppCompatActivity implements View.OnClickListener {


    Button savButton, canselButton;
    EditText nameEditText, perioEditText, systemicEditText, phoneEditText;
    private TextInputLayout inputLayoutname, inputLayoutperiod, inputLayoutsystemic, inputLayoutphone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_patient);

        inputLayoutname = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutperiod = (TextInputLayout) findViewById(R.id.input_layout_perio);
        inputLayoutsystemic = (TextInputLayout) findViewById(R.id.input_layout_systemic);
        inputLayoutphone = (TextInputLayout) findViewById(R.id.input_layout_phone);



        nameEditText = (EditText) findViewById(R.id.name_edit_txt);
        perioEditText = (EditText) findViewById(R.id.perio_edit_txt);
        systemicEditText = (EditText) findViewById(R.id.systemic_edit_txt);
        phoneEditText = (EditText) findViewById(R.id.phone_edit_txt);



        nameEditText.addTextChangedListener(new MyTextWatcher(nameEditText));
        perioEditText.addTextChangedListener(new MyTextWatcher(perioEditText));
        systemicEditText.addTextChangedListener(new MyTextWatcher(systemicEditText));
        phoneEditText.addTextChangedListener(new MyTextWatcher(phoneEditText));



        savButton = (Button) findViewById(R.id.save_button);
        savButton.setOnClickListener(this);

        canselButton = (Button) findViewById(R.id.cansel_button);
        canselButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
                submitForm();
                reservationSuccess();
                break;
            case R.id.cansel_button:
                startMainActivity();
        }
    }


    private void submitForm() {
        if (!validateName()) {
            return;
        }
        if (!validatePeriod()) {
            return;
        }
        if (!validateSystemic()) {
            return;
        }
        if (!validatePhone()) {
            return;
        }
        reservationSuccess();
        startMainActivity();
    }

    private boolean validateName() {
        if (nameEditText.getText().toString().trim().isEmpty()) {
            inputLayoutname.setError(getString(R.string.error_name));
            requestFocus(nameEditText);
            return false;
        } else {
            inputLayoutname.setErrorEnabled(false);
        }
        return true;
    }


    private boolean validatePeriod() {
        if (perioEditText.getText().toString().trim().isEmpty()) {
            inputLayoutperiod.setError(getString(R.string.error_period));
            requestFocus(perioEditText);
            return false;

        } else {
            inputLayoutperiod.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateSystemic() {
        if (systemicEditText.getText().toString().trim().isEmpty()) {
            inputLayoutsystemic.setError(getString(R.string.error_systemic));
            requestFocus(systemicEditText);
            return false;
        } else {
            inputLayoutsystemic.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePhone() {
        if (phoneEditText.getText().toString().trim().isEmpty()) {
            inputLayoutphone.setError(getString(R.string.error_phone));
            requestFocus(phoneEditText);
            return false;
        } else {
            inputLayoutname.setErrorEnabled(false);
        }
        return true;
    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

    }

    void startMainActivity() {
        Intent intent1 = new Intent(AddPatient.this, MainActivity.class);
        startActivity(intent1);
    }


    private void reservationSuccess() {
        final String name = nameEditText.getText().toString();
        final String phone = phoneEditText.getText().toString();
        final String period = perioEditText.getText().toString();
        final String system = systemicEditText.getText().toString();


        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);

        Call<ResultState> addconection = api.createPatient(
                name,
                phone,
                period,
                system
        );
        addconection.enqueue(new Callback<ResultState>() {
            @Override
            public void onResponse(Call<ResultState> call, Response<ResultState> response) {
                nameEditText.setText("");
                perioEditText.setText("");
                systemicEditText.setText("");
                phoneEditText.setText("");
                requestFocus(nameEditText);
                /*Intent intent2 = new Intent(AddPatient.this, MainActivity.class);
                intent2.putExtra("NAME", name);
                intent2.putExtra("PERIOD", period);
                intent2.putExtra("PHONE", phone);
                intent2.putExtra("SYSTEMIC", system);
                intent2.putExtra("COST", cost);
                startActivity(intent2);*/
            }

            @Override
            public void onFailure(Call<ResultState> call, Throwable t) {

            }
        });
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.name_edit_txt:
                    validateName();
                    break;
                case R.id.perio_edit_txt:
                    validatePeriod();
                    break;
                case R.id.systemic_edit_txt:
                    validateSystemic();
                    break;
                case R.id.phone_edit_txt:
                    validatePhone();
                    break;

            }
        }
    }
}
