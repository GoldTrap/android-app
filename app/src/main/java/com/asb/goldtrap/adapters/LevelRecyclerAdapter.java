package com.asb.goldtrap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.eo.migration.Level;
import com.asb.goldtrap.models.levels.LevelModel;

/**
 * Created by arjun on 07/02/16.
 */
public class LevelRecyclerAdapter
        extends RecyclerView.Adapter<LevelRecyclerAdapter.ViewHolder> {
    private Context context;
    private LevelModel levelModel;
    private ViewHolder.ViewHolderClicks listener;

    public LevelRecyclerAdapter(Context context, LevelModel levelModel,
                                ViewHolder.ViewHolderClicks listener) {
        this.context = context;
        this.levelModel = levelModel;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.levels_card, parent, false);
        return new ViewHolder(layoutView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindMenu(levelModel.getLevel(position), context);
    }

    @Override
    public int getItemCount() {
        return levelModel.getLevelCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView textView;
        private ViewHolderClicks mListener;

        public ViewHolder(View itemView, ViewHolderClicks mListener) {
            super(itemView);
            this.mListener = mListener;
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.level_name);
            imageView = (ImageView) itemView.findViewById(R.id.level_image);
        }

        public void bindMenu(Level level, Context context) {
            int imageIdentifier = R.drawable.coins;
            if (level.isCompleted()) {
                imageIdentifier = R.drawable.diamond;
            }
            imageView.setImageResource(imageIdentifier);
            textView.setText(context.getString(R.string.level, level.getNumber()));
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
