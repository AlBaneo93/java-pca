package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {


  public static void pcValueWriter(double[][] pcValues, String fileName) {

    try (var fwriter = new BufferedWriter(new FileWriter(fileName))) {
      StringBuilder sb = null;
      for (var row : pcValues) {
        sb = new StringBuilder();
        for (int i = 0; i < row.length - 1; i++) {
          sb.append(row[i])
            .append(",");
        }
        sb.append(row[row.length - 1]);
      }
      fwriter.write(sb.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void meanValueWriter(double mean, String fileName) {

    try (var fwriter = new BufferedWriter(new FileWriter(fileName))) {
      fwriter.write("mean:" + mean);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public double pcValueLoader(String pcaValuePath) {
    try (var reader = new BufferedReader(new FileReader(pcaValuePath))) {
      return Double.parseDouble(reader.readLine()
                                      .split(":")[1]);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public double[][] meanValueLoader(String meanValuePath) {
    try (var reader = new BufferedReader(new FileReader(meanValuePath))) {
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
