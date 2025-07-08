package com.example.demo.service.author;

import com.example.demo.dto.author.AuthorDTO;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.author.Author;
import com.example.demo.repository.author.AuthorRepository;
import com.example.demo.request.author.StoreAuthorRequest;
import com.example.demo.request.author.UpdateAuthorRequest;
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

    public AuthorDTO getAuthor(Long id) {
        return authorRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id: " + id + " does not exists"));
    }

    public AuthorDTO postAuthor(StoreAuthorRequest storeAuthorRequest) {
        if (authorRepository.existsByEmail(storeAuthorRequest.email())) {
            throw new DuplicateEmailException("Email already in use");
        }

        Author author = new Author();
        author.setName(storeAuthorRequest.name());
        author.setEmail(storeAuthorRequest.email());
        author.setBio(storeAuthorRequest.bio());
        author.setBirthDate(storeAuthorRequest.birthDate());
        return this.convertToDTO(authorRepository.save(author));
    }

    public AuthorDTO putAuthor(Long id, UpdateAuthorRequest updateAuthorRequest) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id: " + id + " does not exists"));

        if (!author.getEmail().equals(updateAuthorRequest.email())
                && authorRepository.existsByEmail(updateAuthorRequest.email())) {
            throw new DuplicateEmailException("Email already in use");
        }

        author.setName(updateAuthorRequest.name() != null ? updateAuthorRequest.name() : author.getName());
        author.setEmail(updateAuthorRequest.email() != null ? updateAuthorRequest.email() : author.getEmail());
        author.setBio(updateAuthorRequest.bio() != null ? updateAuthorRequest.bio() : author.getBio());
        author.setBirthDate(updateAuthorRequest.birthDate() != null ? updateAuthorRequest.birthDate() : author.getBirthDate());
        return this.convertToDTO(authorRepository.save(author));
    }

    public void deleteAuthor(Long id) {
        boolean exists = authorRepository.existsById(id);
        if(! exists) {
            throw new ResourceNotFoundException("Author with id: " + id + " does not exists");
        }
        authorRepository.deleteById(id);
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
