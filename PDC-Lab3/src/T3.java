public class T3 extends Thread {
    private InputMonitor inputMonitor;
    private CalculationMonitor calcMonitor;
    private OutputMonitor outputMonitor;

    T3(InputMonitor inputMonitor, CalculationMonitor calcMonitor, OutputMonitor outputMonitor) {
        this.inputMonitor = inputMonitor;
        this.calcMonitor = calcMonitor;
        this.outputMonitor = outputMonitor;
    }

    @Override
    public void run(){
        int m3Res;
        int start = Main.H * 2, end = Main.H * 3;

        // Початок роботи потоку Т3
        System.out.println("\nT3 started");

        // Повідомлення про початок введення даних
        System.out.println("T3 inputs data\n");

        // 1 - Введення числа d
        Main.d = 1;

        // 1 - Введення матриці MO
        Calculations.inputMatrix(Main.MO, 1);

        // Повідомлення про завершення введення даних
        System.out.println("T3 finished data input\n");

        // 2 - Сигнал Т1, Т2, T4 про завершення введення даних
        inputMonitor.InputSignal();

        // 3 - Очікування завершення введення даних у інших потоках
        inputMonitor.WaitForInput();

        // Повідомлення про початок копіювання даних у потоці Т3
        System.out.println("T3 started copying B, d, MO\n");

        // 4 - Копія B3 = B
        int[] B3 = calcMonitor.copyB();

        // 5 - Копія d3 = d
        int d3 = calcMonitor.copyD();

        // 6 - Копія MO3 = MO
        int[][] MO3 = calcMonitor.copyMO();

        // Повідомлення про закінчення копіювання даних у потоці Т3
        System.out.println("T3 finished copying B, d, MO\n");

        // Повідомлення про початок обчислення мінімуму та вектор-матричного добутку
        System.out.println("T3 started 'm' and 'Aн' calculation\n");

        // 7 - Обчислення m3 = min(Bн);
        int id = 2;
        m3Res = Calculations.vectorMin(Main.B, id); // обчислення мінімуму в 3 частині вектора В

        // 8 - Обчислення m = min(m,m3);
        Main.m = calcMonitor.calcM(m3Res, Main.m); // обчислення мінімуму серед мінімуму

        // 9 - Сигнал потокам Т1, Т2, Т4 про закінчення обчислення m
        calcMonitor.SignalCalcM();

        // 10 - Очікування сигналів від потоків Т1, Т2, Т4 про закінчення обчислення m
        calcMonitor.WaitForCalcM();

        // 11 - Обчислення Aн = (B3 * MRн)
        for (int i = 0; i < Main.N; i++) {
            for (int j = start; j < end; j++) {
                Main.A[j] += B3[j] * Main.MR[i][j];
            }
        }

        // 12 - Сигнал потокам Т1, Т2, Т4 про закінчення обчислення Aн
        calcMonitor.SignalCalcA();

        // 13 - Очікування сигналів від потоків Т1, Т2, Т4 про закінчення обчислення Aн
        calcMonitor.WaitForCalcA();

        // Повідомлення про закінчення обчислення мінімуму та вектор-матричного добутку у потоці Т3
        System.out.println("T3 finished 'm' and 'Aн' calculation\n");

        // Повідомлення про початок копіювання даних у потоці Т3
        System.out.println("T3 started copying m, A\n");

        // 14 - Копія m3 = m
        int m3 = calcMonitor.copyM(Main.m);

        // 15 - Копія A3 = A
        int[] A3 = calcMonitor.copyA();

        // Повідомлення про закінчення копіювання даних у потоці Т3
        System.out.println("T3 finished copying m, A\n");

        // Повідомлення про початок обчислення E у потоці Т3
        System.out.println("T3 started calculation E\n");

        // 16 - Обчислення Eн = A3 *(MMн * MO3) + m3 * Qн * d3
        int[][] tempM = new int[Main.N][Main.N];
        for (int j = start; j < end; j++){
            for (int i = 0; i < Main.N; i++){
                tempM[i][j] = 0;
                for (int k = 0; k < Main.N; k++){
                    tempM[i][j] += MO3[i][k] * Main.MM[j][k];
                }
            }
        }

        for (int i = start; i < end; i++) {
            int temp = 0;
            for (int j = 0; j < Main.N; j++) {
                temp += A3[i] * tempM[j][i];
            }
            Main.E[i] = temp + m3 * Main.Q[i] * d3;
        }

        // Повідомлення про закінчення обчислення E у потоці Т1
        System.out.println("T3 finished calculation E\n");

        // 17 - Сигнал Т1, T2, T4 про завершення обчислення E
        outputMonitor.Signal();

        // Повідомлення про відправлення сигналу потокам Т1, Т2, Т4 про закінчення обчислення E
        System.out.println("T3 sent signal to T1, T2, T4 about calculation E\n");

        // Закінчення роботи потоку Т3
        System.out.println("T3 finished\n");
    }
}