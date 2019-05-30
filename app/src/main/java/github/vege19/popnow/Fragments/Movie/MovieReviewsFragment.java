package github.vege19.popnow.Fragments.Movie;

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

import github.vege19.popnow.Adapters.ReviewsAdapter;
import github.vege19.popnow.Models.Review.Review;
import github.vege19.popnow.Models.Review.ReviewsResponse;
import github.vege19.popnow.MovieDetailsActivity;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieReviewsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<Review> reviews = new ArrayList<>();
    private ReviewsAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //RecyclerView config
        mRecyclerView = getActivity().findViewById(R.id.movieReviewsRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        //Load the reviews
        loadReviews();

        //Refresh reviews
        refreshContent();

    }

    private void loadReviews() {
        mAdapter = new ReviewsAdapter(reviews, getContext());

        //make retrofit call
        Call<ReviewsResponse> reviewsResponseCall = RetrofitClient.getInstance().getApi().getMovieReviews(MovieDetailsActivity.movie_id,
                ApiService.api_key, ApiService.language);

        reviewsResponseCall.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                //Fill recycler view
                reviews = response.body().getResults();
                mAdapter = new ReviewsAdapter(reviews, getContext());
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                reviews.clear();
                mRecyclerView.setAdapter(mAdapter);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void refreshContent() {
        mRefreshLayout = getActivity().findViewById(R.id.movieReviewsRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Clear reviews
                reviews.clear();
                //Reload reviews
                loadReviews();
                //Stop refreshing
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

}
