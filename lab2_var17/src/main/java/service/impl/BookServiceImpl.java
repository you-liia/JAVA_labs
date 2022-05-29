package service.impl;

import dao.impl.BookDao;
import dto.BookDto;
import exception.WrongDataException;
import models.Book;
import service.BookService;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    public BookServiceImpl() {
        bookDao = new BookDao();
    }
    @Override
    public Book save(BookDto bookDto) throws ClassNotFoundException, SQLException, WrongDataException {
        return bookDao.save(BookDto.fromDto(bookDto));
    }

    @Override
    public List<BookDto> getAll() throws ClassNotFoundException, SQLException, WrongDataException {
        return bookDao.getAll().stream().map(BookDto::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BookDto> getBooksWithFewAuthors() throws ClassNotFoundException, SQLException, WrongDataException {
        return bookDao.getBooksWithFewAuthors().stream().map(BookDto::toDto).collect(Collectors.toList());
    }
}
