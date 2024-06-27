package com.widyatama.libraryapp.dao;

import com.widyatama.libraryapp.connection.DatabaseConnection;
import com.widyatama.libraryapp.models.Book;
import com.widyatama.libraryapp.models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {

    public List<Book> getAllBooks() {
        return null; // timpa dari baris ini
    }

    public List<Book> getBooksByLoanId(int loanId) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.id_buku, b.judul, b.penulis, b.penerbit, b.tahun_terbit, b.ISBN, b.jumlah_eksemplar, "
                + "b.id_category, k.name AS category_name, p.tanggal_pinjam, p.id_peminjaman "
                + "FROM buku b "
                + "JOIN kategori k ON b.id_category = k.id_category "
                + "JOIN peminjaman p ON b.id_buku = p.id_buku "
                + "WHERE p.id_peminjaman = ?";
        try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, loanId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Category category = new Category(
                            resultSet.getInt("id_category"),
                            resultSet.getString("category_name")
                    );
                    Book book = new Book(
                            resultSet.getInt("id_buku"),
                            resultSet.getString("judul"),
                            resultSet.getString("penulis"),
                            resultSet.getString("penerbit"),
                            resultSet.getInt("tahun_terbit"),
                            resultSet.getString("ISBN"),
                            resultSet.getInt("jumlah_eksemplar"),
                            category,
                            resultSet.getString("tanggal_pinjam"),
                            resultSet.getInt("id_peminjaman")
                    );
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void addBook(Book book) {

    }

    public void updateBook(Book book) {

    }

    public void deleteBook(int bookId) {

    }

    public Book getBookById(int bookId) {
        return null; // timpa dari baris ini
    }
}
