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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private RecyclerView trendingRecyclerview;
    private ImageView firstTrendingItem;
    private TextView firstItemTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firstItemTitle = getActivity().findViewById(R.id.trendingItem1Title);

        loadTopItem();

        //recycler view set up
        trendingRecyclerview = getActivity().findViewById(R.id.trendingRecyclerView);
        trendingRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));

        //load list of remaining 4 items
        loadTrending();

    }

    private void loadTrending() {
        //retrofit call
        Call<TrendingResponse> trendingResponseCall = RetrofitClient.getInstance().getApi().getTrendingContent(ApiService.api_key);

        trendingResponseCall.enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(Call<TrendingResponse> call, Response<TrendingResponse> response) {
                //list
                List<Trending> tmpList = new ArrayList<>();
                //get response
                TrendingResponse trendingResponse = response.body();
                //fill recycler view
                for (int i = 1; i <= 4; i++) {
                    //trending object
                    Trending tmp = trendingResponse.getResults().get(i);
                    //add tmp object to list
                    tmpList.add(tmp);
                    //fill rv
                    trendingRecyclerview.setAdapter(new TrendingAdapter(tmpList, getContext()));
                }

            }

            @Override
            public void onFailure(Call<TrendingResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTopItem() {
        firstTrendingItem = getActivity().findViewById(R.id.trendingItem1);

        //get the top item
        Call<TrendingResponse> trendingResponseCall = RetrofitClient.getInstance().getApi().getTrendingContent(ApiService.api_key);

        trendingResponseCall.enqueue(new Callback<TrendingResponse>() {
            @Override
            public void onResponse(Call<TrendingResponse> call, Response<TrendingResponse> response) {
                //get the object with position 0
                final Trending tmp = response.body().getResults().get(0);
                //get the top 1 trending item
                firstItemTitle.setText(tmp.getName());

                Glide.with(getContext())
                        .load(ApiService.imageURL + tmp.getBackdrop_path())
                        .into(firstTrendingItem);

                //show details according content type
                firstTrendingItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tmp.getRelease_date() != null) {
                            //if content type is Movie
                            initMovieDetails(tmp);
                        } else if (tmp.getFirst_air_date() != null) {
                            //if content type is Tv
                            initTvShowDetails(tmp);
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<TrendingResponse> call, Throwable t) {
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
