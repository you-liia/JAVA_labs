package dto;

import models.Author;
import models.Book;

import java.util.List;
import java.util.stream.Collectors;

public class BookDto {
    private Long id;
    private String name;
    private Integer yearOfPublication;
    private List<String> authors;

    public Long getId() {
        return id;
    }

    public BookDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public BookDto setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getYearOfPublication() {
        return yearOfPublication;
    }

    public BookDto setYearOfPublication(Integer yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
        return this;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public BookDto setAuthors(List<String> authors) {
        this.authors = authors;
        return this;
    }

    public static BookDto toDto(Book book) {
        return new BookDto()
                .setId(book.getId())
                .setName(book.getName())
                .setYearOfPublication(book.getYearOfPublication())
                .setAuthors(book.getAuthors().stream().map(Author::getName).collect(Collectors.toList()));

    }

    public static Book fromDto(BookDto bookDto) {
        return new Book()
                .setId(bookDto.getId())
                .setName(bookDto.getName())
                .setYearOfPublication(bookDto.getYearOfPublication())
                .setAuthors(bookDto.getAuthors().stream().map(name -> new Author().setName(name)).collect(Collectors.toList()));
    }
}
