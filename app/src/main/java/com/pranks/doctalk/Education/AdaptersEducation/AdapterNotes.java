package com.pranks.doctalk.Education.AdaptersEducation;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.pranks.doctalk.Education.PdfPreview;
import com.pranks.doctalk.Education.UploaderClass.UploadNotes;
import com.pranks.doctalk.R;


import java.security.Timestamp;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.MyViewHolder> {
    private ArrayList<UploadNotes> list1;
    Context context;

    public AdapterNotes(Context context, ArrayList<UploadNotes> list1) {
        this.context = context;
        this.list1 = list1;
        Collections.sort(list1, new Sortbyroll());
    }

    class Sortbyroll implements Comparator<UploadNotes> {
        public int compare(UploadNotes a, UploadNotes b) {
            return (int)(-a.getTimestamp() + b.getTimestamp());
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView notestitle,notesdis,downloads,date,uploadername;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            notestitle = itemView.findViewById(R.id.cardView_notes_title);
            notesdis=itemView.findViewById(R.id.cardView_notes_dis);
            downloads=itemView.findViewById(R.id.cardView_downloads);
            date=itemView.findViewById(R.id.card_date);
            uploadername=itemView.findViewById(R.id.card_uploader_name);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notes_design, viewGroup, false);
        return new MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String toDate(long timestamp) {
        LocalDate date = Instant.ofEpochMilli(timestamp * 1000).atZone(ZoneId.systemDefault()).toLocalDate();
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag((list1.get(i)));
        UploadNotes upl = list1.get(i);
        viewHolder.notestitle.setText(upl.getNoteTitle());
        viewHolder.notesdis.setText(upl.getNoteDis());
        viewHolder.downloads.setText(1000+"");
        viewHolder.uploadername.setText(upl.getUploadername());
        viewHolder.date.setText(toDate(upl.getTimestamp()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, upl.getKey(), Toast.LENGTH_SHORT).show();
                Intent i=new Intent(context, PdfPreview.class);
                i.putExtra("key",upl.getKey());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }
}
