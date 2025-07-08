package com.example.demo.controller.author;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.author.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthorDTO>>> getAuthors() {
        List<AuthorDTO> authors = authorService.getAuthors();
        ApiResponse<List<AuthorDTO>> response = new ApiResponse<>("Authors retrieved successfully", "success", HttpStatus.OK.value(), authors);
        return new ResponseEntity<ApiResponse<List<AuthorDTO>>>(response, HttpStatus.OK);
    }
}
