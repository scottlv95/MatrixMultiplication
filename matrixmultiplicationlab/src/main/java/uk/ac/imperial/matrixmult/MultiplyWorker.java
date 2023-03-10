//package uk.ac.imperial.matrixmult;
//
//
//import static uk.ac.imperial.matrixmult.SMultiply.naiveMultiply;
//
//public class MultiplyWorker extends Thread {
//  private BasicMatrix a, b, c;
//  private boolean isConcurrent;
//
//  public MultiplyWorker(BasicMatrix c, BasicMatrix a, BasicMatrix b, boolean isConcurrent) {
//    this.a = a;
//    this.b = b;
//    this.isConcurrent = isConcurrent;
//  }
//
//  public static BasicMatrix addMatrix(BasicMatrix a, BasicMatrix b) {
//    if (a.getNumRows() != b.getNumRows() || a.getNumColumns() != b.getNumColumns()) {
//      throw new IllegalArgumentException("Invalid Addition Dimension - two matrices must have same dimensions");
//    }
//    BasicMatrix c = new BasicMatrix(a.getNumRows(), a.getNumColumns());
//    for (int i = 0; i < a.getNumRows(); i++) {
//      for (int j = 0; j < a.getNumColumns(); j++) {
//        c.set(i, j, a.get(i, j) + b.get(i, j));
//      }
//    }
//    return c;
//  }
//
//  public static BasicMatrix subtractMatrix(BasicMatrix a, BasicMatrix b) {
//    if (a.getNumRows() != b.getNumRows() || a.getNumColumns() != b.getNumColumns()) {
//      throw new IllegalArgumentException("Invalid Addition Dimension - two matrices must have same dimensions");
//    }
//    BasicMatrix c = new BasicMatrix(a.getNumRows(), a.getNumColumns());
//    for (int i = 0; i < a.getNumRows(); i++) {
//      for (int j = 0; j < a.getNumColumns(); j++) {
//        c.set(i, j, a.get(i, j) - b.get(i, j));
//      }
//    }
//    return c;
//  }
//
//  public BasicMatrix getC() {
//    return c;
//  }
//
//  @Override
//  public void run() {
//
//    int m = a.getNumRows();
//
//    if (m == 1) {
//      c.set(0, 0, (a.get(0, 0) * b.get(0, 0)));
//    } else if (m <= 128) {
//      c = naiveMultiply(a, b);
//
//    } else if (!isConcurrent){
//      c = SMultiply.multiply(a,b);
//    }
//    else {
//      int orgM = m;
//      if (m % 2 == 1) {
//        a = a.pad(m + 1, m + 1);
//        b = b.pad(m + 1, m + 1);
//      }
//      m = a.getNumRows();
//      int half = m / 2;
//
//      BasicMatrix a11 = a.getSubMatrix(0, 0, half, half);
//      BasicMatrix a12 = a.getSubMatrix(0, half, half, half);
//      BasicMatrix a21 = a.getSubMatrix(half, 0, half, half);
//      BasicMatrix a22 = a.getSubMatrix(half, half, half, half);
//      BasicMatrix b11 = b.getSubMatrix(0, 0, half, half);
//      BasicMatrix b12 = b.getSubMatrix(0, half, half, half);
//      BasicMatrix b21 = b.getSubMatrix(half, 0, half, half);
//      BasicMatrix b22 = b.getSubMatrix(half, half, half, half);
//
//      BasicMatrix m1 = new BasicMatrix(new double[half][half]);
//      BasicMatrix m2 = new BasicMatrix(new double[half][half]);
//      BasicMatrix m3 = new BasicMatrix(new double[half][half]);
//      BasicMatrix m4 = new BasicMatrix(new double[half][half]);
//      BasicMatrix m5 = new BasicMatrix(new double[half][half]);
//      BasicMatrix m6 = new BasicMatrix(new double[half][half]);
//      BasicMatrix m7 = new BasicMatrix(new double[half][half]);
//      MultiplyWorker worker1 = new MultiplyWorker(m1, addMatrix(a11, a22), addMatrix(b11, b22),true);
//      MultiplyWorker worker2 = new MultiplyWorker(m2, addMatrix(a21, a22), b11,false);
//      MultiplyWorker worker3 = new MultiplyWorker(m3, a11, subtractMatrix(b12, b22),false);
//      MultiplyWorker worker4 = new MultiplyWorker(m4, a22, subtractMatrix(b21, b11),false);
//      MultiplyWorker worker5 = new MultiplyWorker(m5, addMatrix(a11, a12), b22,false);
//      MultiplyWorker worker6 = new MultiplyWorker(m6, subtractMatrix(a21, a11), addMatrix(b11, b12),false);
//      MultiplyWorker worker7 = new MultiplyWorker(m7, subtractMatrix(a12, a22), addMatrix(b21, b22),false);
//      worker1.start();
//      worker2.start();
//      worker3.start();
//      worker4.start();
//      worker5.start();
//      worker6.start();
//      worker7.start();
//
//      try {
//        worker1.join();
//        worker2.join();
//        worker3.join();
//        worker4.join();
//        worker5.join();
//        worker6.join();
//        worker7.join();
//      } catch (InterruptedException e) {
//        throw new RuntimeException(e);
//      }
//      m1 = worker1.getC();
//      m2 = worker2.getC();
//      m3 = worker3.getC();
//      m4 = worker4.getC();
//      m5 = worker5.getC();
//      m6 = worker6.getC();
//      m7 = worker7.getC();
//      BasicMatrix c11 = addMatrix(m1, addMatrix(m4, subtractMatrix(m7, m5)));
//      BasicMatrix c12 = addMatrix(m3, m5);
//      BasicMatrix c21 = addMatrix(m2, m4);
//      BasicMatrix c22 = addMatrix(m1, addMatrix(m3, subtractMatrix(m6, m2)));
//
//      c = BasicMatrix.joinMatrix(c11, c12, c21, c22);
//      c = c.getSubMatrix(0, 0, orgM, orgM);
//    }
//  }
//}
