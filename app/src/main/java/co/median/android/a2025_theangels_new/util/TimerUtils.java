package co.median.android.a2025_theangels_new.util;

import android.os.Handler;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;
import java.util.function.LongSupplier;

/** Utility methods for event timer handling. */
public class TimerUtils {
    /** Formats seconds into mm:ss string. */
    public static String formatDuration(long seconds) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format(java.util.Locale.getDefault(), "%02d:%02d", mins, secs);
    }

    /** Starts a timer that updates the given TextView every second. */
    public static void startTimer(TextView view, Handler handler,
                                  LongSupplier startTimeSupplier,
                                  BooleanSupplier runningSupplier,
                                  AtomicLong counter) {
        handler.post(new Runnable() {
            @Override public void run() {
                long elapsed;
                long start = startTimeSupplier.getAsLong();
                if (start > 0) {
                    elapsed = (System.currentTimeMillis() - start) / 1000;
                } else {
                    elapsed = counter.get();
                    if (runningSupplier.getAsBoolean()) counter.incrementAndGet();
                }
                view.setText(formatDuration(elapsed));
                handler.postDelayed(this, 1000);
            }
        });
    }

    /** Stops all callbacks for the given handler. */
    public static void stopTimer(Handler handler) {
        handler.removeCallbacksAndMessages(null);
    }
}
