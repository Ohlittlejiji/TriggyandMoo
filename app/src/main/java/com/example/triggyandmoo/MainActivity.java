package com.example.triggyandmoo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer backgroundMusic;
    private static final String PREF_KEY_BACKGROUND_MUSIC_PLAYING = "background_music_playing";
    private Dialog infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // ตรวจสอบสถานะการเล่นเพลงใน SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isBackgroundMusicPlaying = prefs.getBoolean(PREF_KEY_BACKGROUND_MUSIC_PLAYING, false);

        if (backgroundMusic == null) {
            playBackgroundMusic(R.raw.sound2);
        }

        // เริ่มเล่นเพลงถ้าสถานะบอกว่ากำลังเล่น
        if (isBackgroundMusicPlaying && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }

    private void playBackgroundMusic(int resourceId) {
        if (backgroundMusic != null) {
            backgroundMusic.release();
        }
        backgroundMusic = MediaPlayer.create(this, resourceId);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();

        // บันทึกสถานะการเล่นเพลงใน SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(PREF_KEY_BACKGROUND_MUSIC_PLAYING, true);
        editor.apply();
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBackgroundMusic();
        if (backgroundMusic != null) {
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }

    public void startGame(View view) {
        stopBackgroundMusic();
        GameView gameView = new GameView(this);
        setContentView(gameView);
        playBackgroundMusic(R.raw.sound);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopBackgroundMusic();
    }


    public void showInfoDialog(View view) {
        // Create a new dialog
        final Dialog infoDialog = new Dialog(this);

        // Set the layout for the dialog
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(R.layout.info_dialog);

        // Set the width to match parent and height to wrap content
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(infoDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        infoDialog.getWindow().setAttributes(layoutParams);

        // Access the Close button and set its click listener
        Button btnClose = infoDialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoDialog.dismiss(); // Dismiss the dialog when the Close button is clicked
            }
        });

        // Show the dialog
        infoDialog.show();
    }








    public void closeInfoDialog(View view) {
        // ปิดป็อปอัพ (ถูกเรียกจากปุ่ม "Close")
        if (infoDialog != null && infoDialog.isShowing()) {
            infoDialog.dismiss();
        }
    }
}
