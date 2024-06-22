package com.widyatama.libraryapp.pages;

import com.widyatama.libraryapp.dao.LoanDao;
import com.widyatama.libraryapp.dao.ReturnBookDao;
import com.widyatama.libraryapp.models.Book;
import com.widyatama.libraryapp.models.ReturnBook;
import com.widyatama.libraryapp.models.Student;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddReturn extends JFrame {

    private JComboBox<Student> studentComboBox;
    private JPanel booksPanel;
    private JButton submitButton;
    private LoanDao loanDao;
    private ReturnList returnList;

    public AddReturn(ReturnList returnList) {
        this.returnList = returnList;
        loanDao = new LoanDao();
        setTitle("File a Return");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        studentComboBox = new JComboBox<>();
        panel.add(new JLabel("Select Student"));
        panel.add(studentComboBox);

        submitButton = new JButton("Submit Return");
        submitButton.addActionListener(e -> submitReturn());
        panel.add(submitButton);

        booksPanel = new JPanel();
        booksPanel.setLayout(new BoxLayout(booksPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(booksPanel), BorderLayout.CENTER);

        add(panel, BorderLayout.NORTH);

        loadStudents();

        studentComboBox.addActionListener(e -> loadBooksForSelectedStudent());

        // Load books for the initially selected student
        if (studentComboBox.getItemCount() > 0) {
            studentComboBox.setSelectedIndex(0);
            loadBooksForSelectedStudent();
        }
    }

    private void loadStudents() {
        List<Student> students = loanDao.getStudentsWithPendingLoans();
        for (Student student : students) {
            studentComboBox.addItem(student);
        }
    }

    private void loadBooksForSelectedStudent() {
        booksPanel.removeAll();
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent != null) {
            List<Book> books = loanDao.getBooksByStudentId(selectedStudent.getId());
            for (Book book : books) {
                JCheckBox checkBox = new JCheckBox(book.getTitle() + " (Borrowed: " + book.getTanggaltanggalDiPinjam()+ ")");
                checkBox.setName(String.valueOf(book.getIdPeminjaman())); // Use loan id
                booksPanel.add(checkBox);
            }
        }
        booksPanel.revalidate();
        booksPanel.repaint();
    }

    private void submitReturn() {
        Student selectedStudent = (Student) studentComboBox.getSelectedItem();
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(this, "Please select a student.");
            return;
        }

        List<Integer> selectedLoanIds = new ArrayList<>();
        for (Component component : booksPanel.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                if (checkBox.isSelected()) {
                    selectedLoanIds.add(Integer.parseInt(checkBox.getName()));
                }
            }
        }

        if (selectedLoanIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one book to return.");
            return;
        }

        ReturnBookDao returnBookDao = new ReturnBookDao();
        boolean success = true;

        for (int loanId : selectedLoanIds) {
            ReturnBook returnBook = new ReturnBook();
            returnBook.setIdPeminjaman(loanId); // Use loan id
            returnBook.setTanggalKembali(new Date());
            returnBook.setStatusKondisiBuku("Good"); // Adjust to the actual condition of the book
            returnBook.setIdAdmin(1); // Use the appropriate admin ID, this is just an example
            try {
                returnBookDao.addReturn(returnBook);
            } catch (SQLException e) {
                success = false;
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                break;
            }
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Return processed successfully.");
            returnList.loadData();  // Reload data in ReturnList
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddReturn(null).setVisible(true));
    }
}
