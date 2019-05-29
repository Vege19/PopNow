package github.vege19.popnow.Fragments.Search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.vege19.popnow.Adapters.CastAdapter;
import github.vege19.popnow.Adapters.MoviesAdapter;
import github.vege19.popnow.Adapters.TvShowsAdapter;
import github.vege19.popnow.Models.Credits.Cast;
import github.vege19.popnow.Models.Movie.Movie;
import github.vege19.popnow.Models.Search.SearchMovieResponse;
import github.vege19.popnow.Models.Search.SearchPeopleResponse;
import github.vege19.popnow.Models.Search.SearchTvShowResponse;
import github.vege19.popnow.Models.TvShow.TvShow;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private SearchView mSearchView;
    private Spinner mSpinner;
    private RecyclerView mSearchRecyclerView;
    private List<Movie> movies = new ArrayList<>();
    private List<TvShow> tvShows = new ArrayList<>();
    private List<Cast> persons = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Init spinner
        spinnerSetUp();
        Toast.makeText(getContext(), mSpinner.getSelectedItem().toString().trim(), Toast.LENGTH_SHORT).show();

        //recycler view setup
        mSearchRecyclerView = getActivity().findViewById(R.id.searchRecyclerView);
        mSearchRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mSearchRecyclerView.setHasFixedSize(true);

        //get results with user input
        mSearchView = getActivity().findViewById(R.id.mainSearch);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    searchContent(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    searchContent(newText);
                }
                return false;
            }
        });

    }

    private void searchContent(final String query) {

        if (mSpinner.getSelectedItem().toString().equals("Movies")) {
            loadMovies(query);
        } else if (mSpinner.getSelectedItem().toString().equals("Tv Shows")) {
            loadTvShows(query);
        } else if (mSpinner.getSelectedItem().toString().equals("People")) {
            loadPeople(query);
        }

        //To not re-submit after select spinner option
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        loadMovies(query);
                        break;
                    case 1:
                        loadTvShows(query);
                        break;
                    case 2:
                        loadPeople(query);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadMovies(String query) {
        Call<SearchMovieResponse> call = RetrofitClient.getInstance().getApi().findMovies(ApiService.api_key,
                ApiService.language,
                query);

        call.enqueue(new Callback<SearchMovieResponse>() {
            @Override
            public void onResponse(Call<SearchMovieResponse> call, Response<SearchMovieResponse> response) {
                //Save results in the list
                movies = response.body().getResults();
                Log.d("debug", "Success");
                //Set movies adapter to recycler view
                mSearchRecyclerView.setAdapter(new MoviesAdapter(movies, getContext()));
            }

            @Override
            public void onFailure(Call<SearchMovieResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadTvShows(String query) {
        Call<SearchTvShowResponse> call = RetrofitClient.getInstance().getApi().findTvShows(ApiService.api_key,
                ApiService.language,
                query);

        call.enqueue(new Callback<SearchTvShowResponse>() {
            @Override
            public void onResponse(Call<SearchTvShowResponse> call, Response<SearchTvShowResponse> response) {
                //Save results
                tvShows = response.body().getResults();
                //Set tv shows adapter
                mSearchRecyclerView.setAdapter(new TvShowsAdapter(tvShows, getContext()));
            }

            @Override
            public void onFailure(Call<SearchTvShowResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadPeople(String query) {
        Call<SearchPeopleResponse> call = RetrofitClient.getInstance().getApi().findPeople(ApiService.api_key,
                ApiService.language,
                query);

        call.enqueue(new Callback<SearchPeopleResponse>() {
            @Override
            public void onResponse(Call<SearchPeopleResponse> call, Response<SearchPeopleResponse> response) {
                persons = response.body().getResults();
                //Set adapter
                mSearchRecyclerView.setAdapter(new CastAdapter(persons, getContext()));
            }

            @Override
            public void onFailure(Call<SearchPeopleResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void spinnerSetUp() {
        //Get string array of options
        String[] options = getResources().getStringArray(R.array.media_type_array);

        //Create the adapter
        ArrayAdapter<String> optionsAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                options);

        //Get spinner reference
        mSpinner = getActivity().findViewById(R.id.searchTypeSpinner);
        //Set adapter
        mSpinner.setAdapter(optionsAdapter);

    }

}
