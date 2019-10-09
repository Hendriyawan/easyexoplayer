package com.hdev.exoplayer;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.TeeAudioProcessor;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.AssetDataSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.hdev.exoplayer.data.AudioData;
import com.hdev.exoplayer.renderer.RendererFactory;
import com.hdev.exoplayer.util.DownloadUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;

/**
 * @author gdev (Hendriyawan)
 * craated at 23 sept 2019
 */
public class BasePlayer {

    protected SimpleExoPlayer exoPlayer;
    protected HashMap<String, String> metaData;

    //exo player from single local file uri
    protected void prepareExoPlayerFromFileUri(Context context, Uri uri, ExoPlayer.EventListener eventListener) {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(), new DefaultLoadControl());
        exoPlayer.addListener(eventListener);

        DataSource.Factory fileDataSource = FileDataSource::new;
        MediaSource mediaSource = new ExtractorMediaSource.Factory(fileDataSource).createMediaSource(uri);
        exoPlayer.prepare(mediaSource);

    }

    //build media source from single file asset folder
    protected void prepareExoPlayerFromFileAssetUri(Context context, Uri uri, ExoPlayer.EventListener eventListener) {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(), new DefaultLoadControl());
        exoPlayer.addListener(eventListener);

        DataSource.Factory assetDataSource = () -> new AssetDataSource(context);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(assetDataSource).createMediaSource(uri);
        exoPlayer.prepare(mediaSource);

    }

    //exo player from URL
    //popular audiofile types (mp3, m4a..)
    protected void prepareExoPlayerFromURL(Context context, List<AudioData> audioDataList, ExoPlayer.EventListener eventListener) {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        exoPlayer.addListener(eventListener);

        DefaultDataSourceFactory defaultDataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, BuildConfig.APPLICATION_ID));
        //for support offline mode
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(DownloadUtils.getCache(context), defaultDataSourceFactory, CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();

        for (AudioData audioData : audioDataList) {
            Uri uri = audioData.getUri();
            MediaSource mediaSource = new ExtractorMediaSource.Factory(cacheDataSourceFactory).createMediaSource(uri);
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        exoPlayer.prepare(concatenatingMediaSource);

    }

    //exo player from raw source
    protected void prepareExoPlayerFromRawResourceUri(Context context, Uri uri, ExoPlayer.EventListener eventListener) {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(), new DefaultLoadControl());
        exoPlayer.addListener(eventListener);

        DataSource.Factory rawDataSource = () -> new RawResourceDataSource(context);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(rawDataSource).createMediaSource(uri);
        exoPlayer.prepare(mediaSource);
    }

    //exo player from list local files
    protected void prepareExoPlayerFromListLocalFile(Context context, List<AudioData> audioDataList, ExoPlayer.EventListener eventListener) {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector(), new DefaultLoadControl());
        exoPlayer.addListener(eventListener);

        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        for (AudioData audioData : audioDataList) {
            Uri uri = audioData.getUri();
            DataSource.Factory fileDataSource = FileDataSource::new;
            MediaSource mediaSource = new ExtractorMediaSource.Factory(fileDataSource).createMediaSource(uri);
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        exoPlayer.prepare(concatenatingMediaSource);
    }

    //exo player from list asset file
    protected void prepareExoPlayerFromListAssetFile(Context context, List<AudioData> audioDataList, ExoPlayer.EventListener eventListener) {
        RendererFactory rendererFactory = new RendererFactory(context, new TeeAudioProcessor.AudioBufferSink() {
            @Override
            public void flush(int sampleRateHz, int channelCount, int encoding) {

            }

            @Override
            public void handleBuffer(ByteBuffer buffer) {

            }
        });
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context, rendererFactory, new DefaultTrackSelector(), new DefaultLoadControl());
        exoPlayer.addListener(eventListener);

        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        for (AudioData audioData : audioDataList) {
            Uri uri = audioData.getUri();
            DataSource.Factory assetDataSource = () -> new AssetDataSource(context);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(assetDataSource).createMediaSource(uri);
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        exoPlayer.prepare(concatenatingMediaSource);
    }
}