package github.vege19.popnow.Retrofit;

import github.vege19.popnow.Models.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    String base_url = "https://api.themoviedb.org/3/";
    String api_key = "888eed6d5b3879fea3cf535a3b85d827";
    String imageURL = "http://image.tmdb.org/t/p/w500";

    //Get a list of popular movies
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String api_key,
                                                @Query("language") String language,
                                                @Query("page") int page);


}
