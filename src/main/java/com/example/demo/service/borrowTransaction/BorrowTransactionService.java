package com.example.demo.service.borrowTransaction;

import com.example.demo.dto.borrowTransaction.BorrowTransactionDTO;
import com.example.demo.exception.*;
import com.example.demo.model.book.Book;
import com.example.demo.model.borrowTransaction.BorrowTransaction;
import com.example.demo.model.borrowTransaction.Status;
import com.example.demo.model.member.Member;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.repository.member.MemberRepository;
import com.example.demo.repository.borrowTransaction.BorrowTransactionRepository;
import com.example.demo.request.borrowTransaction.BorrowTransactionRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowTransactionService {
    private final BorrowTransactionRepository borrowTransactionRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private static final int MAX_BORROW_LIMIT = 3;
    private static final int DEFAULT_BORROW_DAYS = 14;

    public BorrowTransactionService(BorrowTransactionRepository borrowTransactionRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.borrowTransactionRepository = borrowTransactionRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public BorrowTransactionDTO borrowBook(BorrowTransactionRequest borrowTransactionRequest) {
        Book book = bookRepository.findById(borrowTransactionRequest.bookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + borrowTransactionRequest.bookId() + " does not exists"));
        
        Member member = memberRepository.findById(borrowTransactionRequest.memberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member with id: " + borrowTransactionRequest.memberId() + " does not exists"));

        if (member.getStatus() != com.example.demo.model.member.Status.ACTIVE) {
            throw new IllegalStateException("Member is not active");
        }

        validateBookAvailability(book);
        validateMemberBorrowLimit(member.getId());
        validateDuplicateBorrow(member.getId(), book.getId());
        
        BorrowTransaction borrowTransaction = new BorrowTransaction();
        borrowTransaction.setBook(book);
        borrowTransaction.setMember(member);
        borrowTransaction.setBorrowDate(borrowTransactionRequest.borrowDate());
        borrowTransaction.setDueDate(borrowTransactionRequest.dueDate() != null ? borrowTransactionRequest.dueDate() : borrowTransactionRequest.borrowDate().plusDays(DEFAULT_BORROW_DAYS));
        borrowTransaction.setStatus(Status.BORROWED);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return this.convertToDTO(borrowTransactionRepository.save(borrowTransaction));
    }

    public BorrowTransactionDTO returnBook(Long transactionId) {
        BorrowTransaction borrowTransaction = borrowTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with id: " + transactionId + " does not exists"));
        
        if (borrowTransaction.getStatus() == Status.RETURNED) {
            throw new IllegalStateException("Book has already been returned");
        }
        
        borrowTransaction.setReturnDate(LocalDate.now());
        borrowTransaction.setStatus(Status.RETURNED);

        Book book = borrowTransaction.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return this.convertToDTO(borrowTransactionRepository.save(borrowTransaction));
    }
    
    public List<BorrowTransactionDTO> getTransactions() {
        return borrowTransactionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public BorrowTransactionDTO getTransaction(Long id) {
        return borrowTransactionRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with id: " + id + " does not exists"));
    }

    public List<BorrowTransactionDTO> getMemberBorrowTransactions(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member with id: " + id + " does not exist");
        }

        return borrowTransactionRepository.findByMemberId(id)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BorrowTransactionDTO> getTransactionsByDueDate(LocalDate dueDate) {
        return borrowTransactionRepository.findByDueDate(dueDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private void validateBookAvailability(Book book) {
        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("No copies available for book: " + book.getTitle());
        }
    }
    
    private void validateMemberBorrowLimit(Long memberId) {
        long activeBorrows = borrowTransactionRepository.countByMemberIdAndStatus(memberId, Status.BORROWED);
        if (activeBorrows >= MAX_BORROW_LIMIT) {
            throw new BorrowLimitExceededException("Member has reached maximum borrow limit of " + MAX_BORROW_LIMIT + " books");
        }
    }
    
    private void validateDuplicateBorrow(Long memberId, Long bookId) {
        Optional<BorrowTransaction> existingBorrow = borrowTransactionRepository.findByMemberIdAndBookIdAndStatus(
                memberId, bookId, Status.BORROWED);
        if (existingBorrow.isPresent()) {
            throw new DuplicateBorrowException("Member has already borrowed this book and not returned it yet");
        }
    }
    
    private BorrowTransactionDTO convertToDTO(BorrowTransaction transaction) {
        boolean isOverdue = transaction.getStatus() == Status.BORROWED && transaction.getDueDate().isBefore(LocalDate.now());

        return new BorrowTransactionDTO(
                transaction.getId(),
                transaction.getBook().getId(),
                transaction.getBook().getTitle(),
                transaction.getMember().getId(),
                transaction.getMember().getName(),
                transaction.getMember().getEmail(),
                transaction.getBorrowDate(),
                transaction.getDueDate(),
                transaction.getReturnDate(),
                transaction.getStatus(),
                isOverdue
        );
    }
}
