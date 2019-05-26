package github.vege19.popnow.Models.Trending;

import java.util.List;

public class TrendingResponse {

    private int page;
    private List<Trending> results;
    private int total_pages;
    private int total_results;

    public int getPage() {
        return page;
    }

    public List<Trending> getResults() {
        return results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }
}
