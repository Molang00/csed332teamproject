package com.SoftwareMatrix;

import android.icu.impl.number.Parse;
import com.google.common.collect.Tables;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.util.Calendar;
import java.awt.image.BufferedImage;
import java.util.Random;

public class MetricsResultWindow implements UpdateObserver {
    /* Declare private fields here */
    private JPanel myToolWindowContent;
    private JTable tableStructure;
    private JScrollPane tableContent;
    private Integer MIscore, OOscore;

    /**
     * Constructor of tool window
     */
    public MetricsResultWindow(ToolWindow toolWindow) {
        String[] header = { "", "score", "graph" };
        String[][] body = { { "MI", "", "" }, { "OO", "", "" } };
        DefaultTableModel model = new DefaultTableModel(body, header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableStructure = new JTable();
        tableStructure.setModel(model);
        tableContent = new JScrollPane(tableStructure);

        settingAllStatus();
        tableStructure.setValueAt(MIscore, 0, 1);
        tableStructure.setValueAt(OOscore, 1, 1);
        TableColumnModel tcm = tableStructure.getColumnModel();
        TableColumn tm = tcm.getColumn(2);
        tm.setCellRenderer(new ColoredTableCellRenderer());
    }

    public Integer getOOscore() {
        return OOscore;
    }

    public Integer getMIscore() {
        return MIscore;
    }

    public void setOOscore(Integer s) {
        OOscore = s;
    }

    public void setMIscore(Integer s) {
        MIscore = s;
    }

    public void settingAllStatus() {
        Random rand = new Random();
        setMIscore(rand.nextInt(100));
        setOOscore(rand.nextInt(100));
    }

    /**
     * Returns content of this tool window
     * 
     * @return whole content of tool window
     */
    public JScrollPane getContent() {
        return tableContent;
    }

    @Override
    public void update(Project project, PsiElement elem) {
        settingAllStatus();
        tableStructure.setValueAt(MIscore, 0, 1);
        tableStructure.setValueAt(OOscore, 1, 1);

        // color refresh has 5~10 seconds of delay

        System.out.println(elem);
        System.out.println(ParseAdapter.getContainingMethod(elem));
        System.out.println(ParseAdapter.getContainingClass(elem));
        System.out.println(ParseAdapter.getContainingPackage(elem));
    }

    class ColoredTableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused,
                int row, int column) {
            setEnabled(table == null || table.isEnabled()); // see question above
            int r, g, b = 0;

            if (row == 0) {
                r = (int) ((100 - MIscore) * 2.55);
                g = (int) ((MIscore) * 2.55);
                Color c = new Color(r, g, b);
                setBackground(c);
            } else {
                r = (int) ((100 - OOscore) * 2.55);
                g = (int) ((OOscore) * 2.55);
                Color c = new Color(r, g, b);
                setBackground(c);
            }

            super.getTableCellRendererComponent(table, value, selected, focused, row, column);

            return this;
        }
    }
}
