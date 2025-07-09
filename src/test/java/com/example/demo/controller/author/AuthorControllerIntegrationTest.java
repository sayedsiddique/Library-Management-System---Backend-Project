package com.example.demo.controller.author;

import com.example.demo.model.author.Author;
import com.example.demo.model.book.Book;
import com.example.demo.repository.author.AuthorRepository;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.request.author.StoreAuthorRequest;
import com.example.demo.request.author.UpdateAuthorRequest;
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
class AuthorControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        authorRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    void getAuthors_ShouldReturnEmptyListWhenNoAuthors() throws Exception {
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    void getAuthors_ShouldReturnAllAuthors() throws Exception {
        Author author1 = createAuthor("John Doe", "john@example.com", "Bio 1");
        Author author2 = createAuthor("Jane Smith", "jane@example.com", "Bio 2");
        authorRepository.saveAll(Arrays.asList(author1, author2));

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", is("John Doe")))
                .andExpect(jsonPath("$.data[0].email", is("john@example.com")))
                .andExpect(jsonPath("$.data[1].name", is("Jane Smith")))
                .andExpect(jsonPath("$.data[1].email", is("jane@example.com")));
    }

    @Test
    void getAuthor_ShouldReturnAuthorWhenExists() throws Exception {
        Author author = createAuthor("John Doe", "john@example.com", "Famous author");
        Author savedAuthor = authorRepository.save(author);

        mockMvc.perform(get("/api/authors/{id}", savedAuthor.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.name", is("John Doe")))
                .andExpect(jsonPath("$.data.email", is("john@example.com")))
                .andExpect(jsonPath("$.data.bio", is("Famous author")));
    }

    @Test
    void getAuthor_ShouldReturnNotFoundWhenAuthorDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/authors/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", containsString("Author with id: 999 does not exists")));
    }

    @Test
    void postAuthor_ShouldCreateAuthorSuccessfully() throws Exception {
        StoreAuthorRequest request = new StoreAuthorRequest(
            "John Doe",
            "john@example.com",
            "Famous author",
            LocalDate.of(1980, 1, 1)
        );

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.name", is("John Doe")))
                .andExpect(jsonPath("$.data.email", is("john@example.com")))
                .andExpect(jsonPath("$.data.bio", is("Famous author")));

        assertEquals(1, authorRepository.count());
        Optional<Author> savedAuthor = authorRepository.findAll().stream().filter(a -> a.getEmail().equals("john@example.com")).findFirst();
        assertTrue(savedAuthor.isPresent());
        assertEquals("John Doe", savedAuthor.get().getName());
    }

    @Test
    void postAuthor_ShouldReturnBadRequestWhenEmailAlreadyExists() throws Exception {
        Author existingAuthor = createAuthor("Existing Author", "john@example.com", "Bio");
        authorRepository.save(existingAuthor);

        StoreAuthorRequest request = new StoreAuthorRequest(
            "John Doe",
            "john@example.com", // Same email
            "Famous author",
            LocalDate.of(1980, 1, 1)
        );

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Email already in use")));

        assertEquals(1, authorRepository.count());
    }

    @Test
    void postAuthor_ShouldReturnBadRequestWhenNameIsEmpty() throws Exception {
        StoreAuthorRequest request = new StoreAuthorRequest(
            "", // Empty name
            "john@example.com",
            "Famous author",
            LocalDate.of(1980, 1, 1)
        );

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")));
    }

    @Test
    void putAuthor_ShouldUpdateAuthorSuccessfully() throws Exception {
        Author author = createAuthor("John Doe", "john@example.com", "Old bio");
        Author savedAuthor = authorRepository.save(author);

        UpdateAuthorRequest request = new UpdateAuthorRequest(
            "John Updated",
            "john.updated@example.com",
            "Updated bio",
            LocalDate.of(1980, 1, 1)
        );

        mockMvc.perform(put("/api/authors/{id}", savedAuthor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.data.name", is("John Updated")))
                .andExpect(jsonPath("$.data.email", is("john.updated@example.com")))
                .andExpect(jsonPath("$.data.bio", is("Updated bio")));

        Optional<Author> updatedAuthor = authorRepository.findById(savedAuthor.getId());
        assertTrue(updatedAuthor.isPresent());
        assertEquals("John Updated", updatedAuthor.get().getName());
        assertEquals("john.updated@example.com", updatedAuthor.get().getEmail());
    }

    @Test
    void putAuthor_ShouldReturnNotFoundWhenAuthorDoesNotExist() throws Exception {
        UpdateAuthorRequest request = new UpdateAuthorRequest(
            "John Updated",
            "john.updated@example.com",
            "Updated bio",
            LocalDate.of(1980, 1, 1)
        );

        mockMvc.perform(put("/api/authors/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", containsString("Author with id: 999 does not exists")));
    }

    @Test
    void putAuthor_ShouldReturnBadRequestWhenEmailAlreadyExists() throws Exception {
        Author author1 = createAuthor("John Doe", "john@example.com", "Bio 1");
        Author author2 = createAuthor("Jane Smith", "jane@example.com", "Bio 2");
        Author savedAuthor1 = authorRepository.save(author1);
        authorRepository.save(author2);

        UpdateAuthorRequest request = new UpdateAuthorRequest(
            "John Updated",
            "jane@example.com", // Email already used by author2
            "Updated bio",
            LocalDate.of(1980, 1, 1)
        );

        mockMvc.perform(put("/api/authors/{id}", savedAuthor1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Email already in use")));
    }

    @Test
    void deleteAuthor_ShouldDeleteAuthorSuccessfully() throws Exception {
        Author author = createAuthor("John Doe", "john@example.com", "Bio");
        Author savedAuthor = authorRepository.save(author);

        mockMvc.perform(delete("/api/authors/{id}", savedAuthor.getId()))
                .andExpect(status().isNoContent());

        assertEquals(0, authorRepository.count());
        assertFalse(authorRepository.existsById(savedAuthor.getId()));
    }

    @Test
    void deleteAuthor_ShouldReturnNotFoundWhenAuthorDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/authors/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", containsString("Author with id: 999 does not exists")));
    }

    @Test
    void deleteAuthor_ShouldHandleAuthorWithBooks() throws Exception {
        Author author = createAuthor("John Doe", "john@example.com", "Bio");
        Author savedAuthor = authorRepository.save(author);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setIsbn("9781234567890");
        book.setPublicationYear(2023);
        book.setAvailableCopies(5);
        book.setTotalCopies(10);
        book.setAuthors(Arrays.asList(savedAuthor));
        bookRepository.save(book);

        mockMvc.perform(delete("/api/authors/{id}", savedAuthor.getId()))
                .andExpect(status().isNoContent());

        assertEquals(0, authorRepository.count());
        assertEquals(1, bookRepository.count());
    }

    private Author createAuthor(String name, String email, String bio) {
        Author author = new Author();
        author.setName(name);
        author.setEmail(email);
        author.setBio(bio);
        author.setBirthDate(LocalDate.of(1980, 1, 1));
        return author;
    }
}
