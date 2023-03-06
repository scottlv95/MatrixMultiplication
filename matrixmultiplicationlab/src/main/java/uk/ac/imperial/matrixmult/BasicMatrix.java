package uk.ac.imperial.matrixmult;

public class BasicMatrix implements Matrix{

  private double[][] matrix;
  private final int numRows;
  private final int numColumns;


  public BasicMatrix(double[][] matrix) {
    this.matrix = matrix;
    this.numRows = matrix.length;
    this.numColumns = matrix[0].length;
  }

  public BasicMatrix(int numRows, int numColumns) {
    matrix = new double[numRows][numColumns];
    this.numRows = numRows;
    this.numColumns = numColumns;
  }

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
    return numRows;
  }

  @Override
  public int getNumColumns() {
    return numColumns;
  }
}
