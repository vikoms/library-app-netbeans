package com.widyatama.libraryapp.pages;

import com.widyatama.libraryapp.BaseFrame;
import com.widyatama.libraryapp.dao.StudentDao;
import com.widyatama.libraryapp.models.Student;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ManageStudentsFrame extends BaseFrame {

    private JTable studentsTable;
    private JTextField searchField;
    private DefaultTableModel model;
    private List<Student> students;
    private JLabel statusBar;

    public ManageStudentsFrame() {
        super("Manage Students");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchPanel.add(new JLabel("Search Student:"));
        searchPanel.add(searchField);

        searchField.getDocument().addDocumentListener((SimpleDocumentListener) e -> applyFilters());

        topPanel.add(searchPanel, BorderLayout.NORTH);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton createStudentButton = new JButton("Create Student");
        createStudentButton.setPreferredSize(new Dimension(140, 30));
        createStudentButton.addActionListener(e -> createStudent());
        toolBar.add(createStudentButton);

        JButton updateStudentButton = new JButton("Update Student");
        updateStudentButton.setPreferredSize(new Dimension(140, 30));
        updateStudentButton.addActionListener(e -> updateStudent());
        toolBar.add(updateStudentButton);

        JButton deleteStudentButton = new JButton("Delete Student");
        deleteStudentButton.setPreferredSize(new Dimension(140, 30));
        deleteStudentButton.addActionListener(e -> deleteStudent());
        toolBar.add(deleteStudentButton);

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(120, 30));
        backButton.addActionListener(e -> {
            new Dashboard().showFrame();
            dispose();
        });
        toolBar.add(backButton);

        topPanel.add(toolBar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        studentsTable = new JTable();
        studentsTable.setFillsViewportHeight(true);
        studentsTable.setRowHeight(25);
        studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                return c;
            }
        });

        add(new JScrollPane(studentsTable), BorderLayout.CENTER);

        statusBar = new JLabel("Ready");
        add(statusBar, BorderLayout.SOUTH);

        loadStudentsData();
    }

    private void loadStudentsData() {
    }

    private void applyFilters() {
        
    }

    private void displayData(List<Student> data) {
    }

    private void createStudent() {
    }

    private void updateStudent() {
    }

    private void deleteStudent() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManageStudentsFrame manageStudentsFrame = new ManageStudentsFrame();
            manageStudentsFrame.setVisible(true);
        });
    }

    @FunctionalInterface
    private interface SimpleDocumentListener extends DocumentListener {
        void update(DocumentEvent e);

        @Override
        default void insertUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        default void removeUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        default void changedUpdate(DocumentEvent e) {
            update(e);
        }
    }
}
