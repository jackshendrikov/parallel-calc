/*-----------------------------------------------------
 |                      Labwork #1                    |
 |                   PKS SP in WinAPI                 |
 ------------------------------------------------------
 |  Author  |       Jack (Yevhenii) Shendrikov        |
 |  Group   |                IO-82                    |
 |  Variant |                 #24                     |
 |  Date    |             10.02.2021                  |
 ------------------------------------------------------
 | Function |       MU = MD*MC*d + max(Z)*MR          |
 ------------------------------------------------------
*/


#include <iostream>
#include <windows.h>

using namespace std;

typedef int* vector;
typedef int** matrix;

const int N = 2000;
const int P = 4;
const int H = N / P;

int d, m;
vector Z = new int[N];
matrix MU = new vector[N],

MD = new vector[N],
MC = new vector[N],
MR = new vector[N];

HANDLE E1, E3, E4,
MTX, S_m[3], S_MU[3], S_max;
CRITICAL_SECTION CS;

//-----------------------------------------T1--------------------------------------------
void T1() {
	int d1, m1, s;
	matrix MC1 = new vector[N];
	for (int i = 0; i < N; i++)
		MC1[i] = new int[N];

	cout << "T1 started." << endl;
	
	// 1 - Введення MD
	for (int i = 0; i < N; i++)
		MD[i] = new int[N];

	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
			MD[i][j] = 1;

	// 2 - Сигнал задачам Т2, Т3, Т4 про введення MB
	SetEvent(E1);
	
	// 3 - Чекати на введення MR, MC у задачі Т3
	WaitForSingleObject(E3, INFINITE);
	
	// 4 - Чекати на введення Z, d у задачі T4
	WaitForSingleObject(E4, INFINITE);

	// 5 - Копіювати MC1 := MC
	WaitForSingleObject(MTX, INFINITE);
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
			MC1[i][j] = MC[i][j];

	ReleaseMutex(MTX);
	
	// 6 - Копіювання d1 := d
	EnterCriticalSection(&CS);
	d1 = d;
	LeaveCriticalSection(&CS);

	// 7 - Обчислення m1 := max(Zн)
	m1 = 0;
	for (int i = 0; i < H; i++)
		if (Z[i] > m1) m1 = Z[i];

	// 8 - Обчислення m: = max(m, m1)
	EnterCriticalSection(&CS);
	m = max(m, m1);
	LeaveCriticalSection(&CS);

	// 9 - Сигнал Т3 про про завершення обчислень m
	ReleaseSemaphore(S_m[0], 1, NULL);

	// 10 - Чекати сигналу від T3 про завершення обчислень m
	WaitForSingleObject(S_max, INFINITE);

	// 11 - Копіювання m1 := m
	EnterCriticalSection(&CS);
	m1 = m;
	LeaveCriticalSection(&CS);

	// 12 - Обчислення MUн = MDн∙MC1∙d1 + m1∙MRн
	for (int i = H; i < 2 * H; i++) {
		for (int j = 0; j < N; j++) {
			s = 0;
			for (int k = 0; k < N; k++) {
				s += MD[i][k] * MC1[k][j];
			}
			MU[i][j] = s * d1 + m1 * MD[i][j];
		}
	}

	// 13 - Сигнал T3 про завершення обчислень MU
	ReleaseSemaphore(S_MU[0], 1, NULL);

	cout << "T1 finished." << endl;

}


//-------------------------------------------T2-------------------------------------------
void T2() {
	int d2, s, m2;
	matrix MC2 = new vector[N];
	for (int i = 0; i < N; i++)
		MC2[i] = new int[N];

	cout << "T2 started." << endl;
	
	// 1 - Чекати на введення MD у задачі T1
	WaitForSingleObject(E1, INFINITE);
	
	// 2 - Чекати на введення MR, MC у задачі T3
	WaitForSingleObject(E3, INFINITE);
	
	// 3 - Чекати на введення Z, d у задачі T4
	WaitForSingleObject(E4, INFINITE);
	
	// 4 - Копіювати MC2 := MC	
	WaitForSingleObject(MTX, INFINITE);
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
			MC2[i][j] = MC[i][j];

	ReleaseMutex(MTX);

	// 5 - Копіювання d
	EnterCriticalSection(&CS);
	d2 = d;
	LeaveCriticalSection(&CS);

	// 6 - Обчислення m2 := max(Zн)
	m2 = 0;
	for (int i = H; i < 2 * H; i++)
		if (Z[i] > m2) m2 = Z[i];

	// 7- Обчислення m: = max(m, m2)
	EnterCriticalSection(&CS);
	m = max(m, m2);
	LeaveCriticalSection(&CS);

	// 8 - Сигнал Т3 про про завершення обчислень m
	ReleaseSemaphore(S_m[1], 1, NULL);
	
	// 9 - Чекати сигналу від T3 про завершення обчислень m
	WaitForSingleObject(S_max, INFINITE);
	
	// 10 - Копіювання m2 := m
	EnterCriticalSection(&CS);
	m2 = m;
	LeaveCriticalSection(&CS);

	// 11 - Обчислення MUн = MDн∙MC2∙d2 + m2∙MRн
	for (int i = H; i < 2 * H; i++) {
		for (int j = 0; j < N; j++) {
			s = 0;
			for (int k = 0; k < N; k++) {
				s += MD[i][k] * MC2[k][j];
			}
			MU[i][j] = s * d2 + m2 * MD[i][j];
		}
	}

	// 12 - Сигнал T3 про завершення обчислень MU
	ReleaseSemaphore(S_MU[1], 1, NULL);

	cout << "T2 finished." << endl;

}


//----------------------------------------T3----------------------------------------------
void T3() {
	int d3, s, m3;
	matrix MC3 = new vector[N];
	for (int i = 0; i < N; i++) {
		MC3[i] = new int[N];
		MU[i] = new int[N];
	};

	cout << "T3 started.\n" << endl;
	
	// 1 - Введення МR, МС
	for (int i = 0; i < N; i++) {
		MD[i] = new int[N];
		MC[i] = new int[N];
	};
	
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			MD[i][j] = 1;
			MC[i][j] = 1;
		}
	}

	// 2 - Сигнал задачам Т1, Т3, Т4 про введення МR, МC
	SetEvent(E3);
	
	// 3 - Чекати на введення MD у задачі T1
	WaitForSingleObject(E3, INFINITE);
	
	// 4 - Чекати на введення Z, d у задачі T4
	WaitForSingleObject(E4, INFINITE);
	
	// 5 - Копіювати MC3 := MC
	WaitForSingleObject(MTX, INFINITE);
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
			MC3[i][j] = MC[i][j];

	ReleaseMutex(MTX);
	
	// 6 - Копіювання d
	EnterCriticalSection(&CS);
	d3 = d;
	LeaveCriticalSection(&CS);

	// 7 - Обчислення m3 := max(Zн)
	m3 = 0;
	for (int i = 2 * H; i < 3 * H; i++)
		if (Z[i] > m3) m3 = Z[i];

	// 8 - Обчислення m: = max(m, m3)
	EnterCriticalSection(&CS);
	m = max(m, m3);
	LeaveCriticalSection(&CS);

	// 9 - Чекати на завершення обчислень m в T1, T2, T4
	WaitForMultipleObjects(3, S_m, TRUE, INFINITE);
	
	// 10 - Копіювання m1: = m
	EnterCriticalSection(&CS);
	m3 = m;
	LeaveCriticalSection(&CS);
	
	// 11 - Сигнал T1, T2, T4 про завершення обчислень m
	ReleaseSemaphore(S_max, 1, NULL);
	
	// 12 - Обчислення MUн = MDн∙MC3∙d + m3∙MRн
	for (int i = 0; i < H; i++) {
		for (int j = 0; j < N; j++) {
			s = 0;
			for (int k = 0; k < N; k++) {
				s += MD[i][k] * MC3[k][j];
			}
			MU[i][j] = s * d3 + m3 * MD[i][j];
		}
	}

	// 13 - Чекати на завершення обчислень MU в T1, T2, T4 
	WaitForMultipleObjects(3, S_MU, TRUE, INFINITE);
	
	// 14 - Виведення MU
	if (N < 10) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				cout << MU[i][j];
			}
			cout << endl;
		}
	}
	cout << "T3 finished." << endl;

}


//---------------------------------------T4-------------------------------------------------
void T4() {
	int d4, s, m4;
	matrix MC4 = new vector[N];
	for (int i = 0; i < N; i++)
		MC4[i] = new int[N];

	cout << "T4 started." << endl;
	
	// 1 - Введення Z, d
	for (int i = 0; i < N; i++)
		Z[i] = 1;

	d = 1;

	//2. Сигнал задачам T1, T2, T3 про введення Z, d
	SetEvent(E4);

	//3. Чекати на введення MD у задачі T1
	WaitForSingleObject(E1, INFINITE);

	//4. Чекати на введення MR, MC у задачі T3
	WaitForSingleObject(E3, INFINITE);

	//5. Копіювати MC4 := MC
	WaitForSingleObject(MTX, INFINITE);
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
			MC4[i][j] = MC[i][j];
	ReleaseMutex(MTX);
	
	// 6 - Копіювання d
	EnterCriticalSection(&CS);
	d4 = d;
	LeaveCriticalSection(&CS);

	// 7 - Обчислення m4 := max(Zн)
	m4 = 0;
	for (int i = 3 * H; i < N; i++)
		if (Z[i] > m4) m4 = Z[i];

	// 8 - Обчислення m: = max(m, m4)
	EnterCriticalSection(&CS);
	m = max(m, m4);
	LeaveCriticalSection(&CS);
	
	// 9 - Сигнал Т3 про про завершення обчислень m
	ReleaseSemaphore(S_m[2], 1, NULL);
	
	// 10 - Чекати сигналу від T3 про завершення обчислень m
	WaitForSingleObject(S_max, INFINITE);
	
	// 11 - Копіювання m4: = m
	EnterCriticalSection(&CS);
	m4 = m;
	LeaveCriticalSection(&CS);
	
	// 12 - Обчислення MUн = MDн∙MC4∙d4 + m4∙MRн
	for (int i = 3 * H; i < N; i++) {
		for (int j = 0; j < N; j++) {
			s = 0;
			for (int k = 0; k < N; k++) {
				s += MD[i][k] * MC4[k][j];
			}
			MU[i][j] = s * d4 + m4 * MD[i][j];
		}
	}
	
	// 13 - Сигнал T3 про завершення обчислень MU
	ReleaseSemaphore(S_MU[2], 1, NULL);

	cout << "T4 finished." << endl;
}



int main(int argc, char* argv[]) {
	cout << "Lab 1 started!\n" << endl;

	E4 = CreateEvent(NULL, TRUE, FALSE, NULL);
	E3 = CreateEvent(NULL, TRUE, FALSE, NULL);
	E1 = CreateEvent(NULL, TRUE, FALSE, NULL);

	InitializeCriticalSection(&CS);

	MTX = CreateMutex(NULL, FALSE, NULL);

	S_m[0] = CreateSemaphore(NULL, 0, 1, NULL);
	S_m[1] = CreateSemaphore(NULL, 0, 1, NULL);
	S_m[2] = CreateSemaphore(NULL, 0, 1, NULL);

	DWORD tid1, tid2, tid3, tid4;
	HANDLE threads[] = {
		 CreateThread(NULL, NULL, (LPTHREAD_START_ROUTINE)T1, NULL, NULL, &tid1),
		 CreateThread(NULL, NULL, (LPTHREAD_START_ROUTINE)T2, NULL, NULL, &tid2),
		 CreateThread(NULL, NULL, (LPTHREAD_START_ROUTINE)T3, NULL, NULL, &tid3),
		 CreateThread(NULL, NULL, (LPTHREAD_START_ROUTINE)T4, NULL, NULL, &tid4)
	};

	WaitForMultipleObjects(4, threads, true, INFINITE);

	SetThreadPriority(threads[0], THREAD_PRIORITY_NORMAL);
	SetThreadPriority(threads[1], THREAD_PRIORITY_NORMAL);
	SetThreadPriority(threads[2], THREAD_PRIORITY_HIGHEST);
	SetThreadPriority(threads[3], THREAD_PRIORITY_LOWEST);

	CloseHandle(threads[0]);
	CloseHandle(threads[1]);
	CloseHandle(threads[2]);
	CloseHandle(threads[3]);

	cout << "\nLab 1 finished!\n" << endl;
	char key;
	cin >> key;

	return 0;
}
