package com.example.demo.service.author;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.model.author.Author;
import com.example.demo.repository.author.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDTO> getAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AuthorDTO convertToDTO(Author author) {
        return new AuthorDTO(
            author.getId(),
            author.getName(),
            author.getEmail(),
            author.getBio(),
            author.getBirthDate(),
            author.getCreatedAt(),
            author.getUpdatedAt()
        );
    }
}
