package com.hdev.exoplayer.playback;

import android.support.v4.media.session.PlaybackStateCompat;

import com.hdev.exoplayer.EasyExoPlayer;

public interface AudioServicePlayback {

    void onInitPlayer(EasyExoPlayer player);

    void onInitPlayerUI(EasyExoPlayer player);

    void onPlaybackStateChanged(PlaybackStateCompat playbackStateCompat);
}
