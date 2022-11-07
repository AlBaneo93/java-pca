package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException {
    // 파이썬에서 전처리 후 저장된 경로
    String dataRoot = args[0];
    int numComponent = Integer.parseInt(args[1]);

    double[][] data = null;
    List<double[]> tmp = new ArrayList<>();
    try (var bis = new BufferedReader(new FileReader(dataRoot))) {
      while (bis.ready()) {
        var arr = Arrays.stream(bis.readLine()
                                   .replaceAll("]", "")
                                   .replaceAll("\\[", "")
                                   .split(" "))
                        .filter(s -> !s.isBlank())
                        .mapToDouble(value ->
                            Double.parseDouble(value.replaceAll(",", "")
                                                    .replaceAll(" ", ""))
                        )
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


    double[][] afterPca = Data.PCANIPALS(transPose(data), numComponent);
    matrixPrint(afterPca);

//    StringBuilder sb = new StringBuilder();
//    for (var darr : afterPca) {
//      for (int i = 0; i < darr.length - 1; i++) {
//        sb.append(darr[i])
//          .append(",");
//      }
//      sb.append(darr[darr.length - 1])
//        .append("\n");
//    }
//
//    System.out.print(sb);

  }

  private static double[][] transPose(double[][] mat) {
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
        System.out.print(row[i] + " ");
      }
      System.out.println(row[row.length - 1]);
    }
  }
}
