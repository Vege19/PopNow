package github.vege19.popnow.Models.Genre;

import java.util.List;

import github.vege19.popnow.Models.Genre.Genre;

public class GenresResponse {

    private List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
