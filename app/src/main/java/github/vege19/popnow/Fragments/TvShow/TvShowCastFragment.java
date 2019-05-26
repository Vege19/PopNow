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
import github.vege19.popnow.Models.Credits.CastResponse;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import github.vege19.popnow.TvShowDetailsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowCastFragment extends Fragment {

    private RecyclerView castRecyclerview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_show_cast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        castRecyclerview = getActivity().findViewById(R.id.tvShowCastRecyclerview);
        castRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 3));

        loadCast();

    }

    private void loadCast() {
        //retrofit call
        Call<CastResponse> castResponseCall = RetrofitClient.getInstance().getApi().getTvShowCasts(TvShowDetailsActivity.tv_id,
                ApiService.api_key);

        castResponseCall.enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                //fill the recyclerview
                CastResponse castResponse = response.body();
                castRecyclerview.setAdapter(new CastAdapter(castResponse.getCast(), getContext()));


            }

            @Override
            public void onFailure(Call<CastResponse> call, Throwable t) {
                Toast.makeText(getContext(),"Error: " + t.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
    }
}
