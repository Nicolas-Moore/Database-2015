/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_rw;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Nicolas
 */
public class GpuWriter extends DataWriter {

    public GpuWriter(String writeFile, int numAttributes) {
        super(writeFile, numAttributes);
    }

    @Override
    public void writeData(ArrayList a, ArrayList d, int n) {
        try {

            for (int i = 0; i < a.size(); i++) {
                if (i != a.size() - 1) {
                    bufferedWriter.write((String) a.get(i) + ",");
                } else {
                    bufferedWriter.write((String) a.get(i));
                }

            }

            bufferedWriter.newLine();
            int partMod = 2;
            int lengthMod = 12;

            for (int j = 0; j < d.size(); j++) {
                if ((j + 1) % partMod == 0) {
                    partMod += n;
                    String info = (String) d.get(j);
                    if (info.contains("<")) {
                        info = info.substring(0, info.indexOf("<"));
                    }
                    bufferedWriter.write(info + ",");
                } else if ((j + 1) % lengthMod == 0) {
                    lengthMod += n;
                    String info = (String) d.get(j);
                    info = info.substring(0, info.indexOf("&"));
                    bufferedWriter.write(info + ",");
                } else {
                    if ((j + 1) % n == 0) {
                        bufferedWriter.write((String) d.get(j));
                        bufferedWriter.newLine();
                    } else {
                        bufferedWriter.write((String) d.get(j) + ",");
                    }
                }

            }

            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println(
                    "Error writing to file '"
                    + fileName + "'");
        }
    }
}
