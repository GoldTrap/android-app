package com.asb.goldtrap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.eo.migration.Episode;
import com.asb.goldtrap.models.episodes.EpisodeModel;

/**
 * Created by arjun on 06/02/16.
 */
public class EpisodeRecyclerAdapter
        extends RecyclerView.Adapter<EpisodeRecyclerAdapter.ViewHolder> {

    private Context context;
    private EpisodeModel episodeModel;
    private ViewHolder.ViewHolderClicks listener;

    private static final int[] buttonColors = {
            R.drawable.round_button_1,
            R.drawable.round_button_2,
            R.drawable.round_button_3,
            R.drawable.round_button_4,
            R.drawable.round_button_5
    };

    public EpisodeRecyclerAdapter(Context context, EpisodeModel episodeModel,
                                  ViewHolder.ViewHolderClicks listener) {
        this.context = context;
        this.episodeModel = episodeModel;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.episodes_card, parent, false);
        return new ViewHolder(layoutView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindMenu(episodeModel.getEpisode(position), context);
    }

    @Override
    public int getItemCount() {
        return episodeModel.getEpisodeCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textImage;
        private TextView textName;
        private ViewHolderClicks mListener;

        public ViewHolder(View itemView, ViewHolderClicks mListener) {
            super(itemView);
            this.mListener = mListener;
            itemView.setOnClickListener(this);
            textImage = (TextView) itemView.findViewById(R.id.episode_image);
            textName = (TextView) itemView.findViewById(R.id.episode_name);
        }

        public void bindMenu(Episode episode, Context context) {
            int nameId = context.getResources()
                    .getIdentifier(episode.getName(), "string", context.getPackageName());
            String name = context.getString(nameId);
            textImage.setText(String.valueOf(name.charAt(0)).toUpperCase());
            textImage.setBackgroundResource(
                    buttonColors[this.getAdapterPosition() % buttonColors.length]);
            textName.setText(name);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(this.getAdapterPosition());
        }

        public interface ViewHolderClicks {
            void onClick(int position);
        }
    }
}
