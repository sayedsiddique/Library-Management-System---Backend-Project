package com.example.demo.specification.book;

import com.example.demo.model.author.Author;
import com.example.demo.model.book.Book;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> title == null ? null : criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Book> hasIsbn(String isbn) {
        return (root, query, criteriaBuilder) -> isbn == null ? null : criteriaBuilder.equal(root.get("isbn"), isbn);
    }

    public static Specification<Book> hasAuthor(String authorName) {
        return (root, query, criteriaBuilder) -> {
            if (authorName == null) return null;
            Join<Book, Author> authors = root.join("authors");
            return criteriaBuilder.like(authors.get("name"), "%" + authorName + "%");
        };
    }

    public static Specification<Book> search(String title, String isbn, String author) {
        Specification<Book> specification = Specification.where(hasTitle(title))
                .and(hasIsbn(isbn))
                .and(hasAuthor(author));
        return specification;
    }
}
