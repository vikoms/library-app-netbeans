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
        
    }

    private void applyFilters() {
        
    }

    private void displayData(List<Category> data) {
        
    }

    private void createCategory() {
        
    }

    private void updateCategory() {
        
    }

    private void deleteCategory() {
        
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
