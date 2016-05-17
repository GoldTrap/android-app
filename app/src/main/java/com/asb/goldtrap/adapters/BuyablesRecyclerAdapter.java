package com.asb.goldtrap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.buyables.BuyablesModel;
import com.asb.goldtrap.models.eo.Buyable;

/**
 * BuyablesRecyclerAdapter.
 * Created by arjun on 17/04/16.
 */
public class BuyablesRecyclerAdapter
        extends RecyclerView.Adapter<BuyablesRecyclerAdapter.ViewHolder> {
    private Context context;
    private BuyablesModel buyablesModel;
    private ViewHolder.ViewHolderClicks listener;

    public BuyablesRecyclerAdapter(Context context, BuyablesModel buyablesModel,
                                   ViewHolder.ViewHolderClicks listener) {
        this.context = context;
        this.buyablesModel = buyablesModel;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.buyables_card, parent, false);
        return new ViewHolder(layoutView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindMenu(buyablesModel.getBuyable(position), context);
    }

    @Override
    public int getItemCount() {
        return buyablesModel.getBuyablesCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView buyableImage;
        private TextView buyableName;
        private TextView buyableDesc;
        private ViewHolderClicks mListener;

        public ViewHolder(View itemView, ViewHolderClicks mListener) {
            super(itemView);
            this.mListener = mListener;
            itemView.setOnClickListener(this);
            buyableName = (TextView) itemView.findViewById(R.id.buyable_name);
            buyableDesc = (TextView) itemView.findViewById(R.id.buyable_desc);
            buyableImage = (ImageView) itemView.findViewById(R.id.buyable_image);
        }

        public void bindMenu(Buyable buyable, Context context) {
            int imageId = context.getResources()
                    .getIdentifier(buyable.getImage(), "drawable", context.getPackageName());
            if (0 == imageId) {
                buyableImage.setImageResource(R.drawable.spark);
            }
            else {
                buyableImage.setImageResource(imageId);
            }
            int nameId = context.getResources()
                    .getIdentifier(buyable.getName(), "string", context.getPackageName());
            buyableName.setText(nameId);
            int descId = context.getResources()
                    .getIdentifier(buyable.getDescription(), "string", context.getPackageName());
            buyableDesc.setText(descId);
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
