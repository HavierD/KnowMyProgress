package tech.havier;

public class PerformanceMonitor {
    private double countOfSearchingOnWeb = 0;

    public void countOneSearch() {
        countOfSearchingOnWeb++;
    }

    public void proportionOfSearchingOnline() {
        System.out.println("Times of searching online: " + countOfSearchingOnWeb);
        countOfSearchingOnWeb = 0;
    }
}
