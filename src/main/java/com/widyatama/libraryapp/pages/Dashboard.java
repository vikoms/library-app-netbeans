package com.widyatama.libraryapp.pages;

import com.widyatama.libraryapp.BaseFrame;
import com.widyatama.libraryapp.utils.AuthUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Dashboard extends BaseFrame {
    private JButton kelolaBukuButton;
    private JButton kelolaMahasiswaButton;
    private JButton kelolaPeminjamanButton;
    private JButton kelolaPengembalianButton;
    private JButton logoutButton;

    public Dashboard() {
        super("Dashboard");
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Library App");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Welcome Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Welcome, " + AuthUtils.getInstance().getLoggedInAdmin().getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.BLACK);
        topPanel.add(welcomeLabel);
        add(topPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        kelolaBukuButton = createStyledButton("Manage Books", "book_icon.png", "Manage all books in the library");
        kelolaMahasiswaButton = createStyledButton("Manage Students", "student_icon.png", "Manage student information");
        kelolaPeminjamanButton = createStyledButton("Manage Book Loans", "loan_icon.png", "Manage book loan records");
        kelolaPengembalianButton = createStyledButton("Manage Book Returns", "return_icon.png", "Manage book return records");
        logoutButton = createStyledButton("Logout", "logout_icon.png", "Logout from the application");

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(kelolaBukuButton, gbc);

        gbc.gridx = 1;
        centerPanel.add(kelolaMahasiswaButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(kelolaPeminjamanButton, gbc);

        gbc.gridx = 1;
        centerPanel.add(kelolaPengembalianButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        centerPanel.add(logoutButton, gbc);

        add(centerPanel, BorderLayout.SOUTH);

        kelolaBukuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the book management frame
                ManageBooksFrame manageBooksFrame = new ManageBooksFrame();
                hideFrame();
                manageBooksFrame.showFrame();
            }
        });

        kelolaMahasiswaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageStudentsFrame manageStudentsFrame = new ManageStudentsFrame();
                hideFrame();
                manageStudentsFrame.showFrame();
            }
        });

        kelolaPeminjamanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BorrowBooksHome borrowBooksHome = new BorrowBooksHome(Dashboard.this);
                hideFrame();
                borrowBooksHome.showFrame();
            }
        });

        kelolaPengembalianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReturnList returnList = new ReturnList(Dashboard.this);
                hideFrame();
                returnList.showFrame();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AuthUtils.getInstance().clearLoggedInAdmin();
                dispose();
                new LoginForm().setVisible(true);
            }
        });
    }

    private JButton createStyledButton(String text, String iconPath, String toolTipText) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        try {
            // Load the icon from the resources directory and resize it
            BufferedImage icon = ImageIO.read(getClass().getClassLoader().getResource("icons/" + iconPath));
            Image scaledIcon = icon.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledIcon));
        } catch (IOException e) {
            e.printStackTrace();
        }
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setToolTipText(toolTipText);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.showFrame();
        });
    }
}
