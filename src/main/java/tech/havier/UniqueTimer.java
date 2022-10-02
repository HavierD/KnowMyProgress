package tech.havier;

import java.util.Timer;
import java.util.TimerTask;

public class UniqueTimer {
    private int minute = 0;
    private int second = 0;
    Timer timer = new Timer(true);

    public void start() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                record1Second();
            }
        }, 0, 1000);
    }

    /**
     * output time using every "gap" seconds
     * @param gap
     */
    public void start(int gap) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                record1Second();
                reportPunctually(gap);
            }
        }, 0, 1000);
    }

    private void reportPunctually(int gap) {
        if (this.second % gap == 0) {
            System.out.println("Time using: " + getCurrentTimer() + "...");
        }
    }

    public String getCurrentTimer() {
        return minute + " minutes " + second + "seconds";
    }

    public void cancel() {
        timer.cancel();
        initTime();
    }

    private void initTime() {
        this.minute = 0;
        this.second = 0;
    }

    private void record1Second() {
        second ++;
        checkSecondMax();
    }

    private void checkSecondMax() {
        if (second >= 60) {
            second = 0;
            minute++;
        }
    }
}
