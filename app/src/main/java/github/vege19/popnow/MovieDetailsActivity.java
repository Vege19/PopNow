package github.vege19.popnow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import github.vege19.popnow.Adapters.MovieDetailsAdapter;
import github.vege19.popnow.Fragments.CastFragment;
import github.vege19.popnow.Fragments.OverViewFragment;
import github.vege19.popnow.Models.Movie.PopularMovie;
import github.vege19.popnow.Retrofit.ApiService;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView movieBackdrop;
    private Toolbar toolbar;
    private TabLayout mTabLayout;
    private MovieDetailsAdapter mAdapter;
    private ViewPager mViewPager;
    public static int movie_id;
    public static String overview;
    public static Integer[] genre_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //Fetch popularMovie attrs
        PopularMovie popularMovie = (PopularMovie) getIntent().getSerializableExtra("movieDetails");
        //save the popularMovie id
        movie_id = popularMovie.getId();
        overview = popularMovie.getOverview();
        genre_ids = popularMovie.getGenre_ids();

        //Start the tabs
        initFragments();

        movieBackdrop = findViewById(R.id.movieDetailsBackdrop);
        toolbar = findViewById(R.id.movieDetailsToolbar);

        //Support action bar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24px);

        getSupportActionBar().setTitle(popularMovie.getTitle());
        getSupportActionBar().setSubtitle(popularMovie.getRelease_date());

        Glide.with(this)
                .load(ApiService.imageURL + popularMovie.getBackdrop_path())
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
        mAdapter.addFragment(new OverViewFragment(), "Overview");
        mAdapter.addFragment(new CastFragment(), "Cast");

        //set adapter to viewpager
        mViewPager.setAdapter(mAdapter);

        //set up tab layout to show the array of fragments
        mTabLayout.setupWithViewPager(mViewPager);

    }

}
