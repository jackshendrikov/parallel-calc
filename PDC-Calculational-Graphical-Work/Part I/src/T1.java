public class T1 extends Thread {
    private InputMonitor inputMonitor;
    private CalculationMonitor calcMonitor;
    private OutputMonitor outputMonitor;

    T1(InputMonitor inputMonitor, CalculationMonitor calcMonitor, OutputMonitor outputMonitor) {
        this.inputMonitor = inputMonitor;
        this.calcMonitor = calcMonitor;
        this.outputMonitor = outputMonitor;
    }

    @Override
    public void run(){
        int m1Res;
        int start = 0, end = Main.H;

        // Початок роботи потоку Т1
        System.out.println("\nT1 started");

        // Повідомлення про очікування завершення вводу даних у потоках Т2, Т3, Т4
        System.out.println("T1 waits data input from T2, T3, T4\n");

        // 1-3 - Очікування сигналу від Т2, Т3, Т4 про завершення введення даних
        inputMonitor.WaitForInput();

        // Повідомлення про початок обчислення мінімуму у потоці Т1
        System.out.println("T1 started 'm' calculation\n");

        // 4 - Обчислення m1 = min(Cн);
        int id = 0;
        m1Res = Data.vectorMin(Main.C, id); // обчислення мінімуму в 1 частині вектора C

        // 5 - Обчислення m = min(m, m1);
        Main.m = calcMonitor.calcM(m1Res, Main.m); // обчислення мінімуму серед мінімуму

        // Повідомлення про закінчення обчислення мінімуму у потоці Т1
        System.out.println("T1 finished 'm' calculation\n");

        // 6 - Сигнал потокам Т2, Т3, Т4 про закінчення обчислення m
        calcMonitor.SignalCalcM();

        // 7-9 - Очікування сигналів від потоків Т2, Т3, Т4 про закінчення обчислення m
        calcMonitor.WaitForCalcM();

        // Повідомлення про початок копіювання даних у потоці Т1
        System.out.println("T1 started copying m, D, Z, MX\n");

        // 10 - Копія m1 = m
        int m1 = calcMonitor.copyM(Main.m);

        // 11 - Копія D1 = D
        int[] D1 = calcMonitor.copyD();

        // 12 - Копія Z1 = Z
        int[] Z1 = calcMonitor.copyZ();

        // 13 - Копія MX1 = MX
        int[][] MX1 = calcMonitor.copyMX();

        // Повідомлення про закінчення копіювання даних у потоці Т1
        System.out.println("T1 finished copying m, D, Z, MX\n");

        // Повідомлення про початок обчислення A у потоці Т1
        System.out.println("T1 started calculation A\n");

        // 14 - Обчислення Aн = m1 * Z1 + D1 * (MX1 * MSн)
        int[][] tempM = new int[Main.N][Main.N];
        for (int j = start; j < end; j++){
            for (int i = 0; i < Main.N; i++){
                tempM[i][j] = 0;
                for (int k = 0; k < Main.N; k++){
                    tempM[i][j] += MX1[i][k] * Main.MS[j][k];
                }
            }
        }

        for (int i = start; i < end; i++) {
            int temp = 0;
            for (int j = 0; j < Main.N; j++) {
                temp += D1[i] * tempM[j][i];
            }
            Main.A[i] = m1 * Z1[i] + temp;
        }

        // Повідомлення про закінчення обчислення A у потоці Т1
        System.out.println("T1 finished calculation A\n");

        // 15-17 - Чекати на завершення обчислень A в T2, T3, T4
        outputMonitor.WaitForSignal();

        // 18 - Виведення A
        if (Main.N <= 15){
            System.out.print("A = [ ");
            Data.outputVector(Main.A);
            System.out.println("]");
        }
        // Закінчення роботи потоку Т1
        System.out.println("\nT1 finished");
    }
}