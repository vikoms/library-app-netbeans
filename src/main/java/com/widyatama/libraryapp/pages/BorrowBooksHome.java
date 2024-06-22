package com.widyatama.libraryapp.pages;

import com.widyatama.libraryapp.BaseFrame;
import com.widyatama.libraryapp.dao.LoanDao;
import com.widyatama.libraryapp.models.Loan;
import com.widyatama.libraryapp.models.Student;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BorrowBooksHome extends BaseFrame {
    private JTable memberTable;
    private JTextField searchField;
    private JButton addLoanButton;
    private JButton backButton;
    private BaseFrame previousFrame;
    private LoanDao loanDao;
    private List<Loan> loans;
    private JLabel statusBar;

    public BorrowBooksHome(BaseFrame previousFrame) {
        super("Kelola Peminjaman Buku");
        this.previousFrame = previousFrame;
        this.loanDao = new LoanDao();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchPanel.add(new JLabel("Cari Nama Anggota:"));
        searchPanel.add(searchField);

        searchField.getDocument().addDocumentListener((SimpleDocumentListener) e -> applyFilters());

        topPanel.add(searchPanel, BorderLayout.NORTH);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        addLoanButton = new JButton("Add Loan");
        addLoanButton.setPreferredSize(new Dimension(120, 30));
        addLoanButton.addActionListener(e -> openAddLoanFrame());
        toolBar.add(addLoanButton);

        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(120, 30));
        backButton.addActionListener(e -> goBackToDashboard());
        toolBar.add(backButton);

        topPanel.add(toolBar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        memberTable = new JTable();
        memberTable.setFillsViewportHeight(true);
        memberTable.setRowHeight(25);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                return c;
            }
        });

        memberTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = memberTable.rowAtPoint(evt.getPoint());
                String memberName = (String) memberTable.getValueAt(row, 1);
                String loanDate = memberTable.getValueAt(row, 2).toString();
                showMemberLoansPopup(memberName, loanDate);
            }
        });

        add(new JScrollPane(memberTable), BorderLayout.CENTER);

        statusBar = new JLabel("Ready");
        add(statusBar, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        loans = loanDao.getAllLoans();
        displayData(loans);
    }

    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();

        List<Loan> filteredLoans = loans.stream()
                .filter(loan -> searchText.isEmpty() ||
                        loan.getStudent().getName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        displayData(filteredLoans);
    }

    private void displayData(List<Loan> data) {
        Map<String, List<Loan>> groupedLoans = data.stream()
                .collect(Collectors.groupingBy(loan -> loan.getStudent().getId() + "_" + loan.getLoanDate()));

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID Anggota", "Nama Anggota", "Tanggal Peminjaman", "Jumlah Buku"}, 0);
        for (Map.Entry<String, List<Loan>> entry : groupedLoans.entrySet()) {
            List<Loan> studentLoans = entry.getValue();
            Loan firstLoan = studentLoans.get(0);
            Student student = firstLoan.getStudent();
            model.addRow(new Object[]{student.getId(), student.getName(), firstLoan.getLoanDate(), studentLoans.size()});
        }
        memberTable.setModel(model);
        TableColumnModel columnModel = memberTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(100);
        statusBar.setText(data.size() + " entries found");
    }

    private void showMemberLoansPopup(String memberName, String loanDate) {
        List<Loan> memberLoans = loans.stream()
                .filter(loan -> loan.getStudent().getName().equals(memberName) && loan.getLoanDate().equals(loanDate))
                .collect(Collectors.toList());

        if (memberLoans.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No loans found for " + memberName + " on " + loanDate, "Loan Details", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Loan firstLoan = memberLoans.get(0);
        String adminName = firstLoan.getAdmin().getName();
        String returnDate = firstLoan.getReturnDate();

        List<String> books = memberLoans.stream().map(loan -> loan.getBook().getTitle()).collect(Collectors.toList());

        LoanDetail loanDetailsFrame = new LoanDetail(memberName, adminName, loanDate, returnDate, books);
        loanDetailsFrame.setVisible(true);
    }

    private void openAddLoanFrame() {
        new AddLoan(this).setVisible(true);
    }

    public void updateMemberTable() {
        loadData();
    }

    private void goBackToDashboard() {
        new Dashboard().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BorrowBooksHome borrowBooksHome = new BorrowBooksHome(null);
            borrowBooksHome.setVisible(true);
        });
    }

    @FunctionalInterface
    private interface SimpleDocumentListener extends javax.swing.event.DocumentListener {
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
