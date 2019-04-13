package github.vege19.popnow.Retrofit;

import java.util.List;

import github.vege19.popnow.Models.Movie;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    String base_url = "https://api.themoviedb.org/3";

    //Get a list of popular movies
    @GET("/movie/popular")
    Call<List<Movie>> getPopularMovies(String api_key,
                                       String language,
                                       int page,
                                       String region);


}
