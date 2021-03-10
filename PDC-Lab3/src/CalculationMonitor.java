class CalculationMonitor {
    private static int F = 0;
    private static int F1 = 0;
    private int d = 1;
    private int[] A = Main.A;
    private int[] B = Main.B;
    private int[][] MO = Main.MO;

    synchronized int copyM(int x) {
        return x;
    }

    synchronized int calcM(int val1, int val2) { return val1 < val2 ? val1 : val2; }

    synchronized int[] copyA() {
        return this.A;
    }

    synchronized int copyD() { return this.d; }

    synchronized int[] copyB() { return this.B; }

    synchronized int[][] copyMO() { return this.MO; }

    synchronized void SignalCalcM() {
        F++;
        if (F >= 4) notifyAll();
    }

    synchronized void SignalCalcA() {
        F1++;
        if (F1 >= 4) notifyAll();
    }

    synchronized void WaitForCalcM() {
        try {
            if (F < 4) wait();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    synchronized void WaitForCalcA() {
        try {
            if (F1 < 4) wait();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}