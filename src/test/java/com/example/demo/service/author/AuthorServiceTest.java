package com.example.demo.service.author;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.author.Author;
import com.example.demo.model.book.Book;
import com.example.demo.repository.author.AuthorRepository;
import com.example.demo.request.author.StoreAuthorRequest;
import com.example.demo.request.author.UpdateAuthorRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorDTO authorDTO;
    private StoreAuthorRequest storeAuthorRequest;
    private UpdateAuthorRequest updateAuthorRequest;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setName("John Doe");
        author.setEmail("john@example.com");
        author.setBio("Famous author");
        author.setBirthDate(LocalDate.of(1980, 1, 1));
        author.setCreatedAt(LocalDateTime.now());
        author.setUpdatedAt(LocalDateTime.now());
        author.setBooks(new ArrayList<>());

        authorDTO = new AuthorDTO(
            1L,
            "John Doe",
            "john@example.com",
            "Famous author",
            LocalDate.of(1980, 1, 1),
            author.getCreatedAt(),
            author.getUpdatedAt()
        );

        storeAuthorRequest = new StoreAuthorRequest(
            "John Doe",
            "john@example.com",
            "Famous author",
            LocalDate.of(1980, 1, 1)
        );

        updateAuthorRequest = new UpdateAuthorRequest(
            "John Updated",
            "john.updated@example.com",
            "Updated bio",
            LocalDate.of(1980, 1, 1)
        );
    }

    @Test
    void getAuthors_ShouldReturnAllAuthors() {
        List<Author> authors = Arrays.asList(author);
        when(authorRepository.findAll()).thenReturn(authors);

        List<AuthorDTO> result = authorService.getAuthors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(authorDTO.name(), result.get(0).name());
        assertEquals(authorDTO.email(), result.get(0).email());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void getAuthors_ShouldReturnEmptyListWhenNoAuthors() {
        when(authorRepository.findAll()).thenReturn(new ArrayList<>());

        List<AuthorDTO> result = authorService.getAuthors();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void getAuthor_ShouldReturnAuthorWhenExists() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorDTO result = authorService.getAuthor(1L);

        assertNotNull(result);
        assertEquals(authorDTO.name(), result.name());
        assertEquals(authorDTO.email(), result.email());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void getAuthor_ShouldThrowResourceNotFoundExceptionWhenNotExists() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> authorService.getAuthor(1L)
        );
        assertEquals("Author with id: 1 does not exists", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void postAuthor_ShouldCreateAuthorSuccessfully() {
        when(authorRepository.existsByEmail(anyString())).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorDTO result = authorService.postAuthor(storeAuthorRequest);

        assertNotNull(result);
        assertEquals(storeAuthorRequest.name(), result.name());
        assertEquals(storeAuthorRequest.email(), result.email());
        verify(authorRepository, times(1)).existsByEmail(storeAuthorRequest.email());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void postAuthor_ShouldThrowDuplicateEmailExceptionWhenEmailExists() {
        when(authorRepository.existsByEmail(anyString())).thenReturn(true);

        DuplicateEmailException exception = assertThrows(
            DuplicateEmailException.class,
            () -> authorService.postAuthor(storeAuthorRequest)
        );
        assertEquals("Email already in use", exception.getMessage());
        verify(authorRepository, times(1)).existsByEmail(storeAuthorRequest.email());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void putAuthor_ShouldUpdateAuthorSuccessfully() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.existsByEmail(anyString())).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorDTO result = authorService.putAuthor(1L, updateAuthorRequest);

        assertNotNull(result);
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).existsByEmail(updateAuthorRequest.email());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void putAuthor_ShouldThrowResourceNotFoundExceptionWhenAuthorNotExists() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> authorService.putAuthor(1L, updateAuthorRequest)
        );
        assertEquals("Author with id: 1 does not exists", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void putAuthor_ShouldThrowDuplicateEmailExceptionWhenEmailAlreadyExists() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.existsByEmail(anyString())).thenReturn(true);

        DuplicateEmailException exception = assertThrows(
            DuplicateEmailException.class,
            () -> authorService.putAuthor(1L, updateAuthorRequest)
        );
        assertEquals("Email already in use", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).existsByEmail(updateAuthorRequest.email());
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void putAuthor_ShouldAllowSameEmailForSameAuthor() {
        UpdateAuthorRequest sameEmailRequest = new UpdateAuthorRequest(
            "John Updated",
            "john@example.com", // Same email as original
            "Updated bio",
            LocalDate.of(1980, 1, 1)
        );
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorDTO result = authorService.putAuthor(1L, sameEmailRequest);

        assertNotNull(result);
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, never()).existsByEmail(anyString());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void deleteAuthor_ShouldDeleteAuthorSuccessfully() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthors(new ArrayList<>(Arrays.asList(author)));
        author.setBooks(new ArrayList<>(Arrays.asList(book)));
        
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).deleteById(1L);
        assertTrue(author.getBooks().isEmpty());
        assertFalse(book.getAuthors().contains(author));
    }

    @Test
    void deleteAuthor_ShouldThrowResourceNotFoundExceptionWhenAuthorNotExists() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> authorService.deleteAuthor(1L)
        );
        assertEquals("Author with id: 1 does not exists", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, never()).deleteById(anyLong());
    }

    @Test
    void putAuthor_ShouldHandlePartialUpdate() {
        UpdateAuthorRequest partialRequest = new UpdateAuthorRequest(
            "Updated Name",
            null,
            null,
            null
        );
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorDTO result = authorService.putAuthor(1L, partialRequest);

        assertNotNull(result);
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(any(Author.class));
        verify(authorRepository, never()).existsByEmail(anyString());
    }
}
