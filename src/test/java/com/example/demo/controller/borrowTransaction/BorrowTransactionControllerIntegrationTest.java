package com.example.demo.controller.borrowTransaction;

import com.example.demo.model.author.Author;
import com.example.demo.model.book.Book;
import com.example.demo.model.borrowTransaction.BorrowTransaction;
import com.example.demo.model.borrowTransaction.Status;
import com.example.demo.model.member.Member;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.borrowTransaction.BorrowTransactionRepository;
import com.example.demo.repository.member.MemberRepository;
import com.example.demo.request.borrowTransaction.BorrowTransactionRequest;
import com.example.demo.util.TestDataSetup;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BorrowTransactionControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BorrowTransactionRepository borrowTransactionRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestDataSetup testDataSetup;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        testDataSetup.cleanAll();
    }

    @Test
    void borrowBook_ShouldCreateBorrowTransactionSuccessfully() throws Exception {
        Book book = testDataSetup.createSampleBook();
        Member member = testDataSetup.createSampleMember();
        
        BorrowTransactionRequest request = new BorrowTransactionRequest(
            book.getId(),
            member.getId(),
            LocalDate.now(),
            LocalDate.now().plusDays(14)
        );

        mockMvc.perform(post("/api/transactions/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.bookId", is(book.getId().intValue())))
                .andExpect(jsonPath("$.data.memberId", is(member.getId().intValue())))
                .andExpect(jsonPath("$.data.status", is("BORROWED")))
                .andExpect(jsonPath("$.data.isOverdue", is(false)));

        assertEquals(1, borrowTransactionRepository.count());
        
        Optional<Book> updatedBook = bookRepository.findById(book.getId());
        assertTrue(updatedBook.isPresent());
        assertEquals(4, updatedBook.get().getAvailableCopies());
    }

    @Test
    void borrowBook_ShouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        Member member = testDataSetup.createSampleMember();
        
        BorrowTransactionRequest request = new BorrowTransactionRequest(
            999L,
            member.getId(),
            LocalDate.now(),
            LocalDate.now().plusDays(14)
        );

        mockMvc.perform(post("/api/transactions/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", containsString("Book with id: 999 does not exists")));

        assertEquals(0, borrowTransactionRepository.count());
    }

    @Test
    void borrowBook_ShouldReturnNotFoundWhenMemberDoesNotExist() throws Exception {
        Book book = testDataSetup.createSampleBook();
        
        BorrowTransactionRequest request = new BorrowTransactionRequest(
            book.getId(),
            999L,
            LocalDate.now(),
            LocalDate.now().plusDays(14)
        );

        mockMvc.perform(post("/api/transactions/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", containsString("Member with id: 999 does not exists")));

        assertEquals(0, borrowTransactionRepository.count());
    }

    @Test
    void borrowBook_ShouldReturnBadRequestWhenBookNotAvailable() throws Exception {
        Book book = testDataSetup.createSampleBook();
        book.setAvailableCopies(0);
        bookRepository.save(book);
        
        Member member = testDataSetup.createSampleMember();
        
        BorrowTransactionRequest request = new BorrowTransactionRequest(
            book.getId(),
            member.getId(),
            LocalDate.now(),
            LocalDate.now().plusDays(14)
        );

        mockMvc.perform(post("/api/transactions/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", containsString("No copies available for book")));

        assertEquals(0, borrowTransactionRepository.count());
    }

    @Test
    void borrowBook_ShouldReturnBadRequestWhenMemberIsInactive() throws Exception {
        Book book = testDataSetup.createSampleBook();
        Member member = testDataSetup.createSampleMember();
        member.setStatus(com.example.demo.model.member.Status.INACTIVE);
        memberRepository.save(member);
        
        BorrowTransactionRequest request = new BorrowTransactionRequest(
            book.getId(),
            member.getId(),
            LocalDate.now(),
            LocalDate.now().plusDays(14)
        );

        mockMvc.perform(post("/api/transactions/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Member is not active")));

        assertEquals(0, borrowTransactionRepository.count());
    }

    @Test
    void returnBook_ShouldReturnBookSuccessfully() throws Exception {
        Book book = testDataSetup.createSampleBook();
        Member member = testDataSetup.createSampleMember();
        BorrowTransaction transaction = testDataSetup.createSampleBorrowTransaction(book, member);

        mockMvc.perform(put("/api/transactions/{id}/return", transaction.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.status", is("RETURNED")))
                .andExpect(jsonPath("$.data.returnDate", notNullValue()))
                .andExpect(jsonPath("$.data.isOverdue", is(false)));

        Optional<BorrowTransaction> updatedTransaction = borrowTransactionRepository.findById(transaction.getId());
        assertTrue(updatedTransaction.isPresent());
        assertEquals(Status.RETURNED, updatedTransaction.get().getStatus());
        assertNotNull(updatedTransaction.get().getReturnDate());
        
        Optional<Book> updatedBook = bookRepository.findById(book.getId());
        assertTrue(updatedBook.isPresent());
        assertEquals(5, updatedBook.get().getAvailableCopies());
    }

    @Test
    void returnBook_ShouldReturnNotFoundWhenTransactionDoesNotExist() throws Exception {
        mockMvc.perform(put("/api/transactions/{id}/return", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", containsString("Transaction with id: 999 does not exists")));
    }

    @Test
    void returnBook_ShouldReturnBadRequestWhenBookAlreadyReturned() throws Exception {
        Book book = testDataSetup.createSampleBook();
        Member member = testDataSetup.createSampleMember();
        BorrowTransaction transaction = testDataSetup.createSampleBorrowTransaction(book, member);
        
        transaction.setStatus(Status.RETURNED);
        transaction.setReturnDate(LocalDate.now().minusDays(1));
        borrowTransactionRepository.save(transaction);

        mockMvc.perform(put("/api/transactions/{id}/return", transaction.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Book has already been returned")));
    }

    @Test
    void getTransactions_ShouldReturnAllTransactions() throws Exception {
        testDataSetup.setupFullTestData();

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data", hasSize(4)))
                .andExpect(jsonPath("$.data[0].bookTitle", notNullValue()))
                .andExpect(jsonPath("$.data[0].memberName", notNullValue()))
                .andExpect(jsonPath("$.data[0].status", notNullValue()));
    }

    @Test
    void getTransactions_ShouldReturnEmptyListWhenNoTransactions() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    void getTransaction_ShouldReturnTransactionWhenExists() throws Exception {
        Book book = testDataSetup.createSampleBook();
        Member member = testDataSetup.createSampleMember();
        BorrowTransaction transaction = testDataSetup.createSampleBorrowTransaction(book, member);

        mockMvc.perform(get("/api/transactions/{id}", transaction.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.id", is(transaction.getId().intValue())))
                .andExpect(jsonPath("$.data.bookId", is(book.getId().intValue())))
                .andExpect(jsonPath("$.data.memberId", is(member.getId().intValue())))
                .andExpect(jsonPath("$.data.status", is("BORROWED")));
    }

    @Test
    void getTransaction_ShouldReturnNotFoundWhenTransactionDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/transactions/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", containsString("Transaction with id: 999 does not exists")));
    }

    @Test
    void borrowBook_ShouldSetDefaultDueDateWhenNotProvided() throws Exception {
        Book book = testDataSetup.createSampleBook();
        Member member = testDataSetup.createSampleMember();
        
        BorrowTransactionRequest request = new BorrowTransactionRequest(
            book.getId(),
            member.getId(),
            LocalDate.now(),
            null
        );

        mockMvc.perform(post("/api/transactions/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.dueDate", is(LocalDate.now().plusDays(14).toString())));
    }

    @Test
    void borrowBook_ShouldReturnBadRequestWhenBorrowLimitExceeded() throws Exception {
        Author author = testDataSetup.createSampleAuthor();
        
        Book book1 = testDataSetup.createSampleBook();
        
        Book book2 = new Book();
        book2.setTitle("Sample Book 2");
        book2.setIsbn("9781111111111");
        book2.setPublicationYear(2023);
        book2.setAvailableCopies(5);
        book2.setTotalCopies(10);
        book2.setAuthors(Arrays.asList(author));
        bookRepository.save(book2);
        
        Book book3 = new Book();
        book3.setTitle("Sample Book 3");
        book3.setIsbn("9782222222222");
        book3.setPublicationYear(2023);
        book3.setAvailableCopies(5);
        book3.setTotalCopies(10);
        book3.setAuthors(Arrays.asList(author));
        bookRepository.save(book3);
        
        Book book4 = new Book();
        book4.setTitle("Sample Book 4");
        book4.setIsbn("9783333333333");
        book4.setPublicationYear(2023);
        book4.setAvailableCopies(5);
        book4.setTotalCopies(10);
        book4.setAuthors(Arrays.asList(author));
        bookRepository.save(book4);
        
        Member member = testDataSetup.createSampleMember();

        testDataSetup.createSampleBorrowTransaction(book1, member);
        testDataSetup.createSampleBorrowTransaction(book2, member);
        testDataSetup.createSampleBorrowTransaction(book3, member);
        
        BorrowTransactionRequest request = new BorrowTransactionRequest(
            book4.getId(),
            member.getId(),
            LocalDate.now(),
            LocalDate.now().plusDays(14)
        );

        mockMvc.perform(post("/api/transactions/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", containsString("Member has reached maximum borrow limit of 3 books")));
    }

    @Test
    void borrowBook_ShouldReturnBadRequestWhenDuplicateBorrow() throws Exception {
        Book book = testDataSetup.createSampleBook();
        Member member = testDataSetup.createSampleMember();
        
        testDataSetup.createSampleBorrowTransaction(book, member);
        
        BorrowTransactionRequest request = new BorrowTransactionRequest(
            book.getId(),
            member.getId(),
            LocalDate.now(),
            LocalDate.now().plusDays(14)
        );

        mockMvc.perform(post("/api/transactions/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", containsString("Member has already borrowed this book and not returned it yet")));
    }

    @Test
    void borrowBook_ShouldReturnBadRequestWhenDueDateIsBeforeBorrowDate() throws Exception {
        Book book = testDataSetup.createSampleBook();
        Member member = testDataSetup.createSampleMember();
        
        BorrowTransactionRequest request = new BorrowTransactionRequest(
            book.getId(),
            member.getId(),
            LocalDate.now(),
            LocalDate.now().minusDays(1)
        );

        mockMvc.perform(post("/api/transactions/borrow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")));
    }
}
