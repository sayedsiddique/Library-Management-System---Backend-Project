package com.example.demo.controller.author;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Authors", description = "Author management operations")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @Operation(summary = "Get all authors", description = "Retrieve a list of all authors")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Authors retrieved successfully"),
    })
    public ResponseEntity<ApiResponse<List<AuthorDTO>>> getAuthors() {
        List<AuthorDTO> authors = authorService.getAuthors();
        ApiResponse<List<AuthorDTO>> response = new ApiResponse<>("Authors retrieved successfully", "success", HttpStatus.OK.value(), authors);
        return new ResponseEntity<ApiResponse<List<AuthorDTO>>>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a author by ID", description = "Retrieve details of a specific author by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Author retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<ApiResponse<AuthorDTO>> getAuthor(@PathVariable("id")Long id) {
        AuthorDTO author = authorService.getAuthor(id);
        ApiResponse<AuthorDTO> response = new ApiResponse<>("Author retrieved successfully", "success", HttpStatus.OK.value(), author);
        return new ResponseEntity<ApiResponse<AuthorDTO>>(response, HttpStatus.OK);
    }

    @PostMapping()
    @Operation(summary = "Create a author", description = "Add a new author")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Author created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Author with the same email already exists")
    })
    public ResponseEntity<ApiResponse<AuthorDTO>> postAuthor(@Valid @RequestBody StoreAuthorRequest storeAuthorRequest) {
        AuthorDTO author = authorService.postAuthor(storeAuthorRequest);
        ApiResponse<AuthorDTO> response = new ApiResponse<>("Author created successfully", "success", HttpStatus.CREATED.value(), author);
        return new ResponseEntity<ApiResponse<AuthorDTO>>(response, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update a author", description = "Update an existing author's information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Author updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Author not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Author with the same email already exists")
    })
    public ResponseEntity<ApiResponse<AuthorDTO>> putAuthor(@PathVariable("id") Long id, @Valid @RequestBody UpdateAuthorRequest updateAuthorRequest) {
        AuthorDTO author = authorService.putAuthor(id, updateAuthorRequest);
        ApiResponse<AuthorDTO> response = new ApiResponse<>("Author updated successfully", "success", HttpStatus.OK.value(), author);
        return new ResponseEntity<ApiResponse<AuthorDTO>>(response, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a author", description = "Remove a author by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Author not found")
    })
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
