package com.asb.goldtrap.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.menu.CardGridMenu;

import java.util.List;

/**
 * Created by arjun on 22/11/15.
 */
public class MenuRecyclerAdapter extends RecyclerView.Adapter<MenuRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<? extends CardGridMenu> homePageMenus;
    private ViewHolder.ViewHolderClicks listener;

    public MenuRecyclerAdapter(Context context,
                               List<? extends CardGridMenu> homePageMenus,
                               ViewHolder.ViewHolderClicks listener) {
        this.context = context;
        this.homePageMenus = homePageMenus;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.home_menu_card, null);
        return new ViewHolder(layoutView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindMenu(homePageMenus.get(position), context);
    }

    @Override
    public int getItemCount() {
        return homePageMenus.size();
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

        public void bindMenu(CardGridMenu homePageMenu, Context context) {
            int imageId = context.getResources()
                    .getIdentifier(homePageMenu.getImage(), "drawable", context.getPackageName());
            if (0 == imageId) {
                imageView.setImageResource(R.drawable.spark);
            }
            else {
                imageView.setImageResource(imageId);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imageView.setTransitionName(homePageMenu.getName());
                }
            }
            int nameId = context.getResources()
                    .getIdentifier(homePageMenu.getName(), "string", context.getPackageName());
            textView.setText(nameId);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, this.getAdapterPosition());
        }

        public interface ViewHolderClicks {
            void onClick(View v, int position);
        }
    }
}
