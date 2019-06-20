package com.sidia.fabio.bakingrecipe;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sidia.fabio.bakingrecipe.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class RecipeViewFactory implements
        RemoteViewsService.RemoteViewsFactory {

    private final int appWidgetId;
    private final List<Recipe> recipes;
    private Context context;

    public RecipeViewFactory(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        recipes = loadRecipes();
    }

    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = context.getAssets().open("recipes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private List<Recipe> loadRecipes() {
        List<Recipe> result = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("recipes");
            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                int id = jo_inside.getInt("id");
                int servings = jo_inside.getInt("id");
                String name = jo_inside.getString("name");
                String image = jo_inside.getString("image");
                result.add(new Recipe(id, name, servings, image, null, null));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(context.getPackageName(),
                R.layout.item_widget_recipe_adapter);

        row.setTextViewText(R.id.title, recipes.get(position).getName());

        Intent i = new Intent();
        Bundle extras = new Bundle();

        extras.putInt(RecipeWidgetProvider.EXTRA_WORD, recipes.get(position).getId());
        i.putExtras(extras);
        row.setOnClickFillInIntent(R.id.title, i);

        return (row);
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
        return recipes.get(i).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
