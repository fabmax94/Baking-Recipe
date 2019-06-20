package com.sidia.fabio.bakingrecipe;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.sidia.fabio.bakingrecipe.model.Step;
import com.sidia.fabio.bakingrecipe.viewModel.RecipeViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StepFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView mShort;
    private TextView mDescription;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer mPlayer;
    private RecipeViewModel mRecipeViewModel;

    public StepFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static StepFragment newInstance(Step step) {
        StepFragment fragment = new StepFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipeViewModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        if (mRecipeViewModel.getCurrentStep() == null) {
            mListener.backToRecipe();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        mDescription = view.findViewById(R.id.tv_description);
        mShort = view.findViewById(R.id.tv_short);
        mShort.setText(mRecipeViewModel.getCurrentStep().getShortDescription());
        mDescription.setText(mRecipeViewModel.getCurrentStep().getDescription());
        mSimpleExoPlayerView = view.findViewById(R.id.ep_step);
        String media = mRecipeViewModel.getCurrentStep().getVideoURL();
        if (media.isEmpty()) {
            media = mRecipeViewModel.getCurrentStep().getThumbnailURL();
        }
        if (!media.isEmpty()) {

            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector(), new DefaultLoadControl());
            //mSimpleExoPlayerView.requestFocus();
            mSimpleExoPlayerView.setPlayer(mPlayer);


            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(media),
                    new DefaultDataSourceFactory(getContext(), "Step"), new DefaultExtractorsFactory(), null, null);

            mPlayer.prepare(mediaSource);
            mPlayer.setPlayWhenReady(true);
            mSimpleExoPlayerView.setVisibility(View.VISIBLE);
        } else {
            if (!mRecipeViewModel.getCurrentRecipe().getImage().isEmpty()) {
                ImageView ivRecipe = view.findViewById(R.id.iv_recipe);
                Glide.with(getContext())
                        .load(mRecipeViewModel.getCurrentRecipe().getImage())
                        .into(ivRecipe);
                ivRecipe.setVisibility(View.VISIBLE);
            }
        }

        view.findViewById(R.id.fb_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.nextStep();
            }
        });

        view.findViewById(R.id.fb_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.backStep();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
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
        void backStep();

        void nextStep();

        void backToRecipe();
    }
}