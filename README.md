# EasyExoPlayer [![](https://jitpack.io/v/Hendriyawan/easyexoplayer.svg)](https://jitpack.io/#Hendriyawan/easyexoplayer)
library to make it easier to use [ExoPlayer](https://exoplayer.dev)

what is [ExoPlayer](https://exoplayer.dev) ? ExoPlayer is an application level media player for Android. It provides an alternative to Android’s MediaPlayer API for playing audio and video both locally and over the Internet. ExoPlayer supports features not currently supported by Android’s MediaPlayer API, including DASH and SmoothStreaming adaptive playbacks. Unlike the MediaPlayer API, ExoPlayer is easy to customize and extend, and can be updated through Play Store application updates.


Usage
-------
to use this library, you must add the jetpack.io repository.
```
repository {
      maven { url "https://jitpack.io" }
}
```

and add dependency
```
dependencies {
    ....
    implementation 'com.github.Hendriyawan:easyexoplayer:versionrelease'
}
```

Example
-------

```
public class MainActivity extends AppCompatActivity {
       private SeekBar seekBarPlayer;
       private ImageButton buttonPlayPause;
       private ImageButton buttonPrevious;
       private ImageButton buttonNext;
       private TextView textCurrentPosition;
       private TextView textEndPosition;
       private EaseExoPlayer player;

       @Override
       protected void onCreate(Bundle savedInstanceState){
       .....

           player = EasyExoPlayer.getInstance(this);
           //create media control
           player.createMediaControl(
               seekBarPlayer,
               buttonPlayPause,
               buttonPrevious,
               buttonNext,
               textCurrentPosition,
               textEndPosition
           );
           //build media source from Raw resource
           player.mediaSourceRawUri(R.raw.some_file_to_play.mp3, true);

       }
}
```
Screenshot
----------
<img src="https://raw.githubusercontent.com/Hendriyawan/easyexoplayer/master/ss_1.png" width="250" />

License
-------
    Copyright 2019 Hendriyawan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

Attribution
-----------
* Uses [ExoPlayer](https://exoplayer.dev) licensed under [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
* Uses [support-media-compat](https://developer.android.com/topic/libraries/support-library/features#media-playback) licensed under [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
