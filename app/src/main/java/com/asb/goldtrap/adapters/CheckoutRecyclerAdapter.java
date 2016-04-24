package com.asb.goldtrap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.eo.BoosterExchangeRate;
import com.asb.goldtrap.models.eo.Goodie;
import com.asb.goldtrap.models.goodie.GoodieModel;

/**
 * Checkout Recycler Adapter.
 * Created by arjun on 24/04/16.
 */
public class CheckoutRecyclerAdapter
        extends RecyclerView.Adapter<CheckoutRecyclerAdapter.ViewHolder> {
    private static final String TAG = CheckoutRecyclerAdapter.class.getSimpleName();
    private Context context;
    private GoodieModel goodieModel;
    private ViewHolder.ViewHolderClicks listener;
    private BoosterExchangeRate boosterExchangeRate;

    public CheckoutRecyclerAdapter(Context context, GoodieModel goodieModel,
                                   ViewHolder.ViewHolderClicks listener,
                                   BoosterExchangeRate boosterExchangeRate) {
        this.context = context;
        this.goodieModel = goodieModel;
        this.listener = listener;
        this.boosterExchangeRate = boosterExchangeRate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.checkout_card, parent, false);
        return new ViewHolder(layoutView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindMenu(goodieModel.getGoodie(position), context, boosterExchangeRate);
    }

    @Override
    public int getItemCount() {
        return goodieModel.getGoodiesCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView checkoutImage;
        private TextView goodieName;
        private TextView goodieExchangeRate;
        private Button exchange;
        private ViewHolderClicks mListener;

        public ViewHolder(View itemView, ViewHolderClicks mListener) {
            super(itemView);
            this.mListener = mListener;
            goodieName = (TextView) itemView.findViewById(R.id.goodie_name);
            goodieExchangeRate = (TextView) itemView.findViewById(R.id.goodie_exchange_rate);
            checkoutImage = (ImageView) itemView.findViewById(R.id.checkout_image);
            exchange = (Button) itemView.findViewById(R.id.exchange_button);
            exchange.setOnClickListener(this);
        }

        public void bindMenu(Goodie goodie, Context context,
                             BoosterExchangeRate boosterExchangeRate) {
            int imageId = goodie.getGoodiesState().getDrawableRes();
            if (-1 == imageId) {
                checkoutImage.setImageResource(R.drawable.spark);
            }
            else {
                checkoutImage.setImageResource(imageId);
            }
            goodieName.setText(goodie.getGoodiesState().getNameRes());
            long exchangeRate = boosterExchangeRate.getGoodies().get(goodie.getGoodiesState());
            goodieExchangeRate.setText(
                    context.getString(R.string.exchange_goodie_desc, exchangeRate,
                            context.getString(goodie.getGoodiesState().getNameRes())));
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
