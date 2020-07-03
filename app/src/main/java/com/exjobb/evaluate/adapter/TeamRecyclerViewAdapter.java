package com.exjobb.evaluate.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exjobb.evaluate.R;
import com.exjobb.evaluate.activities.StatisticsPlayerActivity;
import com.exjobb.evaluate.activities.PlayerEditActivity;
import com.exjobb.evaluate.activities.PlayerInfoActivity;
import com.exjobb.evaluate.data.UserModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TeamRecyclerViewAdapter extends FirestoreRecyclerAdapter<UserModel, TeamRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "TeamRecyclerViewAdapter";
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TeamRecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull UserModel model) {
        holder.tvName.setText(model.getName());
        holder.tvNote.setText(model.getNote());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: started");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ViewHolder";
        private TextView tvName, tvNote;
        private ImageButton ibPlayerOptionsMenu;
        private Intent intent;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.i(TAG, "ViewHolder: started");

            tvName = itemView.findViewById(R.id.layout_name);
            tvNote = itemView.findViewById(R.id.layout_note);
            ibPlayerOptionsMenu = itemView.findViewById(R.id.imageButtonOverflow);

            ibPlayerOptionsMenu.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), ibPlayerOptionsMenu);
                popupMenu.inflate(R.menu.user_options_menu);

                popupMenu.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_view_user_profile:
                            intent = new Intent(itemView.getContext(), PlayerInfoActivity.class);
                            intent.putExtra("playerPath", getPlayerAtIndex(getAdapterPosition()));
                            itemView.getContext().startActivity(intent);
                            return true;
                        case R.id.menu_view_user_stats:
                            intent = new Intent(itemView.getContext(), StatisticsPlayerActivity.class);
                            intent.putExtra("playerPath", getPlayerAtIndex(getAdapterPosition()));
                            intent.putExtra("coachPath", getCoachPath(getAdapterPosition()));
                            itemView.getContext().startActivity(intent);
                            return true;
                        case R.id.menu_edit_user_info:
                            intent = new Intent(itemView.getContext(), PlayerEditActivity.class);
                            intent.putExtra("playerPath", getPlayerAtIndex(getAdapterPosition()));
                            itemView.getContext().startActivity(intent);
                            return true;
                        case R.id.menu_delete_user:
                            deletePlayerAtIndex(getAdapterPosition());
                            return true;
                    }
                    return false;
                });
                popupMenu.show();
            });
        }
    }

    private void deletePlayerAtIndex(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    private String getPlayerAtIndex(int position) {
        return getSnapshots().getSnapshot(position).getReference().getPath();
    }

    private String getCoachPath(int position) {
        return getSnapshots().getSnapshot(position).getReference().getParent().getPath();
    }
}
