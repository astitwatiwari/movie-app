package com.example.moviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.moviesapp.R;
import com.example.moviesapp.activities.ViewDetails;
import com.example.moviesapp.models.ResultsItem;

import java.util.List;


public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.MyViewHolder> {

    Context context;
    List<ResultsItem> resultsItems;

    public ViewPagerAdapter(Context context, List<ResultsItem> list) {
        this.context = context;
        this.resultsItems = list;

    }

    public void setList(List<ResultsItem> list){
        this.resultsItems = list;
    }

    @NonNull
    @Override
    public ViewPagerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.utils_viewpager_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.MyViewHolder holder, int position) {
        bindData(resultsItems.get(position), holder);
    }

    @Override
    public int getItemCount() {
        return (resultsItems != null) ? resultsItems.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView movieName;
        TextView movieRating;
        TextView movieDetail;
        ImageView moviePoster;
        ConstraintLayout movieBackdrop;
        Button viewDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            movieName = itemView.findViewById(R.id.subTextMovieNameId);
            movieRating = itemView.findViewById(R.id.subTextMovieRatingId);
            movieDetail = itemView.findViewById(R.id.subTextMovieDetailId);
            moviePoster = itemView.findViewById(R.id.subTextMovieImageId);
            movieBackdrop = itemView.findViewById(R.id.subTextMovieBackdropId);
            viewDetails = itemView.findViewById(R.id.subTextButtonId);
            viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ViewDetails.class);
                    i.putExtra("id",resultsItems.get(getAdapterPosition()).getId());
                    context.startActivity(i);
                }
            });
        }
    }

    public void bindData(ResultsItem resultsItem, MyViewHolder holder) {

        holder.movieDetail.setText(resultsItem.getOverview());
        holder.movieRating.setText(String.valueOf(resultsItem.getVoteAverage()));
        holder.movieName.setText(resultsItem.getTitle());
        Glide.with(context).load("https://image.tmdb.org/t/p/w342" + resultsItem.getPosterPath()).apply(new RequestOptions().fitCenter()).into(holder.moviePoster);
        Glide.with(context).load("https://image.tmdb.org/t/p/w1280" + resultsItem.getBackdropPath()).apply(new RequestOptions().fitCenter()).into(new CustomTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                holder.movieBackdrop.setBackground(resource);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });

    }

}
