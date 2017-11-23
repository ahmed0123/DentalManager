package com.example.android.retrofittest.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.retrofittest.R;
import com.example.android.retrofittest.model.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHold> {

    private Context mcontext;
    private ArrayList<Image> imageArrayList;


    public ImageAdapter(Context mcontext, ArrayList<Image> imageArrayList) {
        this.mcontext = mcontext;
        this.imageArrayList = imageArrayList;
    }


    @Override
    public ImageViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ImageViewHold(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHold holder, int position) {
        final Image image = imageArrayList.get(position);
        Picasso.with(mcontext).load(image.getImagePath()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return imageArrayList.size();
    }

    class ImageViewHold extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ImageViewHold(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imagegallery);
        }
    }

}
