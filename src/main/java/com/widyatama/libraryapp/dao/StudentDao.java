/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.widyatama.libraryapp.dao;
import com.widyatama.libraryapp.connection.DatabaseConnection;
import com.widyatama.libraryapp.models.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Anggota")) {

            while (resultSet.next()) {
                Student student = new Student(
                        resultSet.getInt("id_anggota"),
                        resultSet.getString("nama"),
                        resultSet.getString("alamat"),
                        resultSet.getString("nomor_telepon"),
                        resultSet.getString("email"),
                        resultSet.getString("tanggal_daftar")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
}

