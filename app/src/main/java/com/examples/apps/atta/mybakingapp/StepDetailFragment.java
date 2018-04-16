package com.examples.apps.atta.mybakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.apps.atta.mybakingapp.model.Recipe;
import com.examples.apps.atta.mybakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.examples.apps.atta.mybakingapp.RecipeDetailsActivity.STEPS_BUNDLE;
import static com.examples.apps.atta.mybakingapp.RecipeDetailsActivity.STEP_INDEX;
import static com.examples.apps.atta.mybakingapp.MainActivity.RECIPES_BUNDLE;

public class StepDetailFragment extends Fragment {

    ArrayList<Step> steps;
    @BindView(R.id.step_description_tv)
    TextView stepDescriptiontv;
    @BindView(R.id.next_step_btn)
    Button nextStepBtn;
    @BindView(R.id.previous_step_btn)
    Button previousStepBtn;
    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.noVideo_tv)
    TextView emptyVideotv;

    private SimpleExoPlayer mExoPlayer;
    private int stepIndex;
    private StepClickListener stepClickListener;


    public StepDetailFragment() {
    }

    public interface StepClickListener{
        void onStepClick(ArrayList<Step> steps , int stepIndex);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
             , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_step_detail,container,false);

        ButterKnife.bind(this,rootview);


        stepClickListener = (StepClickListener) getActivity();


        if (savedInstanceState != null){
            steps = (ArrayList<Step>) savedInstanceState.getSerializable(STEPS_BUNDLE);
            stepIndex = savedInstanceState.getInt(STEP_INDEX);
        }else {
            steps = (ArrayList<Step>) getArguments().getSerializable(STEPS_BUNDLE);
            if (steps!=null){
                stepIndex = getArguments().getInt(STEP_INDEX);
            }else {
                steps = (ArrayList<Step>) ((ArrayList<Recipe>)
                        getArguments().getSerializable(RECIPES_BUNDLE)).get(0).getSteps();
                stepIndex = 0;
            }

        }

        Log.i(getString(R.string.step_index) ,String.valueOf(stepIndex ));

        stepDescriptiontv.setText(steps.get(stepIndex).getDescription());
        String imageUrl = steps.get(stepIndex).getThumbnailURL();


        String videoUrl = steps.get(stepIndex).getVideoURL();

        if (videoUrl != ""){
            initializePlayer(Uri.parse(videoUrl));
        }else if (imageUrl!=""){
            initializePlayer(Uri.parse(imageUrl));
        }else {
            mPlayerView.setVisibility(View.GONE);
            emptyVideotv.setVisibility(View.VISIBLE);
        }

        nextStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lastIndex = steps.size() -1 ;
                if (steps.get(stepIndex).getId() < steps.get(lastIndex).getId()){
                    if (mExoPlayer != null){
                        mExoPlayer.stop();
                    }
                    stepClickListener.onStepClick(steps,stepIndex + 1);
                }else {
                    Toast.makeText(getActivity() , "This is the last step",Toast.LENGTH_SHORT).show();
                }
            }
        });

        previousStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (steps.get(stepIndex).getId() > 0){
                    if (mExoPlayer != null){
                        mExoPlayer.stop();
                    }
                    stepClickListener.onStepClick(steps,stepIndex - 1);
                }else {
                    Toast.makeText(getActivity() , "This is the first step",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootview;
    }

    private void initializePlayer(Uri videoUri) {
        if (mExoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getActivity(), "My Baking app");
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STEPS_BUNDLE,steps);
        outState.putInt(STEP_INDEX , stepIndex);
    }
}
