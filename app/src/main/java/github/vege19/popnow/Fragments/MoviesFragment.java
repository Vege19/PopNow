package github.vege19.popnow.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import github.vege19.popnow.Adapters.MoviesAdapter;
import github.vege19.popnow.Models.Movie;
import github.vege19.popnow.Models.MoviesResponse;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesFragment extends Fragment {

    private Retrofit retrofit;
    private ApiService api;
    private MoviesAdapter popularMoviesAdapter, topRatedMoviesAdapter;
    private RecyclerView popularRecyclerview, topRatedMoviesRecyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout noInternetMessage, moviesLayout;
    private List<Movie> popularMoviesList = new ArrayList<>();
    private List<Movie> topRatedMoviesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noInternetMessage = getActivity().findViewById(R.id.moviesNoInternetMessage);
        moviesLayout = getActivity().findViewById(R.id.moviesLayout);

        retrofitSetup();

    }

    @Override
    public void onResume() {
        super.onResume();

        //all views starts invisible
        noInternetMessage.setVisibility(View.INVISIBLE);
        moviesLayout.setVisibility(View.INVISIBLE);

    }

    private RecyclerView.LayoutManager layoutManager() {
        //To show the list in horizontal
        layoutManager = new GridLayoutManager(getContext(),
                1,
                GridLayoutManager.HORIZONTAL,
                false);

        return layoutManager;
    }

    private void recyclerviewSetup() {

        //Adapter
        popularMoviesAdapter = new MoviesAdapter(popularMoviesList, getContext());
        topRatedMoviesAdapter = new MoviesAdapter(topRatedMoviesList, getContext());

        //Recyclerview setup
        popularRecyclerview = getActivity().findViewById(R.id.popularMoviesRecyclerview);
        popularRecyclerview.setLayoutManager(layoutManager());
        popularRecyclerview.setAdapter(popularMoviesAdapter);

        topRatedMoviesRecyclerview = getActivity().findViewById(R.id.topRatedMoviesRecyclerview);
        topRatedMoviesRecyclerview.setLayoutManager(layoutManager());
        topRatedMoviesRecyclerview.setAdapter(topRatedMoviesAdapter);


    }

    private void retrofitSetup() {

        //Required params
        String language = getActivity().getResources().getString(R.string.language);
        int page = 1;

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ApiService.class);

        //Calling popular movies
        Call<MoviesResponse> popularMovies = api.getPopularMovies(ApiService.api_key,
                language,
                page);

        popularMovies.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                showMoviesLayout();
                MoviesResponse moviesResponse = response.body();
                popularMoviesList = moviesResponse.getResults();
                recyclerviewSetup();

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                hideMoviesLayout();

            }
        });

        //Calling toprated movies
        Call<MoviesResponse> topRatedMovies = api.getTopRatedMovies(ApiService.api_key,
                language,
                page);

        topRatedMovies.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                showMoviesLayout();
                MoviesResponse moviesResponse = response.body();
                topRatedMoviesList = moviesResponse.getResults();
                recyclerviewSetup();

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                hideMoviesLayout();

            }
        });
    }

    private void hideMoviesLayout() {
        //Show error view and hide movies layout
        moviesLayout.setVisibility(View.INVISIBLE);
        noInternetMessage.setVisibility(View.VISIBLE);
    }

    private void showMoviesLayout() {
        //Show the layout, hide the error view
        noInternetMessage.setVisibility(View.INVISIBLE);
        moviesLayout.setVisibility(View.VISIBLE);

    }

}
