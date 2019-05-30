package github.vege19.popnow.Fragments.TvShow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import github.vege19.popnow.Adapters.TvShowsAdapter;
import github.vege19.popnow.Models.TvShow.TvShow;
import github.vege19.popnow.Models.TvShow.TvShowsResponse;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowsFragment extends Fragment {

    private RecyclerView mPopularRecyclerView, mTopRatedRecyclerView, mOnAiringRecyclerView;
    private List<TvShow> onAiringTvShows = new ArrayList<>();
    private List<TvShow> popularTvShows = new ArrayList<>();
    private List<TvShow> topRatedTvShows = new ArrayList<>();
    private TvShowsAdapter mPopularAdapter, mOnAiringAdapter, mTopRatedAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //RecyclerView config
        recyclerViewSetUp();

        //Load lists
        loadOnAiringTvShows();
        loadPopularTvShows();
        loadTopRatedTvShows();

        //To refresh content
        refreshContent();

    }

    private void refreshContent() {
        mRefreshLayout = getActivity().findViewById(R.id.tvShowsRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Clear lists
                onAiringTvShows.clear();
                popularTvShows.clear();
                topRatedTvShows.clear();

                //Reload content
                loadOnAiringTvShows();
                loadPopularTvShows();
                loadTopRatedTvShows();

                //Stop refreshing
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void recyclerViewSetUp() {
        //init recycler views
        mPopularRecyclerView = getActivity().findViewById(R.id.popularTvShowsRecyclerview);
        mPopularRecyclerView.setLayoutManager(layoutManager());
        mTopRatedRecyclerView = getActivity().findViewById(R.id.topRatedTvShowsRecyclerview);
        mTopRatedRecyclerView.setLayoutManager(layoutManager());
        mOnAiringRecyclerView = getActivity().findViewById(R.id.onAiringTvShowsRecyclerview);
        mOnAiringRecyclerView.setLayoutManager(layoutManager());

    }

    private void loadPopularTvShows() {
        mPopularAdapter = new TvShowsAdapter(popularTvShows, getContext());

        //Make retrofit call
        Call<TvShowsResponse> tvShowsResponseCall = RetrofitClient.getInstance().getApi().getPopularTvShows(ApiService.api_key,
                ApiService.language);

        tvShowsResponseCall.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                //Fill recycler view
                popularTvShows = response.body().getResults();
                mPopularAdapter = new TvShowsAdapter(popularTvShows, getContext());
                mPopularRecyclerView.setAdapter(mPopularAdapter);
            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                popularTvShows.clear();
                mPopularRecyclerView.setAdapter(mPopularAdapter);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadOnAiringTvShows() {
        mOnAiringAdapter = new TvShowsAdapter(onAiringTvShows, getContext());

        //retrofit call
        Call<TvShowsResponse> tvShowsResponseCall = RetrofitClient.getInstance().getApi().getOnAiringTvShows(ApiService.api_key,
                ApiService.language);

        tvShowsResponseCall.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                //Fill recycler view
                onAiringTvShows = response.body().getResults();
                mOnAiringAdapter = new TvShowsAdapter(onAiringTvShows, getContext());
                mOnAiringRecyclerView.setAdapter(mOnAiringAdapter);
            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                onAiringTvShows.clear();
                mOnAiringRecyclerView.setAdapter(mOnAiringAdapter);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadTopRatedTvShows() {
        mTopRatedAdapter = new TvShowsAdapter(topRatedTvShows, getContext());

        //retrofit call
        Call<TvShowsResponse> tvShowsResponseCall = RetrofitClient.getInstance().getApi().getTopRatedTvShows(ApiService.api_key,
                ApiService.language);

        tvShowsResponseCall.enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                //Fill recycler view
                topRatedTvShows = response.body().getResults();
                mTopRatedAdapter = new TvShowsAdapter(topRatedTvShows, getContext());
                mTopRatedRecyclerView.setAdapter(mTopRatedAdapter);
            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable t) {
                topRatedTvShows.clear();
                mTopRatedRecyclerView.setAdapter(mTopRatedAdapter);
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
