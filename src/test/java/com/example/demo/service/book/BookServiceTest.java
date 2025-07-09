package com.example.demo.service.book;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.exception.DuplicateIsbnException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.author.Author;
import com.example.demo.model.book.Book;
import com.example.demo.repository.author.AuthorRepository;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.request.book.StoreBookRequest;
import com.example.demo.request.book.UpdateBookRequest;
import com.example.demo.specification.book.BookSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDTO bookDTO;
    private Author author;
    private StoreBookRequest storeBookRequest;
    private UpdateBookRequest updateBookRequest;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setName("John Doe");
        author.setEmail("john@example.com");

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setIsbn("978-0123456789");
        book.setPublicationYear(2023);
        book.setAvailableCopies(5);
        book.setTotalCopies(10);
        book.setAuthors(new ArrayList<>(Arrays.asList(author)));
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());

        bookDTO = new BookDTO(
            1L,
            "Test Book",
            "978-0123456789",
            2023,
            5,
            10,
            Arrays.asList(author),
            book.getCreatedAt(),
            book.getUpdatedAt()
        );

        storeBookRequest = new StoreBookRequest(
            "Test Book",
            "978-0123456789",
            2023,
            5,
            10,
            Arrays.asList(1L)
        );

        updateBookRequest = new UpdateBookRequest(
            "Updated Book",
            "978-0987654321",
            2024,
            3,
            8,
            Arrays.asList(1L)
        );

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getBooks_ShouldReturnAllBooks() {
        List<Book> books = Arrays.asList(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(bookPage);

        List<BookDTO> result = bookService.getBooks(null, null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookDTO.title(), result.get(0).title());
        assertEquals(bookDTO.isbn(), result.get(0).isbn());
        verify(bookRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getBooks_ShouldReturnFilteredBooks() {
        List<Book> books = Arrays.asList(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(bookPage);

        List<BookDTO> result = bookService.getBooks("Test", "978-0123456789", "John", pageable);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getBooks_ShouldReturnEmptyListWhenNoBooks() {
        Page<Book> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(bookRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        List<BookDTO> result = bookService.getBooks(null, null, null, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getBook_ShouldReturnBookWhenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDTO result = bookService.getBook(1L);

        assertNotNull(result);
        assertEquals(bookDTO.title(), result.title());
        assertEquals(bookDTO.isbn(), result.isbn());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBook_ShouldThrowResourceNotFoundExceptionWhenNotExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> bookService.getBook(1L)
        );
        assertEquals("Book with id: 1 does not exists", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void postBook_ShouldCreateBookSuccessfully() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(authorRepository.findAllById(any())).thenReturn(Arrays.asList(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookService.postBook(storeBookRequest);

        assertNotNull(result);
        assertEquals(storeBookRequest.title(), result.title());
        assertEquals(storeBookRequest.isbn(), result.isbn());
        verify(bookRepository, times(1)).existsByIsbn(storeBookRequest.isbn());
        verify(authorRepository, times(1)).findAllById(storeBookRequest.authorIds());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void postBook_ShouldThrowDuplicateEmailExceptionWhenIsbnExists() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(true);

        DuplicateEmailException exception = assertThrows(
            DuplicateEmailException.class,
            () -> bookService.postBook(storeBookRequest)
        );
        assertEquals("ISBN already taken", exception.getMessage());
        verify(bookRepository, times(1)).existsByIsbn(storeBookRequest.isbn());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void putBook_ShouldUpdateBookSuccessfully() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(authorRepository.findAllById(any())).thenReturn(Arrays.asList(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookService.putBook(1L, updateBookRequest);

        assertNotNull(result);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).existsByIsbn(updateBookRequest.isbn());
        verify(authorRepository, times(1)).findAllById(updateBookRequest.authorIds());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void putBook_ShouldThrowResourceNotFoundExceptionWhenBookNotExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> bookService.putBook(1L, updateBookRequest)
        );
        assertEquals("Book with id: 1 does not exists", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void putBook_ShouldThrowDuplicateIsbnExceptionWhenIsbnAlreadyExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.existsByIsbn(anyString())).thenReturn(true);

        DuplicateIsbnException exception = assertThrows(
            DuplicateIsbnException.class,
            () -> bookService.putBook(1L, updateBookRequest)
        );
        assertEquals("ISBN already taken", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).existsByIsbn(updateBookRequest.isbn());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void putBook_ShouldAllowSameIsbnForSameBook() {
        UpdateBookRequest sameIsbnRequest = new UpdateBookRequest(
            "Updated Title",
            "978-0123456789", // Same ISBN as original
            2024,
            3,
            8,
            Arrays.asList(1L)
        );
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findAllById(any())).thenReturn(Arrays.asList(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookService.putBook(1L, sameIsbnRequest);

        assertNotNull(result);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).existsByIsbn(anyString());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void putBook_ShouldHandlePartialUpdate() {
        UpdateBookRequest partialRequest = new UpdateBookRequest(
            "Updated Title",
            null,
            null,
            null,
            null,
            null
        );
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookService.putBook(1L, partialRequest);

        assertNotNull(result);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookRepository, never()).existsByIsbn(anyString());
        verify(authorRepository, never()).findAllById(any());
    }

    @Test
    void deleteBook_ShouldDeleteBookSuccessfully() {
        author.setBooks(new ArrayList<>(Arrays.asList(book)));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
        assertTrue(book.getAuthors().isEmpty());
        assertFalse(author.getBooks().contains(book));
    }

    @Test
    void deleteBook_ShouldThrowResourceNotFoundExceptionWhenBookNotExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> bookService.deleteBook(1L)
        );
        assertEquals("Book with id: 1 does not exists", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAuthorBooks_ShouldReturnBooksForAuthor() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.findByAuthors_Id(1L)).thenReturn(Arrays.asList(book));

        List<BookDTO> result = bookService.getAuthorBooks(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookDTO.title(), result.get(0).title());
        verify(authorRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).findByAuthors_Id(1L);
    }

    @Test
    void getAuthorBooks_ShouldThrowResourceNotFoundExceptionWhenAuthorNotExists() {
        when(authorRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> bookService.getAuthorBooks(1L)
        );
        assertEquals("Author with id: 1 does not exist", exception.getMessage());
        verify(authorRepository, times(1)).existsById(1L);
        verify(bookRepository, never()).findByAuthors_Id(anyLong());
    }

    @Test
    void getAuthorBooks_ShouldReturnEmptyListWhenAuthorHasNoBooks() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.findByAuthors_Id(1L)).thenReturn(new ArrayList<>());

        List<BookDTO> result = bookService.getAuthorBooks(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(authorRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).findByAuthors_Id(1L);
    }

    @Test
    void postBook_ShouldCreateBookWithMultipleAuthors() {
        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Jane Doe");
        
        StoreBookRequest multiAuthorRequest = new StoreBookRequest(
            "Test Book",
            "978-0123456789",
            2023,
            5,
            10,
            Arrays.asList(1L, 2L)
        );

        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(authorRepository.findAllById(any())).thenReturn(Arrays.asList(author, author2));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookService.postBook(multiAuthorRequest);

        assertNotNull(result);
        assertEquals(multiAuthorRequest.title(), result.title());
        verify(authorRepository, times(1)).findAllById(multiAuthorRequest.authorIds());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void postBook_ShouldCreateBookWithEmptyAuthorList() {
        StoreBookRequest noAuthorRequest = new StoreBookRequest(
            "Test Book",
            "978-0123456789",
            2023,
            5,
            10,
            new ArrayList<>()
        );

        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(authorRepository.findAllById(any())).thenReturn(new ArrayList<>());
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO result = bookService.postBook(noAuthorRequest);

        assertNotNull(result);
        assertEquals(noAuthorRequest.title(), result.title());
        verify(authorRepository, times(1)).findAllById(noAuthorRequest.authorIds());
        verify(bookRepository, times(1)).save(any(Book.class));
    }
}
