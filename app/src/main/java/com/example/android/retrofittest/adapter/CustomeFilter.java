package com.example.android.retrofittest.adapter;


import android.widget.Filter;

import com.example.android.retrofittest.model.Patient;

import java.util.ArrayList;

public class CustomeFilter extends Filter {

    PatientsAdapter adapter;
    ArrayList<Patient> filterList;

    public CustomeFilter( ArrayList<Patient> filterList , PatientsAdapter adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();

        if (charSequence != null && charSequence.length() > 0)
        {
            charSequence = charSequence.toString().toUpperCase();

            ArrayList<Patient> filteredPatient = new ArrayList<>();

            for (int i = 0;i < filterList.size();i++)
            {
                if (filterList.get(i).getName().toUpperCase().contains(charSequence))
                {
                    filteredPatient.add(filterList.get(i));

                }
            }
            results.count = filteredPatient.size();
            results.values = filteredPatient;

        }
        else
        {
            results.count = filterList.size();
            results.values = filterList;
        }




        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        adapter.patients = (ArrayList<Patient>) filterResults.values;
        adapter.notifyDataSetChanged();
    }
}
