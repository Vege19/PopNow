package github.vege19.popnow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import github.vege19.popnow.Models.Movie;
import github.vege19.popnow.Retrofit.ApiService;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MovieDetailsActivity extends AppCompatActivity {

    private ImageView movieBackdrop;
    private TextView movieOverview;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //Fetch movie attrs
        Movie movie = (Movie) getIntent().getSerializableExtra("movieDetails");

        movieBackdrop = findViewById(R.id.movieDetailsBackdrop);
        toolbar = findViewById(R.id.movieDetailsToolbar);
        movieOverview = findViewById(R.id.movieDetailsOverview);

        //Support action bar
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24px);

        getSupportActionBar().setTitle(movie.getTitle());
        getSupportActionBar().setSubtitle(movie.getRelease_date());

        Glide.with(this)
                .load(ApiService.imageURL + movie.getBackdrop_path())
                .into(movieBackdrop);

        movieOverview.setText(movie.getOverview());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailsActivity.this.finish();
            }
        });


    }

}
