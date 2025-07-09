package com.example.demo.service.borrowTransaction;

import com.example.demo.dto.borrowTransaction.BorrowTransactionDTO;
import com.example.demo.exception.*;
import com.example.demo.model.book.Book;
import com.example.demo.model.borrowTransaction.BorrowTransaction;
import com.example.demo.model.borrowTransaction.Status;
import com.example.demo.model.member.Member;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.borrowTransaction.BorrowTransactionRepository;
import com.example.demo.repository.member.MemberRepository;
import com.example.demo.request.borrowTransaction.BorrowTransactionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowTransactionServiceTest {

    @Mock
    private BorrowTransactionRepository borrowTransactionRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private BorrowTransactionService borrowTransactionService;

    private Book book;
    private Member member;
    private BorrowTransaction borrowTransaction;
    private BorrowTransactionRequest borrowTransactionRequest;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAvailableCopies(5);
        book.setTotalCopies(10);

        member = new Member();
        member.setId(1L);
        member.setName("John Doe");
        member.setEmail("john@example.com");
        member.setStatus(com.example.demo.model.member.Status.ACTIVE);

        borrowTransaction = new BorrowTransaction();
        borrowTransaction.setId(1L);
        borrowTransaction.setBook(book);
        borrowTransaction.setMember(member);
        borrowTransaction.setBorrowDate(LocalDate.now());
        borrowTransaction.setDueDate(LocalDate.now().plusDays(14));
        borrowTransaction.setStatus(Status.BORROWED);

        borrowTransactionRequest = new BorrowTransactionRequest(
            1L,
            1L,
            LocalDate.now(),
            LocalDate.now().plusDays(14)
        );
    }

    @Test
    void borrowBook_ShouldCreateBorrowTransactionSuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowTransactionRepository.countByMemberIdAndStatus(1L, Status.BORROWED)).thenReturn(0L);
        when(borrowTransactionRepository.findByMemberIdAndBookIdAndStatus(1L, 1L, Status.BORROWED))
            .thenReturn(Optional.empty());
        when(borrowTransactionRepository.save(any(BorrowTransaction.class))).thenReturn(borrowTransaction);

        BorrowTransactionDTO result = borrowTransactionService.borrowBook(borrowTransactionRequest);

        assertNotNull(result);
        assertEquals(borrowTransaction.getId(), result.id());
        assertEquals(book.getId(), result.bookId());
        assertEquals(member.getId(), result.memberId());
        assertEquals(Status.BORROWED, result.status());
        verify(bookRepository, times(1)).save(book);
        assertEquals(4, book.getAvailableCopies());
    }

    @Test
    void borrowBook_ShouldThrowResourceNotFoundExceptionWhenBookNotExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> borrowTransactionService.borrowBook(borrowTransactionRequest)
        );
        assertEquals("Book with id: 1 does not exists", exception.getMessage());
        verify(memberRepository, never()).findById(anyLong());
        verify(borrowTransactionRepository, never()).save(any(BorrowTransaction.class));
    }

    @Test
    void borrowBook_ShouldThrowResourceNotFoundExceptionWhenMemberNotExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> borrowTransactionService.borrowBook(borrowTransactionRequest)
        );
        assertEquals("Member with id: 1 does not exists", exception.getMessage());
        verify(borrowTransactionRepository, never()).save(any(BorrowTransaction.class));
    }

    @Test
    void borrowBook_ShouldThrowIllegalStateExceptionWhenMemberIsInactive() {
        member.setStatus(com.example.demo.model.member.Status.INACTIVE);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> borrowTransactionService.borrowBook(borrowTransactionRequest)
        );
        assertEquals("Member is not active", exception.getMessage());
        verify(borrowTransactionRepository, never()).save(any(BorrowTransaction.class));
    }

    @Test
    void borrowBook_ShouldThrowBookNotAvailableExceptionWhenNoAvailableCopies() {
        book.setAvailableCopies(0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        BookNotAvailableException exception = assertThrows(
            BookNotAvailableException.class,
            () -> borrowTransactionService.borrowBook(borrowTransactionRequest)
        );
        assertEquals("No copies available for book: Test Book", exception.getMessage());
        verify(borrowTransactionRepository, never()).save(any(BorrowTransaction.class));
    }

    @Test
    void borrowBook_ShouldThrowBorrowLimitExceededException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowTransactionRepository.countByMemberIdAndStatus(1L, Status.BORROWED)).thenReturn(3L);

        BorrowLimitExceededException exception = assertThrows(
            BorrowLimitExceededException.class,
            () -> borrowTransactionService.borrowBook(borrowTransactionRequest)
        );
        assertEquals("Member has reached maximum borrow limit of 3 books", exception.getMessage());
        verify(borrowTransactionRepository, never()).save(any(BorrowTransaction.class));
    }

    @Test
    void borrowBook_ShouldThrowDuplicateBorrowException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowTransactionRepository.countByMemberIdAndStatus(1L, Status.BORROWED)).thenReturn(1L);
        when(borrowTransactionRepository.findByMemberIdAndBookIdAndStatus(1L, 1L, Status.BORROWED))
            .thenReturn(Optional.of(borrowTransaction));

        DuplicateBorrowException exception = assertThrows(
            DuplicateBorrowException.class,
            () -> borrowTransactionService.borrowBook(borrowTransactionRequest)
        );
        assertEquals("Member has already borrowed this book and not returned it yet", exception.getMessage());
        verify(borrowTransactionRepository, never()).save(any(BorrowTransaction.class));
    }

    @Test
    void borrowBook_ShouldSetDefaultDueDateWhenNotProvided() {
        BorrowTransactionRequest requestWithoutDueDate = new BorrowTransactionRequest(
            1L, 1L, LocalDate.now(), null
        );
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowTransactionRepository.countByMemberIdAndStatus(1L, Status.BORROWED)).thenReturn(0L);
        when(borrowTransactionRepository.findByMemberIdAndBookIdAndStatus(1L, 1L, Status.BORROWED))
            .thenReturn(Optional.empty());
        when(borrowTransactionRepository.save(any(BorrowTransaction.class))).thenReturn(borrowTransaction);

        BorrowTransactionDTO result = borrowTransactionService.borrowBook(requestWithoutDueDate);

        assertNotNull(result);
        assertEquals(LocalDate.now().plusDays(14), result.dueDate());
    }

    @Test
    void returnBook_ShouldReturnBookSuccessfully() {
        when(borrowTransactionRepository.findById(1L)).thenReturn(Optional.of(borrowTransaction));
        when(borrowTransactionRepository.save(any(BorrowTransaction.class))).thenReturn(borrowTransaction);

        BorrowTransactionDTO result = borrowTransactionService.returnBook(1L);

        assertNotNull(result);
        assertEquals(Status.RETURNED, borrowTransaction.getStatus());
        assertEquals(LocalDate.now(), borrowTransaction.getReturnDate());
        assertEquals(6, book.getAvailableCopies());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void returnBook_ShouldThrowResourceNotFoundExceptionWhenTransactionNotExists() {
        when(borrowTransactionRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> borrowTransactionService.returnBook(1L)
        );
        assertEquals("Transaction with id: 1 does not exists", exception.getMessage());
        verify(borrowTransactionRepository, never()).save(any(BorrowTransaction.class));
    }

    @Test
    void returnBook_ShouldThrowIllegalStateExceptionWhenAlreadyReturned() {
        borrowTransaction.setStatus(Status.RETURNED);
        when(borrowTransactionRepository.findById(1L)).thenReturn(Optional.of(borrowTransaction));

        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> borrowTransactionService.returnBook(1L)
        );
        assertEquals("Book has already been returned", exception.getMessage());
        verify(borrowTransactionRepository, never()).save(any(BorrowTransaction.class));
    }

    @Test
    void getTransactions_ShouldReturnAllTransactions() {
        List<BorrowTransaction> transactions = Arrays.asList(borrowTransaction);
        when(borrowTransactionRepository.findAll()).thenReturn(transactions);

        List<BorrowTransactionDTO> result = borrowTransactionService.getTransactions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(borrowTransaction.getId(), result.get(0).id());
        verify(borrowTransactionRepository, times(1)).findAll();
    }

    @Test
    void getTransactions_ShouldReturnEmptyListWhenNoTransactions() {
        when(borrowTransactionRepository.findAll()).thenReturn(new ArrayList<>());

        List<BorrowTransactionDTO> result = borrowTransactionService.getTransactions();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(borrowTransactionRepository, times(1)).findAll();
    }

    @Test
    void getTransaction_ShouldReturnTransactionWhenExists() {
        when(borrowTransactionRepository.findById(1L)).thenReturn(Optional.of(borrowTransaction));

        BorrowTransactionDTO result = borrowTransactionService.getTransaction(1L);

        assertNotNull(result);
        assertEquals(borrowTransaction.getId(), result.id());
        verify(borrowTransactionRepository, times(1)).findById(1L);
    }

    @Test
    void getTransaction_ShouldThrowResourceNotFoundExceptionWhenNotExists() {
        when(borrowTransactionRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> borrowTransactionService.getTransaction(1L)
        );
        assertEquals("Transaction with id: 1 does not exists", exception.getMessage());
        verify(borrowTransactionRepository, times(1)).findById(1L);
    }

    @Test
    void getMemberBorrowTransactions_ShouldReturnMemberTransactions() {
        when(memberRepository.existsById(1L)).thenReturn(true);
        when(borrowTransactionRepository.findByMemberId(1L)).thenReturn(Arrays.asList(borrowTransaction));

        List<BorrowTransactionDTO> result = borrowTransactionService.getMemberBorrowTransactions(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(borrowTransaction.getId(), result.get(0).id());
        verify(memberRepository, times(1)).existsById(1L);
        verify(borrowTransactionRepository, times(1)).findByMemberId(1L);
    }

    @Test
    void getMemberBorrowTransactions_ShouldThrowResourceNotFoundExceptionWhenMemberNotExists() {
        when(memberRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> borrowTransactionService.getMemberBorrowTransactions(1L)
        );
        assertEquals("Member with id: 1 does not exist", exception.getMessage());
        verify(memberRepository, times(1)).existsById(1L);
        verify(borrowTransactionRepository, never()).findByMemberId(anyLong());
    }

    @Test
    void getMemberBorrowTransactions_ShouldReturnEmptyListWhenMemberHasNoTransactions() {
        when(memberRepository.existsById(1L)).thenReturn(true);
        when(borrowTransactionRepository.findByMemberId(1L)).thenReturn(new ArrayList<>());

        List<BorrowTransactionDTO> result = borrowTransactionService.getMemberBorrowTransactions(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(memberRepository, times(1)).existsById(1L);
        verify(borrowTransactionRepository, times(1)).findByMemberId(1L);
    }

    @Test
    void convertToDTO_ShouldDetectOverdueTransaction() {
        borrowTransaction.setDueDate(LocalDate.now().minusDays(1));
        borrowTransaction.setStatus(Status.BORROWED);
        when(borrowTransactionRepository.findById(1L)).thenReturn(Optional.of(borrowTransaction));

        BorrowTransactionDTO result = borrowTransactionService.getTransaction(1L);

        assertNotNull(result);
        assertTrue(result.isOverdue());
    }

    @Test
    void convertToDTO_ShouldNotDetectOverdueForReturnedTransaction() {
        borrowTransaction.setDueDate(LocalDate.now().minusDays(1));
        borrowTransaction.setStatus(Status.RETURNED);
        borrowTransaction.setReturnDate(LocalDate.now().minusDays(2));
        when(borrowTransactionRepository.findById(1L)).thenReturn(Optional.of(borrowTransaction));

        BorrowTransactionDTO result = borrowTransactionService.getTransaction(1L);

        assertNotNull(result);
        assertFalse(result.isOverdue());
    }

    @Test
    void convertToDTO_ShouldNotDetectOverdueForFutureDueDate() {
        borrowTransaction.setDueDate(LocalDate.now().plusDays(1));
        borrowTransaction.setStatus(Status.BORROWED);
        when(borrowTransactionRepository.findById(1L)).thenReturn(Optional.of(borrowTransaction));

        BorrowTransactionDTO result = borrowTransactionService.getTransaction(1L);

        assertNotNull(result);
        assertFalse(result.isOverdue());
    }

    @Test
    void borrowBook_ShouldAllowBorrowingAtBorrowLimit() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(borrowTransactionRepository.countByMemberIdAndStatus(1L, Status.BORROWED)).thenReturn(2L);
        when(borrowTransactionRepository.findByMemberIdAndBookIdAndStatus(1L, 1L, Status.BORROWED))
            .thenReturn(Optional.empty());
        when(borrowTransactionRepository.save(any(BorrowTransaction.class))).thenReturn(borrowTransaction);

        BorrowTransactionDTO result = borrowTransactionService.borrowBook(borrowTransactionRequest);

        assertNotNull(result);
        assertEquals(Status.BORROWED, result.status());
        verify(borrowTransactionRepository, times(1)).save(any(BorrowTransaction.class));
    }
}
