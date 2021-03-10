class Calculations {
    private static int H = Main.H;

    public static void inputVector(int[] Vect, int val){
        for (int i = 0; i < Vect.length; i++){
            Vect[i] = val;
        }
    }

    public static void inputMatrix(int[][] Matr, int val){
        for (int i = 0; i < Matr.length; i++)
            for (int j = 0; j < Matr[i].length; j++)
                Matr[i][j] = val;
    }

    public static void outputVector(int[] Vect){
        for (int aVect : Vect) {
            System.out.print(aVect + " ");
        }
    }
    public static int vectorMin(int[] vector, int id)
    {
        int min = vector[H * id];
        for (int i = H * id; i < H * (id + 1); i++) {
            if (vector[i] < min)
                min = vector[i];
        }
        return min;
    }
}