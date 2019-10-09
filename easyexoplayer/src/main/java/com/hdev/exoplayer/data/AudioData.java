package com.hdev.exoplayer.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author gdev (Hendriyawan) 4 October 2019
 * @see AudioData
 */

public class AudioData {

    private Uri uri;
    private String mediaId;
    private String title;
    private String subtitle;
    private String description;
    private int bitmapResource;
    private Uri iconUri;
    private List<AudioData> audioDataList;

    public AudioData(String uri, String mediaId, String title, String subtitle, String description, int bitmapResource, String iconUri) {
        this.uri = Uri.parse(uri);
        this.mediaId = mediaId;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.bitmapResource = bitmapResource;
        this.iconUri = Uri.parse(iconUri);
    }

    /* get MediaDescriptionCompat for display to Notification */
    public static MediaDescriptionCompat getMediaDescription(Context context, AudioData audioData) {

        Bundle extras = new Bundle();
        Bitmap bitmap = getBitmap(context, audioData.getBitmapResource());
        Bitmap bitmapUri = getBitmap(context, audioData.getIconUri().toString());
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap);
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap);
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, bitmapUri);

        return new MediaDescriptionCompat.Builder()
                .setMediaId(audioData.getMediaId())
                .setMediaUri(audioData.getUri())
                .setIconBitmap(bitmap)
                .setTitle(audioData.getTitle())
                .setSubtitle(audioData.getSubtitle())
                .setDescription(audioData.getDescription())
                .setExtras(extras)
                .setIconUri(audioData.getIconUri())
                .build();
    }

    public static List<AudioData> getAudioData() {
        return getAudioData();
    }

    public void setAudioData(List<AudioData> audioData) {
        this.audioDataList = audioData;
    }

    public static Bitmap getBitmap(Context context, @DrawableRes int bitmapResource) {
        return ((BitmapDrawable) context.getResources().getDrawable(bitmapResource)).getBitmap();
    }

    public static Bitmap getBitmap(Context context, String uriPath) {
        AssetManager assetManager = context.getAssets();
        Bitmap bitmap = null;
        try {
            InputStream inputStream = assetManager.open(uriPath);
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = Uri.parse(uri);
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBitmapResource() {
        return bitmapResource;
    }

    public void setBitmapResource(int bitmapResource) {
        this.bitmapResource = bitmapResource;
    }

    public Uri getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = Uri.parse(iconUri);
    }
}
