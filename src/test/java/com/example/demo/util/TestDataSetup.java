package com.example.demo.util;

import com.example.demo.model.author.Author;
import com.example.demo.model.book.Book;
import com.example.demo.model.borrowTransaction.BorrowTransaction;
import com.example.demo.model.borrowTransaction.Status;
import com.example.demo.model.member.Member;
import com.example.demo.repository.author.AuthorRepository;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.borrowTransaction.BorrowTransactionRepository;
import com.example.demo.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class TestDataSetup {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BorrowTransactionRepository borrowTransactionRepository;

    public void cleanAll() {
        borrowTransactionRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        memberRepository.deleteAll();
    }

    public List<Author> createAuthors() {
        Author author1 = new Author();
        author1.setName("John Doe");
        author1.setEmail("john.doe@example.com");
        author1.setBio("Bestselling author of fiction novels");
        author1.setBirthDate(LocalDate.of(1975, 3, 15));

        Author author2 = new Author();
        author2.setName("Jane Smith");
        author2.setEmail("jane.smith@example.com");
        author2.setBio("Award-winning science fiction writer");
        author2.setBirthDate(LocalDate.of(1982, 7, 22));

        Author author3 = new Author();
        author3.setName("Robert Johnson");
        author3.setEmail("robert.johnson@example.com");
        author3.setBio("Historical fiction specialist");
        author3.setBirthDate(LocalDate.of(1968, 12, 8));

        return authorRepository.saveAll(Arrays.asList(author1, author2, author3));
    }

    public List<Book> createBooks(List<Author> authors) {
        Book book1 = new Book();
        book1.setTitle("The Great Adventure");
        book1.setIsbn("9780123456");
        book1.setPublicationYear(2020);
        book1.setAvailableCopies(5);
        book1.setTotalCopies(10);
        book1.setAuthors(Arrays.asList(authors.get(0)));

        Book book2 = new Book();
        book2.setTitle("Space Odyssey");
        book2.setIsbn("9780987654");
        book2.setPublicationYear(2021);
        book2.setAvailableCopies(3);
        book2.setTotalCopies(8);
        book2.setAuthors(Arrays.asList(authors.get(1)));

        Book book3 = new Book();
        book3.setTitle("Medieval Tales");
        book3.setIsbn("9780456789");
        book3.setPublicationYear(2019);
        book3.setAvailableCopies(2);
        book3.setTotalCopies(6);
        book3.setAuthors(Arrays.asList(authors.get(2)));

        Book book4 = new Book();
        book4.setTitle("Collaborative Work");
        book4.setIsbn("9780789123");
        book4.setPublicationYear(2022);
        book4.setAvailableCopies(4);
        book4.setTotalCopies(7);
        book4.setAuthors(Arrays.asList(authors.get(0), authors.get(1)));

        return bookRepository.saveAll(Arrays.asList(book1, book2, book3, book4));
    }

    public List<Member> createMembers() {
        Member member1 = new Member();
        member1.setName("Alice Johnson");
        member1.setEmail("alice.johnson@example.com");
        member1.setPhone("555-0101");
        member1.setMembershipDate(LocalDate.of(2023, 1, 15).atStartOfDay());
        member1.setStatus(com.example.demo.model.member.Status.ACTIVE);

        Member member2 = new Member();
        member2.setName("Bob Smith");
        member2.setEmail("bob.smith@example.com");
        member2.setPhone("555-0202");
        member2.setMembershipDate(LocalDate.of(2023, 2, 20).atStartOfDay());
        member2.setStatus(com.example.demo.model.member.Status.ACTIVE);

        Member member3 = new Member();
        member3.setName("Charlie Brown");
        member3.setEmail("charlie.brown@example.com");
        member3.setPhone("555-0303");
        member3.setMembershipDate(LocalDate.of(2023, 3, 10).atStartOfDay());
        member3.setStatus(com.example.demo.model.member.Status.INACTIVE);

        return memberRepository.saveAll(Arrays.asList(member1, member2, member3));
    }

    public List<BorrowTransaction> createBorrowTransactions(List<Book> books, List<Member> members) {
        BorrowTransaction transaction1 = new BorrowTransaction();
        transaction1.setBook(books.get(0));
        transaction1.setMember(members.get(0));
        transaction1.setBorrowDate(LocalDate.now().minusDays(10));
        transaction1.setDueDate(LocalDate.now().plusDays(4));
        transaction1.setStatus(Status.BORROWED);

        BorrowTransaction transaction2 = new BorrowTransaction();
        transaction2.setBook(books.get(1));
        transaction2.setMember(members.get(1));
        transaction2.setBorrowDate(LocalDate.now().minusDays(20));
        transaction2.setDueDate(LocalDate.now().minusDays(6));
        transaction2.setReturnDate(LocalDate.now().minusDays(3));
        transaction2.setStatus(Status.RETURNED);

        BorrowTransaction transaction3 = new BorrowTransaction();
        transaction3.setBook(books.get(2));
        transaction3.setMember(members.get(0));
        transaction3.setBorrowDate(LocalDate.now().minusDays(5));
        transaction3.setDueDate(LocalDate.now().plusDays(9));
        transaction3.setStatus(Status.BORROWED);

        BorrowTransaction overdueTransaction = new BorrowTransaction();
        overdueTransaction.setBook(books.get(3));
        overdueTransaction.setMember(members.get(1));
        overdueTransaction.setBorrowDate(LocalDate.now().minusDays(30));
        overdueTransaction.setDueDate(LocalDate.now().minusDays(16));
        overdueTransaction.setStatus(Status.BORROWED);

        return borrowTransactionRepository.saveAll(Arrays.asList(
            transaction1, transaction2, transaction3, overdueTransaction
        ));
    }

    public void setupFullTestData() {
        cleanAll();
        
        List<Author> authors = createAuthors();
        List<Book> books = createBooks(authors);
        List<Member> members = createMembers();
        List<BorrowTransaction> transactions = createBorrowTransactions(books, members);
        
        books.get(0).setAvailableCopies(books.get(0).getAvailableCopies() - 1);
        books.get(2).setAvailableCopies(books.get(2).getAvailableCopies() - 1);
        books.get(3).setAvailableCopies(books.get(3).getAvailableCopies() - 1);
        
        bookRepository.saveAll(books);
    }

    public Author createSampleAuthor() {
        Author author = new Author();
        author.setName("Sample Author");
        author.setEmail("sample@example.com");
        author.setBio("Sample bio");
        author.setBirthDate(LocalDate.of(1980, 1, 1));
        return authorRepository.save(author);
    }

    public Book createSampleBook() {
        Book book = new Book();
        book.setTitle("Sample Book");
        book.setIsbn("9780123456789");
        book.setPublicationYear(2023);
        book.setAvailableCopies(5);
        book.setTotalCopies(10);
        return bookRepository.save(book);
    }

    public Member createSampleMember() {
        Member member = new Member();
        member.setName("Sample Member");
        member.setEmail("sample.member@example.com");
        member.setPhone("555-0000");
        member.setMembershipDate(LocalDateTime.now());
        member.setStatus(com.example.demo.model.member.Status.ACTIVE);
        return memberRepository.save(member);
    }

    public BorrowTransaction createSampleBorrowTransaction(Book book, Member member) {
        BorrowTransaction transaction = new BorrowTransaction();
        transaction.setBook(book);
        transaction.setMember(member);
        transaction.setBorrowDate(LocalDate.now());
        transaction.setDueDate(LocalDate.now().plusDays(14));
        transaction.setStatus(Status.BORROWED);
        
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        
        return borrowTransactionRepository.save(transaction);
    }
}
