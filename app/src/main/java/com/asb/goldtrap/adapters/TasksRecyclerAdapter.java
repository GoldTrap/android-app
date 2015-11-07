package com.asb.goldtrap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asb.goldtrap.R;
import com.asb.goldtrap.models.eo.Task;

import java.util.List;

/**
 * Created by arjun on 07/11/15.
 */
public class TasksRecyclerAdapter extends RecyclerView.Adapter<TasksRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Task> tasks;
    private int lastPosition = -1;

    public TasksRecyclerAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindTask(tasks.get(position));
        setAnimation(holder.container, position);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation =
                    AnimationUtils.loadAnimation(context,
                            android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout container;
        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            container = (RelativeLayout) view.findViewById(R.id.container);
            mTextView = (TextView) view.findViewById(R.id.task_text);
            mImageView = (ImageView) view.findViewById(R.id.task_image);
        }

        public void bindTask(Task task) {
            mImageView.setImageResource(task.getTaskType().getImage());
            switch (task.getTaskType()) {
                case DYNAMIC_GOODIE:
                    mTextView.setText(context.getResources()
                            .getQuantityString(task.getTaskType().getText(), task.getCount(),
                                    task.getCount(), task.getPoints()));
                    break;
                default:
                    mTextView.setText(context.getResources()
                            .getQuantityString(task.getTaskType().getText(), task.getCount(),
                                    task.getCount()));
            }

        }
    }
}
