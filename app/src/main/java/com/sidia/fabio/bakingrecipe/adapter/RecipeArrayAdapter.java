package com.sidia.fabio.bakingrecipe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sidia.fabio.bakingrecipe.R;
import com.sidia.fabio.bakingrecipe.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeArrayAdapter extends RecyclerView.Adapter<RecipeArrayAdapter.RecipeViewHolder> {

    private List<Recipe> mMovieList;

    final private IRecipeHandleAdapter mOnClickListener;

    public void updateMovies(List<Recipe> movies) {
        mMovieList = movies;
    }

    public interface IRecipeHandleAdapter {
        void onListItemClick(int clickedItemIndex);
    }

    public RecipeArrayAdapter(List<Recipe> movies, IRecipeHandleAdapter listener) {
        mMovieList = movies;
        mOnClickListener = listener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_array_recipe_adapter, viewGroup, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(mMovieList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mMovieList == null) {
            mMovieList = new ArrayList<>();
        }
        return mMovieList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView mTitle;
        ImageView mImage;


        public RecipeViewHolder(View viewContext) {
            super(viewContext);

            mTitle = viewContext.findViewById(R.id.title);
            mImage = viewContext.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        void bind(Recipe recipe) {
            mTitle.setText(recipe.getName());

            if (!recipe.getImage().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(recipe.getImage())
                        .into(mImage);
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
