package us.quiztouch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Main timer
 */
public class QuizTimer extends Activity {
    private Handler mHandler = new Handler();
    private long mStartTime = 0L;

    View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            // toggle timer on/off
            if (mStartTime == 0L) {
                mStartTime = SystemClock.uptimeMillis();
                mHandler.removeCallbacks(mUpdateTimeTask);
                mHandler.postDelayed(mUpdateTimeTask, 100);
            } else {
                mHandler.removeCallbacks(mUpdateTimeTask);
                mStartTime = 0L;
            }
        }
    };

    View.OnClickListener mStopListener = new View.OnClickListener() {
        public void onClick(View v) {
            mHandler.removeCallbacks(mUpdateTimeTask);
            mStartTime = 0L;
        }
    };

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            final long start = mStartTime;
            long millis = SystemClock.uptimeMillis() - start;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            TextView mTimeLabel = (TextView) findViewById(R.id.timer_display);

            if (seconds < 10) {
                mTimeLabel.setText("" + minutes + ":0" + seconds);
            } else {
                mTimeLabel.setText("" + minutes + ":" + seconds);
            }

            mHandler.postAtTime(this, start + (((minutes * 60) + seconds + 1) * 1000));
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button tbutton = (Button) findViewById(R.id.timer_button);
        tbutton.setOnClickListener(buttonListener);
    }
}