package github.vege19.popnow.Models.TvShow;

import java.util.List;

public class TvShowsResponse {

    private int page;
    private List<TvShow> results;
    private int total_results;
    private int total_pages;

    public int getPage() {
        return page;
    }

    public List<TvShow> getResults() {
        return results;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }
}
