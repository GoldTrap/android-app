package com.asb.goldtrap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        private ImageView imageView;
        private TextView textView;
        private ViewHolderClicks mListener;

        public ViewHolder(View itemView, ViewHolderClicks mListener) {
            super(itemView);
            this.mListener = mListener;
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.episode_name);
            imageView = (ImageView) itemView.findViewById(R.id.episode_image);
        }

        public void bindMenu(Episode episode, Context context) {
            int imageId = context.getResources()
                    .getIdentifier(episode.getImage(), "drawable", context.getPackageName());
            if (0 == imageId) {
                imageView.setImageResource(R.drawable.spark);
            }
            else {
                imageView.setImageResource(imageId);
            }
            int nameId = context.getResources()
                    .getIdentifier(episode.getName(), "string", context.getPackageName());
            textView.setText(nameId);
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
