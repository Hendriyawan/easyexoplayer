package com.hdev.exoplayer.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.hdev.exoplayer.R;
import com.hdev.exoplayer.data.AudioData;

import java.util.List;

import static com.hdev.exoplayer.constant.Constant.PLAYBACK_CHANNEL_ID;
import static com.hdev.exoplayer.constant.Constant.PLAYBACK_NOTIFICATION_ID;

public class NotificationUtils {

    public static void createNotificationController(Service service, PendingIntent pendingIntent, SimpleExoPlayer exoPlayer, MediaSessionCompat mediaSessionCompat, List<AudioData> audioDataList) {
        PlayerNotificationManager playerNotificationManager = PlayerNotificationManager
                .createWithNotificationChannel(service.getApplicationContext(),
                        PLAYBACK_CHANNEL_ID,
                        R.string.playback_channel_name,
                        PLAYBACK_NOTIFICATION_ID,
                        new PlayerNotificationManager.MediaDescriptionAdapter() {
                            @Override
                            public String getCurrentContentTitle(Player player) {
                                return audioDataList.get(player.getCurrentWindowIndex()).getTitle();
                            }

                            @Nullable
                            @Override
                            public PendingIntent createCurrentContentIntent(Player player) {
                                if (pendingIntent != null) {
                                    return pendingIntent;
                                }
                                return null;
                            }

                            @Nullable
                            @Override
                            public String getCurrentContentText(Player player) {
                                return audioDataList.get(player.getCurrentWindowIndex()).getSubtitle();
                            }

                            @Nullable
                            @Override
                            public String getCurrentSubText(Player player) {
                                return audioDataList.get(player.getCurrentWindowIndex()).getDescription();
                            }

                            @Nullable
                            @Override
                            public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                                String uriPath = audioDataList.get(player.getCurrentWindowIndex()).getIconUri().toString();
                                return PlayerUtils.getBitmap(service.getApplicationContext(), uriPath);
                            }
                        });
        playerNotificationManager.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {
                service.startForeground(notificationId, notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                service.stopSelf();
            }
        });
        playerNotificationManager.setPlayer(exoPlayer);
        playerNotificationManager.setMediaSessionToken(mediaSessionCompat.getSessionToken());
    }
}
