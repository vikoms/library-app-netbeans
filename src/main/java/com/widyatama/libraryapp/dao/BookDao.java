/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.widyatama.libraryapp.dao;

import com.widyatama.libraryapp.connection.DatabaseConnection;
import com.widyatama.libraryapp.models.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Buku")) {

            while (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id_buku"),
                        resultSet.getString("judul"),
                        resultSet.getString("penulis"),
                        resultSet.getString("penerbit"),
                        resultSet.getInt("tahun_terbit"),
                        resultSet.getString("ISBN"),
                        resultSet.getInt("jumlah_eksemplar"),
                        resultSet.getInt("id_category")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    
    
    // Metode untuk mendapatkan buku berdasarkan ID Peminjaman
    public List<Book> getBooksByLoanId(int loanId) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.* FROM db_perpustakaan.buku b " +
                       "JOIN db_perpustakaan.peminjaman p ON b.id_buku = p.id_buku " +
                       "WHERE p.id_peminjaman = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, loanId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
               Book book = new Book(
                        resultSet.getInt("id_buku"),
                        resultSet.getString("judul"),
                        resultSet.getString("penulis"),
                        resultSet.getString("penerbit"),
                        resultSet.getInt("tahun_terbit"),
                        resultSet.getString("ISBN"),
                        resultSet.getInt("jumlah_eksemplar"),
                        resultSet.getInt("id_category")
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

}
