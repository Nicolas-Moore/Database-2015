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
 * @author Nick
 */
public class CpuWriter extends DataWriter {

    public CpuWriter(String writeFile, int numAttributes) {
        super(writeFile, numAttributes);
    }
    
    @Override
    public void writeData(ArrayList a, ArrayList d, int n){
        try {
            for (int i = 0; i < a.size(); i++) {
                    if (i != a.size() - 1) {
                        bufferedWriter.write((String) a.get(i) + ",");
                    } else {
                        bufferedWriter.write((String) a.get(i));
                    }
            }

            bufferedWriter.newLine();
            int startMod = 6;
            for (int j = 0; j < d.size(); j++) {
                if ((j + 1) % startMod == 0) {
                    startMod += n;
                    String info = (String)d.get(j);
                    info = info.substring(0, info.indexOf("G")) + ",";
                    bufferedWriter.write(info);

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
