package github.vege19.popnow.Fragments.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import github.vege19.popnow.Adapters.TrendingAdapter;
import github.vege19.popnow.Models.Trending.Trending;
import github.vege19.popnow.Models.Trending.TrendingResponse;
import github.vege19.popnow.MovieDetailsActivity;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import github.vege19.popnow.TvShowDetailsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView mTrendingRecyclerView;
    private List<Trending> trendingList = new ArrayList<>();
    private TrendingAdapter mTrendingAdapter;
    private ImageView firstTrendingItem;
    private TextView firstItemTitle;
    private CardView mTopItem;
    private SwipeRefreshLayout mRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //RecyclerView config
        recyclerViewSetUp();

        //Load first trending item
        loadTopItem();

        //Load list of remaining 4 items
        loadTrending();

        //To refresh content
        refreshContent();

    }

    private void refreshContent() {
        mRefreshLayout = getActivity().findViewById(R.id.trendingRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Clear trending list
                trendingList.clear();

                //Reload content
                loadTopItem();
                loadTrending();

                //Stop refreshing
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void recyclerViewSetUp() {
        mTrendingRecyclerView = getActivity().findViewById(R.id.mTrendingRecyclerView);
        mTrendingRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void loadTrending() {
        //Retrofit call
        Call<TrendingResponse> trendingResponseCall = RetrofitClient.getInstance().getApi().getTrendingContent(ApiService.api_key);

        trendingResponseCall.enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(Call<TrendingResponse> call, Response<TrendingResponse> response) {
                //Fill recycler view with 4 items
                for (int i = 1; i <= 4; i++) {
                    //Trending object
                    Trending tmp = response.body().getResults().get(i);
                    //Add tmp object to list
                    trendingList.add(tmp);
                }

                mTrendingAdapter = new TrendingAdapter(trendingList, getContext());
                mTrendingRecyclerView.setAdapter(mTrendingAdapter);

            }

            @Override
            public void onFailure(Call<TrendingResponse> call, Throwable t) {
                trendingList.clear();
                mTrendingAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTopItem() {
        mTopItem = getActivity().findViewById(R.id.topItemCard);

        firstItemTitle = getActivity().findViewById(R.id.trendingItem1Title);
        firstTrendingItem = getActivity().findViewById(R.id.trendingItem1);

        //Get the top item
        Call<TrendingResponse> trendingResponseCall = RetrofitClient.getInstance().getApi().getTrendingContent(ApiService.api_key);

        trendingResponseCall.enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(Call<TrendingResponse> call, Response<TrendingResponse> response) {
                //Show card
                mTopItem.setVisibility(View.VISIBLE);
                //Get item with position 0
                final Trending tmp = response.body().getResults().get(0);
                //Set title
                firstItemTitle.setText(tmp.getName());

                //Load backdrop
                Glide.with(getContext())
                        .load(ApiService.imageURL + tmp.getBackdrop_path())
                        .into(firstTrendingItem);

                //Show details according content type
                firstTrendingItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tmp.getRelease_date() != null) {
                            //If content type is Movie
                            initMovieDetails(tmp);
                        } else if (tmp.getFirst_air_date() != null) {
                            //If content type is Tv
                            initTvShowDetails(tmp);
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<TrendingResponse> call, Throwable t) {
                //Remove card
                mTopItem.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initMovieDetails(Trending trending) {
        Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
        intent.putExtra("trending_movie_details", trending);
        startActivity(intent);
    }

    private void initTvShowDetails(Trending trending) {
        Intent intent = new Intent(getContext(), TvShowDetailsActivity.class);
        intent.putExtra("trending_tv_details", trending);
        startActivity(intent);
    }

}
