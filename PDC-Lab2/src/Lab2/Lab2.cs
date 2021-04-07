using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

/*-----------------------------------------------------
 |                      Labwork #2                    |
 |                     PKS SP in C#                   |
 ------------------------------------------------------
 |  Author  |       Jack (Yevhenii) Shendrikov        |
 |  Group   |                IO-82                    |
 |  Variant |                 #30                     |
 |  Date    |             23.02.2021                  |
 ------------------------------------------------------
 | Function |     Z = sort(D*(ME*MM)) + (B*C)*E*x     |
 ------------------------------------------------------ 
 */

namespace Lab2
{
    class Program : Operations
    {
        public const int N = 100;
        public const int P = 4;
        public const int H = N / P;

        public static Semaphore S0;
        public static Semaphore S1;
        public static Semaphore S2;
        public static Semaphore S3;

        public static Semaphore SM0;
        public static Semaphore SM1;
        public static Semaphore SM2;

        public static EventWaitHandle E0;
        public static EventWaitHandle E1;
        public static EventWaitHandle E2;
        public static EventWaitHandle E3;

        public static volatile int x;
        public static Mutex mutex_b = new Mutex(false);
        public static object lockD = new object();
        public static Semaphore S_ME = new Semaphore(1, 1); 

        public static int b = 0;

        public static Vector B = new Vector(N);
        public static Vector C = new Vector(N);
        public static Vector D = new Vector(N);
        public static Vector E = new Vector(N);
        public static Vector K = new Vector(N);
        public static Vector Z = new Vector(N);

        public static Matrix ME = new Matrix(N);
        public static Matrix MM = new Matrix(N);


        /********************************* Задача Т1 *********************************/
        public static void T1() {
            Console.WriteLine("T1 started.");

            // 1 - Введення B, E
            B = inputVector(N, 1);
            E = inputVector(N, 1);

            // 2 - Сигнал задачам Т2, Т3, Т4 про введення B, E
            E0.Set();

            // 3 - Чекати на введення D, MM у задачі Т2
            E1.WaitOne();

            // 4 - Чекати на введення ME у задачі Т3
            E2.WaitOne();

            // 5 - Чекати на введення C, Z, x у задачі Т4
            E3.WaitOne();

            // 6 - Копіювати x1 = x
            int x1 = x;

            // 7 - Копіювати ME1 = ME
            Matrix ME1 = new Matrix(N);

            S_ME.WaitOne();
            ME1 = ME;
            S_ME.Release();

            // 8 - Копіювати D1 = D
            Vector D1 = new Vector(N);
            lock (lockD) {
                D1 = D;
            }

            // 9 - Обчислення b1 = BH * CH
            int b1 = mult(B, C, 0, H);

            // 10 - Обчислення b = b + b1
            mutex_b.WaitOne();
            b += b1;
            mutex_b.ReleaseMutex();

            // 11 - Обчислення KH = sort(D1 * (ME1 * MMH))
            K = mult(D1, mult(ME1, MM, 0, H), 0, H);
            Vector buf = new Vector(H);
            buf = sort(K, 0, H);
            for (int i = 0; i < H; i++)
                K.set(i, buf.get(i));

            // 12 - Чекати на завершення обчислень KH у Т3
            SM1.WaitOne();

            // 13 - Злиття K2H = mergesort(KH, KH)
            mergeSort(K, 0, 2 * H);

            // 14 - Чекати на завершення обчислень K2H у Т2
            SM0.WaitOne();

            // 15 - Обчислення K = mergesort(K2H, K2H)
            mergeSort(K, 0, N);

            // 16 - Сигнал задачам Т2, Т3, Т4 про обчислення K
            S0.Release();

            // 17 - Копіювати b1 = b
            mutex_b.WaitOne();
            b1 = b;
            mutex_b.ReleaseMutex();

            // 18 - Обчислення ZH = KH + b1 * EH * x1
            buf = add(K, mult(b1, mult(x1, E, 0, H), 0, H), 0, H);
            for (int i = 0; i < H; i++)
                Z.set(i, buf.get(i));

            // 19 - Чекати на завершення обчислень ZH в Т2, T3, T4
            S1.WaitOne();
            S2.WaitOne();
            S3.WaitOne();

            // 20 - Виведення результату Z
            outputVector(Z);
            
            Console.WriteLine("T1 finished.");
        }

        /********************************* Задача Т2 *********************************/
        public static void T2() {
            Console.WriteLine("T2 started.");

            // 1 - Введення D, MM
            D = inputVector(N, 1);
            MM = inputMatrix(N, 1);

            // 2 - Сигнал задачам Т1, Т3, Т4 про введення D, MM
            E1.Set();

            // 3 - Чекати на введення B, E у задачі Т1
            E0.WaitOne();

            // 4 - Чекати на введення ME у задачі Т3
            E2.WaitOne();

            // 5 - Чекати на введення C, Z, x у задачі Т4
            E3.WaitOne();

            // 6 - Копіювати x2 = x
            int x2 = x;

            // 7 - Копіювати ME2 = ME
            Matrix ME2 = new Matrix(N);

            S_ME.WaitOne();
            ME2 = ME;
            S_ME.Release();

            // 8 - Копіювати D2 = D
            Vector D2 = new Vector(N);
            lock (lockD) {
                D2 = D;
            }

            // 9 - Обчислення b2 = BH * CH
            int b2 = mult(B, C, H, 2*H);

            // 10 - Обчислення b = b + b2
            mutex_b.WaitOne();
            b += b2;
            mutex_b.ReleaseMutex();

            // 11 - Обчислення KH = sort(D2 * (ME2 * MMH))
            K = mult(D2, mult(ME2, MM, H, 2 * H), H, 2 * H);
            Vector buf = new Vector(H);
            buf = sort(K, H, 2 * H);
            for (int i = H; i < 2 * H; i++)
                K.set(i, buf.get(i));

            // 12 - Чекати на завершення обчислень KH у Т4
            SM2.WaitOne();

            // 13 - Обчислення K2H = mergesort(KH, KH)
            mergeSort(K, 2 * H, N);

            // 14 - Сигнал задачі Т1 про обчислення K2H
            SM0.Release();

            // 15 - Чекати на завершення обчислень K у Т1
            S0.WaitOne();

            // 16 - Копіювати b2 = b
            mutex_b.WaitOne();
            b2 = b;
            mutex_b.ReleaseMutex();

            // 17 - Обчислення ZH = KH + b2 * EH * x2
            buf = add(K, mult(b2, mult(x2, E, H, 2 * H), H, 2 * H), H, 2 * H);
            for (int i = H; i < 2 * H; i++)
                Z.set(i, buf.get(i));

            // 18 - Сигнал задачі Т1 про обчислення ZH
            S1.Release();

            Console.WriteLine("T2 finished.");
        }

        /********************************* Задача Т3 *********************************/
        public static void T3() {
            Console.WriteLine("T3 started.");

            // 1 - Введення ME
            ME = inputMatrix(N, 1);

            // 2 - Сигнал задачам Т1, Т2, Т4 про введення ME
            E2.Set();

            // 3 - Чекати на введення B, E у задачі Т1
            E0.WaitOne();

            // 4 - Чекати на введення D, MM у задачі Т2
            E1.WaitOne();

            // 5 - Чекати на введення C, Z, x у задачі Т4
            E3.WaitOne();

            // 6 - Копіювати x3 = x
            int x3 = x;

            // 7 - Копіювати ME3 = ME
            Matrix ME3 = new Matrix(N);

            S_ME.WaitOne();
            ME3 = ME;
            S_ME.Release();

            // 8 - Копіювати D3 = D
            Vector D3 = new Vector(N);
            lock (lockD) {
                D3 = D;
            }

            // 9 - Обчислення b3 = BH * CH
            int b3 = mult(B, C, 2 * H, 3 * H);

            // 10 - Обчислення b = b + b3
            mutex_b.WaitOne();
            b += b3;
            mutex_b.ReleaseMutex();

            // 11 - Обчислення KH = sort(D3 * (ME3 * MMH))
            K = mult(D3, mult(ME3, MM, 2 * H, 3 * H), 2 * H, 3 * H);
            Vector buf = new Vector(H);
            buf = sort(K, 2 * H, 3 * H);
            for (int i = 2 * H; i < 3 * H; i++)
                K.set(i, buf.get(i));

            // 12 - Сигнал задачі Т1 про обчислення KH
            SM1.Release();

            // 13 - Чекати на завершення обчислень K у Т1
            S0.WaitOne();

            // 14 - Копіювати b3 = b
            mutex_b.WaitOne();
            b3 = b;
            mutex_b.ReleaseMutex();

            // 15 - Обчислення ZH = KH + b3 * EH * x3
            buf = add(K, mult(b3, mult(x3, E, 2 * H, 3 * H), 2 * H, 3 * H), 2 * H, 3 * H);
            for (int i = 2 * H; i < 3 * H; i++)
                Z.set(i, buf.get(i));

            // 16 - Сигнал задачі Т1 про обчислення ZH
            S2.Release();

            Console.WriteLine("T3 finished.");
        }

        /********************************* Задача Т4 *********************************/
        public static void T4() {
            Console.WriteLine("T4 started.");
            // 1 - Введення C, x
            C = inputVector(N, 1);
            x = 1;

            // 2 - Сигнал задачам Т1, Т2, Т4 про введення ME
            E3.Set();

            // 3 - Чекати на введення B, E у задачі Т1
            E0.WaitOne();

            // 4 - Чекати на введення D, MM у задачі Т2
            E1.WaitOne();

            // 5 - Чекати на введення ME у задачі Т3
            E2.WaitOne();

            // 6 - Копіювати x4 = x
            int x4 = x;

            // 7 - Копіювати ME4 = ME
            Matrix ME4 = new Matrix(N);

            S_ME.WaitOne();
            ME4 = ME;
            S_ME.Release();

            // 8 - Копіювати D4 = D
            Vector D4 = new Vector(N);
            lock (lockD) {
                D4 = D;
            }

            // 9 - Обчислення b4 = BH * CH
            int b4 = mult(B, C, 3 * H, 4 * H);

            // 10 - Обчислення b = b + b4
            mutex_b.WaitOne();
            b += b4;
            mutex_b.ReleaseMutex();

            // 11 - Обчислення KH = sort(D4 * (ME4 * MMH))
            K = mult(D4, mult(ME4, MM, 3 * H, 4 * H), 3 * H, 4 * H);
            Vector buf = new Vector(H);
            buf = sort(K, 3 * H, 4 * H);
            for (int i = 3 * H; i < 4 * H; i++)
                K.set(i, buf.get(i));

            // 12 - Сигнал задачі Т2 про обчислення KH
            SM2.Release();

            // 13 - Чекати на завершення обчислень K у Т1
            S0.WaitOne();

            // 14 - Копіювати b4 = b
            mutex_b.WaitOne();
            b4 = b;
            mutex_b.ReleaseMutex();

            // 15 - Обчислення ZH = KH + b4 * EH * x4
            buf = add(K, mult(b4, mult(x4, E, 3 * H, 4 * H), 3 * H, 4 * H), 3 * H, 4 * H);
            for (int i = 3 * H; i < 4 * H; i++)
                Z.set(i, buf.get(i));

            // 16 - Сигнал задачі Т1 про обчислення ZH
            S3.Release();

            Console.WriteLine("T4 finished.");
        }


        static void Main(string[] args) {
            System.Console.WriteLine("Lab 2 started!\n");

            S0 = new Semaphore(0, 1);
            S1 = new Semaphore(0, 1);
            S2 = new Semaphore(0, 1);
            S3 = new Semaphore(0, 1);

            SM0 = new Semaphore(0, 1);
            SM1 = new Semaphore(0, 1);
            SM2 = new Semaphore(0, 1);

            E0 = new ManualResetEvent(false);
            E1 = new ManualResetEvent(false);
            E2 = new ManualResetEvent(false);
            E3 = new ManualResetEvent(false);

            Thread t1 = new Thread(T1);
            Thread t2 = new Thread(T2);
            Thread t3 = new Thread(T3);
            Thread t4 = new Thread(T4);

            t1.Start();
            t2.Start();
            t3.Start();
            t4.Start();
            t1.Join();

            System.Console.WriteLine("\nLab 2 finished!\n");
            Console.ReadKey();
        }
    }
}
