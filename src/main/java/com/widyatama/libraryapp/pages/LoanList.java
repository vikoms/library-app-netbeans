/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.widyatama.libraryapp.pages;
import com.widyatama.libraryapp.dao.LoanDao;
import com.widyatama.libraryapp.models.Loan;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoanList extends JFrame {
    private JTable loanTable;

    public LoanList() {
        setTitle("Loan List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Loan ID", "Student Name", "Book Title", "Loan Date", "Return Date", "Admin"};
        Object[][] data = getLoanData();

        loanTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(loanTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private Object[][] getLoanData() {
        LoanDao loanDao = new LoanDao();
        List<Loan> loans = loanDao.getAllLoans();

        Object[][] data = new Object[loans.size()][6];
        for (int i = 0; i < loans.size(); i++) {
            Loan loan = loans.get(i);
            data[i][0] = loan.getId();
            data[i][1] = loan.getStudent().getName();
            data[i][2] = loan.getBook().getTitle();
            data[i][3] = loan.getLoanDate();
            data[i][4] = loan.getReturnDate();
            data[i][5] = loan.getAdmin().getName();
        }
        return data;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoanList loanList = new LoanList();
            loanList.setVisible(true);
        });
    }
}
