package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

  public static String pcValuePath = null;

  public static String meanValuePath = null;

  public static void main(String[] args) throws IOException {
    // 파이썬에서 전처리 후 저장된 경로
    String beforePcaPath = args[0];
    int numComponent = Integer.parseInt(args[1]);
    Main.pcValuePath = args[2];
    Main.meanValuePath = args[3];


    double[][] data = null;
    List<double[]> tmp = new ArrayList<>();
    try (var bis = new BufferedReader(new FileReader(beforePcaPath))) {
      while (bis.ready()) {
        double[] arr = Arrays.stream(bis.readLine()
                                        .split(","))
                             .mapToDouble(value -> Double.parseDouble(value.replaceAll(",", "")
                                                                           .replaceAll(" ", "")))
                             .toArray();
        tmp.add(arr);
      }
    } catch (IOException e) {
      return;
    }

    data = new double[tmp.size()][0];
    for (int i = 0; i < data.length; i++) {
      data[i] = new double[tmp.get(i).length];
      for (int j = 0; j < tmp.get(i).length; j++) {
        data[i][j] = tmp.get(i)[j];
      }
    }
    tmp = null;


    double[][] afterPca = Data.principalComponentAnalysis(Matrix.transpose(data), numComponent);
    matrixPrint(Matrix.transpose(afterPca));

  }

  private double[][] transPose(double[][] mat) {
    double[][] result = new double[mat[0].length][mat.length];
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[i].length; j++) {
        result[i][j] = mat[j][i];
      }
    }
    return result;
  }

  private static void matrixPrint(double[][] matrix) {
    for (var row : matrix) {
      for (int i = 0; i < row.length - 1; i++) {
        System.out.print(row[i] + ",");
      }
      System.out.println(row[row.length - 1]);
    }
  }

}
