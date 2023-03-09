package uk.ac.imperial.matrixmult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CacheMultiply {



    public static double[][] multiply(double[][] A, double[][] B) {
      int m = A.length;
      int n = B.length;
      int p = B[0].length;
      double[][] C = new double[m][p];


      // Transpose matrix B to maximize cache reuse
      double[][] BT = new double[p][n];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < p; j++) {
          BT[j][i] = B[i][j];
        }
      }

      // Compute matrix multiplication with cache optimization
      for (int i = 0; i < m; i++) {
        for (int j = 0; j < p; j++) {
          int sum = 0;
          for (int k = 0; k < n; k++) {
            sum += A[i][k] * BT[j][k];
          }
          C[i][j] = sum;
        }
      }
      return C;
    }

  public static double[][] blockMultiply(double[][] A, double[][] B, double blockSize) {
    int n = A.length;
    int m = A[0].length;
    int p = B[0].length;
    double[][] C = new double[n][p];

    // Compute matrix multiplication with block optimization
    for (int i = 0; i < n; i += blockSize) {
      for (int j = 0; j < p; j += blockSize) {
        for (int k = 0; k < m; k += blockSize) {
          // Compute block multiplication
          for (int ii = i; ii < Math.min(i + blockSize, n); ii++) {
            for (int jj = j; jj < Math.min(j + blockSize, p); jj++) {
              int sum = 0;
              for (int kk = k; kk < Math.min(k + blockSize, m); kk++) {
                sum += A[ii][kk] * B[kk][jj];
              }
              C[ii][jj] += sum;
            }
          }
        }
      }
    }
    return C;
  }

  public static double[][] concurrentBlockMultiply(double[][] A, double[][] B, double blockSize) {
    int n = A.length;
    int m = A[0].length;
    int p = B[0].length;
    double[][] C = new double[n][p];
    int numThreads = 16;

    double[][] B_transposed = new double[p][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < p; j++) {
        B_transposed[j][i] = B[i][j];
      }
    }
    ExecutorService executor = Executors.newFixedThreadPool(numThreads);

    List<Future<?>> futures = new ArrayList<>();

    // compute matrix multiplication with block optimization
    for (int i = 0; i < n; i += blockSize) {
      for (int j = 0; j < p; j += blockSize) {
        for (int k = 0; k < m; k += blockSize) {
          int finalI = i;
          int finalJ = j;
          int finalK = k;
          // Compute block multiplication
          futures.add(executor.submit(()-> {
                for (int ii = finalI; ii < Math.min(finalI + blockSize, n); ii++) {
                  for (int jj = finalJ; jj < Math.min(finalJ + blockSize, p); jj++) {
                    int sum = 0;
                    for (int kk = finalK; kk < Math.min(finalK + blockSize, m); kk++) {
                      sum += A[ii][kk] * B_transposed[jj][kk];
                    }
                    C[ii][jj] += sum;
                  }
                }
              }
          ));

        }
      }
    }

    for (Future<?> future : futures) {
      try {
        future.get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }

    }

    return C;
  }


  public static double[][] blockMultiplyConcurrent(double[][] A, double[][] B, int blockSize, int numThreads) {
    int n = A.length;
    int m = B.length;
    int p = B[0].length;
    double[][] C = new double[n][p];

    double[][] B_transposed = new double[p][m];
    for (int i = 0; i < m; i++) {
      for (int j = 0; j < p; j++) {
        B_transposed[j][i] = B[i][j];
      }
    }

    ExecutorService executor = Executors.newFixedThreadPool(numThreads);
    // Compute matrix multiplication with block optimization

    for (int i = 0; i < n; i += blockSize) {
      for (int j = 0; j < p; j += blockSize) {
        for (int k = 0; k < m; k += blockSize) {
          final int startI = i;
          final int endI = Math.min(i + blockSize, n);
          final int startJ = j;
          final int endJ = Math.min(j + blockSize, p);
          final int startK = k;
          final int endK = Math.min(k + blockSize, m);

          executor.submit(new Runnable() {
            public void run() {
              // Compute block multiplication
              for (int ii = startI; ii < endI; ii++) {
                for (int jj = startJ; jj < endJ; jj++) {
                  int sum = 0;
                  for (int kk = startK; kk < endK; kk++) {
                    sum += A[ii][kk] * B_transposed[jj][kk];
                  }
                  synchronized (C) {
                    C[ii][jj] += sum;
                  }
                }
              }
            }
          });
        }
      }
    }

    executor.shutdown();
    try {
      executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    return C;
  }

  }

