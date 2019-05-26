package github.vege19.popnow.Models.Trending;

import java.io.Serializable;

public class Trending implements Serializable {

    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private String first_air_date;
    private Integer[] genre_ids;
    private int id;
    private String original_title;
    private String original_language;
    private String title;
    private String name;
    private String backdrop_path;
    private float popularity;
    private int vote_count;
    private boolean video;
    private float vote_average;

    public String getPoster_path() {
        return poster_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public Integer[] getGenre_ids() {
        return genre_ids;
    }

    public int getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public float getPopularity() {
        return popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public float getVote_average() {
        return vote_average;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public String getName() {
        //if movie will return title, else return name
        String name_title = null;
        if (this.name == null) {
            name_title = this.title;
        } else if (this.title == null) {
            name_title = this.name;
        }

        return name_title;
    }
}
