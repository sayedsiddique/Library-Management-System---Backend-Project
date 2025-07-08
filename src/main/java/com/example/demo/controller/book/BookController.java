package com.example.demo.controller.book;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.request.book.StoreBookRequest;
import com.example.demo.request.book.UpdateBookRequest;
import com.example.demo.service.book.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookDTO>>> getBooks(@RequestParam(required = false) String title, @RequestParam(required = false) String isbn, Pageable pageable) {
        List<BookDTO> books = bookService.getBooks(title, isbn, null, pageable);
        ApiResponse<List<BookDTO>> response = new ApiResponse<>("Books retrieved successfully", "success", HttpStatus.OK.value(), books);
        return new ResponseEntity<ApiResponse<List<BookDTO>>>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<BookDTO>> getBook(@PathVariable("id")Long id) {
        BookDTO book = bookService.getBook(id);
        ApiResponse<BookDTO> response = new ApiResponse<>("Book retrieved successfully", "success", HttpStatus.OK.value(), book);
        return new ResponseEntity<ApiResponse<BookDTO>>(response, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<BookDTO>> postBook(@Valid @RequestBody StoreBookRequest storeBookRequest) {
        BookDTO author = bookService.postBook(storeBookRequest);
        ApiResponse<BookDTO> response = new ApiResponse<>("Book created successfully", "success", HttpStatus.CREATED.value(), author);
        return new ResponseEntity<ApiResponse<BookDTO>>(response, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<BookDTO>> putBook(@PathVariable("id") Long id, @Valid @RequestBody UpdateBookRequest updateBookRequest) {
        BookDTO author = bookService.putBook(id, updateBookRequest);
        ApiResponse<BookDTO> response = new ApiResponse<>("Book updated successfully", "success", HttpStatus.OK.value(), author);
        return new ResponseEntity<ApiResponse<BookDTO>>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookDTO>>> searchBooks(@RequestParam(required = false) String title, @RequestParam(required = false) String isbn, @RequestParam(required = false) String author, Pageable pageable) {
        List<BookDTO> books = bookService.getBooks(title, isbn, author, pageable);
        ApiResponse<List<BookDTO>> response = new ApiResponse<>("Books search results", "success", HttpStatus.OK.value(), books);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
