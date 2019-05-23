package github.vege19.popnow.Fragments.TvShow;

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
import github.vege19.popnow.TvShowDetailsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowOverviewFragment extends Fragment {

    private TextView overviewText, releaseDateText, voteAverageText;
    private RecyclerView genresRecyclerview;
    private RatingBar voteRatingBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_show_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //details
        overviewText = getActivity().findViewById(R.id.tvShowOverview);
        overviewText.setText(TvShowDetailsActivity.thisTvShow.getOverview());

        releaseDateText = getActivity().findViewById(R.id.tvShowReleaseDate);
        releaseDateText.setText(TvShowDetailsActivity.thisTvShow.getFirst_air_date());

        voteAverageText = getActivity().findViewById(R.id.tvShowVoteAverage);
        voteAverageText.setText(String.valueOf(TvShowDetailsActivity.thisTvShow.getVote_average()));

        //rating bar setup
        voteRatingBar = getActivity().findViewById(R.id.tvShowRatingBar);
        voteRatingBar.setProgress((int)TvShowDetailsActivity.thisTvShow.getVote_average());

        //recycler view setup
        genresRecyclerview = getActivity().findViewById(R.id.tvShowGenresRecyclerview);
        genresRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        //load this tv show genres
        loadGenres();

    }

    private void loadGenres() {
        //retrofit call
        Call<GenresResponse> genresResponseCall = RetrofitClient.getInstance().getApi().getGenres(ApiService.api_key,
                ApiService.language);

        genresResponseCall.enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                GenresResponse genresResponse = response.body();

                //genre id's of this tv show
                Integer[] tvShow_genres = TvShowDetailsActivity.thisTvShow.getGenre_ids();
                List<Genre> thisGenres = new ArrayList<>();

                //select the genres from all genres where matches with this tv show genres
                for (int id : tvShow_genres) {
                    for (Genre genre : genresResponse.getGenres()) {
                        if (genre.getId() == id) {
                            //add the matched genres to the list of genres
                            thisGenres.add(new Genre(id, genre.getName()));
                        }
                    }
                }

                genresRecyclerview.setAdapter(new GenresAdapter(thisGenres, getContext()));

            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

}
