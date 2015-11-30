package data_rw;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.*;

public class Gui implements ActionListener {

    JFrame guiFrame = new JFrame(); // Frame for holding our JPanes

    final JPanel guiPanel = new JPanel();   // Panel for holding our Combo Boxes  
    private int selCase = 999;
    
ArrayList<String> moboCompForms = new ArrayList<>();
    String[] cases = new String[35];      // current compatible Cases
    String[] mobos = new String[35];      // current compatible Mother Boards
    String[] cpus = new String[35];       // current compatible Central Processing Units
    String[] gpus = new String[35];       // current compatible Graphics Processing Units
    String[] psus = new String[35];       // current compatible Power Supplies
    String[] hardDrives = new String[35]; // current compatible Hard Drives
    String[] heatSinks = new String[35];  // current compatible Heat Sinks
    String[] rams = new String[35];       // current compatible Random Access Memory

    JLabel caseLabel = new JLabel("Case:");
    JLabel moboLabel = new JLabel("Motherboard:");
    JLabel cpuLabel = new JLabel("CPU:");
    JLabel gpuLabel = new JLabel("Video Card:");
    JLabel powerLabel = new JLabel("Power Supply:");
    JLabel hardDriveLabel = new JLabel("Hard Drive:");
    JLabel heatSinkLabel = new JLabel("Heat Sink:");
    JLabel ramLabel = new JLabel("RAM:");

    JComboBox caseCombo;
    JComboBox moboCombo;
    JComboBox cpuCombo;
    JComboBox gpuCombo;
    JComboBox powerCombo;
    JComboBox hardCombo;
    JComboBox heatCombo;
    JComboBox ramCombo;
    
    QueryRunner qr;

    public Gui(QueryRunner inQr) {
        qr = inQr;
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle("Computer Compatability Database");
        guiFrame.setSize(250, 500);
        guiFrame.setLocationRelativeTo(null);   // centers gui
        guiPanel.setLayout(new BoxLayout(guiPanel, BoxLayout.PAGE_AXIS));

        initializeComboBoxes();
        initializeJPane();
    }

    private void initializeComboBoxes() {
        caseCombo = new JComboBox(cases);
        caseCombo.addActionListener(this);
        moboCombo = new JComboBox(mobos);
        moboCombo.addActionListener(this);
        cpuCombo = new JComboBox(cpus);
        cpuCombo.addActionListener(this);
        gpuCombo = new JComboBox(gpus);
        powerCombo = new JComboBox(psus);
        hardCombo = new JComboBox(hardDrives);
        heatCombo = new JComboBox(heatSinks);
        ramCombo = new JComboBox(rams);
    }

    private void initializeJPane() {
        guiPanel.add(caseLabel);
        guiPanel.add(caseCombo);

        guiPanel.add(moboLabel);
        guiPanel.add(moboCombo);

        guiPanel.add(cpuLabel);
        guiPanel.add(cpuCombo);

        guiPanel.add(gpuLabel);
        guiPanel.add(gpuCombo);

        guiPanel.add(powerLabel);
        guiPanel.add(powerCombo);

        guiPanel.add(hardDriveLabel);
        guiPanel.add(hardCombo);

        guiPanel.add(heatSinkLabel);
        guiPanel.add(heatCombo);

        guiPanel.add(ramLabel);
        guiPanel.add(ramCombo);

        guiPanel.setVisible(true);
        guiFrame.add(guiPanel);
        guiFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        //int selCase = 999;
        if (cb == caseCombo) {
            selCase =  cb.getSelectedIndex();
            updateMobos(qr.getMobos(qr.caseManu[selCase], qr.caseMod[selCase])); 
        } 
        
        if (cb == moboCombo && mobos[0] != null) {
            int selMobo =  cb.getSelectedIndex();
            updateCPUS(qr.getCpus(qr.moboSocket[selMobo])); 
            updateGPUS(qr.getGpus(qr.caseVidLen[selCase]));
            updateRAM(qr.getRam(qr.moboManu[selMobo], qr.moboPart[selMobo], qr.moboMemType[selMobo], qr.moboMemSlot[selMobo]));
            updatePSUS(qr.getPsus(qr.moboForm[selMobo]));
            updateHardDrive(qr.getHardDrives());
        } 
        
        if (cb == cpuCombo) {
            int selCpu =  cb.getSelectedIndex();
            updateHeatSink(qr.getHeatSinks(qr.cpuSocket[selCpu]));
        } 
    }

    public void updateCases(String[] newCases) {
        cases = newCases;
        caseCombo.setModel(new DefaultComboBoxModel(cases));
    }

    public void updateMobos(String[] newMobos) {
        mobos = newMobos;
        moboCombo.setModel(new DefaultComboBoxModel(mobos));
    }

    public void updateCPUS(String[] newCPUS) {
        cpus = newCPUS;
        cpuCombo.setModel(new DefaultComboBoxModel(cpus));
    }

    public void updateGPUS(String[] newGPUS) {
        gpus = newGPUS;
        gpuCombo.setModel(new DefaultComboBoxModel(gpus));
    }

    public void updatePSUS(String[] newPSUS) {
        psus = newPSUS;
        powerCombo.setModel(new DefaultComboBoxModel(psus));
    }

    public void updateHardDrive(String[] newHardDrives) {
        hardDrives = newHardDrives;
        hardCombo.setModel(new DefaultComboBoxModel(hardDrives));
    }

    public void updateHeatSink(String[] newHeatSinks) {
        heatSinks = newHeatSinks;
        heatCombo.setModel(new DefaultComboBoxModel(heatSinks));
    }

    public void updateRAM(String[] newRAMS) {
        rams = newRAMS;
        ramCombo.setModel(new DefaultComboBoxModel(rams));
    }
}
