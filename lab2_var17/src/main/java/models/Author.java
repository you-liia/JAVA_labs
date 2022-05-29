package models;

public class Author {
    private Long id;
    private String name;
    private Long bookId;

    public Long getId() {
        return id;
    }

    public Author setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Author setName(String name) {
        this.name = name;
        return this;
    }

    public Long getBookId() {
        return bookId;
    }

    public Author setBookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }
}
