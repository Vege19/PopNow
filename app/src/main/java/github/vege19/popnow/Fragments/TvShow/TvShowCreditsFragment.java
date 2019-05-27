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

import github.vege19.popnow.Adapters.CastAdapter;
import github.vege19.popnow.Adapters.CrewAdapter;
import github.vege19.popnow.Models.Credits.CreditsResponse;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import github.vege19.popnow.TvShowDetailsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowCreditsFragment extends Fragment {

    private RecyclerView castRecyclerview, crewRecyclerview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_show_credits, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        castRecyclerview = getActivity().findViewById(R.id.tvCastRecyclerView);
        castRecyclerview.setLayoutManager(layoutManager());

        crewRecyclerview = getActivity().findViewById(R.id.tvCrewRecyclerView);
        crewRecyclerview.setLayoutManager(layoutManager());

        loadCast();
        loadCrew();

    }

    private void loadCast() {
        //retrofit call
        Call<CreditsResponse> castResponseCall = RetrofitClient.getInstance().getApi().getTvShowCredits(TvShowDetailsActivity.tv_id,
                ApiService.api_key);

        castResponseCall.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                //fill the recyclerview
                CreditsResponse creditsResponse = response.body();
                castRecyclerview.setAdapter(new CastAdapter(creditsResponse.getCast(), getContext()));


            }

            @Override
            public void onFailure(Call<CreditsResponse> call, Throwable t) {
                Toast.makeText(getContext(),"Error: " + t.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadCrew() {
        //retrofit call
        Call<CreditsResponse> creditsResponseCall = RetrofitClient.getInstance().getApi().getTvShowCredits(TvShowDetailsActivity.tv_id,
                ApiService.api_key);

        creditsResponseCall.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                CreditsResponse creditsResponse = response.body();
                crewRecyclerview.setAdapter(new CrewAdapter(creditsResponse.getCrew(), getContext()));
            }

            @Override
            public void onFailure(Call<CreditsResponse> call, Throwable t) {
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
}
