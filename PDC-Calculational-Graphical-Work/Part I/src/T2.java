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

        // 1 - Введення матриці MX
        Data.inputMatrix(Main.MX, 1);

        // Повідомлення про завершення введення даних
        System.out.println("T2 finished data input\n");

        // 2 - Сигнал Т1, Т3, T4 про завершення введення даних
        inputMonitor.InputSignal();

        // Повідомлення про очікування завершення вводу даних у потоках Т1, Т3, Т4
        System.out.println("T2 waits data input from T3, T4\n");

        // 3-4 - Очікування завершення введення даних у інших потоках
        inputMonitor.WaitForInput();

        // Повідомлення про початок обчислення мінімуму у потоці Т2
        System.out.println("T2 started 'm' calculation\n");

        // 5 - Обчислення m2 = min(Cн);
        int id = 0;
        m2Res = Data.vectorMin(Main.C, id); // обчислення мінімуму в 2 частині вектора C

        // 6 - Обчислення m = min(m, m2);
        Main.m = calcMonitor.calcM(m2Res, Main.m); // обчислення мінімуму серед мінімуму

        // Повідомлення про закінчення обчислення мінімуму у потоці Т2
        System.out.println("T2 finished 'm' calculation\n");

        // 7 - Сигнал потокам Т1, Т3, Т4 про закінчення обчислення m
        calcMonitor.SignalCalcM();

        // 8-10 - Очікування сигналів від потоків Т1, Т3, Т4 про закінчення обчислення m
        calcMonitor.WaitForCalcM();

        // Повідомлення про початок копіювання даних у потоці Т2
        System.out.println("T2 started copying m, D, Z, MX\n");

        // 11 - Копія m2 = m
        int m2 = calcMonitor.copyM(Main.m);

        // 12 - Копія D2 = D
        int[] D2 = calcMonitor.copyD();

        // 13 - Копія Z2 = Z
        int[] Z2 = calcMonitor.copyZ();

        // 14 - Копія MX2 = MX
        int[][] MX2 = calcMonitor.copyMX();

        // Повідомлення про закінчення копіювання даних у потоці Т2
        System.out.println("T2 finished copying m, D, Z, MX\n");

        // Повідомлення про початок обчислення A у потоці Т2
        System.out.println("T2 started calculation A\n");

        // 15 - Обчислення Aн = m2 * Z2 + D2 * (MX2 * MSн)
        int[][] tempM = new int[Main.N][Main.N];
        for (int j = start; j < end; j++){
            for (int i = 0; i < Main.N; i++){
                tempM[i][j] = 0;
                for (int k = 0; k < Main.N; k++){
                    tempM[i][j] += MX2[i][k] * Main.MS[j][k];
                }
            }
        }

        for (int i = start; i < end; i++) {
            int temp = 0;
            for (int j = 0; j < Main.N; j++) {
                temp += D2[i] * tempM[j][i];
            }
            Main.A[i] = m2 * Z2[i] + temp;
        }

        // Повідомлення про закінчення обчислення A у потоці Т2
        System.out.println("T2 finished calculation A\n");

        // 16 - Сигнал Т1, T3, T4 про завершення обчислення A
        outputMonitor.Signal();

        // Повідомлення про відправлення сигналу потокам Т1, Т3, Т4 про закінчення обчислення A
        System.out.println("T2 sent signal to T1, T3, T4 about calculation A\n");

        // Закінчення роботи потоку Т2
        System.out.println("T2 finished\n");
    }
}