package github.vege19.popnow.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.vege19.popnow.Adapters.GenresAdapter;
import github.vege19.popnow.Models.Genre.Genre;
import github.vege19.popnow.Models.Genre.GenresResponse;
import github.vege19.popnow.MovieDetailsActivity;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OverViewFragment extends Fragment {

    private RecyclerView genresRecyclerview;
    private TextView movieOverview, movieReleaseDate, movieVoteAverage;
    private RatingBar movieRatingBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_over_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //recyclerview setup
        genresRecyclerview = getActivity().findViewById(R.id.genresRecyclerview);
        genresRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        //set overview
        movieOverview = getActivity().findViewById(R.id.movieOverview);
        movieOverview.setText(MovieDetailsActivity.overview);

        //set release date
        movieReleaseDate = getActivity().findViewById(R.id.movieReleaseDate);
        movieReleaseDate.setText(MovieDetailsActivity.release_date);

        //set movie rating
        movieVoteAverage = getActivity().findViewById(R.id.movieVoteAverage);
        movieVoteAverage.setText(String.valueOf(MovieDetailsActivity.vote_average));
        movieRatingBar = getActivity().findViewById(R.id.movieRatingBar);
        movieRatingBar.setProgress((int)MovieDetailsActivity.vote_average);

        initGenresRecyclerview();

    }

    private void initGenresRecyclerview() {
        //make retrofit call
        Call<GenresResponse> genresResponseCall = RetrofitClient.getInstance().getApi().getGenres(ApiService.api_key, ApiService.language);

        genresResponseCall.enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                GenresResponse genresResponse = response.body();

                //genre id's of this movie
                Integer[] movie_genres = MovieDetailsActivity.genre_ids;
                List<Genre> thisGenres = new ArrayList<>();

                //select the genres from all genres where matches with this movies genres
                for (int id : movie_genres) {
                    for (Genre genre : genresResponse.getGenres()) {
                        if (genre.getId() == id) {
                            //add the matched genres to the list of genres
                            thisGenres.add(new Genre(id, genre.getName()));
                        }
                    }
                }

                //set adapter
                genresRecyclerview.setAdapter(new GenresAdapter(thisGenres, getContext()));

            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
