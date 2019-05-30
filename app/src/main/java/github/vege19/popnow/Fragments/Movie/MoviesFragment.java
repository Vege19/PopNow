package github.vege19.popnow.Fragments.Movie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import github.vege19.popnow.Adapters.MoviesAdapter;
import github.vege19.popnow.Models.Movie.Movie;
import github.vege19.popnow.Models.Movie.MoviesResponse;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesFragment extends Fragment {

    private RecyclerView mPopularRecyclerView, mTopRatedRecyclerView, mUpcomingRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private static List<Movie> popularMovies = new ArrayList<>();
    private static List<Movie> topRatedMovies = new ArrayList<>();
    private static List<Movie> upcomingMovies = new ArrayList<>();
    private MoviesAdapter mPopularAdapter, mTopRatedAdapter, mUpcomingAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Recycler view config
        recyclerViewSetup();

        //Load all content
        loadPopularMovies();
        loadTopRatedMovies();
        loadUpcomingMovies();

        //To refresh
        refreshContent();

    }

    private void refreshContent() {
        mRefreshLayout = getActivity().findViewById(R.id.moviesRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Clean lists
                popularMovies.clear();
                topRatedMovies.clear();
                upcomingMovies.clear();

                //Reload movies
                loadPopularMovies();
                loadTopRatedMovies();
                loadUpcomingMovies();

                //Stop refreshing
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void recyclerViewSetup() {
        //RecyclerView setup
        mPopularRecyclerView = getActivity().findViewById(R.id.popularMoviesRecyclerview);
        mPopularRecyclerView.setLayoutManager(layoutManager());

        mTopRatedRecyclerView = getActivity().findViewById(R.id.topRatedMoviesRecyclerview);
        mTopRatedRecyclerView.setLayoutManager(layoutManager());

        mUpcomingRecyclerView = getActivity().findViewById(R.id.upcomingMoviesRecyclerview);
        mUpcomingRecyclerView.setLayoutManager(layoutManager());

    }

    private RecyclerView.LayoutManager layoutManager() {
        //To show the list in horizontal
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),
                1,
                GridLayoutManager.HORIZONTAL,
                false);

        return layoutManager;
    }

    private void loadPopularMovies() {
        mPopularAdapter = new MoviesAdapter(popularMovies, getContext());

        //Retrofit call
        Call<MoviesResponse> call = RetrofitClient.getInstance().getApi().getPopularMovies(ApiService.api_key,
                ApiService.language);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                //Fill recycler view with results
                popularMovies = response.body().getResults();
                mPopularAdapter = new MoviesAdapter(popularMovies, getContext());
                mPopularRecyclerView.setAdapter(mPopularAdapter);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                popularMovies.clear();
                mPopularRecyclerView.setAdapter(mPopularAdapter);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTopRatedMovies() {
        mTopRatedAdapter = new MoviesAdapter(topRatedMovies, getContext());

        //Retrofit call
        Call<MoviesResponse> call = RetrofitClient.getInstance().getApi().getTopRatedMovies(ApiService.api_key,
                ApiService.language);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                //Fill recycler view with results
                topRatedMovies = response.body().getResults();
                mTopRatedAdapter = new MoviesAdapter(topRatedMovies, getContext());
                mTopRatedRecyclerView.setAdapter(mTopRatedAdapter);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                topRatedMovies.clear();
                mTopRatedRecyclerView.setAdapter(mTopRatedAdapter);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUpcomingMovies() {
        mUpcomingAdapter = new MoviesAdapter(upcomingMovies, getContext());

        //Retrofit call
        Call<MoviesResponse> call = RetrofitClient.getInstance().getApi().getUpcomingMovies(ApiService.api_key,
                ApiService.language);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                //Fill recycler view
                upcomingMovies = response.body().getResults();
                mUpcomingAdapter = new MoviesAdapter(upcomingMovies, getContext());
                mUpcomingRecyclerView.setAdapter(mUpcomingAdapter);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                upcomingMovies.clear();
                mUpcomingRecyclerView.setAdapter(mUpcomingAdapter);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
