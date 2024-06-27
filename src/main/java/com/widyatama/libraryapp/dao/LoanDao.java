
package com.widyatama.libraryapp.dao;

import com.widyatama.libraryapp.models.Loan;
import com.widyatama.libraryapp.connection.DatabaseConnection;
import com.widyatama.libraryapp.models.Admin;
import com.widyatama.libraryapp.models.Book;
import com.widyatama.libraryapp.models.Category;
import com.widyatama.libraryapp.models.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDao {
   public List<Loan> getAllLoans() {
        List<Loan> loans = new ArrayList<>();
        String query = "SELECT p.id_peminjaman, p.id_anggota, a.nama, p.id_buku, b.judul, b.penulis, b.penerbit, b.tahun_terbit, b.ISBN, b.jumlah_eksemplar, b.id_category, k.name AS category_name, p.tanggal_pinjam, p.tanggal_kembali, p.id_admin, ad.name " +
                       "FROM peminjaman p " +
                       "JOIN anggota a ON p.id_anggota = a.id_anggota " +
                       "JOIN buku b ON p.id_buku = b.id_buku " +
                       "JOIN kategori k ON b.id_category = k.id_category " +
                       "JOIN admin ad ON p.id_admin = ad.id_admin";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Category category = new Category(resultSet.getInt("id_category"), resultSet.getString("category_name"));
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
                Loan loan = new Loan(
                        resultSet.getInt("id_peminjaman"),
                        new Student(resultSet.getInt("id_anggota"), resultSet.getString("nama"), null, null, null, null),
                        book,
                        resultSet.getString("tanggal_pinjam"),
                        resultSet.getString("tanggal_kembali"),
                        new Admin(resultSet.getInt("id_admin"), resultSet.getString("name"), null, null, null, null)
                );
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    public boolean addLoan(Student student, List<Book> books, Admin admin) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO peminjaman (id_anggota, id_buku, tanggal_pinjam, tanggal_kembali, id_admin) VALUES (?, ?, ?, ?, ?)";
            for (Book book : books) {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, student.getId());
                    statement.setInt(2, book.getId());
                    statement.setDate(3, new java.sql.Date(System.currentTimeMillis())); // tanggal_pinjam
                    statement.setDate(4, null); // tanggal_kembali, bisa diupdate saat pengembalian
                    statement.setInt(5, admin.getId());
                    statement.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
       
  
    public List<Student> getStudentsWithPendingLoans() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT DISTINCT a.id_anggota, a.nama " +
                       "FROM db_perpustakaan.peminjaman p " +
                       "JOIN db_perpustakaan.anggota a ON p.id_anggota = a.id_anggota " +
                       "LEFT JOIN db_perpustakaan.pengembalian k ON p.id_peminjaman = k.id_peminjaman " +
                       "WHERE k.id_peminjaman IS NULL";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Student student = new Student();
                student.setId(resultSet.getInt("id_anggota"));
                student.setName(resultSet.getString("nama"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public List<Book> getBooksByStudentId(int studentId) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.id_buku, b.judul, p.tanggal_pinjam, p.id_peminjaman " +
                       "FROM db_perpustakaan.peminjaman p " +
                       "JOIN db_perpustakaan.buku b ON p.id_buku = b.id_buku " +
                       "LEFT JOIN db_perpustakaan.pengembalian k ON p.id_peminjaman = k.id_peminjaman " +
                       "WHERE p.id_anggota = ? AND k.id_peminjaman IS NULL";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("id_buku"));
                book.setTitle(resultSet.getString("judul"));
                book.setTanggaltanggalDiPinjam(resultSet.getString("tanggal_pinjam"));
                book.setIdPeminjaman(resultSet.getInt("id_peminjaman"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}
