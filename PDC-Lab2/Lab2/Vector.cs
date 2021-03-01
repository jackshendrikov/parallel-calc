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
    class Vector
    {
        private int[] array;

        public Vector(int n)
        {
            array = new int[n];
        }

        public void set(int index, int value)
        {
            array[index] = value;
        }

        public int get(int index)
        {
            return array[index];
        }

        public int size()
        {
            return array.Length;
        }

        public String toString()
        {
            String res = "";
            for (int i = 0; i < array.Length; i++)
            {
                res += "   " + array[i];
            }
            return res;
        }


    }
}
