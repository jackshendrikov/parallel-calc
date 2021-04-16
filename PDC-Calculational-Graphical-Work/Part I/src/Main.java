/*-----------------------------------------------------
 |                         РГР                        |
 |          Parallel and Distributed Computings       |
 ------------------------------------------------------
 |  Author  |       Jack (Yevhenii) Shendrikov        |
 |  Group   |                IO-82                    |
 |  Variant |                 #24                     |
 |  Date    |             15.04.2021                  |
 ------------------------------------------------------
 | Function |        A = max(C)*Z + D*(MX*MS)         |
 ------------------------------------------------------
 */

public class Main {

    static int N = 8;
    private static int P = 3;
    static int H = N / P;

    static int m = Integer.MAX_VALUE;

    static int[] A = new int[N];
    static int[] C = new int[N];
    static int[] D = new int[N];
    static int[] Z = new int[N];

    static int[][] MX = new int[N][N];
    static int[][] MS = new int[N][N];

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        System.out.println("Program started!");

        InputMonitor inputMonitor = new InputMonitor();
        CalculationMonitor calcMonitor = new CalculationMonitor();
        OutputMonitor outputMonitor = new OutputMonitor();

        T1 T1 = new T1(inputMonitor, calcMonitor, outputMonitor);
        T2 T2 = new T2(inputMonitor, calcMonitor, outputMonitor);
        T3 T3 = new T3(inputMonitor, calcMonitor, outputMonitor);
        T4 T4 = new T4(inputMonitor, calcMonitor, outputMonitor);

        T1.start();
        T2.start();
        T3.start();
        T4.start();

        try {
            T1.join();
            T2.join();
            T3.join();
            T4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nProgram finished!\n\nTime: " + (System.currentTimeMillis() - startTime));
    }
}