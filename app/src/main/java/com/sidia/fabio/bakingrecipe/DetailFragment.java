package com.sidia.fabio.bakingrecipe;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sidia.fabio.bakingrecipe.model.Ingredient;
import com.sidia.fabio.bakingrecipe.model.Step;
import com.sidia.fabio.bakingrecipe.viewModel.RecipeViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView mTitle;
    private ImageView mImage;
    private LinearLayout mIngredientContainer;
    private LinearLayout mStepContainer;
    private RecipeViewModel mRecipeViewModel;

    public DetailFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipeViewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        if(mRecipeViewModel.getCurrentRecipe() == null){
            mListener.backToList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mTitle = view.findViewById(R.id.title);
        mTitle.setText(mRecipeViewModel.getCurrentRecipe().getName());
        mImage = view.findViewById(R.id.image);
        mIngredientContainer = view.findViewById(R.id.ingredient_container);
        mStepContainer = view.findViewById(R.id.steps_container);
        if (!mRecipeViewModel.getCurrentRecipe().getImage().isEmpty()) {
            Glide.with(getContext())
                    .load(mRecipeViewModel.getCurrentRecipe().getImage())
                    .into(mImage);
        }
        loadIngredients();
        loadSteps();
        return view;
    }

    private void loadIngredients() {
        for (Ingredient ingredient : mRecipeViewModel.getCurrentRecipe().getIngredients()) {
            @SuppressLint("InflateParams") View viewPrepare = getLayoutInflater().inflate(R.layout.ingredient_list, null);
            TextView ingredientName = viewPrepare.findViewById(R.id.ingredient_name);
            ingredientName.setText(ingredient.toString());
            mIngredientContainer.addView(viewPrepare);
        }
    }

    private void loadSteps() {
        for (final Step step : mRecipeViewModel.getCurrentRecipe().getSteps()) {
            @SuppressLint("InflateParams") View viewPrepare = getLayoutInflater().inflate(R.layout.step_list, null);
            TextView stepName = viewPrepare.findViewById(R.id.step_name);
            stepName.setText(step.getShortDescription());
            mStepContainer.addView(viewPrepare);
            viewPrepare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.openStep(step);
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void backToList();
        void openStep(Step step);
    }
}