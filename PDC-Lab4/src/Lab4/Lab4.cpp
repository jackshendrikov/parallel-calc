/*-----------------------------------------------------
 |                      Labwork #4                    |
 |          OpenMP. Barriers, critical sections       |
 ------------------------------------------------------
 |  Author  |       Jack (Yevhenii) Shendrikov        |
 |  Group   |                IO-82                    |
 |  Variant |                 #25                     |
 |  Date    |             23.03.2021                  |
 ------------------------------------------------------
 | Function |      Z = (B*C)*D + E*(MA*MB)*x          | 
 ------------------------------------------------------
 */

#include "omp.h"
#include "Data.h"
#include <iostream>
#include <windows.h>

using namespace std;

// Розмір стеку
#pragma comment(linker, "/stack:160000000")

const int P = 4;
int N = 8;

int main()
{
	int b = 0;
	int x = 0;

	vector Z = new int[N];
	vector B = new int[N];
	vector C = new int[N];
	vector D = new int[N];
	vector E = new int[N];

	matrix MA;
	matrix MB;

	const int H = N / P;

	void cs();
	omp_lock_t lock_copy;
	omp_init_lock(&lock_copy);

	cout << "Lab4 started!\n\n";

	omp_set_num_threads(P);

	#pragma omp parallel
	{
		int tid = omp_get_thread_num();
		cout << "T" << tid << " started.\n";
		
		//---------------------------- Ввід Даних ---------------------------
		switch (tid)
		{
			case 0:
				C = inVector(1);
				x = 1;
				break;
			case 1:
				B = inVector(1);
				MA = inMatrix(1);
				break;
			case 2:
				E = inVector(1);
				break;
			case 3:
				D = inVector(1);
				MB = inMatrix(1);
				break;
		}

		//----------------- Синхронізація по введенню даних -----------------
		#pragma omp barrier
			int bi = 0;
			for (int i = tid * H; i < (tid + 1) * H; i++)
				bi += B[i] * C[i];
		
			// Обчислення b
			#pragma omp_atomic
			{
				b += bi;
			}

		//------------------ Синхронізація по обчисленню b ------------------
		#pragma omp barrier
			int xi = 0;
			vector Ei = new int[N];
			matrix MAi = new vector[N];

			// Директива atomic - копіювання b
			#pragma omp_atomic
			{
				bi = b;
			}

			// Директива atomic - копіювання x
			#pragma omp_atomic
			{
				xi = x;
			}

			// Критична секція - копіювання E
			#pragma omp critical(cs)
			{
				Ei = copyVector(E);
			}

			// Замок - копіювання MA
			omp_set_lock(&lock_copy);
				MAi = copyMatrix(MA);
			omp_unset_lock(&lock_copy);


			// Обчислення Z
			int buf;
			for (int i = tid * H; i < (tid + 1) * H; i++) {
				buf = 0;
				Z[i] = 0;

				Z[i] += bi * D[i];

				for (int j = 0; j < N; j++) {
					buf = 0;
					for (int k = 0; k < N; k++)
						buf += MAi[i][k] * MB[k][j];
					Z[i] += Ei[j] * buf * xi;
				}
			}
		
		//------------------ Синхронізація по обчисленню Z ------------------
		#pragma omp barrier
			if (tid == 0 && N < 15) {
				outVector(Z, 'Z');
			}
			Sleep(100);
			cout << "T" << tid + 1 << " finished." << endl;
	}

	cout << "\nLab4 finished!\n";
	getchar();
	return 0;
}