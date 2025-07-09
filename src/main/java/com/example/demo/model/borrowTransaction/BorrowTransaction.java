package com.example.demo.model.borrowTransaction;

import com.example.demo.model.book.Book;
import com.example.demo.model.member.Member;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "borrow_transactions")
public class BorrowTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    @Column(name = "return_date")
    private LocalDate returnDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
