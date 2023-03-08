package uk.ac.imperial.matrixmult;

public class CacheMatrix implements Matrix{



  public CacheMatrix(double[][] matrix) {
    this.matrix = matrix;
  }

  public CacheMatrix(int row, int col) {
    matrix = new double[row][col];
  }

  private double[][] matrix;

  @Override
  public double get(int row, int column) {
    return matrix[row][column];
  }

  @Override
  public void set(int row, int column, double value) {
    matrix[row][column] = value;
  }

  @Override
  public int getNumRows() {
    return matrix.length;
  }

  @Override
  public int getNumColumns() {
    return matrix[0].length;
  }

  public double[][] getMatrix() {
    return matrix;
  }
}
