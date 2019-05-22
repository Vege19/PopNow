package github.vege19.popnow.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import github.vege19.popnow.Adapters.MoviesAdapter;
import github.vege19.popnow.Models.Movie.PopularMovie;
import github.vege19.popnow.Models.Movie.PopularMoviesResponse;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesFragment extends Fragment {

    private MoviesAdapter popularMoviesAdapter, topRatedMoviesAdapter, upcomingMoviesAdapter;
    private RecyclerView popularRecyclerview, topRatedMoviesRecyclerview, upcomingRecyclerview;
    private LinearLayout noInternetMessage, moviesLayout;
    private List<PopularMovie> popularMoviesList = new ArrayList<>();
    private List<PopularMovie> topRatedMoviesList = new ArrayList<>();
    private List<PopularMovie> upcomingMoviesList = new ArrayList<>();

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

        //all views starts invisible
        noInternetMessage.setVisibility(View.INVISIBLE);
        moviesLayout.setVisibility(View.INVISIBLE);

        //init content
        retrofitSetup();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private RecyclerView.LayoutManager layoutManager() {
        //To show the list in horizontal
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),
                1,
                GridLayoutManager.HORIZONTAL,
                false);

        return layoutManager;
    }

    private void recyclerviewSetup() {

        //Adapter
        popularMoviesAdapter = new MoviesAdapter(popularMoviesList, getContext());
        topRatedMoviesAdapter = new MoviesAdapter(topRatedMoviesList, getContext());
        upcomingMoviesAdapter = new MoviesAdapter(upcomingMoviesList, getContext());


        //Recyclerview setup
        popularRecyclerview = getActivity().findViewById(R.id.popularMoviesRecyclerview);
        popularRecyclerview.setLayoutManager(layoutManager());
        popularRecyclerview.setAdapter(popularMoviesAdapter);

        topRatedMoviesRecyclerview = getActivity().findViewById(R.id.topRatedMoviesRecyclerview);
        topRatedMoviesRecyclerview.setLayoutManager(layoutManager());
        topRatedMoviesRecyclerview.setAdapter(topRatedMoviesAdapter);

        upcomingRecyclerview = getActivity().findViewById(R.id.upcomingMoviesRecyclerview);
        upcomingRecyclerview.setLayoutManager(layoutManager());
        upcomingRecyclerview.setAdapter(upcomingMoviesAdapter);


    }

    private void retrofitSetup() {

        //Required params
        String language = getActivity().getResources().getString(R.string.language);
        int page = 1;

        //Calling popular movies
        Call<PopularMoviesResponse> popularMovies = RetrofitClient.getInstance().getApi().getPopularMovies(ApiService.api_key,
                language,
                page);

        popularMovies.enqueue(new Callback<PopularMoviesResponse>() {
            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                showMoviesLayout();
                PopularMoviesResponse popularMoviesResponse = response.body();
                popularMoviesList = popularMoviesResponse.getResults();
                recyclerviewSetup();

            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {
                hideMoviesLayout();

            }
        });

        //Calling toprated movies
        Call<PopularMoviesResponse> topRatedMovies = RetrofitClient.getInstance().getApi().getTopRatedMovies(ApiService.api_key,
                language,
                page);

        topRatedMovies.enqueue(new Callback<PopularMoviesResponse>() {
            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                showMoviesLayout();
                PopularMoviesResponse popularMoviesResponse = response.body();
                topRatedMoviesList = popularMoviesResponse.getResults();
                recyclerviewSetup();

            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {
                hideMoviesLayout();

            }
        });

        //Calling upcoming movies
        Call<PopularMoviesResponse> upcomingMovies = RetrofitClient.getInstance().getApi().getUpcomingMovies(ApiService.api_key,
                language,
                page);

        upcomingMovies.enqueue(new Callback<PopularMoviesResponse>() {
            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                showMoviesLayout();
                PopularMoviesResponse popularMoviesResponse = response.body();
                upcomingMoviesList = popularMoviesResponse.getResults();
                recyclerviewSetup();

            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {
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
