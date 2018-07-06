package com.example.marcelolongen.snakeblock;

import android.view.View;

/**
 *
 * Created by park on 2018-02-08.
 * This class is a helper to wrap up some of the ugly code needed to
 * initiate an animation thread that repaints a view at regular intervals.
 */
public class AnimationHelper {
    private View view = null;
    private int fps;
    private Thread thread = null;
    private volatile boolean isRunning = false;

    /**
     * Constructs a new drawing thread to update the given view
     * the given number of times per second.
     * Does NOT start the thread running; call start() to do so.
     */
    public AnimationHelper(View view, int fps) {
        if (view == null || fps <= 0) {
            throw new IllegalArgumentException();
        }
        this.view = view;
        this.fps = fps;
    }

    /**
     * Returns true if the drawing thread is currently started and running.
     */
    public boolean isRunning() {
        return thread != null;
    }

    /**
     * Starts the thread running so that it will repaint the view repeatedly.
     */
    public void start() {
        if (thread == null) {
            thread = new Thread(new MainRunner());
            thread.start();
        }
    }

    /**
     * Stops the thread so that it will not repaint the view any more.
     */
    public void stop() {
        if (thread != null) {
            isRunning = false;
            try {
                thread.join();
            } catch (InterruptedException ie) {
                // empty
            }
            thread = null;
        }
    }

    /*
     * Small runnable helper class that contains the thread's main loop
     * to repeatedly sleep-and-redraw the view.
     */
    private class MainRunner implements Runnable {
        public void run() {
            isRunning = true;
            while (isRunning) {
                // sleep for a short time between frames of animation
                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException ie) {
                    isRunning = false;
                }

                // might have been stopped while sleeping
                if (!isRunning) {
                    break;
                }

                // the view to redraw
                view.postInvalidate();
            }
        }
    }
}
