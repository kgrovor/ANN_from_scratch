public class MyUtil {
    public static double activation(double input,String func){
        if(func.equals("sigmoid")){
            return 1/(1+Math.exp(-input));
        }
        else return input;
    }
    public static double[][] trasposeMatrix(double[][] matrix)
    {
        int m = matrix.length;
        int n = matrix[0].length;

        double[][] trasposedMatrix = new double[n][m];

        for(int x = 0; x < n; x++)
        {
            for(int y = 0; y < m; y++)
            {
                trasposedMatrix[x][y] = matrix[y][x];
            }
        }

        return trasposedMatrix;
    }
}

