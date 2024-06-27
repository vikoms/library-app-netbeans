package com.widyatama.libraryapp.models;

public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private String isbn;
    private int copies;
    private Category category;
    private String tanggaltanggalDiPinjam;
    private int idPeminjaman;

    public Book() {}

    public Book(int id, String title, String author, String publisher, int year, String isbn, int copies,Category category, String tanggaltanggalDiPinjam, int idPeminjaman) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.isbn = isbn;
        this.copies = copies;
        this.category = category;
        this.tanggaltanggalDiPinjam = tanggaltanggalDiPinjam;
        this.idPeminjaman = idPeminjaman;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTanggaltanggalDiPinjam() {
        return tanggaltanggalDiPinjam;
    }

    public void setTanggaltanggalDiPinjam(String tanggaltanggalDiPinjam) {
        this.tanggaltanggalDiPinjam = tanggaltanggalDiPinjam;
    }

    public int getIdPeminjaman() {
        return idPeminjaman;
    }

    public void setIdPeminjaman(int idPeminjaman) {
        this.idPeminjaman = idPeminjaman;
    }

    @Override
    public String toString() {
        return title;
    }
}
