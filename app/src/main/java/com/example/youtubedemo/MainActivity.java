package com.example.youtubedemo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    Button openVideoButton;
    EditText youtubeHashText;
    YouTubePlayerView youTubePlayerView;
    String API_KEY = "YOUR_API_KEY";
    private static final int RECOVERY_REQUEST = 1;
    String youtubeHash;
    YouTubePlayer youTubePlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        openVideoButton = findViewById(R.id.open_video_button);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        youtubeHashText = findViewById(R.id.enter_youtube_thumbnail_editText);

        youtubeHashText.addTextChangedListener(youtubeHashTextWatcher);

        // Initialize youtubePlayerView and initialize youtubePlayer
        youTubePlayerView.initialize(API_KEY, this);

        // When clicked openVideoButton, opens a youtube video with user entered thumbnail
        openVideoButton.setOnClickListener(v -> {

                    //If youTubePlayer is initialized and user has entered a video thumbnail cue the video and clear user's enter
                    if (youtubeHash != null && youTubePlayer != null) {
                        youTubePlayer.cueVideo(youtubeHash);
                        youtubeHashText.getText().clear();
                    }
                    // Else toast a message to the user
                    else {
                        Toast.makeText(this, "You did not enter any thumbnail", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    // Update youtubePlayer from null to youtubePlayer
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        this.youTubePlayer = youTubePlayer;
    }

    // If youtube initialization fails, gives an error and toast a message
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            Toast.makeText(this, "Error occurred while initializing Youtube", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            getYouTubePlayerProvider().initialize(API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubePlayerView;
    }

    //If video hash is filled, then open video button enables
    private final TextWatcher youtubeHashTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        // Update youtubeHash with user's entered EditText
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            youtubeHash = youtubeHashText.getText().toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}