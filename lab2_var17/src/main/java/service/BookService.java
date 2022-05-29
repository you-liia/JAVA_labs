package service;

import dto.BookDto;
import exception.WrongDataException;
import models.Book;

import java.sql.SQLException;
import java.util.List;

public interface BookService {
    Book save(BookDto bookDto) throws ClassNotFoundException, SQLException, WrongDataException;

    List<BookDto> getAll() throws ClassNotFoundException, SQLException, WrongDataException;

    List<BookDto> getBooksWithFewAuthors() throws ClassNotFoundException, SQLException, WrongDataException;
}
