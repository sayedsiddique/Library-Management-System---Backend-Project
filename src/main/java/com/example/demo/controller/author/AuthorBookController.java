package com.example.demo.controller.author;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.book.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/authors/{id}/books")
public class AuthorBookController {
    private final BookService bookService;

    public AuthorBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<BookDTO>>> getAuthorBooks(@PathVariable("id")Long id) {
        List<BookDTO> books = bookService.getAuthorBooks(id);
        ApiResponse<List<BookDTO>> response = new ApiResponse<>("Books retrieved successfully", "success", HttpStatus.OK.value(), books);
        return new ResponseEntity<ApiResponse<List<BookDTO>>>(response, HttpStatus.OK);
    }
}
