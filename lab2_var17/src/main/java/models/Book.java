package models;

import java.util.List;

public class Book {
    private Long id;
    private String name;
    private List<Author> authors;
    private Integer yearOfPublication;

    public Long getId() {
        return id;
    }

    public Book setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public Book setAuthors(List<Author> authors) {
        this.authors = authors;
        return this;
    }

    public Integer getYearOfPublication() {
        return yearOfPublication;
    }

    public Book setYearOfPublication(Integer yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
        return this;
    }
}
