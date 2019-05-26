package github.vege19.popnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import github.vege19.popnow.Fragments.Home.HomeFragment;
import github.vege19.popnow.Fragments.Movie.MoviesFragment;
import github.vege19.popnow.Fragments.TvShow.TVShowsFragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private FragmentManager fragmentManager;

    //Fragments
    private Fragment moviesFragment;
    private Fragment tvshowsFragment;
    private Fragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navigationView);

        //Init base navigation
        initContent();

    }

    //Init fragments and bottom navigation view functionality
    private void initContent() {

        moviesFragment = new MoviesFragment();
        tvshowsFragment = new TVShowsFragment();
        homeFragment = new HomeFragment();

        fragmentManager = getSupportFragmentManager();

        //Setting movie fragment as first to load
        fragmentManager.beginTransaction()
                .replace(R.id.navigationContainer, homeFragment)
                .commit();

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment currentFragment = null;

                //Switch to select fragments
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        currentFragment = homeFragment;
                        break;
                    case R.id.navigation_movies:
                        currentFragment = moviesFragment;
                        break;
                    case R.id.navigation_tv:
                        currentFragment = tvshowsFragment;
                        break;
                }

                fragmentManager.beginTransaction()
                        .replace(R.id.navigationContainer, currentFragment)
                        .commit();

                return true;
            }

        });

    }

}
