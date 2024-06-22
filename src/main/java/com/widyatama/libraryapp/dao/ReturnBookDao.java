package com.widyatama.libraryapp.dao;

import com.widyatama.libraryapp.connection.DatabaseConnection;
import com.widyatama.libraryapp.models.ReturnBook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnBookDao {

    public List<ReturnBook> getAllReturns() {
        List<ReturnBook> returns = new ArrayList<>();
        String query = "SELECT a.nama AS nama_anggota, p.id_anggota, p.id_peminjaman, adm.name AS nama_admin, k.tanggal_kembali, k.status_kondisi_buku " +
                       "FROM db_perpustakaan.pengembalian k " +
                       "JOIN db_perpustakaan.peminjaman p ON k.id_peminjaman = p.id_peminjaman " +
                       "JOIN db_perpustakaan.anggota a ON p.id_anggota = a.id_anggota " +
                       "JOIN db_perpustakaan.admin adm ON k.id_admin = adm.id_admin " +
                       "ORDER BY a.nama, p.id_anggota, k.tanggal_kembali";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ReturnBook returnBook = new ReturnBook();
                returnBook.setNamaAnggota(resultSet.getString("nama_anggota"));
                returnBook.setIdAnggota(resultSet.getInt("id_anggota"));
                returnBook.setIdPeminjaman(resultSet.getInt("id_peminjaman"));
                returnBook.setNamaAdmin(resultSet.getString("nama_admin"));
                returnBook.setTanggalKembali(resultSet.getDate("tanggal_kembali"));
                returnBook.setStatusKondisiBuku(resultSet.getString("status_kondisi_buku"));
                returns.add(returnBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returns;
    }
    
     public List<ReturnBook> getReturnsByStudentId(int studentId) {
        List<ReturnBook> returns = new ArrayList<>();
        String query = "SELECT a.nama AS nama_anggota, p.id_anggota, p.id_peminjaman, b.judul AS nama_buku, adm.name AS nama_admin, k.tanggal_kembali, k.status_kondisi_buku " +
                       "FROM db_perpustakaan.pengembalian k " +
                       "JOIN db_perpustakaan.peminjaman p ON k.id_peminjaman = p.id_peminjaman " +
                       "JOIN db_perpustakaan.anggota a ON p.id_anggota = a.id_anggota " +
                       "JOIN db_perpustakaan.buku b ON p.id_buku = b.id_buku " +
                       "JOIN db_perpustakaan.admin adm ON k.id_admin = adm.id_admin " +
                       "WHERE p.id_anggota = ? " +
                       "ORDER BY k.tanggal_kembali";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ReturnBook returnBook = new ReturnBook();
                returnBook.setNamaAnggota(resultSet.getString("nama_anggota"));
                returnBook.setIdAnggota(resultSet.getInt("id_anggota"));
                returnBook.setIdPeminjaman(resultSet.getInt("id_peminjaman"));
                returnBook.setNamaAdmin(resultSet.getString("nama_admin"));
                returnBook.setTanggalKembali(resultSet.getDate("tanggal_kembali"));
                returnBook.setStatusKondisiBuku(resultSet.getString("status_kondisi_buku"));
                returnBook.setNamaBuku(resultSet.getString("nama_buku"));
                returns.add(returnBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returns;
    }
    
    public void addReturn(ReturnBook returnBook) throws SQLException {
        String query = "INSERT INTO db_perpustakaan.pengembalian (id_peminjaman, tanggal_kembali, status_kondisi_buku, id_admin) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, returnBook.getIdPeminjaman());
            statement.setDate(2, new java.sql.Date(returnBook.getTanggalKembali().getTime()));
            statement.setString(3, returnBook.getStatusKondisiBuku());
            statement.setInt(4, returnBook.getIdAdmin());
            statement.executeUpdate();
        }
    }
}
