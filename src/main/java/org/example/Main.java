package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

  public static String pcValuePath = null;

  public static String meanValuePath = null;

  public static void main(String[] args) throws IOException {
    // 파이썬에서 전처리 후 저장된 경로
    String beforePcaPath = "/Users/alban/Downloads/pca_test_test.txt";
    int numComponent = 50;
    Main.pcValuePath = "/Users/alban/Downloads/pcaValue.txt";
    Main.meanValuePath = "/Users/alban/Downloads/meanValue.txt";
    String resultSavePath = "/Users/alban/Downloads/afterPCA.txt";

    double[][] data = null;
    System.out.println("파일 읽기 시작");

    double n = Math.pow(10.0, 7);

    List<double[]> tmp = new ArrayList<>();
    try (var bis = new BufferedReader(new FileReader(beforePcaPath))) {
      while (bis.ready()) {
        double[] arr = Arrays.stream(bis.readLine()
                                        .split(","))
                             .mapToDouble(value -> {
                               double d = Double.parseDouble(value.replaceAll(",", "")
                                                                  .replaceAll(" ", ""));
                               return Math.round(d * n) / n;
                             })
                             .toArray();
        tmp.add(arr);
      }
    } catch (IOException e) {
      return;
    }

    System.out.println("파일 읽기 종료");

    data = new double[tmp.size()][0];
    for (int i = 0; i < data.length; i++) {
      data[i] = new double[tmp.get(i).length];
      for (int j = 0; j < tmp.get(i).length; j++) {
        data[i][j] = tmp.get(i)[j];
      }
    }
    tmp = null;
    System.out.println("PCA 시작");
    Data pcaData = new Data();
    double[][] afterPca = pcaData.principalComponentAnalysis(Matrix.transpose(data), numComponent);
    System.out.println("PCA 종료");
    matrixPrint(Matrix.transpose(afterPca));
    savePCAResult(afterPca, resultSavePath);
  }

  private static void matrixPrint(double[][] matrix) {
    for (var row : matrix) {
      for (int i = 0; i < row.length - 1; i++) {
        System.out.print(row[i] + ",");
      }
      System.out.println(row[row.length - 1]);
    }
  }

  private static void savePCAResult(double[][] pcaResult, String savePath) {
    try (var br = new BufferedWriter(new FileWriter(savePath))) {
      System.out.println("PCA 결과 저장 시작");

      for (double[] doubles : pcaResult) {
        StringBuilder sb = new StringBuilder();
        for (double d : doubles) {
          sb.append(d)
            .append(",");
        }
        sb.append("\n");
        br.write(sb.toString());
      }

      System.out.println("PCA 결과 저장 완료");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
