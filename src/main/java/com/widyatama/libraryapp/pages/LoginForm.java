/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.widyatama.libraryapp.pages;
import com.widyatama.libraryapp.dao.AdminDao;
import com.widyatama.libraryapp.models.Admin;
import com.widyatama.libraryapp.utils.AuthUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AdminDao adminDao;
    public LoginForm() {
        adminDao = new AdminDao();
        setTitle("Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        JLabel instructionLabel = new JLabel("Insert your username and password to access your account.");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        add(instructionLabel, gbc);

        gbc.gridwidth = 1;

        JLabel usernameLabel = new JLabel("Username");
        usernameField = new JTextField(20);
        usernameField.setText("admin1@example.com");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(usernameLabel, gbc);
        gbc.gridx = 1;
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password");
        passwordField = new JPasswordField(20);
        passwordField.setText("password1");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);
        
        loginButton = new JButton("Sign in");
        loginButton.setBackground(new Color(102, 0, 255));
        loginButton.setForeground(Color.WHITE);
        gbc.gridy = 6;
        add(loginButton, gbc);

        

        // Add action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

    }
    
    
     private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Admin admin = adminDao.authenticate(username, password);
        if (admin != null) {
            AuthUtils.getInstance().setLoggedInAdmin(admin);
            JOptionPane.showMessageDialog(this, "Login successful!");
            
            dispose();
            new Dashboard().setVisible(true);
            // Lanjutkan ke frame berikutnya atau tindakan selanjutnya
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginForm loginForm = new LoginForm();
            loginForm.setVisible(true);
        });
    }
}
