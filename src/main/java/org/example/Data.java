package org.example;

import org.example.exception.MatrixException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the information of a data set. Each row contains a single data point. Primary computations
 * of PCA are performed by the Data object.
 *
 * @author Kushal Ranjan
 * @version 051313
 */
public class Data {

  // 1600 * 2700
//  double[][] matrix; //matrix[i] is the ith row; matrix[i][j] is the ith row, jth column


  /**
   * Constructs a new data matrix.
   *
   * @param vals data for new Data object; dimensions as columns, data points as rows.
   */
//  Data(double[][] vals) {
//    matrix = Matrix.copy(vals);
//  }


  /**
   * Performs principal component analysis with a specified number of principal components.
   *
   * @param input         input data; each double[] in input is an array of values of a single
   *                      variable for each data point
   * @param numComponents number of components desired
   * @return the transformed data set
   */
  public double[][] principalComponentAnalysis(double[][] input, int numComponents) {
    Instant startCenter = Instant.now();
    double[][] centeredMatrix = center(input);
    System.out.println("center :: " + Duration.between(startCenter, Instant.now())
                                              .toMillis() + " ms");

    Instant starteigen = Instant.now();
    EigenSet eigen = getCovarianceEigenSet(centeredMatrix);
    System.out.println("eigen :: " + Duration.between(starteigen, Instant.now())
                                             .toMillis() + " ms");

    Instant startfeatureVector = Instant.now();
    double[][] featureVector = buildPrincipalComponents(numComponents, eigen);
    System.out.println("featureVector :: " + Duration.between(startfeatureVector, Instant.now())
                                                     .toMillis() + " ms");
    Instant startPCVector = Instant.now();
    double[][] PC = Matrix.transpose(featureVector);
    System.out.println("PC :: " + Duration.between(startPCVector, Instant.now())
                                          .toMillis() + " ms");

    Utils.pcValueWriter(PC, Main.pcValuePath);

    double[][] inputTranspose = Matrix.transpose(input);
    return Matrix.transpose(Matrix.multiply(PC, inputTranspose));
  }

  /**
   * Returns a list containing the principal components of this data set with the number of
   * loadings specified.
   *
   * @param numComponents the number of principal components desired
   * @param eigen         EigenSet containing the eigenvalues and eigenvectors
   * @return the numComponents most significant eigenvectors
   */
  double[][] buildPrincipalComponents(int numComponents, EigenSet eigen) {
    double[] vals = eigen.values;
    if (numComponents > vals.length) {
      throw new RuntimeException("Cannot produce more principal components than those provided.");
    }
    boolean[] chosen = new boolean[vals.length];
    double[][] vecs = eigen.vectors;
    double[][] PC = new double[numComponents][];
    for (int i = 0; i < PC.length; i++) {
      int max = 0;
      while (chosen[max]) {
        max++;
      }
      for (int j = 0; j < vals.length; j++) {
        if (Math.abs(vals[j]) > Math.abs(vals[max]) && !chosen[j]) {
          max = j;
        }
      }
      chosen[max] = true;
      PC[i] = vecs[max];
    }
    return PC;
  }

  /**
   * Uses the QR algorithm to determine the eigenvalues and eigenvectors of the covariance
   * matrix for this data set. Iteration continues until no eigenvalue changes by more than
   * 1/10000.
   *
   * @return an EigenSet containing the eigenvalues and eigenvectors of the covariance matrix
   */
  EigenSet getCovarianceEigenSet(double[][] input) {
    double[][] data = covarianceMatrix(input);
    Instant start = Instant.now();
    EigenSet result = Matrix.eigenDecomposition(data);
    System.out.println("eigenDecomposition spent time :: " + (Duration.between(start, Instant.now())
                                                                      .toMillis() * 1000) / 1000 + " ms");
    return result;
  }

  /**
   * Constructs the covariance matrix for this data set.
   *
   * @return the covariance matrix of this data set
   */
  double[][] covarianceMatrix(double[][] input) {
    double[][] out = new double[input.length][input.length];
    for (int i = 0; i < out.length; i++) {
      for (int j = 0; j < out.length; j++) {
        double[] dataA = input[i];
        double[] dataB = input[j];
        out[i][j] = covariance(dataA, dataB);
      }
    }
    return out;
  }

  /**
   * Returns the covariance of two data vectors.
   *
   * @param a double[] of data
   * @param b double[] of data
   * @return the covariance of a and b, cov(a,b)
   */
  double covariance(double[] a, double[] b) {
    if (a.length != b.length) {
      throw new MatrixException("Cannot take covariance of different dimension vectors.");
    }
    double divisor = a.length - 1;
    double sum = 0;
    double aMean = mean(a);
    double bMean = mean(b);
    for (int i = 0; i < a.length; i++) {
      sum += (a[i] - aMean) * (b[i] - bMean);
    }
    return sum / divisor;
  }

  /**
   * Centers each column of the data matrix at its mean.
   *
   * @return
   */
  double[][] center(double[][] mat) {
    return normalize(mat);
  }


  /**
   * Normalizes the input matrix so that each column is centered at 0.
   */
  double[][] normalize(double[][] input) {
    double mean = 0.0;
    double[][] out = new double[input.length][input[0].length];

    List<Double> meanValue = new ArrayList<>();

    for (int i = 0; i < input.length; i++) {
      mean = mean(input[i]);
      meanValue.add(mean);
      for (int j = 0; j < input[i].length; j++) {
        out[i][j] = input[i][j] - mean;
      }
    }
    Utils.meanValueWriter(meanValue, Main.meanValuePath);
    return out;
  }

  /**
   * Calculates the mean of an array of doubles.
   *
   * @param entries input array of doubles
   */
  double mean(double[] entries) {
    double out = 0.0;
    for (double d : entries) {
      out += d / entries.length;
    }
    return out;
  }
}