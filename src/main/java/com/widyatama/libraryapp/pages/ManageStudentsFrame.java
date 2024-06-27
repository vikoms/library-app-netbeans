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
        StudentDao studentDao = new StudentDao();
        students = studentDao.getAllStudents();
        displayData(students);
    }

    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();
        List<Student> filteredStudents = students.stream()
                .filter(student -> searchText.isEmpty() ||
                        student.getName().toLowerCase().contains(searchText) ||
                        student.getEmail().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        displayData(filteredStudents);
    }

    private void displayData(List<Student> data) {
        String[] columnNames = {"ID", "Name", "Address", "Phone", "Email", "Registration Date"};
        model = new DefaultTableModel(columnNames, 0);

        for (Student student : data) {
            Object[] row = {
                student.getId(),
                student.getName(),
                student.getAddress(),
                student.getPhoneNumber(),
                student.getEmail(),
                student.getRegistrationDate()
            };
            model.addRow(row);
        }

        studentsTable.setModel(model);
        TableColumnModel columnModel = studentsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(200);
        columnModel.getColumn(5).setPreferredWidth(150);
        statusBar.setText(data.size() + " entries found");
    }

    private void createStudent() {
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Create Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            if (!name.trim().isEmpty() && !address.trim().isEmpty() && !phone.trim().isEmpty() && !email.trim().isEmpty()) {
                StudentDao studentDao = new StudentDao();
                Student student = new Student(0, name, address, phone, email, new java.sql.Date(System.currentTimeMillis()).toString());
                studentDao.addStudent(student);
                loadStudentsData();
            } else {
                JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int studentId = (int) studentsTable.getValueAt(selectedRow, 0);
        String currentName = (String) studentsTable.getValueAt(selectedRow, 1);
        String currentAddress = (String) studentsTable.getValueAt(selectedRow, 2);
        String currentPhone = (String) studentsTable.getValueAt(selectedRow, 3);
        String currentEmail = (String) studentsTable.getValueAt(selectedRow, 4);

        JTextField nameField = new JTextField(currentName);
        JTextField addressField = new JTextField(currentAddress);
        JTextField phoneField = new JTextField(currentPhone);
        JTextField emailField = new JTextField(currentEmail);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Update Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            if (!name.trim().isEmpty() && !address.trim().isEmpty() && !phone.trim().isEmpty() && !email.trim().isEmpty()) {
                StudentDao studentDao = new StudentDao();
                Student student = new Student(studentId, name, address, phone, email, new java.sql.Date(System.currentTimeMillis()).toString());
                studentDao.updateStudent(student);
                loadStudentsData();
            } else {
                JOptionPane.showMessageDialog(this, "All fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int studentId = (int) studentsTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            StudentDao studentDao = new StudentDao();
            studentDao.deleteStudent(studentId);
            loadStudentsData();
        }
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
