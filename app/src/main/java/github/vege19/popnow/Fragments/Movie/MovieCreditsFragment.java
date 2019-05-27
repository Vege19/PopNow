package github.vege19.popnow.Fragments.Movie;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import github.vege19.popnow.Adapters.CastAdapter;
import github.vege19.popnow.Adapters.CrewAdapter;
import github.vege19.popnow.Models.Credits.CreditsResponse;
import github.vege19.popnow.MovieDetailsActivity;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieCreditsFragment extends Fragment {

    private RecyclerView castRecyclerView, crewRecyclerView;

    public MovieCreditsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_credits, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //recyclerview setup
        castRecyclerView = getActivity().findViewById(R.id.castRecyclerView);
        castRecyclerView.setLayoutManager(layoutManager());

        crewRecyclerView = getActivity().findViewById(R.id.crewRecyclerView);
        crewRecyclerView.setLayoutManager(layoutManager());

        loadCast();
        loadCrew();

    }

    private void loadCrew() {
        //make retrofit call
        Call<CreditsResponse> crewResponseCall = RetrofitClient.getInstance().getApi().getMovieCredits(MovieDetailsActivity.movie_id,
                ApiService.api_key);

        crewResponseCall.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                CreditsResponse creditsResponse = response.body();
                crewRecyclerView.setAdapter(new CrewAdapter(creditsResponse.getCrew(), getContext()));
            }

            @Override
            public void onFailure(Call<CreditsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadCast() {
        //make the retrofit call
        Call<CreditsResponse> castResponseCall = RetrofitClient.getInstance().getApi().getMovieCredits(MovieDetailsActivity.movie_id, ApiService.api_key);

        castResponseCall.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                CreditsResponse creditsResponse = response.body();
                Log.d("debug", String.valueOf(response.body().getId()));
                castRecyclerView.setAdapter(new CastAdapter(creditsResponse.getCast(), getActivity()));
            }

            @Override
            public void onFailure(Call<CreditsResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

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

}
