package com.widyatama.libraryapp.pages;

import com.widyatama.libraryapp.BaseFrame;
import com.widyatama.libraryapp.dao.ReturnBookDao;
import com.widyatama.libraryapp.models.ReturnBook;
import com.widyatama.libraryapp.models.Student;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReturnList extends BaseFrame {

    private JTable table;
    private JTextField searchField;
    private List<ReturnBook> returns;
    private BaseFrame previousFrame;
    private ReturnBookDao returnBookDao;
    private JLabel statusBar;

    public ReturnList(BaseFrame previousFrame) {
        super("Riwayat Pengembalian");
        this.previousFrame = previousFrame;
        this.returnBookDao = new ReturnBookDao();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchPanel.add(new JLabel("Cari Nama Anggota/Admin:"));
        searchPanel.add(searchField);

        searchField.getDocument().addDocumentListener((SimpleDocumentListener) e -> applyFilters());

        topPanel.add(searchPanel, BorderLayout.NORTH);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton addButton = new JButton("Add Return");
        addButton.setIcon(new ImageIcon("icons/add.png")); // Add icon path here
        addButton.setPreferredSize(new Dimension(120, 30)); // Set preferred size
        addButton.addActionListener(e -> openAddReturnFrame());
        toolBar.add(addButton);

        JButton backButton = new JButton("Back");
        backButton.setIcon(new ImageIcon("icons/back.png")); // Add icon path here
        backButton.setPreferredSize(new Dimension(120, 30)); // Set preferred size
        backButton.addActionListener(e -> goBackToDashboard());
        toolBar.add(backButton);

        topPanel.add(toolBar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        table = new JTable();
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                return c;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        String studentName = (String) table.getValueAt(selectedRow, 0);
                        Student student = findStudentByName(studentName);
                        if (student != null) {
                            new ReturnDetail(student).setVisible(true);
                        }
                    }
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        statusBar = new JLabel("Ready");
        add(statusBar, BorderLayout.SOUTH);

        loadData();
    }

    private Student findStudentByName(String name) {
        for (ReturnBook returnBook : returns) {
            if (returnBook.getNamaAnggota().equals(name)) {
                return new Student(returnBook.getIdAnggota(), returnBook.getNamaAnggota(), null, null, null, null);
            }
        }
        return null;
    }

    public void loadData() {
        returns = returnBookDao.getAllReturns();
        displayData(returns);
    }

    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();

        List<ReturnBook> filteredReturns = returns.stream()
                .filter(returnBook -> searchText.isEmpty() ||
                        returnBook.getNamaAnggota().toLowerCase().contains(searchText) ||
                        returnBook.getNamaAdmin().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        displayData(filteredReturns);
    }

    private void displayData(List<ReturnBook> data) {
        Map<String, List<ReturnBook>> groupedReturns = data.stream()
                .collect(Collectors.groupingBy(returnBook -> returnBook.getIdAnggota() + "_" + returnBook.getNamaAnggota() + "_" + returnBook.getTanggalKembali()));

        DefaultTableModel model = new DefaultTableModel(new String[]{"Nama Peminjam", "Tanggal Pengembalian", "Nama Admin"}, 0);
        for (Map.Entry<String, List<ReturnBook>> entry : groupedReturns.entrySet()) {
            List<ReturnBook> returnBooks = entry.getValue();
            ReturnBook firstReturn = returnBooks.get(0);
            model.addRow(new Object[]{firstReturn.getNamaAnggota(), firstReturn.getTanggalKembali(), firstReturn.getNamaAdmin()});
        }
        table.setModel(model);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(150);
        statusBar.setText(data.size() + " entries found");
    }

    private void openAddReturnFrame() {
        new AddReturn(this).setVisible(true);  // Pass the current ReturnList instance
    }

    private void goBackToDashboard() {
        new Dashboard().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReturnList returnList = new ReturnList(null);
            returnList.setVisible(true);
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
