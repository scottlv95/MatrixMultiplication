package uk.ac.imperial.matrixmult;

// idea is to have imaginary "padded zeros" and also subMatrix just change the startPositions
public class BasicMatrix implements Matrix {

  private double[][] matrix;
  private int startRow, startCol, numRows, numCols;

  // 0 index startPos
  public BasicMatrix(double[][] matrix, int startRow, int startCol, int numRows, int numCols) {
    this.matrix = matrix;
    this.startRow = startRow;
    this.startCol = startCol;
    this.numRows = numRows;
    this.numCols = numCols;
  }

  public BasicMatrix(int startRow, int startCol, int numRows, int numCols) {
    matrix = new double[startRow + numRows][startCol + numCols];
    this.startRow = startRow;
    this.startCol = startCol;
    this.numRows = numRows;
    this.numCols = numCols;
  }

  public BasicMatrix(double[][] matrix) {
    this(matrix,0,0,matrix.length,matrix[0].length);
  }


  public BasicMatrix(int numRows, int numCols) {
    this(0, 0, numRows, numCols);
  }

  public static BasicMatrix joinMatrix(Matrix m11, Matrix m12, Matrix m21, Matrix m22) {
    int resultRow = m11.getNumRows() + m21.getNumRows();
    int resultCol = m11.getNumColumns() + m12.getNumColumns();
    BasicMatrix result = new BasicMatrix(resultRow, resultCol);
    for (int i = 0; i < m11.getNumRows(); i++) {
      for (int j = 0; j < m11.getNumColumns(); j++) {
        result.set(i, j, m11.get(i, j));
      }
    }
    for (int i = m11.getNumRows(); i < resultRow; i++) {
      for (int j = 0; j < m21.getNumColumns(); j++) {
        result.set(i, j, m21.get(i - m21.getNumRows(), j));
      }
    }
    for (int i = 0; i < m11.getNumRows(); i++) {
      for (int j = m12.getNumColumns(); j < resultCol; j++) {
        result.set(i, j, m12.get(i, j - m12.getNumColumns()));
      }
    }
    for (int i = m22.getNumRows(); i < resultRow; i++) {
      for (int j = m22.getNumColumns(); j < resultCol; j++) {
        result.set(i, j, m22.get(i - m22.getNumRows(), j - m22.getNumRows()));
      }
    }
    return result;
  }

  public int getStartRow() {
    return startRow;
  }

  public int getStartCol() {
    return startCol;
  }

  @Override
  public double get(int row, int column) {
    if (startRow + row >= matrix.length || startCol + column >= matrix[0].length) {
      return 0;
    }

    return matrix[startRow + row][startCol + column];
  }

  @Override
  public void set(int row, int column, double value) {
    matrix[startRow + row][startCol + column] = value;
  }

  @Override
  public int getNumRows() {
    return numRows;
  }

  @Override
  public int getNumColumns() {
    return numCols;
  }

  public BasicMatrix getSubMatrix(int startRow, int startCol, int numRows, int numCols) {
    return new BasicMatrix(matrix, startRow + this.startRow, startCol + this.startCol, numRows, numCols);
  }

  // we make a new copy, with startingPos at 0; not really the same matrix
  public BasicMatrix copy() {
    BasicMatrix copy = new BasicMatrix(new double[numRows][numCols]);
    for (int i = 0; i<numRows;i++) {
      for (int j = 0; j<numCols; j++) {
        copy.set(i,j,get(i,j));
      }
    }
    return copy;
  }

  public BasicMatrix pad(int row, int col) {
    BasicMatrix pad = new BasicMatrix(new double[row][col]);
    for (int i = 0; i<numRows;i++) {
      for (int j = 0; j<numCols; j++) {
        pad.set(i,j,get(i,j));
      }
    }
    return pad;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < numRows; i++){
      for (int j = 0; j< numCols; j++) {
        sb.append(get(i,j) + " ");
      }
      sb.append('\n');
    }
    return sb.toString();
  }
  public double[] getMatrix() {
    return null;
  }
}
