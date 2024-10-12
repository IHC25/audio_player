package com.imran.audioplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    ImageView playImg1, playImg2, playImg3;
    MediaPlayer mediaPlayer;
    private int pausedPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Assign Variables
        playImg1 = findViewById(R.id.playImg1);
        playImg2 = findViewById(R.id.playImg2);
        playImg3 = findViewById(R.id.playImg3);

        // Set initial tags to avoid null issues
        playImg1.setTag("FINISHED");
        playImg2.setTag("FINISHED");
        playImg3.setTag("FINISHED");

        //Set OnClick Listener
        playImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAudioPlayer(playImg1, new ImageView[]{playImg2,playImg3}, R.raw.onno_groher_chad);
            }
        });

        playImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAudioPlayer(playImg2, new ImageView[]{playImg1,playImg3}, R.raw.amarsonarbangla_james);
            }
        });

        playImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAudioPlayer(playImg3, new ImageView[]{playImg1,playImg2}, R.raw.ami_shunechi_shedin);
            }
        });
    }


    //--------------------------------------------------------------------------------------------------//
    //-------------------------------------Handling Audio Player-----------------------------------------//
    //--------------------------------------------------------------------------------------------------//

    private void handleAudioPlayer(ImageView playImg, ImageView[] otherPlayImgs, int audioResource) {
        String currentTag = (String) playImg.getTag(); // Ensure it's cast to String to avoid class cast exceptions

        // Use switch to handle different states (PAUSED, PLAY, FINISHED)
        switch (currentTag) {
            case "PAUSED":
                // If the song is in PAUSED state, resume it from the paused position
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(MainActivity.this, audioResource); // Create a new MediaPlayer for the selected song
                } else {
                    // Resume from where the song was paused
                    mediaPlayer.seekTo(pausedPosition);
                }
                // Start the song
                mediaPlayer.start();

                // Update the tag to PLAY and change the icon
                playImg.setTag("PLAY");
                playImg.setImageResource(R.drawable.pause_icon);

                // Reset all other play buttons (other songs) to the FINISHED state
                for (ImageView otherPlayImg : otherPlayImgs) {
                    otherPlayImg.setTag("FINISHED");
                    otherPlayImg.setImageResource(R.drawable.play_icon);
                }
                break;

            case "PLAY":
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause(); // Pause the song
                    pausedPosition = mediaPlayer.getCurrentPosition(); // Save the current position
                }
                playImg.setTag("PAUSED"); // Update the tag to PAUSED
                playImg.setImageResource(R.drawable.play_icon); // Change the icon to indicate paused state
                break;

            case "FINISHED":
                // If the song has finished, start it from the beginning
                if (mediaPlayer != null) {
                    mediaPlayer.release(); // Release any existing MediaPlayer instance
                }
                mediaPlayer = MediaPlayer.create(MainActivity.this, audioResource); // Create a new MediaPlayer for the selected song
                mediaPlayer.start(); // Start playing the song

                // Update the tag to PLAY and change the icon to indicate playing state
                playImg.setTag("PLAY");
                playImg.setImageResource(R.drawable.pause_icon);

                // Reset all other play buttons to FINISHED state
                for (ImageView otherPlayImg : otherPlayImgs) {
                    otherPlayImg.setTag("FINISHED");
                    otherPlayImg.setImageResource(R.drawable.play_icon);
                }

                // Handle when the song finishes playing
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.release(); // Release MediaPlayer when the song is done
                        playImg.setImageResource(R.drawable.play_icon); // Reset to play icon
                        playImg.setTag("FINISHED"); // Set tag to FINISHED
                        pausedPosition = 0; // Reset paused position
                    }
                });
                break;
        }
    }

}
