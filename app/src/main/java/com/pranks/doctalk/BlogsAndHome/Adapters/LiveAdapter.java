package com.pranks.doctalk.BlogsAndHome.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.pranks.doctalk.BlogsAndHome.UploadingClass.LiveClass;
import com.pranks.doctalk.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.MyViewHolder> {
    private ArrayList<LiveClass> list1;
    Context context;

    public LiveAdapter(Context context, ArrayList<LiveClass> list1) {
        this.context = context;
        this.list1 = list1;
        Collections.sort(list1,new Sortbyroll());
    }

    class Sortbyroll implements Comparator<LiveClass>
    {
        public int compare(LiveClass a, LiveClass b)
        {
            return (-a.getTotalcases() + b.getTotalcases());
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView country,countCountry;
        HorizontalBarChart skillRatingChart;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.cardView_countryname);
            skillRatingChart=itemView.findViewById(R.id.skill_rating_chart);
            countCountry=itemView.findViewById(R.id.count_country);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.designoflive, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag((list1.get(i)));
        LiveClass blg = list1.get(i);
        if(blg.getCountryname().equals("Total:"))
            viewHolder.countCountry.setText("");
        else
            viewHolder.countCountry.setText(""+(i+1));
        viewHolder.country.setText(blg.getCountryname());
        setSkillGraph(blg,viewHolder);
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }


    void setSkillGraph(LiveClass blg,MyViewHolder viewHolder){
        BarData data = new BarData( getDataSet(blg));
        viewHolder.skillRatingChart.setData(data);
        viewHolder.skillRatingChart.animateXY(1000, 1000);
        viewHolder.skillRatingChart.getLegend().setEnabled(false);
        viewHolder.skillRatingChart.getDescription().setEnabled(false);
        viewHolder.skillRatingChart.getXAxis().setEnabled(false);
        viewHolder.skillRatingChart.getAxisLeft().setDrawAxisLine(false);
        viewHolder.skillRatingChart.getAxisLeft().setDrawGridLines(false);
        viewHolder.skillRatingChart.getXAxis().setDrawGridLines(false);
        viewHolder.skillRatingChart.invalidate();

    }




    private ArrayList getDataSet(LiveClass blg) {
        ArrayList dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(0,(float)blg.getTotalcases()); // Total
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(1,(float)blg.getTotaldeath()); // Death
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(2,(float)blg.getTotalrecovered()); // Recovered
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry( 3,(float)blg.getActivecases()); // Active
        valueSet1.add(v1e4);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "People affected");
        barDataSet1.setColors(new int[]{Color.rgb(0, 155, 0)
                ,Color.rgb(155, 0, 0)
                ,Color.rgb(8, 87, 109)
                ,Color.rgb(180, 155, 0)
        });
        dataSets = new ArrayList();
        dataSets.add(barDataSet1);
        return dataSets;
    }


    public void updateList(ArrayList<LiveClass> list){
        this.list1 = list;
        Collections.sort(list1,new Sortbyroll());
        notifyDataSetChanged();
    }


}


