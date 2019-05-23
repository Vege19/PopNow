package github.vege19.popnow.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import github.vege19.popnow.Models.TvShow.TvShow;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.ViewHolder> {

    private List<TvShow> tvShows;
    private Context context;

    public TvShowsAdapter(List<TvShow> tvShows, Context context) {
        this.tvShows = tvShows;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tv_show, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TvShow tvShow = tvShows.get(position);

        //load poster image
        Glide.with(context)
                .load(ApiService.imageURL + tvShow.getPoster_path())
                .into(holder.poster);

    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView poster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.tvShowPoster);

        }
    }
}
