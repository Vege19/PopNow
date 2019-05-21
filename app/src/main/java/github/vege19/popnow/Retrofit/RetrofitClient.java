package github.vege19.popnow.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private final static String BASE_URL = "https://api.themoviedb.org/3/";
    private static Retrofit retrofit;
    private static  RetrofitClient retrofitClient;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static synchronized  RetrofitClient getInstance() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();

        }

        return retrofitClient;

    }

    public ApiService getApi() {
        return retrofit.create(ApiService.class);

    }

}
