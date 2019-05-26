package github.vege19.popnow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import github.vege19.popnow.Models.Trending.Trending;
import github.vege19.popnow.MovieDetailsActivity;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;
import github.vege19.popnow.TvShowDetailsActivity;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder> {

    private List<Trending> trendings;
    private Context context;

    public TrendingAdapter(List<Trending> trendings, Context context) {
        this.trendings = trendings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trending, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Trending trending = trendings.get(position);

        //set title
        holder.title.setText(trending.getName());

        //load image
        Glide.with(context)
                .load(ApiService.imageURL + trending.getBackdrop_path())
                .into(holder.backdrop);

    }

    @Override
    public int getItemCount() {
        return trendings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView backdrop;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            backdrop = itemView.findViewById(R.id.trendinItemBackdrop);
            title = itemView.findViewById(R.id.trendingTitle);

        }
    }

}
