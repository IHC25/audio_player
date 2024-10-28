package com.imran.audioplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    ListView songList;
    private int pausedPosition = 0;

    ArrayList< HashMap<String,String> > songsCollection = new ArrayList<>();
    HashMap<String,String> hashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        songList = findViewById(R.id.songList);

        hashMap = new HashMap<>();
        hashMap.put("songName","Onno Groher Chad");
        hashMap.put("songDetails","Onno Groher Chand | অন্য গ্রহের চাঁদ Artist - Sohan Ali Cover - Tahsin Ariyan #onnogroherchand #tahsinariyan #cover.");
        hashMap.put("songCover","@drawable/onno_groher_chad_img");
        hashMap.put("songAudio","@raw/onno_groher_chad");

        songsCollection.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("songName","Amar sonar Bangla");
        hashMap.put("songDetails","Amar sonar bangla James ♪ আমার সোনার বাংলা by জেমস James আমার সোনার বাংলা #james");
        hashMap.put("songCover","@drawable/sonar_bangla_james");
        hashMap.put("songAudio","@raw/amarsonarbangla_james");

        songsCollection.add(hashMap);

        hashMap = new HashMap<>();
        hashMap.put("songName","Ami Shunechi Shedin");
        hashMap.put("songDetails","আমি শুনেছি সেদিন তুমি - মৌসুমী ভৌমিক. Ami Sunechi Shedin Tumi - Moushumi Bhowmik. আমি শুনেছি সেদিন তুমি সাগরের ঢেউয়ে চেপে নীলজল দিগন্ত ছুঁয়ে এসেছো আমি শুনেছি সেদিন");
        hashMap.put("songCover","@drawable/ami_shunechi_cover");
        hashMap.put("songAudio","@raw/ami_shunechi_shedin");

        songsCollection.add(hashMap);


        MyAdapter myAdapter = new MyAdapter();
        songList.setAdapter(myAdapter);


    }


    //--------------------------------------------------------------------------------------------------//
    //-------------------------------------Handling Audio Player-----------------------------------------//
    //--------------------------------------------------------------------------------------------------//

    private void handleAudioPlayer(ImageView playImg, int audioResource) {
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
    //===============================================================================================//
    private class MyAdapter extends BaseAdapter {

        LayoutInflater layoutInflator;

        @Override
        public int getCount() {
            return songsCollection.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            layoutInflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = layoutInflator.inflate(R.layout.item,viewGroup,false);

            //Assign Variables
            ImageView songConverImg = myView.findViewById(R.id.songConverImg);
            TextView songName = myView.findViewById(R.id.songName);
            TextView songDetails = myView.findViewById(R.id.songDetails);
            ImageView playImg = myView.findViewById(R.id.playImg);

            HashMap<String,String> hashMap = songsCollection.get(position);
            songName.setText(hashMap.get("songName"));
            songDetails.setText(hashMap.get("songDetails"));
            playImg.setTag("FINISHED");

            String songCover = hashMap.get("songCover");
            int resourceId = getResources().getIdentifier(songCover.substring(1), "drawable", getPackageName());
            songConverImg.setImageResource(resourceId);

            String songAudio = hashMap.get("songAudio");
            int audioResourceId = getResources().getIdentifier(songAudio.substring(1), "raw", getPackageName());


            playImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleAudioPlayer(playImg,audioResourceId);
                    // Change other PlayImg resouce to playicon
                    for (int i = 0; i < songList.getChildCount(); i++) {
                        View otherView = songList.getChildAt(i);
                        ImageView otherPlayImg = otherView.findViewById(R.id.playImg);
                        if (otherPlayImg != playImg) {
                            otherPlayImg.setImageResource(R.drawable.play_icon);
                            otherPlayImg.setTag("FINISHED");
                        }
                    }
                }
            });


            return myView;
        }
    }

}
