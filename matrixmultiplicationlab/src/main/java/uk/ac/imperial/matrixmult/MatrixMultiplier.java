package uk.ac.imperial.matrixmult;

public class MatrixMultiplier{

  public static Matrix multiply(Matrix a, Matrix b) throws Exception {
    if (a.getNumRows()<=128) {
      return naiveMultiply(a, b);
    }
    else {
      BasicMatrix dcA = (BasicMatrix) a;
      BasicMatrix dcB = (BasicMatrix) b;
      return SMultiply.multiplyFinal(dcA,dcB);
    }
  }


  public static Matrix naiveMultiply(Matrix a, Matrix b) throws Exception{

      // basic for perhaps small cases

      int aRow = a.getNumRows();
      int aCol = a.getNumColumns();
      int bRow = b.getNumRows();
      int bCol = b.getNumColumns();

      Matrix result = new BasicMatrix(aRow, bCol);

      if (aCol != bRow) {
        throw new IllegalArgumentException("Invalid Dimension");
      }

      for (int i = 0; i < aRow; i++) {
        for (int j = 0; j < bCol; j++) {
          double value = 0;
          for (int k = 0; k < bRow; k++){
            value += a.get(i,k) * b.get(k,j);
          }
          result.set(i,j,value);
        }
      }
      return result;
    }


}
