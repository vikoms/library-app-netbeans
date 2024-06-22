/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.widyatama.libraryapp.models;

import com.widyatama.libraryapp.models.Admin;

public class Loan {
    private int id;
    private Student student;
    private Book book;
    private String loanDate;
    private String returnDate;
    private Admin admin;
    
    public Loan(){}
    // Constructors, getters, and setters
    public Loan(int id, Student student, Book book, String loanDate, String returnDate, Admin admin) {
        this.id = id;
        this.student = student;
        this.book = book;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.admin = admin;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
