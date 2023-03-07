package uk.ac.imperial.matrixmult;


public class SMultiply {



  public static BasicMatrix multiplyFinal(BasicMatrix a, BasicMatrix b) {
    int r1 = a.getNumRows();
    int c2 = b.getNumColumns();
    int x = Math.max(a.getNumRows(),a.getNumColumns());
    int y = Math.max(b.getNumRows(),b.getNumColumns());
    int m = Math.max(x,y);
    a = a.pad(m,m);
    b = b.pad(m,m);
    BasicMatrix c = new BasicMatrix(new double[m][m]);
    MultiplyWorker mainWorker = new MultiplyWorker(c,a,b,true);
    mainWorker.start();
    try {
      mainWorker.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    c = mainWorker.getC();
    c = c.getSubMatrix(0,0,r1,c2);
    return c;
  }

  // assume square matrices first mxm;
  public static BasicMatrix multiply(BasicMatrix a, BasicMatrix b) {

    int m = a.getNumRows();
    BasicMatrix c = new BasicMatrix(m,m);

    if (m == 1) {
      c.set(0,0,(a.get(0,0)*b.get(0,0)));
    }
    else if (m<=128) {
      c=naiveMultiply(a,b);
    }
    else {
      int orgM = m;
      if (m%2 == 1) {
        a = a.pad(m+1,m+1);
        b = b.pad(m+1,m+1);
      }
      m = a.getNumRows();
      int half = m/2;

      BasicMatrix a11 = a.getSubMatrix(0,0,half,half);
      BasicMatrix a12 = a.getSubMatrix(0,half,half,half);
      BasicMatrix a21 = a.getSubMatrix(half,0,half,half);
      BasicMatrix a22 = a.getSubMatrix(half,half,half,half);
      BasicMatrix b11 = b.getSubMatrix(0,0,half,half);
      BasicMatrix b12 = b.getSubMatrix(0,half,half,half);
      BasicMatrix b21 = b.getSubMatrix(half,0,half,half);
      BasicMatrix b22 = b.getSubMatrix(half,half,half,half);

      BasicMatrix m1 = multiply(addMatrix(a11,a22),addMatrix(b11,b22));
      BasicMatrix m2 = multiply(addMatrix(a21,a22),b11);
      BasicMatrix m3 = multiply(a11,subtractMatrix(b12,b22));
      BasicMatrix m4 = multiply(a22, subtractMatrix(b21,b11));
      BasicMatrix m5 = multiply(addMatrix(a11,a12),b22);
      BasicMatrix m6 = multiply(subtractMatrix(a21,a11),addMatrix(b11,b12));
      BasicMatrix m7 = multiply(subtractMatrix(a12,a22),addMatrix(b21,b22));

      BasicMatrix c11 = addMatrix(m1,addMatrix(m4,subtractMatrix(m7,m5)));
      BasicMatrix c12 = addMatrix(m3,m5);
      BasicMatrix c21 = addMatrix(m2,m4);
      BasicMatrix c22 = addMatrix(m1,addMatrix(m3,subtractMatrix(m6,m2)));

      c = BasicMatrix.joinMatrix(c11,c12,c21,c22);
      c = c.getSubMatrix(0,0,orgM,orgM);
    }
    return c;
  }




  public static BasicMatrix addMatrix(BasicMatrix a, BasicMatrix b) {
    if (a.getNumRows() != b.getNumRows() || a.getNumColumns() != b.getNumColumns()) {
      throw new IllegalArgumentException("Invalid Addition Dimension - two matrices must have same dimensions");
    }
    BasicMatrix c = new BasicMatrix(a.getNumRows(), a.getNumColumns());
    for (int i = 0; i< a.getNumRows(); i++) {
      for (int j = 0; j < a.getNumColumns(); j++){
        c.set(i,j,a.get(i,j)+b.get(i,j));
      }
    }
    return c;
  }

  public static BasicMatrix subtractMatrix(BasicMatrix a, BasicMatrix b) {
    if (a.getNumRows() != b.getNumRows() || a.getNumColumns() != b.getNumColumns()) {
      throw new IllegalArgumentException("Invalid Addition Dimension - two matrices must have same dimensions");
    }
    BasicMatrix c = new BasicMatrix(a.getNumRows(), a.getNumColumns());
    for (int i = 0; i< a.getNumRows(); i++) {
      for (int j = 0; j < a.getNumColumns(); j++){
        c.set(i,j,a.get(i,j)-b.get(i,j));
      }
    }
    return c;
  }
  public static BasicMatrix naiveMultiply(BasicMatrix a, BasicMatrix b) {

    // basic for perhaps small cases
    int aRow = a.getNumRows();
    int aCol = a.getNumColumns();
    int bRow = b.getNumRows();
    int bCol = b.getNumColumns();

    BasicMatrix result = new BasicMatrix(aRow, bCol);

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
