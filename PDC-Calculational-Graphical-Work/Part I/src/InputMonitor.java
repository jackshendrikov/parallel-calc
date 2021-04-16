class InputMonitor {
    private static int F = 0;

    synchronized void InputSignal() {
        F++;
        if (F >= 3) notifyAll();
    }

    synchronized void WaitForInput() {
        try {
            if (F < 3) wait();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}