package com.examples.apps.atta.mybakingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.examples.apps.atta.mybakingapp.R;
import com.examples.apps.atta.mybakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private ArrayList<Recipe> recipes;
    private Context mContext;
    private final RecipeClickHandler onClickHandler;

    public RecipeAdapter( Context mContext, RecipeClickHandler onClickHandler) {
        this.mContext = mContext;
        this.onClickHandler = onClickHandler;
    }

    public interface RecipeClickHandler{
        void onClick(Recipe recipe);
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item,parent,false);
        view.setFocusable(true);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        if (recipes != null) {

            holder.textView.setText(recipes.get(position).getName());

            String imageUrl = recipes.get(position).getImage();

            if (imageUrl != "") {
                Uri imageUri = Uri.parse(imageUrl).buildUpon().build();

                Picasso.get()
                        .load(imageUri)
                        .into(holder.imageView);
            }
        }
    }

    public void setRecipeData(ArrayList<Recipe> recipes , Context context){
        this.recipes = recipes;
        this.mContext = context;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (recipes == null){
            return 0;
        }else {
            return recipes.size();
        }
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder
          implements View.OnClickListener{

        TextView textView;
        ImageView imageView;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.recipe_title);
            imageView = itemView.findViewById(R.id.recipe_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onClickHandler.onClick(recipes.get(position));
        }
    }
}
