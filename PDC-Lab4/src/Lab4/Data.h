typedef int* vector;
typedef int** matrix;

extern int N;

vector inVector(int);
matrix inMatrix(int);

void outVector(vector, char);

vector copyVector(vector);
matrix copyMatrix(matrix);