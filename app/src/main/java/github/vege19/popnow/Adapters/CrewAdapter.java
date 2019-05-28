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
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import github.vege19.popnow.Models.Credits.Crew;
import github.vege19.popnow.PersonDetailsActivity;
import github.vege19.popnow.R;
import github.vege19.popnow.Retrofit.ApiService;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.ViewHolder> {

    private List<Crew> crewList;
    private Context context;

    public CrewAdapter(List<Crew> crewList, Context context) {
        this.crewList = crewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crew, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Crew crew = crewList.get(position);

        holder.name.setText(crew.getName());
        holder.job.setText(crew.getJob());

        Glide.with(context)
                .load(ApiService.imageURL + crew.getProfile_path())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.profile);

        //send id to person details activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PersonDetailsActivity.class);
                intent.putExtra("person_id", crew.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return crewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, job;
        private ImageView profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.crewName);
            job = itemView.findViewById(R.id.crewJob);
            profile = itemView.findViewById(R.id.crewProfileImage);
        }
    }
}
