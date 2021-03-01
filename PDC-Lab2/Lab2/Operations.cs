using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
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
    class Operations
    {

        public static Vector inputVector(int n, int value)
        {
            Vector vector = new Vector(n);
            for (int i = 0; i < n; i++)
            {
                vector.set(i, value);
            }
            return vector;
        }

        public static void outputVector(Vector vector)
        {
            if (vector.size() < 9)
            {
                Console.WriteLine(vector.toString());
            }

        }

        public static Vector mult(Vector left, Matrix right, int l, int r)
        {
            Vector result = new Vector(left.size());
            for (int i = l; i < r; i++)
            {
                result.set(i, 0);
                for (int j = 0; j < left.size(); j++)
                {
                    result.set(i, result.get(i) + left.get(j) * right.get(j, i));
                }
            }
            return result;
        }

        public static Vector mult(int value, Vector vect, int l, int r)
        {
            Vector result = new Vector(vect.size());
            for (int i = l; i < r; i++)
            {
                result.set(i, value * vect.get(i));
            }
            return result;
        }

        public static int mult(Vector vect1, Vector vect2, int l, int r)
        {
            int result = 0;
            for (int i = l; i < r; i++)
                result += vect1.get(i) * vect2.get(i);
            return result;
        }

        public static Matrix inputMatrix(int n, int value)
        {
            Matrix matrix = new Matrix(n);
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    matrix.set(i, j, value);
                }
            }
            return matrix;
        }

        public static void outputMatrix(Matrix matrix)
        {
            if (matrix.size() < 9)
            {
                Console.WriteLine(matrix.toString());
            }

        }


        public static Matrix mult(Matrix left, Matrix right, int l, int r)
        {
            Matrix result = new Matrix(left.size());
            for (int i = 0; i < left.size(); i++)
            {
                for (int j = l; j < r; j++)
                {
                    result.set(i, j, 0);
                    for (int k = 0; k < left.size(); k++)
                    {
                        result.set(i, j, result.get(i, j) + left.get(i, k)
                                * right.get(k, j));
                    }
                }
            }
            return result;
        }

        public static Vector add(Vector left, Vector right, int l, int r)
        {
            Vector result = new Vector(left.size());
            for (int i = l; i < r; i++)
            {
                result.set(i, left.get(i) + right.get(i));
            }
            return result;
        }

        public static Vector sort(Vector vector, int l, int r)
        {
            int tmp = vector.get(0);
            Vector res;
            res = vector;

            for (int i = l; i < r; i++)
            {
                for (int k = i + 1; k < r; k++)
                {
                    if (res.get(i) > res.get(k))
                    {
                        tmp = res.get(k);
                        res.set(k, res.get(i));
                        res.set(i, tmp);
                    }
                }
            }

            return res;
        }

        private static int[] merge(int[] left, int[] right)
        {
            int a = 0, b = 0;
            int[] merged = new int[left.Length + right.Length];
            for (Int32 i = 0; i < left.Length + right.Length; i++)
            {
                if (b < right.Length && a < left.Length)
                    if (left[a] > right[b] && b < right.Length)
                        merged[i] = right[b++];
                    else
                        merged[i] = left[a++];
                else
                    if (b < right.Length)
                    merged[i] = right[b++];
                else
                    merged[i] = left[a++];
            }
            return merged;
        }

        public static void mergeSort(Vector vector, int l, int r)
        {
            if (vector.size() == 1)
                return;
            int mid = (r - l) / 2;
            int[] merged = new int[r - l];
            int[] array1 = new int[mid];
            int[] array2 = new int[mid];
            for (int i = 0; i < r - l; i++)
            {
                if (i < mid)
                    array1[i] = vector.get(i + l);
                else
                    array2[i - mid] = vector.get(i + l);
            }
            merged = merge(array1, array2);
            for (int i = l; i < r; i++)
                vector.set(i, merged[i - l]);
        }


    }
}
