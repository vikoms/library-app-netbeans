package com.widyatama.libraryapp.dao;

import com.widyatama.libraryapp.connection.DatabaseConnection;
import com.widyatama.libraryapp.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM anggota";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

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

    public Student getStudentById(int studentId) {
        Student student = null;
        String query = "SELECT * FROM anggota WHERE id_anggota = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    student = new Student(
                            resultSet.getInt("id_anggota"),
                            resultSet.getString("nama"),
                            resultSet.getString("alamat"),
                            resultSet.getString("nomor_telepon"),
                            resultSet.getString("email"),
                            resultSet.getString("tanggal_daftar")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public void addStudent(Student student) {
        String query = "INSERT INTO anggota (nama, alamat, nomor_telepon, email, tanggal_daftar) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getAddress());
            statement.setString(3, student.getPhoneNumber());
            statement.setString(4, student.getEmail());
            statement.setString(5, student.getRegistrationDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(Student student) {
        String query = "UPDATE anggota SET nama = ?, alamat = ?, nomor_telepon = ?, email = ?, tanggal_daftar = ? WHERE id_anggota = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getAddress());
            statement.setString(3, student.getPhoneNumber());
            statement.setString(4, student.getEmail());
            statement.setString(5, student.getRegistrationDate());
            statement.setInt(6, student.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int studentId) {
        String query = "DELETE FROM anggota WHERE id_anggota = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, studentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
