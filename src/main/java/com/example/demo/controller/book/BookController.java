package com.example.demo.controller.book;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.request.book.StoreBookRequest;
import com.example.demo.request.book.UpdateBookRequest;
import com.example.demo.service.book.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
@Tag(name = "Books", description = "Book management operations")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieve a list of all books with optional filtering by title and ISBN")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    public ResponseEntity<ApiResponse<List<BookDTO>>> getBooks(
            @Parameter(description = "Filter by book title") @RequestParam(required = false) String title,
            @Parameter(description = "Filter by book ISBN") @RequestParam(required = false) String isbn,
            @Parameter(description = "Pagination information") Pageable pageable) {
        List<BookDTO> books = bookService.getBooks(title, isbn, null, pageable);
        ApiResponse<List<BookDTO>> response = new ApiResponse<>("Books retrieved successfully", "success", HttpStatus.OK.value(), books);
        return new ResponseEntity<ApiResponse<List<BookDTO>>>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a book by ID", description = "Retrieve details of a specific book by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Book retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<ApiResponse<BookDTO>> getBook(@Parameter(description = "ID of the book to retrieve") @PathVariable("id") Long id) {
        BookDTO book = bookService.getBook(id);
        ApiResponse<BookDTO> response = new ApiResponse<>("Book retrieved successfully", "success", HttpStatus.OK.value(), book);
        return new ResponseEntity<ApiResponse<BookDTO>>(response, HttpStatus.OK);
    }

    @PostMapping()
    @Operation(summary = "Create a book", description = "Add a new book to the library")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Book created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Book with the same ISBN already exists")
    })
    public ResponseEntity<ApiResponse<BookDTO>> postBook(@Parameter(description = "Book data for new book creation") @Valid @RequestBody StoreBookRequest storeBookRequest) {
        BookDTO author = bookService.postBook(storeBookRequest);
        ApiResponse<BookDTO> response = new ApiResponse<>("Book created successfully", "success", HttpStatus.CREATED.value(), author);
        return new ResponseEntity<ApiResponse<BookDTO>>(response, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update a book", description = "Update an existing book's information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Book with the same ISBN already exists")
    })
    public ResponseEntity<ApiResponse<BookDTO>> putBook(
            @Parameter(description = "ID of the book to update") @PathVariable("id") Long id,
            @Parameter(description = "Updated book data") @Valid @RequestBody UpdateBookRequest updateBookRequest) {
        BookDTO author = bookService.putBook(id, updateBookRequest);
        ApiResponse<BookDTO> response = new ApiResponse<>("Book updated successfully", "success", HttpStatus.OK.value(), author);
        return new ResponseEntity<ApiResponse<BookDTO>>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a book", description = "Remove a book from the library by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Void> deleteBook(@Parameter(description = "ID of the book to delete") @PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search for books by title, ISBN, or author name")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Books search results retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    public ResponseEntity<ApiResponse<List<BookDTO>>> searchBooks(
            @Parameter(description = "Search by book title") @RequestParam(required = false) String title,
            @Parameter(description = "Search by book ISBN") @RequestParam(required = false) String isbn,
            @Parameter(description = "Search by author name") @RequestParam(required = false) String author,
            @Parameter(description = "Pagination information") Pageable pageable) {
        List<BookDTO> books = bookService.getBooks(title, isbn, author, pageable);
        ApiResponse<List<BookDTO>> response = new ApiResponse<>("Books search results", "success", HttpStatus.OK.value(), books);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
