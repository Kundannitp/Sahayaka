package com.pranks.doctalk.BlogsAndHome.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.ProfileClass;
import com.pranks.doctalk.R;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterLiked extends RecyclerView.Adapter<AdapterLiked.MyViewHolder> {
    private ArrayList<ProfileClass> list1;
    Context context;

    public AdapterLiked(Context context, ArrayList<ProfileClass> list1) {
        this.context = context;
        this.list1 = list1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView personname,personemail;
        CircleImageView personimg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            personname = itemView.findViewById(R.id.cardView_commodityName);
            personimg = itemView.findViewById(R.id.cardView_commodityImageView);
            personemail=itemView.findViewById(R.id.cardView_commodityemail);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.likedpersondesign, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag((list1.get(i)));
        ProfileClass blg = list1.get(i);
        viewHolder.personname.setText(blg.getName());
        viewHolder.personemail.setText(blg.getEmail());
        Glide.with(context)
                .load(blg.getProfileimg())
                .into(viewHolder.personimg);
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

}


