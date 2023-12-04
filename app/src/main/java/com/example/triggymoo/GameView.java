package com.example.triggymoo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {
    Bitmap background, ground, cow;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float TEXT_SIZE = 120;
    int points = 0;
    int life = 3;
    static int dWidth, dHeight;
    Random random;
    float cowX, cowY;
    float oldX;
    float oldCowX;
    ArrayList<Spike> spikes;
    ArrayList<Explosion> explosion;

    public GameView(Context context){
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_cow);
        ground   = BitmapFactory.decodeResource(getResources(), R.drawable.bg_cow);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_cow);
    }
}
