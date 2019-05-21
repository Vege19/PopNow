package github.vege19.popnow.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import github.vege19.popnow.Models.Cast;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private List<Cast> casts;
    private Context context;

    public CastAdapter(List<Cast> casts, Context context) {
        this.casts = casts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cast, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cast cast = casts.get(position);

        holder.name.setText(cast.getName());
        holder.character.setText(cast.getCharacter());

        Glide.with(context)
                .load(ApiService.imageURL + cast.getProfile_path())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.profileImage);

    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profileImage;
        private TextView name, character;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.castProfileImage);
            name = itemView.findViewById(R.id.castName);
            character = itemView.findViewById(R.id.castCharacterName);

        }
    }
}
