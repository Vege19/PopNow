package github.vege19.popnow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import github.vege19.popnow.Adapters.MovieDetailsAdapter;
import github.vege19.popnow.Fragments.Movie.MovieCreditsFragment;
import github.vege19.popnow.Fragments.Movie.MovieReviewsFragment;
import github.vege19.popnow.Fragments.Movie.MovieOverviewFragment;
import github.vege19.popnow.Models.Movie.Movie;
import github.vege19.popnow.Models.Trending.Trending;
import github.vege19.popnow.Models.Video.Video;
import github.vege19.popnow.Models.Video.VideosResponse;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView movieBackdrop, playVideoButton;
    private Toolbar toolbar;
    private TabLayout mTabLayout;
    private MovieDetailsAdapter mAdapter;
    private ViewPager mViewPager;
    public static int movie_id;
    public static String overview, release_date, title, backdrop_path;
    public static float vote_average;
    public static Integer[] genre_ids;
    private String youtube_base_url = "https://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //Fetch movie attrs
        //from movies fragment
        Movie movie = (Movie) getIntent().getSerializableExtra("movieDetails");
        //from home fragment
        Trending movieTrend = (Trending) getIntent().getSerializableExtra("trending_movie_details");

        //save movie details
        //if intent comes from movies
        if (movie != null) {
            movie_id = movie.getId();
            overview = movie.getOverview();
            genre_ids = movie.getGenre_ids();
            release_date = movie.getRelease_date();
            vote_average = movie.getVote_average();
            title = movie.getTitle();
            backdrop_path = movie.getBackdrop_path();
            //if comes from home
        } else if (movieTrend != null) {
            movie_id = movieTrend.getId();
            overview = movieTrend.getOverview();
            genre_ids = movieTrend.getGenre_ids();
            release_date = movieTrend.getRelease_date();
            vote_average = movieTrend.getVote_average();
            title = movieTrend.getTitle();
            backdrop_path = movieTrend.getBackdrop_path();
        }

        //Start the tabs
        initFragments();

        playTrailer();

        movieBackdrop = findViewById(R.id.movieDetailsBackdrop);
        toolbar = findViewById(R.id.movieDetailsToolbar);

        //Support action bar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24px);

        getSupportActionBar().setTitle(title);

        Glide.with(this)
                .load(ApiService.imageURL + backdrop_path)
                .into(movieBackdrop);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailsActivity.this.finish();
            }
        });

    }

    private void initFragments() {
        //get necessary views
        mTabLayout = findViewById(R.id.movieDetailsTabLayout);
        mViewPager = findViewById(R.id.movieDetailsViewPager);

        //create adapter for the viewpager and add fragments
        mAdapter = new MovieDetailsAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new MovieOverviewFragment(), "Overview");
        mAdapter.addFragment(new MovieCreditsFragment(), "Cast");
        mAdapter.addFragment(new MovieReviewsFragment(), "Reviews");

        //set adapter to viewpager
        mViewPager.setAdapter(mAdapter);

        //set up tab layout to show the array of fragments
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void playTrailer() {
        //get play button view
        playVideoButton = findViewById(R.id.playMovieVideoButton);

        //video info call
        Call<VideosResponse> movieVideosResponseCall = RetrofitClient.getInstance().getApi().getMovieVideos(movie_id,
                ApiService.api_key,
                ApiService.language);

        movieVideosResponseCall.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                VideosResponse videosResponse = response.body();

                if (!videosResponse.getResults().isEmpty()) {
                    //get movie video
                    final Video video = response.body().getResults().get(0);

                    //Intent to youtube
                    playVideoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube_base_url + video.getKey()));
                            startActivity(intent);
                        }
                    });

                } else {
                    playVideoButton.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                Toast.makeText(MovieDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

}
