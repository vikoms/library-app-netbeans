/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.widyatama.libraryapp.pages;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoanDetail extends JFrame {
    public LoanDetail(String memberName, String adminName, String loanDate, String returnDate, List<String> books) {
        setTitle("Loan Details");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel memberLabel = createStyledLabel("Nama Peminjam: " + memberName, 18, true);
        mainPanel.add(memberLabel);

        JLabel adminLabel = createStyledLabel("Penanggung Jawab Admin: " + adminName, 18, true);
        mainPanel.add(adminLabel);

        JLabel loanDateLabel = createStyledLabel("Tanggal Pinjaman: " + loanDate, 18, true);
        mainPanel.add(loanDateLabel);

        JLabel returnDateLabel = createStyledLabel("Tanggal Pengembalian: " + (returnDate != null ? returnDate : "Belum dikembalikan"), 18, true);
        mainPanel.add(returnDateLabel);

        JLabel booksLabel = createStyledLabel("List Buku:", 18, true);
        mainPanel.add(booksLabel);

        JList<String> booksList = new JList<>(books.toArray(new String[0]));
        booksList.setFont(new Font("Arial", Font.PLAIN, 16));
        booksList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        booksList.setBackground(new Color(240, 240, 240));
        booksList.setVisibleRowCount(8); // Menyesuaikan jumlah baris yang terlihat
        booksList.setFixedCellHeight(30); // Menyesuaikan tinggi sel

        booksList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setText((index + 1) + ". " + value.toString()); // Menambahkan nomor pada setiap item
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                if (isSelected) {
                    label.setBackground(new Color(0, 120, 215));
                    label.setForeground(Color.WHITE);
                }
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(booksList);
        scrollPane.setPreferredSize(new Dimension(450, 250)); // Menyesuaikan tinggi JScrollPane
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        mainPanel.add(scrollPane);

        add(mainPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(220, 53, 69));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createStyledLabel(String text, int fontSize, boolean isBold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", isBold ? Font.BOLD : Font.PLAIN, fontSize));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return label;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<String> books = List.of("Badai di Ujung Jalan", "Pembalasan Sang Samurai", "Operasi Hitam", "Badai Pasir", "Jejak Pemburu");
            new LoanDetail("Eko Saputra", "Admin One", "2024-06-21", null, books).setVisible(true);
        });
    }
}
