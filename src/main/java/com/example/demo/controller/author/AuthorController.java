package com.example.demo.controller.author;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.request.author.StoreAuthorRequest;
import com.example.demo.request.author.UpdateAuthorRequest;
import com.example.demo.service.author.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<AuthorDTO>> getAuthor(@PathVariable("id")Long id) {
        AuthorDTO author = authorService.getAuthor(id);
        ApiResponse<AuthorDTO> response = new ApiResponse<>("Author retrieved successfully", "success", HttpStatus.OK.value(), author);
        return new ResponseEntity<ApiResponse<AuthorDTO>>(response, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<AuthorDTO>> postAuthor(@Valid @RequestBody StoreAuthorRequest storeAuthorRequest) {
        AuthorDTO author = authorService.postAuthor(storeAuthorRequest);
        ApiResponse<AuthorDTO> response = new ApiResponse<>("Author created successfully", "success", HttpStatus.CREATED.value(), author);
        return new ResponseEntity<ApiResponse<AuthorDTO>>(response, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<AuthorDTO>> putAuthor(@PathVariable("id") Long id, @Valid @RequestBody UpdateAuthorRequest updateAuthorRequest) {
        AuthorDTO author = authorService.putAuthor(id, updateAuthorRequest);
        ApiResponse<AuthorDTO> response = new ApiResponse<>("Author updated successfully", "success", HttpStatus.OK.value(), author);
        return new ResponseEntity<ApiResponse<AuthorDTO>>(response, HttpStatus.OK);
    }
}
