package github.vege19.popnow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import github.vege19.popnow.Models.Movie.PopularMovie;
import github.vege19.popnow.MovieDetailsActivity;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private List<PopularMovie> popularMovies;
    private Context context;

    public MoviesAdapter(List<PopularMovie> popularMovies, Context context) {
        this.popularMovies = popularMovies;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PopularMovie popularMovie = popularMovies.get(position);

        Glide.with(context)
                .load(ApiService.imageURL + popularMovie.getPoster_path())
                .into(holder.moviePoster);

        //Intent to details
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra("movieDetails", popularMovie);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return popularMovies.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView moviePoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            moviePoster = itemView.findViewById(R.id.moviePoster);

        }

    }

}
