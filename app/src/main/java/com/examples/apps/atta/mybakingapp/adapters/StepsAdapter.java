package com.examples.apps.atta.mybakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.examples.apps.atta.mybakingapp.R;
import com.examples.apps.atta.mybakingapp.model.Step;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepAdapterViewHolder> {

    private List<Step> steps;
    private Context mContext;
    private final StepClickHandler onClickHandler;

    public StepsAdapter(Context mContext, StepClickHandler onClickHandler) {
        this.mContext = mContext;
        this.onClickHandler = onClickHandler;
    }

    public interface StepClickHandler{
        void onClick(List<Step> steps , int stepIndex);
    }


    @NonNull
    @Override
    public StepAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item,parent,false);
        return new StepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapterViewHolder holder, int position) {
        if (steps!=null){
            holder.stepNumber.setText(String.valueOf(steps.get(position).getId()) + mContext.getString(R.string.dot_symbol));
            holder.stepTitle.setText(steps.get(position).getShortDescription());
        }
    }

    public void setStepsData(List<Step> steps , Context context){
        this.steps = steps;
        this.mContext = context;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (steps==null){
            return 0;
        }else {
            return steps.size();
        }
    }


    public class StepAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        TextView stepNumber;
        TextView stepTitle;

        public StepAdapterViewHolder(View itemView) {
            super(itemView);
            stepNumber = itemView.findViewById(R.id.step_number);
            stepTitle = itemView.findViewById(R.id.step_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onClickHandler.onClick(steps,position);
        }
    }
}
