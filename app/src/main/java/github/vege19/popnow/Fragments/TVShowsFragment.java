package github.vege19.popnow.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import github.vege19.popnow.Adapters.TvShowsAdapter;
import github.vege19.popnow.Models.TvShow.TvShowsResponse;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowsFragment extends Fragment {

    private RecyclerView popularRecyclerview, topRatedRecyclerview, onAiringRecyclerview;
    private LinearLayout noInternetView, tvShowsLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshows, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //views
        noInternetView = getActivity().findViewById(R.id.tvShowsNoInternetMessage);
        tvShowsLayout = getActivity().findViewById(R.id.tvShowsLayout);

        //all views starts invisible
        noInternetView.setVisibility(View.INVISIBLE);
        tvShowsLayout.setVisibility(View.INVISIBLE);

        //init recyclerviews
        popularRecyclerview = getActivity().findViewById(R.id.popularTvShowsRecyclerview);
        popularRecyclerview.setLayoutManager(layoutManager());
        topRatedRecyclerview = getActivity().findViewById(R.id.topRatedTvShowsRecyclerview);
        topRatedRecyclerview.setLayoutManager(layoutManager());
        onAiringRecyclerview = getActivity().findViewById(R.id.onAiringTvShowsRecyclerview);
        onAiringRecyclerview.setLayoutManager(layoutManager());

        //Load lists
        loadOnAiringTvShows();
        loadPopularTvShows();
        loadTopRatedTvShows();

    }

    private void loadPopularTvShows() {
        //make retrofit call
        Call<TvShowsResponse> tvShowsResponseCall = RetrofitClient.getInstance().getApi().getPopularTvShows(ApiService.api_key,
                ApiService.language);

        tvShowsResponseCall.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                //show views
                showTvShowsLayout();
                //fill recycler view
                TvShowsResponse tvShowsResponse = response.body();
                popularRecyclerview.setAdapter(new TvShowsAdapter(tvShowsResponse.getResults(), getContext()));

            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                //hide views and show no internet message
                hideTvShowsLayout();
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadOnAiringTvShows() {
        //retrofit call
        Call<TvShowsResponse> tvShowsResponseCall = RetrofitClient.getInstance().getApi().getOnAiringTvShows(ApiService.api_key,
                ApiService.language);

        tvShowsResponseCall.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                //show views
                showTvShowsLayout();
                //fill recycler view
                TvShowsResponse tvShowsResponse = response.body();
                onAiringRecyclerview.setAdapter(new TvShowsAdapter(tvShowsResponse.getResults(), getContext()));

            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                //hide views and show no internet message
                hideTvShowsLayout();
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void loadTopRatedTvShows() {
        //retrofit call
        Call<TvShowsResponse> tvShowsResponseCall = RetrofitClient.getInstance().getApi().getTopRatedTvShows(ApiService.api_key,
                ApiService.language);

        tvShowsResponseCall.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                //show views
                showTvShowsLayout();
                //fill recycler view
                TvShowsResponse tvShowsResponse = response.body();
                topRatedRecyclerview.setAdapter(new TvShowsAdapter(tvShowsResponse.getResults(), getContext()));

            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                //hide views and show no internet message
                hideTvShowsLayout();
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**Global layout manager**/
    private RecyclerView.LayoutManager layoutManager() {
        //To show the list in horizontal
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),
                1,
                GridLayoutManager.HORIZONTAL,
                false);

        return layoutManager;
    }

    private void hideTvShowsLayout() {
        //Show error view and hide tvShows layout
        tvShowsLayout.setVisibility(View.INVISIBLE);
        noInternetView.setVisibility(View.VISIBLE);
    }

    private void showTvShowsLayout() {
        //Show the layout, hide the error view
        noInternetView.setVisibility(View.INVISIBLE);
        tvShowsLayout.setVisibility(View.VISIBLE);

    }

}
