package com.sidia.fabio.bakingrecipe;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sidia.fabio.bakingrecipe.adapter.RecipeArrayAdapter;
import com.sidia.fabio.bakingrecipe.model.Recipe;
import com.sidia.fabio.bakingrecipe.viewModel.RecipeViewModel;


public class ListFragment extends Fragment implements RecipeArrayAdapter.IRecipeHandleAdapter {

    private OnFragmentInteractionListener mListener;

    private RecipeArrayAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecipeViewModel mRecipeViewModel;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipeViewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = view.findViewById(R.id.rv_recipes);
        loadRecipes();
        return view;
    }


    private void loadRecipes() {
        if (mAdapter == null) {
            mAdapter = new RecipeArrayAdapter(mRecipeViewModel.getRecipes(), ListFragment.this);
            int mGridColumn = 1;
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), mGridColumn);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.updateMovies(mRecipeViewModel.getRecipes());
            mAdapter.notifyDataSetChanged();
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

    @Override
    public void onListItemClick(int clickedItemIndex) {
        mListener.onListClick(mRecipeViewModel.getRecipes().get(clickedItemIndex));
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
        void onListClick(Recipe recipe);
    }
}
