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

        // Повідомлення про початок введення даних
        System.out.println("T4 inputs data\n");

        // 1 - Введення вектора C
        Data.inputVector(Main.C, 1);

        // 1 - Введення матриці MS
        Data.inputMatrix(Main.MS, 1);

        // Повідомлення про завершення введення даних
        System.out.println("T4 finished data input\n");

        // 2 - Сигнал Т1, Т2, T3 про завершення введення даних
        inputMonitor.InputSignal();

        // Повідомлення про очікування завершення вводу даних у потоках Т2, Т3
        System.out.println("T4 waits data input from T2, T3\n");

        // 3-4 - Очікування завершення введення даних у інших потоках
        inputMonitor.WaitForInput();

        // Повідомлення про початок обчислення мінімуму у потоці Т4
        System.out.println("T4 started 'm' calculation\n");

        // 5 - Обчислення m4 = min(Cн);
        int id = 0;
        m4Res = Data.vectorMin(Main.C, id); // обчислення мінімуму в 4 частині вектора C

        // 6 - Обчислення m = min(m, m4);
        Main.m = calcMonitor.calcM(m4Res, Main.m); // обчислення мінімуму серед мінімуму

        // Повідомлення про закінчення обчислення мінімуму у потоці Т4
        System.out.println("T4 finished 'm' calculation\n");

        // 7 - Сигнал потокам Т1, Т2, Т3 про закінчення обчислення m
        calcMonitor.SignalCalcM();

        // 8-10 - Очікування сигналів від потоків Т1, Т2, Т3 про закінчення обчислення m
        calcMonitor.WaitForCalcM();

        // Повідомлення про початок копіювання даних у потоці Т4
        System.out.println("T4 started copying m, D, Z, MX\n");

        // 11 - Копія m4 = m
        int m4 = calcMonitor.copyM(Main.m);

        // 12 - Копія D4 = D
        int[] D4 = calcMonitor.copyD();

        // 13 - Копія Z4 = Z
        int[] Z4 = calcMonitor.copyZ();

        // 14 - Копія MX4 = MX
        int[][] MX4 = calcMonitor.copyMX();

        // Повідомлення про закінчення копіювання даних у потоці Т4
        System.out.println("T4 finished copying m, D, Z, MX\n");

        // Повідомлення про початок обчислення A у потоці Т4
        System.out.println("T4 started calculation A\n");

        // 15 - Обчислення Aн = m4 * Z4 + D4 * (MX4 * MSн)
        int[][] tempM = new int[Main.N][Main.N];
        for (int j = start; j < end; j++){
            for (int i = 0; i < Main.N; i++){
                tempM[i][j] = 0;
                for (int k = 0; k < Main.N; k++){
                    tempM[i][j] += MX4[i][k] * Main.MS[j][k];
                }
            }
        }

        for (int i = start; i < end; i++) {
            int temp = 0;
            for (int j = 0; j < Main.N; j++) {
                temp += D4[i] * tempM[j][i];
            }
            Main.A[i] = m4 * Z4[i] + temp;
        }

        // Повідомлення про закінчення обчислення A у потоці Т4
        System.out.println("T4 finished calculation A\n");

        // 16 - Сигнал Т1, T2, T3 про завершення обчислення A
        outputMonitor.Signal();

        // Повідомлення про відправлення сигналу потокам Т1, Т2, Т3 про закінчення обчислення A
        System.out.println("T4 sent signal to T1, T2, T3 about calculation A\n");

        // Закінчення роботи потоку Т4
        System.out.println("T4 finished\n");
    }
}