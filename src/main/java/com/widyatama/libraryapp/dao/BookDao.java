package com.widyatama.libraryapp.dao;

import com.widyatama.libraryapp.connection.DatabaseConnection;
import com.widyatama.libraryapp.models.Book;
import com.widyatama.libraryapp.models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.id_buku, b.judul, b.penulis, b.penerbit, b.tahun_terbit, b.ISBN, b.jumlah_eksemplar, " +
                       "b.id_category, k.name AS category_name " +
                       "FROM buku b " +
                       "JOIN kategori k ON b.id_category = k.id_category";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

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
                        null, // tanggaltanggalDiPinjam is not retrieved in this query
                        0    // idPeminjaman is not retrieved in this query
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> getBooksByLoanId(int loanId) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.id_buku, b.judul, b.penulis, b.penerbit, b.tahun_terbit, b.ISBN, b.jumlah_eksemplar, " +
                       "b.id_category, k.name AS category_name, p.tanggal_pinjam, p.id_peminjaman " +
                       "FROM buku b " +
                       "JOIN kategori k ON b.id_category = k.id_category " +
                       "JOIN peminjaman p ON b.id_buku = p.id_buku " +
                       "WHERE p.id_peminjaman = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

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
        String query = "INSERT INTO buku (judul, penulis, penerbit, tahun_terbit, ISBN, jumlah_eksemplar, id_category) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setInt(4, book.getYear());
            statement.setString(5, book.getIsbn());
            statement.setInt(6, book.getCopies());
            statement.setInt(7, book.getCategory().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(Book book) {
        String query = "UPDATE buku SET judul = ?, penulis = ?, penerbit = ?, tahun_terbit = ?, ISBN = ?, jumlah_eksemplar = ?, id_category = ? WHERE id_buku = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setInt(4, book.getYear());
            statement.setString(5, book.getIsbn());
            statement.setInt(6, book.getCopies());
            statement.setInt(7, book.getCategory().getId());
            statement.setInt(8, book.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBook(int bookId) {
        String query = "DELETE FROM buku WHERE id_buku = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
      public Book getBookById(int bookId) {
        Book book = null;
        String query = "SELECT b.id_buku, b.judul, b.penulis, b.penerbit, b.tahun_terbit, b.ISBN, b.jumlah_eksemplar, " +
                       "b.id_category, k.name AS category_name " +
                       "FROM buku b " +
                       "JOIN kategori k ON b.id_category = k.id_category " +
                       "WHERE b.id_buku = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Category category = new Category(
                            resultSet.getInt("id_category"),
                            resultSet.getString("category_name")
                    );
                    book = new Book(
                            resultSet.getInt("id_buku"),
                            resultSet.getString("judul"),
                            resultSet.getString("penulis"),
                            resultSet.getString("penerbit"),
                            resultSet.getInt("tahun_terbit"),
                            resultSet.getString("ISBN"),
                            resultSet.getInt("jumlah_eksemplar"),
                            category,
                            null, // tanggaltanggalDiPinjam is not retrieved in this query
                            0    // idPeminjaman is not retrieved in this query
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }
}
