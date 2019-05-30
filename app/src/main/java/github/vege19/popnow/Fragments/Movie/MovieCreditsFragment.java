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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import github.vege19.popnow.Adapters.CastAdapter;
import github.vege19.popnow.Adapters.CrewAdapter;
import github.vege19.popnow.Models.Credits.Cast;
import github.vege19.popnow.Models.Credits.CreditsResponse;
import github.vege19.popnow.Models.Credits.Crew;
import github.vege19.popnow.MovieDetailsActivity;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieCreditsFragment extends Fragment {

    private RecyclerView mCastRecyclerView, mCrewRecyclerView;
    private List<Cast> castList = new ArrayList<>();
    private List<Crew> crewList = new ArrayList<>();
    private CastAdapter mCastAdapter;
    private CrewAdapter mCrewAdapter;
    private SwipeRefreshLayout mRefreshLayout;

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

        //RecyclerView config
        recyclerViewSetUp();

        //Load all content
        loadCast();
        loadCrew();

        //To refresh content
        refreshContent();

    }

    private void refreshContent() {
        mRefreshLayout = getActivity().findViewById(R.id.movieCreditsRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Clear lists
                castList.clear();
                crewList.clear();

                //Reload content
                loadCast();
                loadCrew();

                //Stop refreshing
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void recyclerViewSetUp() {
        mCastRecyclerView = getActivity().findViewById(R.id.castRecyclerView);
        mCastRecyclerView.setLayoutManager(layoutManager());

        mCrewRecyclerView = getActivity().findViewById(R.id.crewRecyclerView);
        mCrewRecyclerView.setLayoutManager(layoutManager());
    }

    private void loadCrew() {
        mCrewAdapter = new CrewAdapter(crewList, getContext());

        //make retrofit call
        Call<CreditsResponse> crewResponseCall = RetrofitClient.getInstance().getApi().getMovieCredits(MovieDetailsActivity.movie_id,
                ApiService.api_key);

        crewResponseCall.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                //Fill recycler view
                crewList = response.body().getCrew();
                mCrewAdapter = new CrewAdapter(crewList, getContext());
                mCrewRecyclerView.setAdapter(mCrewAdapter);
            }

            @Override
            public void onFailure(Call<CreditsResponse> call, Throwable t) {
                crewList.clear();
                mCrewRecyclerView.setAdapter(mCrewAdapter);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadCast() {
        mCastAdapter = new CastAdapter(castList, getContext());

        //make the retrofit call
        Call<CreditsResponse> castResponseCall = RetrofitClient.getInstance().getApi().getMovieCredits(MovieDetailsActivity.movie_id, ApiService.api_key);

        castResponseCall.enqueue(new Callback<CreditsResponse>() {
            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                //Fill recycler view
                castList = response.body().getCast();
                mCastAdapter = new CastAdapter(castList, getContext());
                mCastRecyclerView.setAdapter(mCastAdapter);

            }

            @Override
            public void onFailure(Call<CreditsResponse> call, Throwable t) {
                castList.clear();
                mCastRecyclerView.setAdapter(mCastAdapter);
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
