package github.vege19.popnow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import github.vege19.popnow.Adapters.CastAdapter;
import github.vege19.popnow.Adapters.MovieDetailsAdapter;
import github.vege19.popnow.Fragments.CastFragment;
import github.vege19.popnow.Models.Movie;
import github.vege19.popnow.Retrofit.ApiService;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView movieBackdrop;
    private Toolbar toolbar;
    private TabLayout mTabLayout;
    private MovieDetailsAdapter mAdapter;
    private ViewPager mViewPager;
    public static int movie_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //Fetch movie attrs
        Movie movie = (Movie) getIntent().getSerializableExtra("movieDetails");
        //save the movie id
        movie_id = movie.getId();

        //Start the tabs
        initFragments();

        movieBackdrop = findViewById(R.id.movieDetailsBackdrop);
        toolbar = findViewById(R.id.movieDetailsToolbar);

        //Support action bar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24px);

        getSupportActionBar().setTitle(movie.getTitle());
        getSupportActionBar().setSubtitle(movie.getRelease_date());

        Glide.with(this)
                .load(ApiService.imageURL + movie.getBackdrop_path())
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
        mAdapter.addFragment(new CastFragment(), "Cast");

        //set adapter to viewpager
        mViewPager.setAdapter(mAdapter);

        //set up tab layout to show the array of fragments
        mTabLayout.setupWithViewPager(mViewPager);

    }

}
