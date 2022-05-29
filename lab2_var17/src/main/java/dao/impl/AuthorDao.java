package dao.impl;

import dao.Dao;
import db.DBConnection;
import models.Author;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class AuthorDao implements Dao<Author> {

    private final String GET = "select * from authors where id = ?";
    private final String GET_ALL = "select * from authors order by id;";
    private final String GET_ALL_BY_BOOK_ID = "select * from authors where book_id = ? order by id;";
    private final String SAVE = "insert into authors (name, book_id) values (?, ?)";
    private final String UPDATE = "update authors set name = ?, book_id = ? where id = ?";
    private final String DELETE = "delete from authors where id = ?";
    private final String DELETE_ALL_BY_BOOK_ID = "delete from authors where book_id = ?";


    @Override
    public Optional<Author> get(long id) throws SQLException, ClassNotFoundException {
        Author author;
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                DBConnection.DATABASE_URL,
                DBConnection.DATABASE_USERNAME,
                DBConnection.DATABASE_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(GET)) {
            statement.setLong(1, id);
            ResultSet res = statement.executeQuery();
            res.next();
            author = new Author()
                    .setId(res.getLong("id"))
                    .setName(res.getString("name"))
                    .setBookId(res.getLong("book_id"));
            statement.close();
            connection.close();
        }
        return Optional.ofNullable(author);
    }

    @Override
    public List<Author> getAll() throws SQLException, ClassNotFoundException {
        List<Author> authors = new LinkedList<>();
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                DBConnection.DATABASE_URL,
                DBConnection.DATABASE_USERNAME,
                DBConnection.DATABASE_PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet res = statement.executeQuery(GET_ALL);
            while (res.next()) {
                authors.add(new Author()
                        .setId(res.getLong("id"))
                        .setName(res.getString("name"))
                        .setBookId(res.getLong("book_id")));
            }
            statement.close();
            connection.close();
        }
        return authors;
    }

    @Override
    public Author save(Author author) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                DBConnection.DATABASE_URL,
                DBConnection.DATABASE_USERNAME,
                DBConnection.DATABASE_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, author.getName());
            statement.setLong(2, author.getBookId());
            int id = statement.executeUpdate();

            if (id == 0) {
                throw new SQLException("Creating group failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    author.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating group failed, no ID obtained.");
                }
            }
            statement.close();
            connection.close();
        }
        return author;
    }

    @Override
    public Author update(Author author) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                DBConnection.DATABASE_URL,
                DBConnection.DATABASE_USERNAME,
                DBConnection.DATABASE_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, author.getName());
            statement.setLong(2, author.getBookId());
            statement.setLong(3, author.getId());
            statement.executeUpdate();
            statement.close();
            connection.close();
        }
        return author;
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
            statement.executeUpdate();
            statement.close();
            connection.close();
        }
    }

    public List<Author> getAllByBookId(Long bookId) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        List<Author> authors = new LinkedList<>();
        try (Connection connection = DriverManager.getConnection(
                DBConnection.DATABASE_URL,
                DBConnection.DATABASE_USERNAME,
                DBConnection.DATABASE_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(GET_ALL_BY_BOOK_ID)) {
            statement.setLong(1, bookId);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                authors.add(new Author()
                        .setId(res.getLong("id"))
                        .setName(res.getString("name"))
                        .setBookId(res.getLong("book_id")));
            }
            statement.close();
            connection.close();
        }
        return authors;
    }

    public void deleteByBookId(Long id) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager.getConnection(
                DBConnection.DATABASE_URL,
                DBConnection.DATABASE_USERNAME,
                DBConnection.DATABASE_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(DELETE_ALL_BY_BOOK_ID)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            statement.close();
            connection.close();
        }
    }
}
