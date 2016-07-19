package com.asb.goldtrap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.eo.Goodie;
import com.asb.goldtrap.models.goodie.GoodieModel;

/**
 * Points Recycler Adapter.
 * Created by arjun on 19/07/16.
 */
public class PointsRecyclerAdapter
        extends RecyclerView.Adapter<PointsRecyclerAdapter.ViewHolder> {
    private static final String TAG = PointsRecyclerAdapter.class.getSimpleName();
    private Context context;
    private GoodieModel goodieModel;

    public PointsRecyclerAdapter(Context context, GoodieModel goodieModel) {
        this.context = context;
        this.goodieModel = goodieModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.points_card, parent, false);
        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindMenu(goodieModel.getGoodie(position), context);
    }

    @Override
    public int getItemCount() {
        return goodieModel.getGoodiesCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView pointsImage;
        private TextView goodieValue;

        public ViewHolder(View itemView) {
            super(itemView);
            pointsImage = (ImageView) itemView.findViewById(R.id.points_image);
            goodieValue = (TextView) itemView.findViewById(R.id.goodie_value);
        }

        public void bindMenu(Goodie goodie, Context context) {
            int imageId = goodie.getGoodiesState().getDrawableRes();
            if (-1 == imageId) {
                pointsImage.setImageResource(R.drawable.spark);
            }
            else {
                pointsImage.setImageResource(imageId);
            }
            goodieValue.setText(context.getString(R.string.goodie_value_name, goodie.getCount(),
                    context.getString(goodie.getGoodiesState().getNameRes())));
        }
    }
}
