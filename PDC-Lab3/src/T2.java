public class T2 extends Thread {
    private InputMonitor inputMonitor;
    private CalculationMonitor calcMonitor;
    private OutputMonitor outputMonitor;

    T2(InputMonitor inputMonitor, CalculationMonitor calcMonitor, OutputMonitor outputMonitor) {
        this.inputMonitor = inputMonitor;
        this.calcMonitor = calcMonitor;
        this.outputMonitor = outputMonitor;
    }

    @Override
    public void run(){
        int m2Res;
        int start = Main.H, end = Main.H * 2;

        // Початок роботи потоку Т2
        System.out.println("\nT2 started");

        // Повідомлення про початок введення даних
        System.out.println("T2 inputs data\n");

        // 1 - Введення вектора B
        Calculations.inputVector(Main.B, 1);

        // 1 - Введення матриці MM
        Calculations.inputMatrix(Main.MM, 1);

        // Повідомлення про завершення введення даних
        System.out.println("T2 finished data input\n");

        // 2 - Сигнал Т1, Т3, T4 про завершення введення даних
        inputMonitor.InputSignal();

        // 3 - Очікування завершення введення даних у інших потоках
        inputMonitor.WaitForInput();

        // Повідомлення про початок копіювання даних у потоці Т2
        System.out.println("T2 started copying B, d, MO\n");

        // 4 - Копія B2 = B
        int[] B2 = calcMonitor.copyB();

        // 5 - Копія d2 = d
        int d2 = calcMonitor.copyD();

        // 6 - Копія MO2 = MO
        int[][] MO2 = calcMonitor.copyMO();

        // Повідомлення про закінчення копіювання даних у потоці Т2
        System.out.println("T2 finished copying B, d, MO\n");

        // Повідомлення про початок обчислення мінімуму та вектор-матричного добутку
        System.out.println("T2 started 'm' and 'Aн' calculation\n");

        // 7 - Обчислення m2 = min(Bн);
        int id = 1;
        m2Res = Calculations.vectorMin(Main.B, id); // обчислення мінімуму в 2 частині вектора В

        // 8 - Обчислення m = min(m,m2);
        Main.m = calcMonitor.calcM(m2Res, Main.m); // обчислення мінімуму серед мінімуму

        // 9 - Сигнал потокам Т1, Т3, Т4 про закінчення обчислення m
        calcMonitor.SignalCalcM();

        // 10 - Очікування сигналів від потоків Т1, Т3, Т4 про закінчення обчислення m
        calcMonitor.WaitForCalcM();

        // 11 - Обчислення Aн = (B2 * MRн)
        for (int i = 0; i < Main.N; i++) {
            for (int j = start; j < end; j++) {
                Main.A[j] += B2[j] * Main.MR[i][j];
            }
        }

        // 12 - Сигнал потокам Т1, Т3, Т4 про закінчення обчислення Aн
        calcMonitor.SignalCalcA();

        // 13 - Очікування сигналів від потоків Т1, Т3, Т4 про закінчення обчислення Aн
        calcMonitor.WaitForCalcA();

        // Повідомлення про закінчення обчислення мінімуму та вектор-матричного добутку у потоці Т2
        System.out.println("T2 finished 'm' and 'Aн' calculation\n");

        // Повідомлення про початок копіювання даних у потоці Т2
        System.out.println("T2 started copying m, A\n");

        // 14 - Копія m2 = m
        int m2 = calcMonitor.copyM(Main.m);

        // 15 - Копія A2 = A
        int[] A2 = calcMonitor.copyA();

        // Повідомлення про закінчення копіювання даних у потоці Т2
        System.out.println("T2 finished copying m, A\n");

        // Повідомлення про початок обчислення E у потоці Т2
        System.out.println("T2 started calculation E\n");

        // 16 - Обчислення Eн = A2 *(MMн * MO2) + m2 * Qн * d2
        int[][] tempM = new int[Main.N][Main.N];
        for (int j = start; j < end; j++){
            for (int i = 0; i < Main.N; i++){
                tempM[i][j] = 0;
                for (int k = 0; k < Main.N; k++){
                    tempM[i][j] += MO2[i][k] * Main.MM[j][k];
                }
            }
        }

        for (int i = start; i < end; i++) {
            int temp = 0;
            for (int j = 0; j < Main.N; j++) {
                temp += A2[i] * tempM[j][i];
            }
            Main.E[i] = temp + m2 * Main.Q[i] * d2;
        }

        // Повідомлення про закінчення обчислення E у потоці Т2
        System.out.println("T2 finished calculation E\n");

        // 17 - Сигнал Т1, T3, T4 про завершення обчислення E
        outputMonitor.Signal();

        // Повідомлення про відправлення сигналу потокам Т1, Т3, Т4 про закінчення обчислення E
        System.out.println("T2 sent signal to T1, T3, T4 about calculation E\n");

        // Закінчення роботи потоку Т2
        System.out.println("T2 finished\n");
    }
}