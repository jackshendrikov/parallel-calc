class OutputMonitor {
    private static int F = 0;

    synchronized void Signal() {
        F++;
        if (F >= 3) notifyAll();
    }

    synchronized void WaitForSignal() {
        try {
            if (F < 3) wait();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}