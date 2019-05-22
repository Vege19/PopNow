package github.vege19.popnow.Fragments;

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
import github.vege19.popnow.Models.Credits.CastResponse;
import github.vege19.popnow.MovieDetailsActivity;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CastFragment extends Fragment {

    private RecyclerView castRecyclerView;

    public CastFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cast, container, false);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initContent();

    }


    private void initContent() {
        //recyclerview setup
        castRecyclerView = getActivity().findViewById(R.id.castRecyclerView);
        castRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        //make the retrofit call
        Call<CastResponse> castResponseCall = RetrofitClient.getInstance().getApi().getCasts(MovieDetailsActivity.movie_id, ApiService.api_key);

        castResponseCall.enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                CastResponse castResponse = response.body();
                Log.d("debug", String.valueOf(response.body().getId()));
                castRecyclerView.setAdapter(new CastAdapter(castResponse.getCast(), getActivity()));
            }

            @Override
            public void onFailure(Call<CastResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

}
