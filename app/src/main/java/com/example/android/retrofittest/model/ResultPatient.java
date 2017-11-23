package com.example.android.retrofittest.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class ResultPatient implements Parcelable {

    @SerializedName("patients")
    @Expose
    private List<Patient> patients ;

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.patients);
    }

    public ResultPatient() {
    }

    protected ResultPatient(Parcel in) {
        this.patients = in.createTypedArrayList(Patient.CREATOR);
    }

    public static final Parcelable.Creator<ResultPatient> CREATOR = new Parcelable.Creator<ResultPatient>() {
        @Override
        public ResultPatient createFromParcel(Parcel source) {
            return new ResultPatient(source);
        }

        @Override
        public ResultPatient[] newArray(int size) {
            return new ResultPatient[size];
        }
    };
}
