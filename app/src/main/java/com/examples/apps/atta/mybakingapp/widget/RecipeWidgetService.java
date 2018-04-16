package com.examples.apps.atta.mybakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.examples.apps.atta.mybakingapp.R;
import com.examples.apps.atta.mybakingapp.RecipeDetailsActivity;
import com.examples.apps.atta.mybakingapp.model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class RecipeWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this.getApplicationContext());
    }

    class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        Context mContext;
        ArrayList<Ingredient> remoteIngredientsList;

        public RecipeRemoteViewsFactory(Context mContext) {
            this.mContext = mContext;
            remoteIngredientsList = new ArrayList<>();
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String json = preferences.getString(RecipeDetailsActivity.SHARED_PREFS_KEY, "");
            if (!json.equals("")) {
                Gson gson = new Gson();
                remoteIngredientsList = gson.fromJson(json, new TypeToken<ArrayList<Ingredient>>() {
                }.getType());
            }
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (remoteIngredientsList != null) {
                return remoteIngredientsList.size();
            } else return 0;
        }

        @Override
        public RemoteViews getViewAt(int i) {

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_list_item);

            Ingredient ingredient = remoteIngredientsList.get(i);

            views.setTextViewText(R.id.widget_list_item_text ,"\u2022" + " " + ingredient.getIngredient()
            + " (" + ingredient.getQuantity() + " " + ingredient.getMeasure() +")") ;

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
