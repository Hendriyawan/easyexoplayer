package com.hdev.exoplayer.renderer;


import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.audio.AudioProcessor;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.TeeAudioProcessor;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;

import java.util.ArrayList;

public class RendererFactory extends DefaultRenderersFactory {
    private TeeAudioProcessor.AudioBufferSink listener;

    public RendererFactory(Context context, TeeAudioProcessor.AudioBufferSink listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void buildAudioRenderers(Context context, int extensionRendererMode, MediaCodecSelector mediaCodecSelector, @Nullable DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys, AudioProcessor[] audioProcessors, Handler eventHandler, AudioRendererEventListener eventListener, ArrayList<Renderer> out) {
        AudioProcessor[] audioProcessor = new AudioProcessor[1];
        for (int i = 0; i < audioProcessor.length; i++) {
            audioProcessor[i] = new TeeAudioProcessor(listener);
        }
        super.buildAudioRenderers(context, extensionRendererMode, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, audioProcessors, eventHandler, eventListener, out);
    }
}

