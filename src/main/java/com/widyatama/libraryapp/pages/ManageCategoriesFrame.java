package com.widyatama.libraryapp.pages;

import com.widyatama.libraryapp.BaseFrame;
import com.widyatama.libraryapp.dao.CategoryDao;
import com.widyatama.libraryapp.models.Category;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ManageCategoriesFrame extends BaseFrame {

    private JTable categoriesTable;
    private JTextField searchField;
    private DefaultTableModel model;
    private List<Category> categories;
    private JLabel statusBar;

    public ManageCategoriesFrame() {
        super("Manage Categories");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchPanel.add(new JLabel("Search Category:"));
        searchPanel.add(searchField);

        searchField.getDocument().addDocumentListener((SimpleDocumentListener) e -> applyFilters());

        topPanel.add(searchPanel, BorderLayout.NORTH);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton createCategoryButton = new JButton("Create Category");
        createCategoryButton.setPreferredSize(new Dimension(140, 30));
        createCategoryButton.addActionListener(e -> createCategory());
        toolBar.add(createCategoryButton);

        JButton updateCategoryButton = new JButton("Update Category");
        updateCategoryButton.setPreferredSize(new Dimension(140, 30));
        updateCategoryButton.addActionListener(e -> updateCategory());
        toolBar.add(updateCategoryButton);

        JButton deleteCategoryButton = new JButton("Delete Category");
        deleteCategoryButton.setPreferredSize(new Dimension(140, 30));
        deleteCategoryButton.addActionListener(e -> deleteCategory());
        toolBar.add(deleteCategoryButton);

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(120, 30));
        backButton.addActionListener(e -> {
            new ManageBooksFrame().showFrame();
            dispose();
        });
        toolBar.add(backButton);

        topPanel.add(toolBar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        categoriesTable = new JTable();
        categoriesTable.setFillsViewportHeight(true);
        categoriesTable.setRowHeight(25);
        categoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoriesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                return c;
            }
        });

        add(new JScrollPane(categoriesTable), BorderLayout.CENTER);

        statusBar = new JLabel("Ready");
        add(statusBar, BorderLayout.SOUTH);

        loadCategoriesData();
    }

    private void loadCategoriesData() {
        CategoryDao categoryDao = new CategoryDao();
        categories = categoryDao.getAllCategories();
        displayData(categories);
    }

    private void applyFilters() {
        String searchText = searchField.getText().toLowerCase();
        List<Category> filteredCategories = categories.stream()
                .filter(category -> searchText.isEmpty() || category.getName().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        displayData(filteredCategories);
    }

    private void displayData(List<Category> data) {
        String[] columnNames = {"ID", "Name"};
        model = new DefaultTableModel(columnNames, 0);

        for (Category category : data) {
            Object[] row = {
                category.getId(),
                category.getName()
            };
            model.addRow(row);
        }

        categoriesTable.setModel(model);
        TableColumnModel columnModel = categoriesTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(200);
        statusBar.setText(data.size() + " entries found");
    }

    private void createCategory() {
        String name = JOptionPane.showInputDialog(this, "Enter Category Name:");
        if (name != null && !name.trim().isEmpty()) {
            CategoryDao categoryDao = new CategoryDao();
            Category category = new Category(0, name);
            categoryDao.addCategory(category);
            loadCategoriesData();
        } else {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCategory() {
        int selectedRow = categoriesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int categoryId = (int) categoriesTable.getValueAt(selectedRow, 0);
        String currentName = (String) categoriesTable.getValueAt(selectedRow, 1);

        String newName = JOptionPane.showInputDialog(this, "Enter new Category Name:", currentName);
        if (newName != null && !newName.trim().isEmpty()) {
            CategoryDao categoryDao = new CategoryDao();
            Category category = new Category(categoryId, newName);
            categoryDao.updateCategory(category);
            loadCategoriesData();
        } else {
            JOptionPane.showMessageDialog(this, "Category name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCategory() {
        int selectedRow = categoriesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int categoryId = (int) categoriesTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this category?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            CategoryDao categoryDao = new CategoryDao();
            categoryDao.deleteCategory(categoryId);
            loadCategoriesData();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManageCategoriesFrame manageCategoriesFrame = new ManageCategoriesFrame();
            manageCategoriesFrame.setVisible(true);
        });
    }

    @FunctionalInterface
    private interface SimpleDocumentListener extends DocumentListener {
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
