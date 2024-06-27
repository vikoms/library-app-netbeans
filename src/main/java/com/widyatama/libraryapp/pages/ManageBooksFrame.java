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
        BookDao bookDao = new BookDao();
        books = bookDao.getAllBooks();
        displayData(books);
    }

    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();
        List<Book> filteredBooks = books.stream()
                .filter(book -> searchText.isEmpty() ||
                        book.getTitle().toLowerCase().contains(searchText) ||
                        book.getAuthor().toLowerCase().contains(searchText))
                .toList();
        displayData(filteredBooks);
    }

    private void displayData(List<Book> data) {
        String[] columnNames = {"ID", "Title", "Author", "Publisher", "Year", "ISBN", "Copies", "Category"};
        model = new DefaultTableModel(columnNames, 0);

        for (Book book : data) {
            Object[] row = {
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getYear(),
                book.getIsbn(),
                book.getCopies(),
                book.getCategory().getName()
            };
            model.addRow(row);
        }

        booksTable.setModel(model);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        booksTable.setRowSorter(sorter);
        statusBar.setText(data.size() + " entries found");
    }

    private void openAddBookDialog() {
        JDialog dialog = new JDialog(this, "Add Book", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(9, 2));

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField publisherField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField copiesField = new JTextField();
        JComboBox<Category> categoryComboBox = new JComboBox<>();
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.getAllCategories();
        for (Category category : categories) {
            categoryComboBox.addItem(category);
        }

        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel("Author:"));
        dialog.add(authorField);
        dialog.add(new JLabel("Publisher:"));
        dialog.add(publisherField);
        dialog.add(new JLabel("Year:"));
        dialog.add(yearField);
        dialog.add(new JLabel("ISBN:"));
        dialog.add(isbnField);
        dialog.add(new JLabel("Copies:"));
        dialog.add(copiesField);
        dialog.add(new JLabel("Category:"));
        dialog.add(categoryComboBox);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            Book book = new Book();
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            book.setPublisher(publisherField.getText());
            book.setYear(Integer.parseInt(yearField.getText()));
            book.setIsbn(isbnField.getText());
            book.setCopies(Integer.parseInt(copiesField.getText()));
            book.setCategory((Category) categoryComboBox.getSelectedItem());

            BookDao bookDao = new BookDao();
            bookDao.addBook(book);
            loadBooksData();
            dialog.dispose();
        });

        dialog.add(addButton);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void openUpdateBookDialog(int bookId) {
        BookDao bookDao = new BookDao();
        Book book = bookDao.getBookById(bookId);

        if (book != null) {
            JDialog dialog = new JDialog(this, "Update Book", true);
            dialog.setSize(400, 300);
            dialog.setLayout(new GridLayout(9, 2));

            JTextField titleField = new JTextField(book.getTitle());
            JTextField authorField = new JTextField(book.getAuthor());
            JTextField publisherField = new JTextField(book.getPublisher());
            JTextField yearField = new JTextField(String.valueOf(book.getYear()));
            JTextField isbnField = new JTextField(book.getIsbn());
            JTextField copiesField = new JTextField(String.valueOf(book.getCopies()));
            JComboBox<Category> categoryComboBox = new JComboBox<>();
            CategoryDao categoryDao = new CategoryDao();
            List<Category> categories = categoryDao.getAllCategories();
            for (Category category : categories) {
                categoryComboBox.addItem(category);
            }
            categoryComboBox.setSelectedItem(book.getCategory());

            dialog.add(new JLabel("Title:"));
            dialog.add(titleField);
            dialog.add(new JLabel("Author:"));
            dialog.add(authorField);
            dialog.add(new JLabel("Publisher:"));
            dialog.add(publisherField);
            dialog.add(new JLabel("Year:"));
            dialog.add(yearField);
            dialog.add(new JLabel("ISBN:"));
            dialog.add(isbnField);
            dialog.add(new JLabel("Copies:"));
            dialog.add(copiesField);
            dialog.add(new JLabel("Category:"));
            dialog.add(categoryComboBox);

            JButton updateButton = new JButton("Update");
            updateButton.addActionListener(e -> {
                book.setTitle(titleField.getText());
                book.setAuthor(authorField.getText());
                book.setPublisher(publisherField.getText());
                book.setYear(Integer.parseInt(yearField.getText()));
                book.setIsbn(isbnField.getText());
                book.setCopies(Integer.parseInt(copiesField.getText()));
                book.setCategory((Category) categoryComboBox.getSelectedItem());

                bookDao.updateBook(book);
                loadBooksData();
                dialog.dispose();
            });

            dialog.add(updateButton);

            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }
    }

    private void deleteBook(int bookId) {
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Delete Book", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            BookDao bookDao = new BookDao();
            bookDao.deleteBook(bookId);
            loadBooksData();
        }
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
