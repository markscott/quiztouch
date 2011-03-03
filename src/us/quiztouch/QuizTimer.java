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
    private Handler handler = new Handler();
    private long startTime = 0L;
    int timeLimit = 30;

    View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View v) {
            Button button = (Button) findViewById(R.id.timer_button);
            // toggle timer on/off
            if (startTime == 0L) {
                startTime = SystemClock.uptimeMillis();
                handler.removeCallbacks(mUpdateTimeTask);
                handler.postDelayed(mUpdateTimeTask, 100);
                button.setText("Stop");
            } else {
                handler.removeCallbacks(mUpdateTimeTask);
                startTime = 0L;
                timeLimit = 30;
                button.setText("Start");
            }
        }
    };

    View.OnClickListener mStopListener = new View.OnClickListener() {
        public void onClick(View v) {
            handler.removeCallbacks(mUpdateTimeTask);
            startTime = 0L;
            Button button = (Button) findViewById(R.id.timer_button);
            button.setText("Start");
        }
    };

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            TextView mTimeLabel = (TextView) findViewById(R.id.timer_display);
            long upTime = SystemClock.uptimeMillis();
            mTimeLabel.setText(getRemainingTime(upTime));
            handler.postAtTime(this, upTime + 200L);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button) findViewById(R.id.timer_button);
        button.setText("Start");
        button.setOnClickListener(buttonListener);
    }

    private String getRemainingTime(long currentTime) {
        if(startTime == 0L) return formatTime(30);
        int secondsElapsed = (int) ((currentTime - startTime) / 1000);
        int timeRemaining = timeLimit - secondsElapsed;
        if(timeRemaining <= 0) {
            handler.removeCallbacks(mUpdateTimeTask);
            startTime = 0L;
            Button button = (Button) findViewById(R.id.timer_button);
            button.setText("Start");
            return formatTime(0);
        }

        return formatTime(timeLimit - secondsElapsed);
    }

    private String formatTime(int seconds) {
        if(seconds < 10) return ("00:0" + seconds);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return ((minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds);
    }
}