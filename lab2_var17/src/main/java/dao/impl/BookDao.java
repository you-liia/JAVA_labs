package dao.impl;

import dao.Dao;
import db.DBConnection;
import exception.WrongDataException;
import models.Author;
import models.Book;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class BookDao implements Dao<Book> {
    private final String GET = "select * from public.books where id = ?";
    private final String GET_ALL = "select * from public.books";
    private final String CREATE = "insert into public.books (name, year_of_publication) values (?, ?)";
    private final String UPDATE = "update public.books set name  = ?, year_of_publication = ? where id = ?";
    private final String DELETE = "delete from books where id = ?";
    private final String GET_ALL_WITH_FEW_AUTHORS = "select * from public.books b where (select count(a.id) from public.authors a where a.book_id = b.id) > 1";
    private final String GET_LAST_ID = "select id from public.books order by id desc limit 1";

    private final AuthorDao authorDao;

    public BookDao() {
        authorDao = new AuthorDao();
    }

    @Override
    public Optional<Book> get(long id) throws SQLException, WrongDataException, ClassNotFoundException {
        Book book;
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                DBConnection.DATABASE_URL,
                DBConnection.DATABASE_USERNAME,
                DBConnection.DATABASE_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(GET)) {
            statement.setLong(1, id);
            ResultSet res = statement.executeQuery();
            res.next();
            book = new Book()
                    .setId(res.getLong("id"))
                    .setName(res.getString("name"))
                    .setYearOfPublication(res.getInt("year_of_publication"))
                    .setAuthors(authorDao.getAllByBookId(res.getLong("id")));
            statement.close();
            connection.close();
        }
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> getAll() throws SQLException, WrongDataException, ClassNotFoundException {
        List<Book> books = new LinkedList<>();
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                DBConnection.DATABASE_URL,
                DBConnection.DATABASE_USERNAME,
                DBConnection.DATABASE_PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet res = statement.executeQuery(GET_ALL);
            while (res.next()) {
                books.add(new Book()
                        .setId(res.getLong("id"))
                        .setName(res.getString("name"))
                        .setYearOfPublication(res.getInt("year_of_publication"))
                        .setAuthors(authorDao.getAllByBookId(res.getLong("id"))));
            }
            statement.close();
            connection.close();
        }
        return books;
    }

    @Override
    public Book save(Book book) throws SQLException, WrongDataException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                DBConnection.DATABASE_URL,
                DBConnection.DATABASE_USERNAME,
                DBConnection.DATABASE_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
             Statement statement1 = connection.createStatement()) {
            statement.setString(1, book.getName());
            statement.setInt(2, book.getYearOfPublication());
            int created_records = statement.executeUpdate();

            if (created_records == 0) {
                throw new SQLException("Creating rating failed, no rows affected.");
            }

            ResultSet res = statement1.executeQuery(GET_LAST_ID);
            res.next();
            int id = res.getInt("id");

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getLong(1));
                    book.getAuthors().forEach(author -> {
                        author.setBookId((long) id);
                    });
                    try {
                        for (Author author: book.getAuthors()) {
                            authorDao.save(author);
                        }
                    } catch (SQLException | ClassNotFoundException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    throw new SQLException("Creating rating failed, no ID obtained.");
                }
            }
            statement.close();
            connection.close();
        }

        return book;
    }

    @Override
    public Book update(Book book) throws SQLException, WrongDataException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                DBConnection.DATABASE_URL,
                DBConnection.DATABASE_USERNAME,
                DBConnection.DATABASE_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, book.getName());
            statement.setInt(2, book.getYearOfPublication());
            statement.executeUpdate();
            statement.close();
            connection.close();
        }
        return book;
    }

    @Override
    public void deleteById(Long id) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                DBConnection.DATABASE_URL,
                DBConnection.DATABASE_USERNAME,
                DBConnection.DATABASE_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);
            authorDao.deleteByBookId(id);
            statement.executeUpdate();
            statement.close();
            connection.close();
        }
    }

    public List<Book> getBooksWithFewAuthors() throws SQLException, WrongDataException, ClassNotFoundException {
        List<Book> books = new LinkedList<>();
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                DBConnection.DATABASE_URL,
                DBConnection.DATABASE_USERNAME,
                DBConnection.DATABASE_PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet res = statement.executeQuery(GET_ALL_WITH_FEW_AUTHORS);
            while (res.next()) {
                books.add(new Book()
                        .setId(res.getLong("id"))
                        .setName(res.getString("name"))
                        .setYearOfPublication(res.getInt("year_of_publication"))
                        .setAuthors(authorDao.getAllByBookId(res.getLong("id"))));
            }
            statement.close();
            connection.close();
        }
        return books;
    }
}
