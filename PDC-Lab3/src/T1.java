import java.util.Arrays;

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

        // Повідомлення про початок введення даних
        System.out.println("T1 inputs data\n");

        // 1 - Введення вектора Q
        Calculations.inputVector(Main.Q, 1);

        // 1 - Введення матриці MR
        Calculations.inputMatrix(Main.MR, 1);

        // Повідомлення про завершення введення даних
        System.out.println("T1 finished data input\n");

        // 2 - Сигнал Т2, Т3, T4 про завершення введення даних
        inputMonitor.InputSignal();

        // 3 - Очікування завершення введення даних у інших потоках
        inputMonitor.WaitForInput();

        // Повідомлення про початок копіювання даних у потоці Т1
        System.out.println("T1 started copying B, d, MO\n");

        // 4 - Копія B1 = B
        int[] B1 = calcMonitor.copyB();

        // 5 - Копія d1 = d
        int d1 = calcMonitor.copyD();

        // 6 - Копія MO1 = MO
        int[][] MO1 = calcMonitor.copyMO();

        // Повідомлення про закінчення копіювання даних у потоці Т1
        System.out.println("T1 finished copying B, d, MO\n");

        // Повідомлення про початок обчислення мінімуму та вектор-матричного добутку
        System.out.println("T1 started 'm' and 'Aн' calculation\n");

        // 7 - Обчислення m1 = min(Bн);
        int id = 0;
        m1Res = Calculations.vectorMin(Main.B, id); // обчислення мінімуму в 1 частині вектора В

        // 8 - Обчислення m = min(m,m1);
        Main.m = calcMonitor.calcM(m1Res, Main.m); // обчислення мінімуму серед мінімуму

        // 9 - Сигнал потокам Т2, Т3, Т4 про закінчення обчислення m
        calcMonitor.SignalCalcM();

        // 10 - Очікування сигналів від потоків Т2, Т3, Т4 про закінчення обчислення m
        calcMonitor.WaitForCalcM();

        // 11 - Обчислення Aн = (B1 * MRн)
        for (int i = 0; i < Main.N; i++) {
            for (int j = start; j < end; j++) {
                Main.A[j] += B1[j] * Main.MR[i][j];
            }
        }

        // 12 - Сигнал потокам Т2, Т3, Т4 про закінчення обчислення Aн
        calcMonitor.SignalCalcA();

        // 13 - Очікування сигналів від потоків Т2, Т3, Т4 про закінчення обчислення Aн
        calcMonitor.WaitForCalcA();

        // Повідомлення про закінчення обчислення мінімуму та вектор-матричного добутку у потоці Т1
        System.out.println("T1 finished 'm' and 'Aн' calculation\n");

        // Повідомлення про початок копіювання даних у потоці Т1
        System.out.println("T1 started copying m, A\n");

        // 14 - Копія m1 = m
        int m1 = calcMonitor.copyM(Main.m);

        // 15 - Копія A1 = A
        int[] A1 = calcMonitor.copyA();

        // Повідомлення про закінчення копіювання даних у потоці Т1
        System.out.println("T1 finished copying m, A\n");

        // Повідомлення про початок обчислення E у потоці Т1
        System.out.println("T1 started calculation E\n");

        // 16 - Обчислення Eн = A1 *(MMн * MO1) + m1 * Qн * d1
        int[][] tempM = new int[Main.N][Main.N];
        for (int j = start; j < end; j++){
            for (int i = 0; i < Main.N; i++){
                tempM[i][j] = 0;
                for (int k = 0; k < Main.N; k++){
                    tempM[i][j] += MO1[i][k] * Main.MM[j][k];
                }
            }
        }

        for (int i = start; i < end; i++) {
            int temp = 0;
            for (int j = 0; j < Main.N; j++) {
                temp += A1[i] * tempM[j][i];
            }
            Main.E[i] = temp + m1 * Main.Q[i] * d1;
        }

        // Повідомлення про закінчення обчислення E у потоці Т1
        System.out.println("T1 finished calculation E\n");

        // 17 - Чекати на завершення обчислень E в T2, T3, T4
        outputMonitor.WaitForSignal();

        // 18 - Виведення E
        if (Main.N <= 15){
            System.out.print("E = [ ");
            Calculations.outputVector(Main.E);
            System.out.println("]");
        }
        // Закінчення роботи потоку Т1
        System.out.println("\nT1 finished");
    }
}