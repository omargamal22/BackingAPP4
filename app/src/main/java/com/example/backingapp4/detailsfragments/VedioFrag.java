package com.example.backingapp4.detailsfragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.backingapp4.Adapters.IngredientsAdapter;
import com.example.backingapp4.Events;
import com.example.backingapp4.GlobalBus;
import com.example.backingapp4.R;
import com.example.backingapp4.viewmodels.IngredientViewModel;
import com.example.backingapp4.viewmodels.RecipeCardsViewModel;
import com.example.backingapp4.viewmodels.StepsViewModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class VedioFrag extends Fragment implements Player.EventListener {



    private static final String TAG = "ExoPlayerActivity";

    private static final String KEY_VIDEO_URI = "video_uri";

    PlayerView videoFullScreenPlayer;
    ProgressBar spinnerVideoDetails;

    private IngredientsAdapter ingredientsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private final String LIST_STATE_KEY = "IngredientState";
    private RecipeCardsViewModel recipeCardsViewModel;
    IngredientViewModel ingredientViewModel;
    RecyclerView ings;
    //ImageView imageViewExit;

    String videoUri;
    String DESC , SDESC;
    SimpleExoPlayer player;
    Handler mHandler;
    Bundle savedState;
    boolean playWhenReady;
    long playbackPosition;
    long currentWindow;

    public VedioFrag(){}

    public VedioFrag(StepsViewModel stepsViewModel) {
       DESC = stepsViewModel.getDescription();
       SDESC = stepsViewModel.getShortDescription();
        this.videoUri = stepsViewModel.getVideoURL();
    }

    Runnable mRunnable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vedio_frag,container,false);
        videoFullScreenPlayer = view.findViewById(R.id.video_view);
        spinnerVideoDetails = view.findViewById(R.id.spinnerVideoDetails);
        //setUp();

        ingredientsAdapter = new IngredientsAdapter(getContext());
        ingredientViewModel = new ViewModelProvider(this,new VedioFrag.Myfactory()).get(IngredientViewModel.class);
        GlobalBus.getBus().register(this);
        ings = view.findViewById(R.id.ingredientsCards);
        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        if(savedInstanceState!=null){
            linearLayoutManager.onRestoreInstanceState( savedInstanceState.getParcelable(LIST_STATE_KEY));
            savedState = new Bundle();
            savedState.putLong("PLAYER_CURRENT_POS_KEY",savedInstanceState.getLong("PLAYER_CURRENT_POS_KEY"));
            savedState.putLong("PLAYER_CURRENT_Window_KEY", savedInstanceState.getLong("PLAYER_CURRENT_Window_KEY"));
            savedState.putBoolean("PLAYER_IS_READY_KEY",savedInstanceState.getBoolean("PLAYER_IS_READY_KEY"));
            videoUri = savedInstanceState.getString("uri");
            DESC = savedInstanceState.getString("DESC");
            SDESC = savedInstanceState.getString("SDESC");
        }
        ((TextView)view.findViewById(R.id.fulldes)).setText(DESC);
        ((TextView)view.findViewById(R.id.SHORT)).setText(SDESC);
        return view;
    }

    private void setUp() {
        initializePlayer();
        if (videoUri == null) {
            return;
        }
        buildMediaSource(Uri.parse(videoUri));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        pausePlayer();
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 && player == null)) {
            setUp();
            return;
        }
        resumePlayer();
    }

    private void initializePlayer() {
        if (player == null) {
            // 1. Create a default TrackSelector
            LoadControl loadControl = new DefaultLoadControl(
                    new DefaultAllocator(true, 16),
                    VideoPlayerConfig.MIN_BUFFER_DURATION,
                    VideoPlayerConfig.MAX_BUFFER_DURATION,
                    VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                    VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true);

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            // 2. Create the player
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()), trackSelector, loadControl);
            videoFullScreenPlayer.setPlayer(player);
            if(savedState!=null){
                player.setPlayWhenReady(savedState.getBoolean("PLAYER_IS_READY_KEY"));
                player.seekTo(savedState.getLong("PLAYER_CURRENT_Window_KEY",savedState.getLong("PLAYER_CURRENT_POS_KEY")));
            }
        }


    }

    public void buildMediaSource(Uri mUri) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mUri);
        // Prepare the player with the source.
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        player.addListener(this);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    private void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
    }

    private void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {

            case Player.STATE_BUFFERING:
                spinnerVideoDetails.setVisibility(View.VISIBLE);
                break;
            case Player.STATE_ENDED:
                // Activate the force enable
                break;
            case Player.STATE_IDLE:

                break;
            case Player.STATE_READY:
                spinnerVideoDetails.setVisibility(View.GONE);

                break;
            default:
                // status = PlaybackStatus.IDLE;
                break;
        }

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
    public class VideoPlayerConfig {
        //Minimum Video you want to buffer while Playing
        public static final int MIN_BUFFER_DURATION = 3000;
        //Max Video you want to buffer during PlayBack
        public static final int MAX_BUFFER_DURATION = 5000;
        //Min Video you want to buffer before start Playing it
        public static final int MIN_PLAYBACK_START_BUFFER = 1500;
        //Min video You want to buffer when user resumes video
        public static final int MIN_PLAYBACK_RESUME_BUFFER = 5000;

        public static final String DEFAULT_VIDEO_URL = "https://androidwave.com/media/androidwave-video-exo-player.mp4";
    }

    private void updateAdapter(ArrayList<IngredientViewModel> ingredientViewModels) {

        ings.setLayoutManager(linearLayoutManager);
        ingredientsAdapter.setViewModels(ingredientViewModels);
        ings.setAdapter(ingredientsAdapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE_KEY,linearLayoutManager.onSaveInstanceState());
        outState.putLong("PLAYER_CURRENT_POS_KEY", Math.max(0, playbackPosition));
        outState.putLong("PLAYER_CURRENT_Window_KEY", Math.max(0, currentWindow));
        outState.putBoolean("PLAYER_IS_READY_KEY", playWhenReady);
        outState.putString("uri",videoUri);
        outState.putString("DESC",DESC);
        outState.putString("SDESC",SDESC);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23&& player == null) {
            setUp();
            return;
        }
        resumePlayer();
    }

    @Subscribe(sticky = true  )
    public void ReciveViewModel(Events.ActivityActivityMessage event){
        recipeCardsViewModel=(RecipeCardsViewModel) event.getMessage();
        ingredientViewModel.MakeViewModel(recipeCardsViewModel.ingredients);
        ingredientViewModel.getArrayListMutableLiveData().observe(this, new Observer<ArrayList<IngredientViewModel>>() {
            @Override
            public void onChanged(ArrayList<IngredientViewModel> ingredientViewModels) {
                updateAdapter(ingredientViewModels);
                //ingredientsAdapter.setViewModels(ingredientViewModels);

            }
        });
    }

    class Myfactory implements ViewModelProvider.Factory {
        public Myfactory() {
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new IngredientViewModel();
        }
    }
}
