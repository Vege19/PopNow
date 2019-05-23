package github.vege19.popnow.Models.TvShow;

import java.io.Serializable;

public class TvShow implements Serializable {

    private String poster_path;
    private float popularity;
    private int id;
    private String backdrop_path;
    private float vote_average;
    private String overview;
    private String first_air_date;
    private String[] origin_country;
    private Integer[] genre_ids;
    private String original_language;
    private int vote_count;
    private String name;
    private String original_name;

    public String getPoster_path() {
        return poster_path;
    }

    public float getPopularity() {
        return popularity;
    }

    public int getId() {
        return id;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public float getVote_average() {
        return vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public String[] getOrigin_country() {
        return origin_country;
    }

    public Integer[] getGenre_ids() {
        return genre_ids;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public int getVote_count() {
        return vote_count;
    }

    public String getName() {
        return name;
    }

    public String getOriginal_name() {
        return original_name;
    }
}
