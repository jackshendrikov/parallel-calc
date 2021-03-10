/*-----------------------------------------------------
 |                      Labwork #3                    |
 |                    Java Monitors                   |
 ------------------------------------------------------
 |  Author  |       Jack (Yevhenii) Shendrikov        |
 |  Group   |                IO-82                    |
 |  Variant |                 #17                     |
 |  Date    |             07.03.2021                  |
 ------------------------------------------------------
 | Function |    E = (B*MR)*(MM*MO) + min(B)*Q*d      |
 ------------------------------------------------------
 */

public class Main {

    static int N = 8;
    private static int P = 4;
    static int H = N / P;

    static int m = Integer.MAX_VALUE;
    static int d;

    static int[] A = new int[N];
    static int[] B = new int[N];
    static int[] E = new int[N];
    static int[] Q = new int[N];

    static int[][] MM = new int[N][N];
    static int[][] MO = new int[N][N];
    static int[][] MR = new int[N][N];

    public static void main(String[] args) {
        System.out.println("\nLab2 started!");

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

        System.out.println("\nLab2 finished!");
    }
}