package github.vege19.popnow.Models.Search;

import java.util.List;

import github.vege19.popnow.Models.TvShow.TvShow;

public class SearchTvShowResponse {
    private int page;
    private List<TvShow> results;
    private int total_results;
    private int total_pages;

    public List<TvShow> getResults() {
        return results;
    }

    public void setResults(List<TvShow> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

}
