class CalculationMonitor {
    private static int F = 0;
    private int[] D = Main.D;
    private int[] Z = Main.Z;
    private int[][] MX = Main.MX;

    synchronized int copyM(int x) {
        return x;
    }

    synchronized int calcM(int val1, int val2) { return val1 < val2 ? val1 : val2; }

    synchronized int[] copyD() {
        return this.D;
    }

    synchronized int[] copyZ() { return this.Z; }

    synchronized int[][] copyMX() { return this.MX; }

    synchronized void SignalCalcM() {
        F++;
        if (F >= 4) notifyAll();
    }

    synchronized void WaitForCalcM() {
        try {
            if (F < 4) wait();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}