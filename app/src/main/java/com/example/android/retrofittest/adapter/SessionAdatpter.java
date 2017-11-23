package com.example.android.retrofittest.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.retrofittest.R;
import com.example.android.retrofittest.model.Session;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class SessionAdatpter extends RecyclerView.Adapter<SessionAdatpter.MyViewHolder> {


    private Context mcontext;
    private List<Session> sessionList;

    public SessionAdatpter(Context context, List<Session> sessionList) {
        this.mcontext = context;
        this.sessionList = sessionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.session_item_row, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

                Session session = sessionList.get(position);
        /*try {
            holder.session_date.setText(session.getSessionDate()*//*formatDate(session.getSessionDate())*//*);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        holder.session_date.setText(session.getSessionDate()/*formatDate(session.getSessionDate())*/);

        holder.session_cost.setText(Integer.toString(session.getCost()));
        holder.session_txt.setText(session.getDetails());


    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView session_date, session_cost ;
        ExpandableTextView session_txt;

        public MyViewHolder(View itemView) {
            super(itemView);

            session_date = (TextView) itemView.findViewById(R.id.session_date);
            session_cost = (TextView)itemView.findViewById(R.id.session_cost);
            session_txt = (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);
        }
    }

    private String formatDate(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");
           final Date newDate = format.parse(date);

        format = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
        String date1 = format.format(newDate);
        return date1;

    }
}
