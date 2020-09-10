package com.example.moviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesapp.R;
import com.example.moviesapp.activities.ViewDetails;
import com.example.moviesapp.models.ResultsItem;


import java.util.List;

public class PopularMoviesRecyclerAdapter extends RecyclerView.Adapter<PopularMoviesRecyclerAdapter.MyViewHolder> {

    Context context;
    List<ResultsItem> list;

    public PopularMoviesRecyclerAdapter(Context context, List<ResultsItem> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<ResultsItem>list){
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.utils_popular_recycler_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMoviesRecyclerAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load("https://image.tmdb.org/t/p/w342" + list.get(position).getPosterPath()).into(holder.iv);
        holder.tv.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        CardView viewDetails;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.popularRecyclerMovieImageId);
            tv = itemView.findViewById(R.id.popularRecyclerMovieNameId);
            viewDetails = itemView.findViewById(R.id.cardView);

            viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ViewDetails.class);
                    i.putExtra("id",list.get(getAdapterPosition()).getId());
                    context.startActivity(i);
                }
            });
        }
    }
}
