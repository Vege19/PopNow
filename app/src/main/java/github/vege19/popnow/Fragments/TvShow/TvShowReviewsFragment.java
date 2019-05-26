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

import github.vege19.popnow.Adapters.ReviewsAdapter;
import github.vege19.popnow.Models.Review.ReviewsResponse;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import github.vege19.popnow.TvShowDetailsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowReviewsFragment extends Fragment {

    private RecyclerView reviewsRecyclerview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_show_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //recycler view setup
        reviewsRecyclerview = getActivity().findViewById(R.id.tvShowReviewsRecyclerview);
        reviewsRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 1));

        loadReviews();
    }

    private void loadReviews() {
        //retrofit call
        Call<ReviewsResponse> reviewsResponseCall = RetrofitClient.getInstance().getApi().getTvShowReviews(TvShowDetailsActivity.thisTvShow.getId(),
                ApiService.api_key,
                ApiService.language);

        reviewsResponseCall.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                //get response
                ReviewsResponse reviewsResponse = response.body();
                //load reviews in recyclerview
                reviewsRecyclerview.setAdapter(new ReviewsAdapter(reviewsResponse.getResults(), getContext()));
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

}
