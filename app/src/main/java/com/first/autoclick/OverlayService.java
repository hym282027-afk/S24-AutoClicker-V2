
package com.first.autoclick;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class OverlayService extends Service {
    private WindowManager wm;
    private final TextView[] markers = new TextView[3];
    private final WindowManager.LayoutParams[] params =
            new WindowManager.LayoutParams[3];

    public static final int[] x = new int[3];
    public static final int[] y = new int[3];

    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        for (int i = 0; i < 3; i++) {
            createMarker(i);
        }
    }

    private void createMarker(final int index) {
        TextView marker = new TextView(this);
        marker.setText(String.valueOf(index + 1));
        marker.setTextColor(Color.WHITE);
        marker.setTextSize(20);
        marker.setTypeface(null, Typeface.BOLD);
        marker.setGravity(Gravity.CENTER);
        marker.setBackgroundColor(
                new int[]{Color.BLUE, Color.GREEN, Color.RED}[index]);

        int type = Build.VERSION.SDK_INT >= 26
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;

        WindowManager.LayoutParams p =
                new WindowManager.LayoutParams(
                        110, 110, type,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

        p.gravity = Gravity.TOP | Gravity.LEFT;
        p.x = 100 + index * 150;
        p.y = 250 + index * 150;

        markers[index] = marker;
        params[index] = p;
        updatePosition(index);

        marker.setOnTouchListener(new View.OnTouchListener() {
            float startX, startY;
            int originalX, originalY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getRawX();
                        startY = event.getRawY();
                        originalX = p.x;
                        originalY = p.y;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        p.x = originalX +
                                (int)(event.getRawX() - startX);
                        p.y = originalY +
                                (int)(event.getRawY() - startY);
                        wm.updateViewLayout(marker, p);
                        updatePosition(index);
                        return true;

                    case MotionEvent.ACTION_UP:
                        updatePosition(index);
                        return true;
                }
                return false;
            }
        });

        wm.addView(marker, p);
    }

    private void updatePosition(int index) {
        x[index] = params[index].x + 55;
        y[index] = params[index].y + 55;
    }

    @Override
    public void onDestroy() {
        for (TextView marker : markers) {
            if (marker != null && marker.getParent() != null) {
                wm.removeView(marker);
            }
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
