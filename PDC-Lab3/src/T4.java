public class T4 extends Thread {
    private InputMonitor inputMonitor;
    private CalculationMonitor calcMonitor;
    private OutputMonitor outputMonitor;

    T4(InputMonitor inputMonitor, CalculationMonitor calcMonitor, OutputMonitor outputMonitor) {
        this.inputMonitor = inputMonitor;
        this.calcMonitor = calcMonitor;
        this.outputMonitor = outputMonitor;
    }

    @Override
    public void run(){
        int m4Res;
        int start = Main.H * 3, end = Main.N;

        // Початок роботи потоку Т4
        System.out.println("\nT4 started");

        // Повідомлення про очікування завершення вводу даних у потоках Т1, Т2, Т3
        System.out.println("T4 waits data input from T1, T2, T3\n");

        // 1 - Очікування сигналу від Т1, Т2, Т3 про завершення введення даних
        inputMonitor.WaitForInput();

        // Повідомлення про початок копіювання даних у потоці Т4
        System.out.println("T4 started copying B, d, MO\n");

        // 2 - Копія B4 = B
        int[] B4 = calcMonitor.copyB();

        // 3 - Копія d4 = d
        int d4 = calcMonitor.copyD();

        // 4 - Копія MO4 = MO
        int[][] MO4 = calcMonitor.copyMO();

        // Повідомлення про закінчення копіювання даних у потоці Т4
        System.out.println("T4 finished copying B, d, MO\n");

        // Повідомлення про початок обчислення мінімуму та вектор-матричного добутку
        System.out.println("T4 started 'm' and 'Aн' calculation\n");

        // 5 - Обчислення m4 = min(Bн);
        int id = 3;
        m4Res = Calculations.vectorMin(Main.B, id); // обчислення мінімуму в 4 частині вектора В

        // 6 - Обчислення m = min(m,m4);
        Main.m = calcMonitor.calcM(m4Res, Main.m); // обчислення мінімуму серед мінімуму

        // 7 - Сигнал потокам Т1, Т2, Т3 про закінчення обчислення m
        calcMonitor.SignalCalcM();

        // 8 - Очікування сигналів від потоків Т1, Т2, Т3 про закінчення обчислення m
        calcMonitor.WaitForCalcM();

        // 9 - Обчислення Aн = (B4 * MRн)
        for (int i = 0; i < Main.N; i++) {
            for (int j = start; j < end; j++) {
                Main.A[j] += B4[j] * Main.MR[i][j];
            }
        }

        // 10 - Сигнал потокам Т1, Т2, Т3 про закінчення обчислення Aн
        calcMonitor.SignalCalcA();

        // 11 - Очікування сигналів від потоків Т1, Т2, Т3 про закінчення обчислення Aн
        calcMonitor.WaitForCalcA();

        // Повідомлення про закінчення обчислення мінімуму та вектор-матричного добутку у потоці Т4
        System.out.println("T4 finished 'm' and 'Aн calculation\n");

        // Повідомлення про початок копіювання даних у потоці Т4
        System.out.println("T4 started copying m, A\n");

        // 12 - Копія m4 = m
        int m4 = calcMonitor.copyM(Main.m);

        // 13 - Копія A4 = A
        int[] A4 = calcMonitor.copyA();

        // Повідомлення про закінчення копіювання даних у потоці Т4
        System.out.println("T4 finished copying m, A\n");

        // Повідомлення про початок обчислення E у потоці Т4
        System.out.println("T4 started calculation E\n");

        // 14 - Обчислення Eн = A4 *(MMн * MO4) + m4 * Qн * d4
        int[][] tempM = new int[Main.N][Main.N];
        for (int j = start; j < end; j++){
            for (int i = 0; i < Main.N; i++){
                tempM[i][j] = 0;
                for (int k = 0; k < Main.N; k++){
                    tempM[i][j] += MO4[i][k] * Main.MM[j][k];
                }
            }
        }

        for (int i = start; i < end; i++) {
            int temp = 0;
            for (int j = 0; j < Main.N; j++) {
                temp += A4[i] * tempM[j][i];
            }
            Main.E[i] = temp + m4 * Main.Q[i] * d4;
        }

        // Повідомлення про закінчення обчислення E у потоці Т1
        System.out.println("T1 finished calculation E\n");

        // 15 - Сигнал Т1, T2, T4 про завершення обчислення E
        outputMonitor.Signal();

        // Повідомлення про відправлення сигналу потокам Т1, Т2, Т3 про закінчення обчислення E
        System.out.println("T4 sent signal to T1, T2, T3 about calculation E\n");

        // Закінчення роботи потоку Т4
        System.out.println("T4 finished\n");
    }
}