package github.vege19.popnow.Retrofit;

import github.vege19.popnow.Models.Credits.CastResponse;
import github.vege19.popnow.Models.Genre.GenresResponse;
import github.vege19.popnow.Models.Movie.PopularMoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    String base_url = "https://api.themoviedb.org/3/";
    String api_key = "888eed6d5b3879fea3cf535a3b85d827";
    String imageURL = "http://image.tmdb.org/t/p/w500";
    String language = "en-US";

    //Get a list of popular movies
    @GET("movie/popular")
    Call<PopularMoviesResponse> getPopularMovies(@Query("api_key") String api_key,
                                                 @Query("language") String language,
                                                 @Query("page") int page);

    //Get a list of top rated movies
    @GET("movie/top_rated")
    Call<PopularMoviesResponse> getTopRatedMovies(@Query("api_key") String api_key,
                                                  @Query("language") String language,
                                                  @Query("page") int page);

    //Get a list of upcoming movies
    @GET("movie/upcoming")
    Call<PopularMoviesResponse> getUpcomingMovies(@Query("api_key") String api_key,
                                                  @Query("language") String language,
                                                  @Query("page") int page);

    //Get credits from a movie
    @GET("movie/{movie_id}/credits")
    Call<CastResponse> getCasts(@Path("movie_id") int movie_id,
                                @Query("api_key") String api_key);

    //Get a list of movie genres
    @GET("genre/movie/list")
    Call<GenresResponse> getGenres(@Query("api_key") String api_key,
                                   @Query("language") String language);


}
