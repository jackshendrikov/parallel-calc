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

        // 1 - Введення вектора D
        Data.inputVector(Main.D, 1);

        // 1 - Введення вектора Z
        Data.inputVector(Main.Z, 1);

        // Повідомлення про завершення введення даних
        System.out.println("T3 finished data input\n");

        // 2 - Сигнал Т1, Т2, T4 про завершення введення даних
        inputMonitor.InputSignal();

        // Повідомлення про очікування завершення вводу даних у потоках Т1, Т2, Т4
        System.out.println("T3 waits data input from T2, T4\n");

        // 3-4 - Очікування завершення введення даних у інших потоках
        inputMonitor.WaitForInput();

        // Повідомлення про початок обчислення мінімуму у потоці Т3
        System.out.println("T3 started 'm' calculation\n");

        // 5 - Обчислення m3 = min(Cн);
        int id = 0;
        m3Res = Data.vectorMin(Main.C, id); // обчислення мінімуму в 3 частині вектора C

        // 6 - Обчислення m = min(m, m3);
        Main.m = calcMonitor.calcM(m3Res, Main.m); // обчислення мінімуму серед мінімуму

        // Повідомлення про закінчення обчислення мінімуму у потоці Т3
        System.out.println("T3 finished 'm' calculation\n");

        // 7 - Сигнал потокам Т1, Т2, Т4 про закінчення обчислення m
        calcMonitor.SignalCalcM();

        // 8-10 - Очікування сигналів від потоків Т1, Т2, Т4 про закінчення обчислення m
        calcMonitor.WaitForCalcM();

        // Повідомлення про початок копіювання даних у потоці Т3
        System.out.println("T3 started copying m, D, Z, MX\n");

        // 11 - Копія m3 = m
        int m3 = calcMonitor.copyM(Main.m);

        // 12 - Копія D3 = D
        int[] D3 = calcMonitor.copyD();

        // 13 - Копія Z3 = Z
        int[] Z3 = calcMonitor.copyZ();

        // 14 - Копія MX3 = MX
        int[][] MX3 = calcMonitor.copyMX();

        // Повідомлення про закінчення копіювання даних у потоці Т3
        System.out.println("T3 finished copying m, D, Z, MX\n");

        // Повідомлення про початок обчислення A у потоці Т3
        System.out.println("T3 started calculation A\n");

        // 15 - Обчислення Aн = m3 * Z3 + D3 * (MX3 * MSн)
        int[][] tempM = new int[Main.N][Main.N];
        for (int j = start; j < end; j++){
            for (int i = 0; i < Main.N; i++){
                tempM[i][j] = 0;
                for (int k = 0; k < Main.N; k++){
                    tempM[i][j] += MX3[i][k] * Main.MS[j][k];
                }
            }
        }

        for (int i = start; i < end; i++) {
            int temp = 0;
            for (int j = 0; j < Main.N; j++) {
                temp += D3[i] * tempM[j][i];
            }
            Main.A[i] = m3 * Z3[i] + temp;
        }

        // Повідомлення про закінчення обчислення A у потоці Т3
        System.out.println("T3 finished calculation A\n");

        // 16 - Сигнал Т1, T2, T4 про завершення обчислення A
        outputMonitor.Signal();

        // Повідомлення про відправлення сигналу потокам Т1, Т2, Т4 про закінчення обчислення A
        System.out.println("T3 sent signal to T1, T2, T4 about calculation A\n");

        // Закінчення роботи потоку Т3
        System.out.println("T3 finished\n");
    }
}