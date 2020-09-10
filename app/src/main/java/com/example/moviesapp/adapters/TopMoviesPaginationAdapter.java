package com.example.moviesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesapp.R;
import com.example.moviesapp.activities.ViewDetails;
import com.example.moviesapp.models.ResultsItem;

import java.util.List;

public class TopMoviesPaginationAdapter extends RecyclerView.Adapter<TopMoviesPaginationAdapter.MyViewHolder> {

    Context context;
    List<ResultsItem> list;
    Typeface typefaceHeading, typefaceDetails, typefaceLight;

    public void setList(List<ResultsItem> resultsItems) {
        this.list = resultsItems;
    }

    OnBottomReachedListener onBottomReachedListener;

    public interface OnBottomReachedListener {
        void onBottomReached(int position);
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    public TopMoviesPaginationAdapter(Context context, List<ResultsItem> list) {
        this.context = context;
        this.list = list;
        typefaceHeading = Typeface.createFromAsset(context.getAssets(), "font/Oswald-SemiBold.ttf");
        typefaceDetails = Typeface.createFromAsset(context.getAssets(), "font/Oswald-Regular.ttf");
        typefaceLight = Typeface.createFromAsset(context.getAssets(), "font/Oswald-Light.ttf");


    }

    @NonNull
    @Override
    public TopMoviesPaginationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.utils_topmovies_recycler_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopMoviesPaginationAdapter.MyViewHolder holder, int position) {
        bindData(list.get(position), holder);
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView movieTitle;
        TextView movieYear;
        TextView movieRating;
        TextView movieDetail;
        ImageView moviePoster;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            movieTitle = itemView.findViewById(R.id.paginationMovieNameId);
            movieYear = itemView.findViewById(R.id.paginationMovieYearId);
            movieRating = itemView.findViewById(R.id.paginationMovieRatingId);
            movieDetail = itemView.findViewById(R.id.paginationMovieDetailId);
            moviePoster = itemView.findViewById(R.id.paginationMovieImageId);

            movieTitle.setTypeface(typefaceHeading);
            movieRating.setTypeface(typefaceLight);
            movieYear.setTypeface(typefaceLight);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ViewDetails.class);
                    i.putExtra("id",list.get(getAdapterPosition()).getId());
                    context.startActivity(i);
                }
            });
        }

    }

    public void bindData(ResultsItem resultsItem, MyViewHolder myViewHolder) {


        int position = myViewHolder.getAdapterPosition();

        if (position == list.size() - 5) {
            onBottomReachedListener.onBottomReached(position);
        }


        myViewHolder.movieTitle.setText(resultsItem.getTitle());
        myViewHolder.movieYear.setText(resultsItem.getReleaseDate().substring(0, 4));
        myViewHolder.movieRating.setText(String.valueOf(resultsItem.getVoteAverage()));
        myViewHolder.movieDetail.setText(resultsItem.getOverview());
        Glide.with(context).load("https://image.tmdb.org/t/p/w342" + resultsItem.getPosterPath()).into(myViewHolder.moviePoster);
    }
}
