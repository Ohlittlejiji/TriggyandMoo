package com.example.triggyandmoo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;
import android.graphics.Path;

public class GameView extends View {

    Bitmap background, ground, moo,moo2,moo3,moo4, heart;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Path heartPath = new Path();
    float TEXT_SIZE = 120;
    int points = 0;
    int life = 3;
    static int dWidth, dHeight;
    Random random;
    float mooX, mooY;
    float oldX;
    float oldmooX;
    ArrayList<Spike> spikes;
    ArrayList<Explosion> explosions;
    int randCharIndex;




    public GameView(Context context) {
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        moo = BitmapFactory.decodeResource(getResources(), R.drawable.moo);
        moo2 = BitmapFactory.decodeResource(getResources(), R.drawable.moo2);
        moo3 = BitmapFactory.decodeResource(getResources(), R.drawable.moo3);
        moo4 = BitmapFactory.decodeResource(getResources(), R.drawable.moo4);
        heart = BitmapFactory.decodeResource(getResources(), R.drawable.heart);  // รูปหัวใจ
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        textPaint.setColor(Color.rgb(255, 165, 0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.acade));
        heartPath.moveTo(dWidth - 200 + 20, 30);  // ตำแหน่งเริ่มต้นของหัวใจ
        heartPath.quadTo(dWidth - 200 + 60 + 20, 30, dWidth - 200 + 120 + 20, 30);  // จุดกึ่งกลางของหัวใจ
        heartPath.quadTo(dWidth - 200 + 180 + 20, 30, dWidth - 200 + 240 + 20, 30);  // จุดสุดท้ายของหัวใจ
        heartPath.close();
        random = new Random();
        mooX = dWidth / 2 - moo.getWidth() / 2;
        mooY = dHeight - ground.getHeight() - moo.getHeight();
        spikes = new ArrayList<>();
        explosions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Spike spike = new Spike(context);
            spikes.add(spike);
        }
        randCharIndex = random.nextInt(4);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);

        if(randCharIndex==0){
            canvas.drawBitmap(moo, mooX, mooY, null);
        }else if(randCharIndex==1){
            canvas.drawBitmap(moo2, mooX, mooY, null);
        }else if(randCharIndex==2){
            canvas.drawBitmap(moo3, mooX, mooY, null);
        }else if(randCharIndex==3){
            canvas.drawBitmap(moo4, mooX, mooY, null);
        }

        for (int i = 0; i < spikes.size(); i++) {
            canvas.drawBitmap(spikes.get(i).getSpike(spikes.get(i).spikeFrame), spikes.get(i).spikeX, spikes.get(i).spikeY, null);
            spikes.get(i).spikeFrame=0;
             if(randCharIndex==1){
                 spikes.get(i).spikeFrame = 1;
            }else if(randCharIndex==2){
                 spikes.get(i).spikeFrame = 2;
            }else if(randCharIndex==3){
                 spikes.get(i).spikeFrame = 3;
            }
            spikes.get(i).spikeY += spikes.get(i).spikeVelocity;
            if (spikes.get(i).spikeY + spikes.get(i).getSpikeHeight() >= dHeight - ground.getHeight()) {
                points += 10;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = spikes.get(i).spikeX;
                explosion.explosionY = spikes.get(i).spikeY;
                explosions.add(explosion);
                spikes.get(i).resetPosition();
            }
        }

        for (int i = 0; i < spikes.size(); i++) {
            if (spikes.get(i).spikeX + spikes.get(i).getSpikeWidth() >= mooX
                    && spikes.get(i).spikeX <= mooX + moo.getWidth()
                    && spikes.get(i).spikeY + spikes.get(i).getSpikeWidth() >= mooY
                    && spikes.get(i).spikeY + spikes.get(i).getSpikeWidth() <= mooY + moo.getHeight()) {
                life--;
                spikes.get(i).resetPosition();
                if (life == 0) {
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }

        for (int i = 0; i < explosions.size(); i++) {
            canvas.drawBitmap(explosions.get(i).getExplosion(explosions.get(i).explosionFrame), explosions.get(i).explosionX,
                    explosions.get(i).explosionY, null);
            explosions.get(i).explosionFrame++;
            if (explosions.get(i).explosionFrame > 3) {
                explosions.remove(i);
            }
        }


        if (life == 2) {
            drawHeart(canvas, dWidth - 200 + 20, 30);  // ปรับตำแหน่ง
            drawHeart(canvas, dWidth - 200 + 70 + 20, 30);  // ปรับตำแหน่ง
        } else if (life == 1) {
            drawHeart(canvas, dWidth - 200 + 20, 30);  // ปรับตำแหน่ง
        }
        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    private void drawHeart(Canvas canvas, float x, float y) {
        float heartSize = 60;

        // สร้าง Path สำหรับรูปหัวใจ
        heartPath.reset();
        heartPath.moveTo(x + heartSize / 2, y + heartSize / 4);
        heartPath.quadTo(x, y - heartSize / 2, x - heartSize / 2, y + heartSize / 4);
        heartPath.quadTo(x, y - heartSize / 2, x + heartSize / 2, y + heartSize / 4);
        heartPath.lineTo(x + heartSize / 2, y + heartSize);

        // วาดรูปหัวใจ
        canvas.drawBitmap(heart, null, new Rect((int) x, (int) y, (int) (x + heartSize), (int) (y + heartSize)), null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchY >= mooY){
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN){
                oldX = event.getX();
                oldmooX = mooX;
            }
            if (action == MotionEvent.ACTION_MOVE){
                float shift = oldX - touchX;
                float newMooX = oldmooX - shift;
                if (newMooX <= 0)
                    mooX = 0;
                else if(newMooX >= dWidth - moo.getWidth())
                    mooX = dWidth - moo.getWidth();
                else
                    mooX = newMooX;
            }
        }
        return true;
    }


}
