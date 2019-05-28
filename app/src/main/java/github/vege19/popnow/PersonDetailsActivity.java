package github.vege19.popnow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import github.vege19.popnow.Models.Credits.Person;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.Retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonDetailsActivity extends AppCompatActivity {

    private TextView personBio, personBirthPlace, personBirthDate;
    private ImageView personProfile;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        toolbarSetup();

        obtainPerson();

    }

    private void initContent(String name, String biography, String birthDate, String birthPlace, String profilePath) {
        //set name in toolbar
        mToolbar.setTitle(name);

        //set person data
        personProfile = findViewById(R.id.personProfilePhoto);
        personProfile.setElevation(4);
        Glide.with(this)
                .load(ApiService.imageURL + profilePath)
                .apply(RequestOptions.circleCropTransform())
                .into(personProfile);


        personBio = findViewById(R.id.personBiography);
        personBio.setText(biography);

        personBirthPlace = findViewById(R.id.personBirthPlace);
        personBirthPlace.setText("Place of birth: " + birthPlace);

        personBirthDate = findViewById(R.id.personBirthDate);
        personBirthDate.setText("Birthday: " + birthDate);

    }

    private void obtainPerson() {
        //retrofit call
        Call<Person> personCall = RetrofitClient.getInstance().getApi().getPerson(fetchPersonId(),
                ApiService.api_key, ApiService.language);

        personCall.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                //fetch person data
                Person person = response.body();
                Log.d("debug", person.getName());
                initContent(person.getName(),
                        person.getBiography(),
                        person.getBirthday(),
                        person.getPlace_of_birth(),
                        person.getProfile_path());

            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                Toast.makeText(PersonDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private int fetchPersonId() {
        int person_id = (int) getIntent().getSerializableExtra("person_id");
        return person_id;
    }

    private void toolbarSetup() {
        mToolbar = findViewById(R.id.personDetailsToolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_round_arrow_back_24px));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonDetailsActivity.this.finish();
            }
        });
    }

}
