package com.example.android.retrofittest.adapter;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.android.retrofittest.R;
import com.example.android.retrofittest.activity.DetailsActivity;
import com.example.android.retrofittest.activity.MainActivity;
import com.example.android.retrofittest.model.Patient;

import java.util.ArrayList;


public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.PatientViewHolder> implements Filterable {


    Context context;
    ArrayList<Patient> patients, filterList;
    CustomeFilter filter;
    Activity mActivity;


    public PatientsAdapter(ArrayList<Patient> patients, Context context) {

        this.patients = patients;
        this.filterList = patients;
        this.context = context;
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_item_row, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {

        Patient patient = patients.get(position);
        holder.patientID.setText(patient.getId().toString());
        holder.patientName.setText(patient.getName());

    }

    @Override
    public int getItemCount() {
        return patients.size();
    }


    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomeFilter(filterList, this);
        }
        return filter;
    }

    public void addItem(ArrayList<Patient> patients){
        this.patients = patients;
        notifyDataSetChanged();

    }
    public class PatientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView patientID, patientName;


        public PatientViewHolder(View itemView) {
            super(itemView);
            patientID = (TextView) itemView.findViewById(R.id.id_txt);
            patientName = (TextView) itemView.findViewById(R.id.name_txt);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {


                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Patient clickedDataItem = patients.get(pos);
                    Intent intent = new Intent(context , DetailsActivity.class);
                    intent.putExtra("patient", clickedDataItem);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

        }
    }

}
