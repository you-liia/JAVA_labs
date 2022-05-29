package dao;

import exception.WrongDataException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(long id) throws SQLException, WrongDataException, ClassNotFoundException;

    List<T> getAll() throws SQLException, WrongDataException, ClassNotFoundException;

    T save(T t) throws SQLException, WrongDataException, ClassNotFoundException;

    T update(T t) throws SQLException, WrongDataException, ClassNotFoundException;

    void deleteById(Long id) throws SQLException, ClassNotFoundException;
}
