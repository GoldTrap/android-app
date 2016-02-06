package com.asb.goldtrap.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asb.goldtrap.R;

import static com.asb.goldtrap.models.dao.EpisodeDao.IMAGE;
import static com.asb.goldtrap.models.dao.EpisodeDao.NAME;

/**
 * Created by arjun on 06/02/16.
 */
public class CursorRecyclerAdapter extends RecyclerView.Adapter<CursorRecyclerAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private ViewHolder.ViewHolderClicks listener;

    public CursorRecyclerAdapter(Context context, Cursor cursor,
                                 ViewHolder.ViewHolderClicks listener) {
        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        this.notifyDataSetChanged();
    }

    public Cursor getItem(int position) {
        if (null != this.cursor && !this.cursor.isClosed()) {
            this.cursor.moveToPosition(position);
        }
        return this.cursor;
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
        final Cursor cursor = this.getItem(position);
        holder.bindMenu(cursor, context);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView textView;
        private ViewHolderClicks mListener;

        public ViewHolder(View itemView, ViewHolderClicks mListener) {
            super(itemView);
            this.mListener = mListener;
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.menu_name);
            imageView = (ImageView) itemView.findViewById(R.id.menu_image);
        }

        public void bindMenu(Cursor cursor, Context context) {
            int imageId = context.getResources()
                    .getIdentifier(cursor.getString(cursor.getColumnIndex(IMAGE)), "mipmap",
                            context.getPackageName());
            if (0 == imageId) {
                imageView.setImageResource(R.drawable.spark);
            }
            else {
                imageView.setImageResource(imageId);
            }
            int nameId = context.getResources()
                    .getIdentifier(cursor.getString(cursor.getColumnIndex(NAME)), "string",
                            context.getPackageName());
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
