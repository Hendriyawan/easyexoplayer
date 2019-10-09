package com.hdev.exoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.hdev.exoplayer.data.AudioData;
import com.hdev.exoplayer.playback.Playback;
import com.hdev.exoplayer.util.PlayerUtils;

import java.util.HashMap;
import java.util.List;

import static com.hdev.exoplayer.constant.Constant.MEDIA_SESSION_TAG;

/**
 * @author gdev (Hendriyawan) 23 sept 2019
 * EasyExoPlayer, make it easy with ExoPlayer
 */

public class EasyExoPlayer extends BasePlayer {
    private static EasyExoPlayer instance;
    private Context context;
    private SeekBar seekBar;
    private ImageButton buttonPlay;
    private ImageButton buttonPrev;
    private ImageButton buttonNext;
    private TextView textViewTextCurrentTime;
    private TextView textViewTextEndTime;
    private boolean isPlaying = false;
    private int currentWindowIndex = 0;
    private int windowCount = 0;
    private int audioSessionId = -1;
    private Playback playback;
    private MediaSessionCompat mediaSessionCompat;

    //ExoPlayer Event Listener
    private ExoPlayer.EventListener eventListener = new ExoPlayer.EventListener() {

        @Override
        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
            PlayerUtils.debug("onTimeLineChanged");
            setProgress();
            //get information current window index and window count
            currentWindowIndex = exoPlayer.getCurrentWindowIndex();
            windowCount = timeline.getWindowCount();

            if(playback != null){
                playback.onTimeLineChanged(currentWindowIndex);
            }
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            PlayerUtils.debug("onPlayerStateChanged");
            PlayerUtils.debug("current window " + String.valueOf(currentWindowIndex));
            PlayerUtils.debug("window count " + String.valueOf(windowCount));

            audioSessionId = exoPlayer.getAudioSessionId();
            getAudioSessionId();

            switch (playbackState) {
                case Player.STATE_ENDED:
                    //we're stop it and return to start position / 0
                    setPlayPause(false);
                    exoPlayer.seekTo(0);
                    if (seekBar != null) seekBar.setProgress(0);
                    break;

                case ExoPlayer.STATE_READY:
                    setProgress();
                    break;

                case ExoPlayer.STATE_BUFFERING:
                    //do nothing
                    break;

                case ExoPlayer.STATE_IDLE:
                    break;
            }

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            PlayerUtils.debug("onTrackChanged");
            if(playback != null){
                playback.onTrackChanged(currentWindowIndex);
            }
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            PlayerUtils.debug("discontinuity");
            if(playback != null){
                playback.onTimeLineChanged(currentWindowIndex);
            }
        }
    };

    //constructor
    private EasyExoPlayer(Context context) {
        this.context = context;
    }

    public static EasyExoPlayer getInstance(Context context) {
        if (instance == null) {
            instance = new EasyExoPlayer(context);
        }
        return instance;
    }

    public void setPlayback(Playback playback) {
        this.playback = playback;
    }

    /**
     * <h3>Build MediaSource from single local file</h3>
     *
     * @param uri           Uri file
     * @param playWhenReady method will start the media when it is ready.
     *                      if the player is already in the ready state then this method can be used to pause and resume playback
     */
    public void mediaSourceSingleLocalFileUri(Uri uri, boolean playWhenReady) {
        isPlaying = playWhenReady;
        prepareExoPlayerFromFileUri(context, uri, eventListener);
        createMediaControl(buttonPlay);
        createMediaControl(buttonPlay, textViewTextCurrentTime, textViewTextEndTime);
        createMediaControl(seekBar, buttonPlay, textViewTextCurrentTime, textViewTextEndTime);
        createMediaControl(seekBar, buttonPlay, buttonPrev, buttonNext, textViewTextCurrentTime, textViewTextEndTime);

    }

    /**
     * <h3>Build MediaSource from single file Assets folder</h3>
     *
     * @param uri           uri /path/to/filename, e.g /song/nice_to_meet_you.mp3
     * @param playWhenReady method will start the media when it is ready.
     *                      if the player is already in the ready state then this method can be used to pause and resume playback
     */
    public void mediaSourceSingleAssetFileUri(Uri uri, boolean playWhenReady) {
        isPlaying = playWhenReady;
        prepareExoPlayerFromFileAssetUri(context, uri, eventListener);
        createMediaControl(buttonPlay);
        createMediaControl(seekBar, buttonPlay, textViewTextCurrentTime, textViewTextEndTime);
        createMediaControl(buttonPlay, textViewTextCurrentTime, textViewTextEndTime);
        createMediaControl(seekBar, buttonPlay, buttonPrev, buttonNext, textViewTextCurrentTime, textViewTextEndTime);
    }

    /**
     * <h3>Build MediaSource from URL</h3><br>
     * <p>popular audio file types (mp3, m4a..)</p>
     *
     * @param audioDataList AudioData list
     * @param playWhenReady method will start the media when it is ready.
     *                      if the player is already in the ready state then this method can be used to pause and resume playback
     */
    public void mediaSourceURL(List<AudioData> audioDataList, boolean playWhenReady) {
        isPlaying = playWhenReady;
        prepareExoPlayerFromURL(context, audioDataList, eventListener);
        createMediaControl(buttonPlay);
        createMediaControl(seekBar, buttonPlay, textViewTextCurrentTime, textViewTextEndTime);
        createMediaControl(buttonPlay, textViewTextCurrentTime, textViewTextEndTime);
        createMediaControl(seekBar, buttonPlay, buttonPrev, buttonNext, textViewTextCurrentTime, textViewTextEndTime);
    }

    /**
     * <h3>Build MediaSource from raw resource file</h3><br>
     *
     * @param rawResource   raw resource
     * @param playWhenReady method will start the media when it is ready.
     *                      if the player is already in the ready state then this method can be used to pause and resume playback
     */
    public void mediaSourceRawUri(int rawResource, boolean playWhenReady) {
        isPlaying = playWhenReady;
        prepareExoPlayerFromRawResourceUri(context, RawResourceDataSource.buildRawResourceUri(rawResource), eventListener);
        createMediaControl(buttonPlay);
        createMediaControl(seekBar, buttonPlay, textViewTextCurrentTime, textViewTextEndTime);
        createMediaControl(buttonPlay, textViewTextCurrentTime, textViewTextEndTime);
        createMediaControl(seekBar, buttonPlay, buttonPrev, buttonNext, textViewTextCurrentTime, textViewTextEndTime);
    }


    /**
     * <h3>Build MediaSource from list local file</h3><br>
     *
     * @param audioDataList list audio data
     * @param playWhenReady method will start the media when it is ready.
     *                      if the player is already in the ready state then this method can be used to pause and resume playback
     */
    public void mediaSourceListLocalFile(List<AudioData> audioDataList, boolean playWhenReady) {
        isPlaying = playWhenReady;
        prepareExoPlayerFromListLocalFile(context, audioDataList, eventListener);
        createMediaControl(buttonPlay);
        createMediaControl(seekBar, buttonPlay, textViewTextCurrentTime, textViewTextEndTime);
        createMediaControl(buttonPlay, textViewTextCurrentTime, textViewTextEndTime);
        createMediaControl(seekBar, buttonPlay, buttonPrev, buttonNext, textViewTextCurrentTime, textViewTextEndTime);
    }

    /**
     * <h3>Build MediaSource from list asset file</h3><br>
     *
     * @param audioDataList list audio data
     * @param playWhenReady method will start the media when it is ready.
     *                      if the player is already in the ready state then this method can be used to pause and resume playback
     */
    public void mediaSourceListAssetFile(List<AudioData> audioDataList, boolean playWhenReady) {
        isPlaying = playWhenReady;
        prepareExoPlayerFromListAssetFile(context, audioDataList, eventListener);
        createMediaControl(buttonPlay);
        createMediaControl(seekBar, buttonPlay, textViewTextCurrentTime, textViewTextEndTime);
        createMediaControl(buttonPlay, textViewTextCurrentTime, textViewTextEndTime);
        createMediaControl(seekBar, buttonPlay, buttonPrev, buttonNext, textViewTextCurrentTime, textViewTextEndTime);
    }


    /**
     * <h3>Create Media Control</h3><br>
     *
     * @param buttonPlay ImageButton button play/pause
     */
    public void createMediaControl(ImageButton buttonPlay) {
        this.buttonPlay = buttonPlay;
        initButton();
        setProgress();
        setPlayPause(isPlaying);
    }


    /**
     * <h3>Create Media Control</h3><br>
     *
     * @param seekBar                 SeekBar Progress
     * @param buttonPlay              ImageButton button play/pause
     * @param textViewTextCurrentTime TextView text current time position
     * @param textViewTextEndTime     TextView text end time / duration
     */
    public void createMediaControl(SeekBar seekBar, ImageButton buttonPlay, TextView textViewTextCurrentTime, TextView textViewTextEndTime) {
        this.seekBar = seekBar;
        this.buttonPlay = buttonPlay;
        this.textViewTextCurrentTime = textViewTextCurrentTime;
        this.textViewTextEndTime = textViewTextEndTime;

        initSeekBar();
        initButton();
        setProgress();
        setPlayPause(isPlaying);
    }

    /**
     * <h3>Create Media Control</h3><br>
     *
     * @param buttonPlay              ImageButton button play/pause
     * @param textViewTextCurrentTime TextView text current time position
     * @param textViewTextEndTime     TextView text end time / duration
     */
    public void createMediaControl(ImageButton buttonPlay, TextView textViewTextCurrentTime, TextView textViewTextEndTime) {
        this.buttonPlay = buttonPlay;
        this.textViewTextCurrentTime = textViewTextCurrentTime;
        this.textViewTextEndTime = textViewTextEndTime;

        initButton();
        setProgress();
        setPlayPause(isPlaying);
    }

    /**
     * <h3>Create Media Control</h3>
     *
     * @param seekBar                 SeekBar of Progress
     * @param buttonPlay              ImageButton button play/pause
     * @param buttonPrev              ImageButton button previous
     * @param buttonNext              ImageButton button next
     * @param textViewTextCurrentTime TextView text current time position
     * @param textViewTextEndTime     TextView text end time / duration
     */
    public void createMediaControl(SeekBar seekBar, ImageButton buttonPlay, ImageButton buttonPrev, ImageButton buttonNext, TextView textViewTextCurrentTime, TextView textViewTextEndTime) {
        this.seekBar = seekBar;
        this.buttonPlay = buttonPlay;
        this.buttonPrev = buttonPrev;
        this.buttonNext = buttonNext;
        this.textViewTextCurrentTime = textViewTextCurrentTime;
        this.textViewTextEndTime = textViewTextEndTime;

        initSeekBar();
        initButton();
        setProgress();
        setPlayPause(isPlaying);

    }


    /**
     * <b>start play media</b>
     */
    public void play() {
        setPlayPause(true);
    }

    /**
     * <b>play media source by window index position</b>
     *
     * @param windowIndex window index position
     */
    public void play(int windowIndex) {
        if (exoPlayer != null) {
            exoPlayer.seekTo(windowIndex, 0);
            setPlayPause(true);
        }
    }

    /**
     * <b>set pause media</b>
     */
    public void pause() {
        setPlayPause(false);
    }

    /**
     * <b>set to stop</b>
     */
    public void stop() {
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    /**
     * retrieve status playing of Media
     *
     * @return True if is playing
     */
    public boolean getPlaying() {
        if (exoPlayer != null) {
            return exoPlayer.getPlayWhenReady();
        }
        return false;
    }

    /**
     * get audio session ID
     */
    public int getAudioSessionId() {
        return audioSessionId;
    }

    public SimpleExoPlayer getPlayer() {
        return exoPlayer;
    }

    /**
     * get status window index playing position
     *
     * @return index
     */
    public int getCurrentWindowIndex() {
        return currentWindowIndex;
    }

    /**
     * retrieve MetaData from media source
     * contains <b>ARTIST, ALBUM, DATE, AUTHOR etc.</b>
     *
     * @return HashMap metadata
     */
    public HashMap<String, String> getMetaData() {
        return metaData;
    }

    //init SeekBar
    private void initSeekBar() {
        if (seekBar != null) {
            seekBar.requestFocus();
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    if (!fromUser) {
                        return;
                    }
                    exoPlayer.seekTo(progress * 1000);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            //we've always to check if ExoPlayer is null
            long duration = exoPlayer != null ? exoPlayer.getDuration() / 1000 : 0;
            seekBar.setMax((int) duration);
        }
    }

    //init play pause button
    private void initButton() {
        //button play / pause
        if (buttonPlay != null) {
            buttonPlay.requestFocus();
            buttonPlay.setOnClickListener((view -> {
                setPlayPause(!isPlaying);
            }));
        }

        //button previous
        if (buttonPrev != null) {
            buttonPrev.requestFocus();
            buttonPrev.setOnClickListener((view -> {
                if (exoPlayer != null) {
                    if (currentWindowIndex != 0) {
                        exoPlayer.seekTo(currentWindowIndex - 1, 0);
                        setPlayPause(getPlaying());
                    } else {
                        exoPlayer.seekTo(currentWindowIndex, 0);
                        setPlayPause(getPlaying());
                    }
                }
            }));
        }

        //button next
        if (buttonNext != null) {
            buttonNext.setOnClickListener((view -> {
                if (exoPlayer != null) {
                    if (currentWindowIndex == windowCount - 1) {
                        exoPlayer.seekTo(0, 0);
                        setPlayPause(getPlaying());

                    } else {
                        exoPlayer.seekTo(currentWindowIndex + 1, 0);
                        setPlayPause(getPlaying());
                    }
                }
            }));
        }
    }

    /**
     * Skip To Previous
     */
    public void previous() {
        if (exoPlayer != null) {
            if (currentWindowIndex != 0) {
                exoPlayer.seekTo(currentWindowIndex - 1, 0);
                setPlayPause(getPlaying());
            } else {
                exoPlayer.seekTo(currentWindowIndex, 0);
                setPlayPause(getPlaying());
            }
        }
    }

    /**
     * Skip to Next
     */
    public void next() {
        if (exoPlayer != null) {
            if (currentWindowIndex == windowCount - 1) {
                exoPlayer.seekTo(0, 0);
                setPlayPause(getPlaying());

            } else {
                exoPlayer.seekTo(currentWindowIndex + 1, 0);
                setPlayPause(getPlaying());
            }
        }
    }


    // set play pause
    private void setPlayPause(boolean play) {
        if (buttonPlay != null) {
            isPlaying = play;
            if (exoPlayer != null) exoPlayer.setPlayWhenReady(play);
            if (getPlaying()) {
                buttonPlay.setImageResource(android.R.drawable.ic_media_pause);
            } else {
                buttonPlay.setImageResource(android.R.drawable.ic_media_play);
            }
        }
    }

    // set progress
    // we're make sure update SeekBar progress on UI Thread
    private void setProgress() {

        long max = exoPlayer != null ? exoPlayer.getDuration() / 1000 : 0;
        long position = exoPlayer != null ? exoPlayer.getCurrentPosition() : 0;
        long duration = exoPlayer != null ? exoPlayer.getDuration() : 0;

        if (seekBar != null) seekBar.setMax((int) max);
        if (textViewTextCurrentTime != null)
            textViewTextCurrentTime.setText(PlayerUtils.stringForTime((int) position));
        if (textViewTextEndTime != null)
            textViewTextEndTime.setText(PlayerUtils.stringForTime((int) duration));

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                setPlayPause(getPlaying());
                currentWindowIndex = exoPlayer != null ? exoPlayer.getCurrentWindowIndex() : 0;
                if (playback != null) {
                    if (exoPlayer != null) {
                        playback.onPlayerStateChanged(getPlaying(), exoPlayer.getPlaybackState(), currentWindowIndex, exoPlayer.getCurrentPosition());
                    }
                }

                long updateMax = exoPlayer != null ? exoPlayer.getDuration() / 1000 : 0;
                long updateProgress = exoPlayer != null ? exoPlayer.getCurrentPosition() / 1000 : 0;
                if (seekBar != null) seekBar.setMax((int) updateMax);
                if (seekBar != null) seekBar.setProgress((int) updateProgress);
                long updatePosition = exoPlayer != null ? exoPlayer.getCurrentPosition() : 0;
                long updateDuration = exoPlayer != null ? exoPlayer.getDuration() : 0;
                if (textViewTextCurrentTime != null)
                    textViewTextCurrentTime.setText(PlayerUtils.stringForTime((int) updatePosition));
                if (textViewTextEndTime != null)
                    textViewTextEndTime.setText(PlayerUtils.stringForTime((int) updateDuration));
                handler.postDelayed(this, 1000);
            }
        });
    }

    // MediaSessionCompat
    public MediaSessionCompat getMediaSessionCompat() {
        mediaSessionCompat = new MediaSessionCompat(context, MEDIA_SESSION_TAG);
        mediaSessionCompat.setActive(true);
        return mediaSessionCompat;
    }
}