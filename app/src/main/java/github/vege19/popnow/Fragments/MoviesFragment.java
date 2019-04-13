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
    private MoviesAdapter moviesAdapter;
    private RecyclerView popularRecyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout noInternetMessage, moviesLayout;

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

    }

    @Override
    public void onResume() {
        super.onResume();

        //all views starts invisible
        noInternetMessage.setVisibility(View.INVISIBLE);
        moviesLayout.setVisibility(View.INVISIBLE);

        retrofitSetup();
    }

    private void recyclerviewSetup(List<Movie> movies) {

        //Adapter
        moviesAdapter = new MoviesAdapter(movies, getContext());

        //To show the list in horizontal
        layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,
                false);

        //Recyclerview setup
        popularRecyclerview = getActivity().findViewById(R.id.popularMoviesRecyclerview);
        popularRecyclerview.setLayoutManager(layoutManager);
        popularRecyclerview.setAdapter(moviesAdapter);

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
                //Show the layout
                noInternetMessage.setVisibility(View.INVISIBLE);
                moviesLayout.setVisibility(View.VISIBLE);

                MoviesResponse moviesResponse = response.body();
                List<Movie> results = moviesResponse.getResults();
                recyclerviewSetup(results);

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                moviesLayout.setVisibility(View.INVISIBLE);
                noInternetMessage.setVisibility(View.VISIBLE);

            }
        });
    }

}
