package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {


  public static void pcValueWriter(double[][] pcValues, String savePath) {

    try (var bw = new BufferedWriter(new FileWriter(savePath))) {
      StringBuilder sb = new StringBuilder();
      for (var row : pcValues) {
        for (int i = 0; i < row.length - 1; i++) {
          sb.append(row[i])
            .append(",");
        }
        sb.append(row[row.length - 1]);
      }
      bw.write(sb.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void meanValueWriter(List<Double> mean, String savePath) {

    try (var bw = new BufferedWriter(new FileWriter(savePath))) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < mean.size() - 1; i++) {
        sb.append(mean.get(i))
          .append(",");
      }
      sb.append(mean.get(mean.size() - 1));

      bw.write(sb.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public double pcValueLoader(String savePath) {
    try (var reader = new BufferedReader(new FileReader(savePath))) {
      return Double.parseDouble(reader.readLine()
                                      .split(":")[1]);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public double[][] meanValueLoader(String savePath) {
    try (var reader = new BufferedReader(new FileReader(savePath))) {
      List<double[]> list = new ArrayList<>();
      while (reader.ready()) {
        list.add(Arrays.stream(reader.readLine()
                                     .split(","))
                       .mapToDouble(Double::parseDouble)
                       .toArray()
        );
      }
      double[][] result = new double[list.size()][];
      for (int i = 0; i < list.size(); i++) {
        result[i] = list.get(i);
      }
      return result;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
