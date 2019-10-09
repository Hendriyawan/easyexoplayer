package com.hdev.exoplayer.playback;

public interface Playback {

    void onPlayerStateChanged(boolean playWhenReady, int playbackState, int windowIndex, long currentPosition);

    void onTimeLineChanged(int currentWindowIndex);

    void onTrackChanged(int currentWindowIndex);
}
