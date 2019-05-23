package github.vege19.popnow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import github.vege19.popnow.Adapters.TvShowDetailsAdapter;
import github.vege19.popnow.Fragments.TvShow.TvShowCastFragment;
import github.vege19.popnow.Fragments.TvShow.TvShowOverviewFragment;
import github.vege19.popnow.Fragments.TvShow.TvShowsReviewsFragment;
import github.vege19.popnow.Models.TvShow.TvShow;
import github.vege19.popnow.Models.Video.Video;
import github.vege19.popnow.Models.Video.VideosResponse;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowDetailsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TvShowDetailsAdapter mAdapter;
    public static TvShow thisTvShow;
    private ImageView backdropImage, playVideoButton;
    private String youtube_base_url = "https://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_details);

        //fetch tv movie details data
        thisTvShow = (TvShow) getIntent().getSerializableExtra("tv_show_details");
        Log.d("debug", String.valueOf(thisTvShow.getId()));

        //enable video button
        playVideo();

        //set backdrop image
        backdropImage = findViewById(R.id.tvShowDetailsBackdrop);
        Glide.with(this)
                .load(ApiService.imageURL + thisTvShow.getBackdrop_path())
                .into(backdropImage);

        toolbarSetUp();

        //load fragments
        initFragments();

    }

    private void initFragments() {
        //get necessary views
        mTabLayout = findViewById(R.id.tvShowDetailsTabLayout);
        mViewPager = findViewById(R.id.tvShowDetailsViewPager);

        //create adapter for the viewpager and add fragments
        mAdapter = new TvShowDetailsAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new TvShowOverviewFragment(), "Overview");
        mAdapter.addFragment(new TvShowCastFragment(), "Cast");
        mAdapter.addFragment(new TvShowsReviewsFragment(), "Reviews");

        //set adapter to viewpager
        mViewPager.setAdapter(mAdapter);

        //set up tab layout to show the array of fragments
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void toolbarSetUp() {
        mToolbar = findViewById(R.id.tvShowDetailsToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_round_arrow_back_24px));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TvShowDetailsActivity.this.finish();
            }
        });

        //set title
        getSupportActionBar().setTitle(thisTvShow.getName());

    }

    private void playVideo() {
        playVideoButton = findViewById(R.id.playTvShowVideoButton);

        //make retrofit call
        Call<VideosResponse> videosResponseCall = RetrofitClient.getInstance().getApi().getTvShowVideos(thisTvShow.getId(),
                ApiService.api_key,
                ApiService.language);

        videosResponseCall.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                VideosResponse videosResponse = response.body();
                //if the tv show contains video
                if (!videosResponse.getResults().isEmpty()) {
                    //get video
                    final Video video = videosResponse.getResults().get(0);

                    //Intent to youtube
                    playVideoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube_base_url + video.getKey()));
                            startActivity(intent);
                        }
                    });

                } else {
                    //if tv show does not contains a video, play button disappears
                    playVideoButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                Toast.makeText(TvShowDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}
