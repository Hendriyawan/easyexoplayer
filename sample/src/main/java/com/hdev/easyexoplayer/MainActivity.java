package com.hdev.easyexoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hdev.exoplayer.EasyExoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gdev (Hendriyawan) 9 October 2019
 * EasyExoPlayer sample
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.seekbar_progress_position)
    SeekBar seekBarProgressPosition;
    @BindView(R.id.text_view_current_position)
    TextView textViewCurrentPosition;
    @BindView(R.id.text_view_end_position)
    TextView textViewEndPosition;
    @BindView(R.id.button_media_previous)
    ImageButton imageButtonMediaPrevious;
    @BindView(R.id.button_media_next)
    ImageButton imageButtonMediaNext;
    @BindView(R.id.button_media_play_pause)
    ImageButton imageButtonPlayPause;
    private EasyExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        player = EasyExoPlayer.getInstance(this);
        player.createMediaControl(
                seekBarProgressPosition,
                imageButtonPlayPause,
                imageButtonMediaPrevious,
                imageButtonMediaNext,
                textViewCurrentPosition,
                textViewEndPosition
        );

        player.mediaSourceRawUri(R.raw.jennie_solo, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
    }
}
