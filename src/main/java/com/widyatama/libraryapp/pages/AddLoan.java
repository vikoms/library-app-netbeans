/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.widyatama.libraryapp.pages;
import com.widyatama.libraryapp.BaseFrame;
import com.widyatama.libraryapp.dao.BookDao;
import com.widyatama.libraryapp.dao.LoanDao;
import com.widyatama.libraryapp.dao.StudentDao;
import com.widyatama.libraryapp.models.Book;
import com.widyatama.libraryapp.models.Student;
import com.widyatama.libraryapp.utils.AuthUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddLoan extends JFrame {
    private JComboBox<Student> studentComboBox;
    private JList<Book> bookList;
    private JButton submitButton;
    private BorrowBooksHome borrowBooksHome;

    public AddLoan(BorrowBooksHome borrowBooksHome) {
        this.borrowBooksHome = borrowBooksHome;
        setTitle("Add a Loan");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentLabel = new JLabel("Select Student");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(studentLabel, gbc);

        studentComboBox = new JComboBox<>(getStudentData().toArray(new Student[0]));
        gbc.gridx = 1;
        add(studentComboBox, gbc);

        JLabel bookLabel = new JLabel("Select Books");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(bookLabel, gbc);

        bookList = new JList<>(getBookData().toArray(new Book[0]));
        bookList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        gbc.gridx = 1;
        add(new JScrollPane(bookList), gbc);

        submitButton = new JButton("Submit Loan");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(submitButton, gbc);

        // Add action listener
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Student student = (Student) studentComboBox.getSelectedItem();
                List<Book> books = bookList.getSelectedValuesList();
                submitLoan(student, books);
            }
        });
    }

    private List<Student> getStudentData() {
        StudentDao studentDao = new StudentDao();
        return studentDao.getAllStudents();
    }

    private List<Book> getBookData() {
        BookDao bookDao = new BookDao();
        return bookDao.getAllBooks();
    }

    private void submitLoan(Student student, List<Book> books) {
        LoanDao loanDao = new LoanDao();
        boolean success = loanDao.addLoan(student, books, AuthUtils.getInstance().getLoggedInAdmin());

        if (success) {
            JOptionPane.showMessageDialog(this, "Loan submitted for student: " + student.getName() + " with books: " + books);
            dispose();
            borrowBooksHome.updateMemberTable(); // Update the loan table
            borrowBooksHome.showFrame(); // Show the updated loan list
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit loan", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddLoan addLoan = new AddLoan(null);
            addLoan.setVisible(true);
        });
    }
}


