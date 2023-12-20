package com.example.triggyandmoo;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer backgroundMusic;
    private static final String PREF_KEY_BACKGROUND_MUSIC_PLAYING = "background_music_playing";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            // ตรวจสอบสถานะการเล่นเพลงใน SharedPreferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean isBackgroundMusicPlaying = prefs.getBoolean(PREF_KEY_BACKGROUND_MUSIC_PLAYING, false);

            if (backgroundMusic == null || !backgroundMusic.isPlaying()) {
                playBackgroundMusic(R.raw.sound2);
            }

            // เริ่มเล่นเพลงถ้าสถานะบอกว่ากำลังเล่น
            if (isBackgroundMusicPlaying) {
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
            if (backgroundMusic != null) {
                backgroundMusic.release();
                backgroundMusic = null;

                // เคลียร์สถานะการเล่นเพลงใน SharedPreferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(PREF_KEY_BACKGROUND_MUSIC_PLAYING, false);
                editor.apply();
            }
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            stopBackgroundMusic();
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

}