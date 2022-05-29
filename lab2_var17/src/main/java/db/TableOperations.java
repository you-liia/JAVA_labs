package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableOperations {
    public final String CREATE_BOOKS = "create table books (id serial not null, name character varying(75) not null, year_of_publication integer, primary key (id))";
    public final String CREATE_AUTHORS = "create table  authors (id serial not null, name character varying(30) not null, book_id integer not null, " +
            "primary key (id), foreign key (book_id) references public.books(id))";
    public final String DROP_BOOKS = "drop table public.books;";
    public final String DROP_AUTHORS = "drop table public.authors;";

    public void createBooksTable() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_BOOKS);
            System.out.println("books was created");
        }
    }

    public void dropBooksTable() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_BOOKS);
        }
    }

    public void createAuthorsTable() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_AUTHORS);
            System.out.println("authors was created");
        }
    }

    public void dropAuthorsTable() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_AUTHORS);
        }
    }

    public void createAllTable() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_BOOKS);
            statement.executeUpdate(CREATE_AUTHORS);
        }
    }

    public void dropAllTables() throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_AUTHORS);
            statement.executeUpdate(DROP_BOOKS);
        }
    }

    public static void main(String[] args) {
        try {
            new TableOperations().createAllTable();
//            new TableOperations().dropAllTables();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
}
