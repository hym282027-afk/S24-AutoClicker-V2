package com.first.autoclick;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;

public class ClickService extends AccessibilityService {
    public static ClickService instance;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean running = false;
    private int index = 0;

    private int[] xs = {300, 500, 700};
    private int[] ys = {500, 700, 900};
    private long[] delays = {100, 500, 1000};

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (!running) return;

            int current = index;
            Path path = new Path();
            path.moveTo(xs[current], ys[current]);

            GestureDescription gesture =
                new GestureDescription.Builder()
                    .addStroke(new GestureDescription.StrokeDescription(
                        path, 0, 50))
                    .build();

            dispatchGesture(gesture, null, null);

            index = (index + 1) % xs.length;
            handler.postDelayed(this, delays[current]);
        }
    };

    public void start(int px, int py, long ms) {
        startMultiple(
            new int[]{px},
            new int[]{py},
            new long[]{ms}
        );
    }

    public void startMultiple(int[] x, int[] y, long[] ms) {
        stop();
        if (x == null || y == null || ms == null ||
            x.length == 0 || x.length != y.length ||
            x.length != ms.length) return;

        xs = x.clone();
        ys = y.clone();
        delays = ms.clone();

        for (int i = 0; i < delays.length; i++) {
            delays[i] = Math.max(50, delays[i]);
        }

        index = 0;
        running = true;
        handler.post(task);
    }

    public void stop() {
        running = false;
        handler.removeCallbacks(task);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {}

    @Override
    public void onInterrupt() {
        stop();
    }

    @Override
    protected void onServiceConnected() {
        instance = this;
    }

    @Override
    public void onDestroy() {
        stop();
        instance = null;
        super.onDestroy();
    }
}
