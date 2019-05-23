package github.vege19.popnow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;

import github.vege19.popnow.Adapters.TvShowDetailsAdapter;
import github.vege19.popnow.Fragments.TvShow.TvShowCastFragment;
import github.vege19.popnow.Fragments.TvShow.TvShowOverviewFragment;
import github.vege19.popnow.Fragments.TvShow.TvShowsReviewsFragment;
import github.vege19.popnow.Models.TvShow.TvShow;
import github.vege19.popnow.Retrofit.ApiService;

public class TvShowDetailsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private TvShowDetailsAdapter mAdapter;
    public static TvShow thisTvShow;
    private ImageView backdropImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_details);

        //fetch tv movie details data
        thisTvShow = (TvShow) getIntent().getSerializableExtra("tv_show_details");

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
}
