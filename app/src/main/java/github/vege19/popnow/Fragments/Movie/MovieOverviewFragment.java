package github.vege19.popnow.Fragments.Movie;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class MovieOverviewFragment extends Fragment {

    private RecyclerView mGenresRecyclerView;
    private List<Genre> genres = new ArrayList<>();
    private GenresAdapter mAdapter;
    private TextView mOverviewText, mReleaseDateText, mVoteAverageText;
    private RatingBar mRatingBar;
    private SwipeRefreshLayout mRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_over_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initContent();

        refreshContent();

    }

    private void refreshContent() {
        mRefreshLayout = getActivity().findViewById(R.id.movieOverviewRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Clear genres list
                genres.clear();

                //Reload content
                initContent();

                //Stop refreshing
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initContent() {
        //recyclerview setup
        mGenresRecyclerView = getActivity().findViewById(R.id.mGenresRecyclerView);
        mGenresRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));

        //set overview
        mOverviewText = getActivity().findViewById(R.id.mOverviewText);
        mOverviewText.setText(MovieDetailsActivity.overview);

        //set release date
        mReleaseDateText = getActivity().findViewById(R.id.mReleaseDateText);
        mReleaseDateText.setText(MovieDetailsActivity.release_date);

        //set movie rating
        mVoteAverageText = getActivity().findViewById(R.id.mVoteAverageText);
        mVoteAverageText.setText(String.valueOf(MovieDetailsActivity.vote_average));
        mRatingBar = getActivity().findViewById(R.id.movieRatingBar);
        mRatingBar.setProgress((int)MovieDetailsActivity.vote_average);

        loadGenres();

    }

    private void loadGenres() {
        mAdapter = new GenresAdapter(genres, getContext());

        //make retrofit call
        Call<GenresResponse> genresResponseCall = RetrofitClient.getInstance().getApi().getGenres(ApiService.api_key, ApiService.language);

        genresResponseCall.enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                //genre id's of this movie
                Integer[] movie_genres = MovieDetailsActivity.genre_ids;

                //select the genres from all genres where matches with this movies genres
                for (int id : movie_genres) {
                    for (Genre genre : response.body().getGenres()) {
                        if (genre.getId() == id) {
                            //add the matched genres to the list of genres
                            genres.add(new Genre(id, genre.getName()));
                        }
                    }
                }

                //set adapter
                mAdapter = new GenresAdapter(genres, getContext());
                mGenresRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {
                genres.clear();
                mGenresRecyclerView.setAdapter(mAdapter);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
