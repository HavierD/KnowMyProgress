package tech.havier.timeToolkit;

import java.util.Timer;
import java.util.TimerTask;

public class HavierTimer {
    private int minute = 0;
    private int second = 0;
    private int second10th = 0;
    private boolean isThreadLocked = false;

    Timer timer = new Timer(true);

    /**
     * output time using every "gap" seconds
     * @param reportIntervals
     */
    public void start(int reportIntervals) {
        if(isThreadLocked){
            throw new RuntimeException("This timer is timing. A HavierTimer instance must be one thread.");
        }
        isThreadLocked = true;
        timer = new Timer();
        timer.schedule(timerTask(reportIntervals), 0, 100);
    }

    private TimerTask timerTask(int reportPunctuallyGap) {
        return new TimerTask() {
            @Override
            public void run() {
                recordSecond10th();
                if (reportPunctuallyGap > 0) {
                    reportPunctually(reportPunctuallyGap);
                }
            }
        };
    }

    private void reportPunctually(int gap) {
        if (this.second % gap == 0 && this.second10th ==0) {
            System.out.println("Time using: " + getCurrentTimer() + "...");
        }
    }

    private String getCurrentTimer() {
        return minute + " minutes " + second + "." + second10th + "seconds";
    }

    public void cancel() {
        timer.cancel();
        initTime();
        isThreadLocked = false;
    }

    public void cancel(String timingFor) {
        System.out.println("Time cost for " + timingFor + " finished. Time cost: " + getCurrentTimer());
        cancel();
    }

    private void initTime() {
        this.second10th = 0;
        this.minute = 0;
        this.second = 0;
    }

    private void recordSecond10th() {
        second10th ++;
        checkSecond10thMax();
    }

    private void checkSecond10thMax() {
        if (second10th >= 10) {
            second10th = 0;
            second++;
            checkSecondMax();
        }
    }

    private void checkSecondMax() {
        if (second >= 60) {
            second = 0;
            minute++;
        }
    }
}
