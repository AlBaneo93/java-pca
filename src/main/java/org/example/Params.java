package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Params {
  public static String pcaDataPath = "./pca_data.ini";

  public static String meanFilePath = "./pca_mean.ini";

  public static void pcValueWriter(double[][] pcValues) {
    try (var fwriter = new BufferedWriter(new FileWriter(pcaDataPath))) {
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

  public static void meanValueWriter(double mean) {
    try (var fwriter = new BufferedWriter(new FileWriter(meanFilePath))) {
      fwriter.write("mean:" + mean);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static double pcValueLoader(double[][] pcValues) {
    try (var reader = new BufferedReader(new FileReader(pcaDataPath))) {
      return Double.parseDouble(reader.readLine()
                                      .split(":")[1]);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static double[][] meanValueLoader(double mean) {
    try (var reader = new BufferedReader(new FileReader(meanFilePath))) {
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
