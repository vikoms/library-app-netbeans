package com.widyatama.libraryapp.pages;

import com.widyatama.libraryapp.BaseFrame;
import com.widyatama.libraryapp.dao.BookDao;
import com.widyatama.libraryapp.dao.CategoryDao;
import com.widyatama.libraryapp.models.Book;
import com.widyatama.libraryapp.models.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableCellRenderer;

public class ManageBooksFrame extends BaseFrame {

    private JTable booksTable;
    private JTextField searchField;
    private JButton addBookButton;
    private JButton deleteBookButton;
    private JButton updateBookButton;
    private JButton manageCategoriesButton;
    private JButton backButton;
    private DefaultTableModel model;
    private List<Book> books;
    private JLabel statusBar;

    public ManageBooksFrame() {
        super("Manage Books");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchPanel.add(new JLabel("Search Book:"));
        searchPanel.add(searchField);

        searchField.getDocument().addDocumentListener((SimpleDocumentListener) e -> applyFilters());

        topPanel.add(searchPanel, BorderLayout.NORTH);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        addBookButton = new JButton("Add Book");
        addBookButton.setPreferredSize(new Dimension(120, 30));
        addBookButton.addActionListener(e -> openAddBookDialog());
        toolBar.add(addBookButton);

        updateBookButton = new JButton("Update Book");
        updateBookButton.setPreferredSize(new Dimension(120, 30));
        updateBookButton.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookId = (int) booksTable.getValueAt(selectedRow, 0);
                openUpdateBookDialog(bookId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to update.");
            }
        });
        toolBar.add(updateBookButton);

        deleteBookButton = new JButton("Delete Book");
        deleteBookButton.setPreferredSize(new Dimension(120, 30));
        deleteBookButton.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookId = (int) booksTable.getValueAt(selectedRow, 0);
                deleteBook(bookId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            }
        });
        toolBar.add(deleteBookButton);

        manageCategoriesButton = new JButton("Manage Categories");
        manageCategoriesButton.setPreferredSize(new Dimension(160, 30));
        manageCategoriesButton.addActionListener(e -> {
            new ManageCategoriesFrame().showFrame();
            dispose();
        });
        toolBar.add(manageCategoriesButton);

        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(120, 30));
        backButton.addActionListener(e -> {
            new Dashboard().showFrame();
            dispose();
        });
        toolBar.add(backButton);

        topPanel.add(toolBar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        booksTable = new JTable();
        booksTable.setFillsViewportHeight(true);
        booksTable.setRowHeight(25);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                return c;
            }
        });

        add(new JScrollPane(booksTable), BorderLayout.CENTER);

        statusBar = new JLabel("Ready");
        add(statusBar, BorderLayout.SOUTH);

        loadBooksData();
    }

    private void loadBooksData() {
        
    }

    private void applyFilters() {
    }

    private void displayData(List<Book> data) {
        
    }

    private void openAddBookDialog() {
            
    }

    private void openUpdateBookDialog(int bookId) {
    }

    private void deleteBook(int bookId) {

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            ManageBooksFrame manageBooksFrame = new ManageBooksFrame();
            manageBooksFrame.setVisible(true);
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
