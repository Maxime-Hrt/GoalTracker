package com.example.goaltracker;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record Book(Integer id, String name, Integer pageCount, Integer authorId) {
    public static List<Book> books = Arrays.asList(
        new Book(1, "Java", 200, 1),
        new Book(2, "Python", 300, 2),
        new Book(3, "C++", 400, 1),
        new Book(4, "C", 500, 1),
        new Book(5, "JavaScript", 600, 2)
    );

    public static Optional<Book> getBookById(Integer id) {
        return books.stream()
                .filter(b -> b.id.equals(id))
                .findFirst();
    }
}
