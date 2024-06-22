package com.widyatama.libraryapp.pages;

import com.widyatama.libraryapp.dao.LoanDao;
import com.widyatama.libraryapp.dao.ReturnBookDao;
import com.widyatama.libraryapp.models.Book;
import com.widyatama.libraryapp.models.Loan;
import com.widyatama.libraryapp.models.ReturnBook;
import com.widyatama.libraryapp.models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ReturnDetail extends JFrame {

    private JTable returnedBooksTable;
    private JTable notReturnedBooksTable;
    private ReturnBookDao returnBookDao;
    private LoanDao loanDao;
    private Student student;

    public ReturnDetail(Student student) {
        this.student = student;
        this.returnBookDao = new ReturnBookDao();
        this.loanDao = new LoanDao();

        setTitle("Return Details for " + student.getName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel returnedBooksLabel = new JLabel("Books Returned");
        returnedBooksLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(returnedBooksLabel);

        returnedBooksTable = new JTable();
        setupTable(returnedBooksTable);
        mainPanel.add(new JScrollPane(returnedBooksTable));

        JLabel notReturnedBooksLabel = new JLabel("Books Not Returned");
        notReturnedBooksLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(notReturnedBooksLabel);

        notReturnedBooksTable = new JTable();
        setupTable(notReturnedBooksTable);
        mainPanel.add(new JScrollPane(notReturnedBooksTable));

        add(mainPanel, BorderLayout.CENTER);

        loadReturnDetails();
    }

    private void setupTable(JTable table) {
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
    }

    private void loadReturnDetails() {
        List<ReturnBook> returnedBooks = returnBookDao.getReturnsByStudentId(student.getId());
        displayData(returnedBooksTable, returnedBooks);

        List<Book> notReturnedBooks = loanDao.getBooksByStudentId(student.getId());
        displayData(notReturnedBooksTable, notReturnedBooks);
    }

    private void displayData(JTable table, List<?> data) {
        DefaultTableModel model;
        if (data != null && !data.isEmpty()) {
            Object firstItem = data.get(0);
            if (firstItem instanceof ReturnBook) {
                model = new DefaultTableModel(new String[]{"Book Title", "Return Date", "Book Condition", "Admin Name"}, 0);
                for (Object obj : data) {
                    ReturnBook returnBook = (ReturnBook) obj;
                    model.addRow(new Object[]{returnBook.getNamaBuku(), returnBook.getTanggalKembali(), returnBook.getStatusKondisiBuku(), returnBook.getNamaAdmin()});
                }
            } else if (firstItem instanceof Book) {
                model = new DefaultTableModel(new String[]{"Book Title", "Borrowed Date"}, 0);
                for (Object obj : data) {
                    Book book = (Book) obj;
                    model.addRow(new Object[]{book.getTitle(), book.getTanggaltanggalDiPinjam()});
                }
            } else {
                model = new DefaultTableModel();
            }
            table.setModel(model);
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(150);
            columnModel.getColumn(1).setPreferredWidth(150);
            if (firstItem instanceof ReturnBook) {
                columnModel.getColumn(2).setPreferredWidth(150);
                columnModel.getColumn(3).setPreferredWidth(150);
            }
        }
    }

    public static void main(String[] args) {
        // Example usage
        SwingUtilities.invokeLater(() -> {
            Student student = new Student(1, "John Doe", null, null, null, null);
            new ReturnDetail(student).setVisible(true);
        });
    }
}
