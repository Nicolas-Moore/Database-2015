/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_rw;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Nick
 */
public class QueryRunner {

    private Connection conn;
    private Statement statement;
    private ResultSet caseResultSet;
    private ResultSet moboCompResultSet;
    private ResultSet moboSpeedResultSet;

    protected String[] caseManu = new String[35];
    protected String[] caseMod = new String[35];
    protected String[] moboManu = new String[35];
    protected String[] moboPart = new String[35];
    protected String[] moboSocket = new String[35];
    protected String[] moboMemSlot = new String[35];
    protected String[] moboMemType = new String[35];
    protected String[] moboForm = new String[35];
    protected String[] cpuSocket = new String[35];
    protected Double[] caseVidLen = new Double[35];

    public QueryRunner(Connection c) {
        conn = c;
    }

    public String[] getCases() {
        String[] cases = new String[35];
        try {
            int i = 0;
            statement = conn.createStatement();
            caseResultSet = statement
                    .executeQuery("select * from CASE_TABLE limit 35");

            while (caseResultSet.next()) {
                caseManu[i] = caseResultSet.getString("Manufacturer");
                caseMod[i] = caseResultSet.getString("Model");
                caseVidLen[i] = caseResultSet.getDouble("MaximumVideoCardLength");
                cases[i] = caseResultSet.getNString(1) + " " + caseResultSet.getNString(2);
                i++;
            }

        } catch (SQLException ex) {
            System.out.println("Can't run query");
        }
        return cases;
    }

    public String[] getMobos(String caseManu, String caseMod) {
        ArrayList<String> moboCompForms = new ArrayList<>();
        String[] compMobos = new String[35];
        int i = 0;

        String query = "SELECT AT, ATX, EATX, EEATX, Flex_ATX, HPTX, Micro_ATX, Mini_ITX, SSI_CEB, SSI_EEB, Thin_Mini_ITX, XL_ATX"
                + " FROM CASE_MOBO_COMP WHERE CASE_MOBO_COMP.CASEManufacturer = '"
                + caseManu + "' AND " + "CASE_MOBO_COMP.CASEModel = '" + caseMod + "'";

        try {
            statement = conn.createStatement();
            moboCompResultSet = statement.executeQuery(query);
            ResultSetMetaData rsmd = moboCompResultSet.getMetaData();

            moboCompResultSet.next();

            for (int j = 0; j < rsmd.getColumnCount(); j++) {
                if (moboCompResultSet.getString(j + 1).equals("1")) {
                    moboCompForms.add(rsmd.getColumnName(j + 1));
                }
            }

            String orString = "";
            int size = moboCompForms.size();
            if (size > 1) {
                for (int k = 0; k < size; k++) {
                    String form = moboCompForms.get(k);
                    if (form.contains("_")) {
                        form = form.replaceAll("_", " ");
                    }
                    if (k != size - 1) {
                        orString += "MOBO_TABLE.FormFactor = '" + form + "'" + " or ";
                    } else {
                        orString += "MOBO_TABLE.FormFactor = '" + form + "'";
                    }
                }
            } else {
                String form = moboCompForms.get(1);
                if (form.contains("_")) {
                    form = form.replaceAll("_", " ");
                }
                orString = "MOBO_TABLE.FormFactor = '" + form + "'";
            }

            String moboQuery = "select * from MOBO_TABLE where " + orString + " limit 35";
            
            moboCompResultSet = statement.executeQuery(moboQuery);

            while (moboCompResultSet.next()) {
                moboManu[i] = moboCompResultSet.getString("Manufacturer");
                moboPart[i] = moboCompResultSet.getString("PartNum");
                moboSocket[i] = moboCompResultSet.getString("CPUSocket");
                moboMemSlot[i] = moboCompResultSet.getString("MemorySlots");
                moboMemType[i] = moboCompResultSet.getString("MemoryType");
                moboForm[i] = moboCompResultSet.getString("FormFactor");
                compMobos[i] = moboCompResultSet.getString("Manufacturer") + " " + moboCompResultSet.getString("PartNum");
                i++;
            }

        } catch (SQLException ex) {
            System.out.println("Can't run query");
        }
        return compMobos;
    }

    public String[] getCpus(String moboSocket) {
        String[] compCpus = new String[35];
        int i = 0;
        try {
            String cpuQuery = "select * from CPU_TABLE where CPU_TABLE.Socket = '" + moboSocket + "' " + "limit 35";
            ResultSet cpuCompResultSet = statement.executeQuery(cpuQuery);

            while (cpuCompResultSet.next()) {
                cpuSocket[i] = cpuCompResultSet.getString("Socket");
                compCpus[i] = cpuCompResultSet.getString("Manufacturer") + " " + cpuCompResultSet.getString("Model");
                i++;
            }
        } catch (SQLException ex) {
            System.out.println("Can't run query");
        }
        return compCpus;
    }

    public String[] getHeatSinks(String cpuSock) {
        String[] compHS = new String[35];
        int i = 0;
        cpuSock = cpuSock.replaceAll(" ", "");
        try {
            String hsQuery = "select * from HEAT_SINK_SOCKET where HEAT_SINK_SOCKET." + cpuSock + " = 1 " + "limit 35";
            ResultSet cpuCompResultSet = statement.executeQuery(hsQuery);

            while (cpuCompResultSet.next()) {
                compHS[i] = cpuCompResultSet.getString("HEATSINKManufacturer") + " " + cpuCompResultSet.getString("HEATSINKModel");
                i++;
            }
        } catch (SQLException ex) {
            System.out.println("Can't run query");
        }
        return compHS;
    }

    public String[] getGpus(Double vidL) {
        String[] compGpus = new String[35];
        int i = 0;
        try {
            String gpuQuery = "select * from GPU_TABLE where GPU_TABLE.Length <= " + vidL + " limit 35";
            ResultSet gpuCompResultSet = statement.executeQuery(gpuQuery);

            while (gpuCompResultSet.next()) {
                compGpus[i] = gpuCompResultSet.getString("Manufacturer") + " " + gpuCompResultSet.getString("PartNum");
                i++;
            }
        } catch (SQLException ex) {
            System.out.println("Can't run query");
        }
        return compGpus;
    }

    public String[] getRam(String moboManu, String moboPart, String ramType, String ramPin) {
        ArrayList<String> moboCompSpeeds = new ArrayList<>();
        String[] compRam = new String[35];
        int i = 0;

        String query = "SELECT S1066, S1333, S1600, S1800, S1866, S2000, S2133, S2200, S2400, S2500, S2600, S2666, S2800, S2933, S3000, S3100, S3200, S3300"
                + " FROM MOBO_SPEED WHERE MOBO_SPEED.MOBOManufacturer = '"
                + moboManu + "' AND " + "MOBO_SPEED.MOBOPartNum = '" + moboPart + "'";

        try {
            statement = conn.createStatement();
            moboSpeedResultSet = statement.executeQuery(query);
            ResultSetMetaData rsmd = moboSpeedResultSet.getMetaData();

            moboSpeedResultSet.next();

            for (int j = 0; j < rsmd.getColumnCount(); j++) {
                if (moboSpeedResultSet.getString(j + 1).equals("1")) {
                    moboCompSpeeds.add(rsmd.getColumnName(j + 1));
                }
            }

            String orString = "";
            int size = moboCompSpeeds.size();
            if (size > 1) {
                for (int k = 0; k < size; k++) {
                    String speed = moboCompSpeeds.get(k);
                    speed = speed.replaceAll("S", "");
                    if (k != size - 1) {
                        orString += "RAM_TABLE.Speed = '" + speed + "'" + " or ";
                    } else {
                        orString += "RAM_TABLE.Speed = '" + speed + "'";
                    }
                }
            } else {
                String speed = moboCompSpeeds.get(0);
                speed = speed.replaceAll("S", "");
                orString = "RAM_TABLE.Speed = '" + speed + "'";
            }

            String ramQuery = "select * from RAM_TABLE where (" + orString + ")" + " AND " + "RAM_TABLE.DDRType = '" + ramType + "'" + " AND " + "RAM_TABLE.Type = '" + ramPin + "' limit 35";
            moboSpeedResultSet = statement.executeQuery(ramQuery);

            while (moboSpeedResultSet.next()) {
                compRam[i] = moboSpeedResultSet.getString("Manufacturer") + " " + moboSpeedResultSet.getString("PartNum");
                i++;
            }

        } catch (SQLException ex) {
            System.out.println("Can't run query");
        }
        return compRam;
    }

    public String[] getPsus(String moboForm) {
        String[] compPS = new String[35];
        int i = 0;
        try {
            if (moboForm.contains(" ")) {
                moboForm = "ATX";
            } 
            String psQuery = "select * from PSU_TABLE where PSU_TABLE.Type = '" + moboForm + "' OR PSU_TABLE.Type = 'ATX12V' limit 35";
            ResultSet psCompResultSet = statement.executeQuery(psQuery);

            while (psCompResultSet.next()) {
                compPS[i] = psCompResultSet.getString("Manufacturer") + " " + psCompResultSet.getString("Model") + " [" + psCompResultSet.getString("Wattage")+"]";
                i++;
            }
        } catch (SQLException ex) {
            System.out.println("Can't run query");
        }
        return compPS;
    }

    public String[] getHardDrives() {
        String[] compHD = new String[35];
        int i = 0;
        try {
            String hdQuery = "select * from HARD_DRIVE limit 35";
            ResultSet hdCompResultSet = statement.executeQuery(hdQuery);

            while (hdCompResultSet.next()) {
                compHD[i] = hdCompResultSet.getString("Manufacturer") + " " + hdCompResultSet.getString("PartNum") + " [" + hdCompResultSet.getString("Capacity") + " GB]";
                i++;
            }
        } catch (SQLException ex) {
            System.out.println("Can't run query");
        }
        return compHD;
    }

    public void printArray(ArrayList a) {
        for (Object a1 : a) {

            System.out.print(a1 + ", ");
        }
    }

    public void printString(String[] a) {
        for (Object a1 : a) {

            System.out.print(a1 + ", ");
        }
    }

}
