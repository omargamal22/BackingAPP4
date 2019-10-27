package com.example.backingapp4.DialogBOX;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.backingapp4.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class Custm_Dialog_Box extends Dialog implements
        android.view.View.OnClickListener  {

    public Activity activity;
    public Dialog dialog;
    public ImageButton Exit;
    public PlayerView playerView;
    public SimpleExoPlayer player;
    public String URI;

    public Custm_Dialog_Box(@NonNull Activity activity,String URI) {
        super(activity);
        this.activity = activity;
        this.URI = URI;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vedio_frag);
        //Exit =(ImageButton) findViewById(R.id.Exit);
        Exit.setOnClickListener(this);
        playerView = findViewById(R.id.video_view);

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.release();
    }

    /*@Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }*/

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(activity),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(true);
        player.seekTo(0, 0);
        Uri uri = Uri.parse(URI);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, true);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("BackingAPP4")).
                createMediaSource(uri);
    }
}
