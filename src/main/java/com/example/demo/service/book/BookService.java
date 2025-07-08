package com.example.demo.service.book;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.author.Author;
import com.example.demo.model.book.Book;
import com.example.demo.repository.author.AuthorRepository;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.request.book.StoreBookRequest;
import com.example.demo.request.book.UpdateBookRequest;
import com.example.demo.specification.book.BookSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<BookDTO> getBooks(String title, String isbn, String author, Pageable pageable) {
        Page<Book> pageResult = bookRepository.findAll(BookSpecification.search(title, isbn, author), pageable);
        return pageResult
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBook(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " does not exists"));
    }

    public BookDTO postBook(StoreBookRequest storeBookRequest) {
        if (bookRepository.existsByIsbn(storeBookRequest.isbn())) {
            throw new DuplicateEmailException("ISBN already taken");
        }

        Book book = new Book();
        book.setTitle(storeBookRequest.title());
        book.setIsbn(storeBookRequest.isbn());
        book.setPublicationYear(storeBookRequest.publicationYear());
        book.setAvailableCopies(storeBookRequest.availableCopies());
        book.setTotalCopies(storeBookRequest.totalCopies());
        List<Author> authors = authorRepository.findAllById(storeBookRequest.authorIds());
        book.setAuthors(authors);
        return this.convertToDTO(bookRepository.save(book));
    }

    public BookDTO putBook(Long id, UpdateBookRequest updateBookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id: " + id + " does not exists"));

        if (!book.getIsbn().equals(updateBookRequest.isbn())
                && bookRepository.existsByIsbn(updateBookRequest.isbn())) {
            throw new DuplicateEmailException("ISBN already taken");
        }

        book.setTitle(updateBookRequest.title() != null ? updateBookRequest.title() : book.getTitle());
        book.setIsbn(updateBookRequest.isbn() != null ? updateBookRequest.isbn() : book.getIsbn());
        book.setPublicationYear(updateBookRequest.publicationYear() != null ? updateBookRequest.publicationYear() : book.getPublicationYear());
        book.setAvailableCopies(updateBookRequest.availableCopies() != null ? updateBookRequest.availableCopies() : book.getAvailableCopies());
        book.setTotalCopies(updateBookRequest.totalCopies() != null ? updateBookRequest.totalCopies() : book.getTotalCopies());
        List<Author> authors = (updateBookRequest.authorIds() != null) ? authorRepository.findAllById(updateBookRequest.authorIds()) : book.getAuthors();
        book.setAuthors(authors);
        return this.convertToDTO(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        boolean exists = bookRepository.existsById(id);
        if(! exists) {
            throw new ResourceNotFoundException("Book with id: " + id + " does not exists");
        }
        bookRepository.deleteById(id);
    }

    public List<BookDTO> getAuthorBooks(Long id) {
        return bookRepository.findByAuthors_Id(id)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getAvailableCopies(),
                book.getTotalCopies(),
                book.getAuthors(),
                book.getCreatedAt(),
                book.getUpdatedAt()
        );
    }
}
