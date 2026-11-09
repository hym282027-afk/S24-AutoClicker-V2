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
    private int x, y;
    private long interval;
    private final Runnable task = new Runnable() {
        @Override public void run() {
            if (!running) return;
            Path path = new Path(); path.moveTo(x, y);
            GestureDescription.StrokeDescription stroke = new GestureDescription.StrokeDescription(path, 0, 60);
            GestureDescription gesture = new GestureDescription.Builder().addStroke(stroke).build();
            dispatchGesture(gesture, null, null);
            if (running) handler.postDelayed(this, interval);
        }
    };
    public void start(int px, int py, long ms) {
        stop(); x = px; y = py; interval = Math.max(100, ms); running = true; handler.post(task);
    }
    public void stop() { running = false; handler.removeCallbacks(task); }
    @Override public void onAccessibilityEvent(AccessibilityEvent event) {}
    @Override public void onInterrupt() { stop(); }
    @Override protected void onServiceConnected() { instance = this; }
    @Override public void onDestroy() { stop(); instance = null; super.onDestroy(); }
}
