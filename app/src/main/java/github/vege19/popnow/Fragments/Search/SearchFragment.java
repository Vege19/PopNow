package github.vege19.popnow.Fragments.Search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import github.vege19.popnow.Adapters.CastAdapter;
import github.vege19.popnow.Adapters.MoviesAdapter;
import github.vege19.popnow.Adapters.TvShowsAdapter;
import github.vege19.popnow.Models.Search.SearchMovieResponse;
import github.vege19.popnow.Models.Search.SearchPeopleResponse;
import github.vege19.popnow.Models.Search.SearchTvShowResponse;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private SearchView mSearchView;
    private RecyclerView moviesRecyclerView, tvShowsRecyclerView, peopleRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //recycler view setup
        moviesRecyclerView = getActivity().findViewById(R.id.searchedMoviesRecyclerView);
        moviesRecyclerView.setLayoutManager(layoutManager());
        tvShowsRecyclerView = getActivity().findViewById(R.id.searchedTvShowsRecyclerView);
        tvShowsRecyclerView.setLayoutManager(layoutManager());
        peopleRecyclerView = getActivity().findViewById(R.id.searchedPeopleRecyclerView);
        peopleRecyclerView.setLayoutManager(layoutManager());

        //get results with user input
        mSearchView = getActivity().findViewById(R.id.mainSearch);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    getMovieResults(query);
                    getTvShowResults(query);
                    getPeopleResults(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    getMovieResults(newText);
                    getTvShowResults(newText);
                    getPeopleResults(newText);
                }
                return true;
            }
        });

    }

    private void getMovieResults(final String query) {
        //retrofit call
        Call<SearchMovieResponse> searchResponseCall = RetrofitClient.getInstance().getApi().findMovies(ApiService.api_key,
                ApiService.language,
                query);

        searchResponseCall.enqueue(new Callback<SearchMovieResponse>() {
            @Override
            public void onResponse(Call<SearchMovieResponse> call, Response<SearchMovieResponse> response) {
                SearchMovieResponse searchMovieResponse = response.body();
                //save movies
                if (searchMovieResponse.getResults().isEmpty()) {
                    moviesRecyclerView.setAdapter(new MoviesAdapter(searchMovieResponse.getResults(), getContext()));
                }

            }

            @Override
            public void onFailure(Call<SearchMovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getTvShowResults(String query) {
        //retrofit call
        Call<SearchTvShowResponse> searchTvShowResponseCall = RetrofitClient.getInstance().getApi().findTvShows(ApiService.api_key,
                ApiService.language,
                query);

        searchTvShowResponseCall.enqueue(new Callback<SearchTvShowResponse>() {
            @Override
            public void onResponse(Call<SearchTvShowResponse> call, Response<SearchTvShowResponse> response) {
                //save tv shows
                tvShowsRecyclerView.setAdapter(new TvShowsAdapter(response.body().getResults(), getContext()));
            }

            @Override
            public void onFailure(Call<SearchTvShowResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getPeopleResults(String query) {
        //retrofit call
        Call<SearchPeopleResponse> searchPeopleResponseCall = RetrofitClient.getInstance().getApi().findPeople(ApiService.api_key,
                ApiService.language,
                query);

        searchPeopleResponseCall.enqueue(new Callback<SearchPeopleResponse>() {
            @Override
            public void onResponse(Call<SearchPeopleResponse> call, Response<SearchPeopleResponse> response) {
                //save people
                peopleRecyclerView.setAdapter(new CastAdapter(response.body().getResults(), getContext()));
            }

            @Override
            public void onFailure(Call<SearchPeopleResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private RecyclerView.LayoutManager layoutManager() {
        //To show the list in horizontal
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),
                1,
                GridLayoutManager.HORIZONTAL,
                false);

        return layoutManager;
    }


}
