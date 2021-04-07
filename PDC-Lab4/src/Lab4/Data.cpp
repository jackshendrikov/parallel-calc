#include <windows.h>
#include <iostream>
#include "Data.h"

using namespace std;

// ------------------- Fill Matrix/Vector With Specific Number -------------------
vector inVector(int value) {
	vector result = new int[N];
	for (int i = 0; i < N; i++)
		result[i] = value;
	
	return result;
}

matrix inMatrix(int value) {
	matrix result = new vector[N];
	for (int i = 0; i < N; i++)
		result[i] = new int[N];

	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
			result[i][j] = value;

	return result;
}


// ------------- Print Vector Into Console --------------
void outVector(vector vec, char name) {
	cout << "\nVector " << name << ": ";
	for (int i = 0; i < N; i++)
	{
		cout << vec[i] << " ";
	}
	cout << "\n" << endl;
}


// ------------- Copy Vector, Matrix --------------
vector copyVector(vector vec) {
	vector result = new int[N];
	for (int i = 0; i < N; i++)
	{
		result[i] = vec[i];
	}
	return result;
}

matrix copyMatrix(matrix matr) {
	matrix result = new vector[N];
	for (int i = 0; i < N; i++)
		result[i] = new int[N];

	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
			result[i][j] = matr[i][j];

	return result;
}
