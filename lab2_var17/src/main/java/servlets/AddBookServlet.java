package servlets;

import dto.BookDto;
import exception.WrongDataException;
import service.BookService;
import service.impl.BookServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/books_add")
public class AddBookServlet extends HttpServlet {
    private final List<Throwable> exceptions = new ArrayList<>();

    private final BookService bookService;

    public AddBookServlet() {
        bookService = new BookServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!exceptions.isEmpty()){
            BookServlet.printExceptionPage(resp, exceptions);
            return;
        }
        PrintWriter pw = resp.getWriter();

        pw.println("<html><head><meta charset=\"utf-8\"><title>Lab2</title></head><body align=\"center\" style=\"background-color: #eadecf\">");
        pw.println("<form action=\"books\" method=\"get\">");
        pw.println("<input style=\"height:30px; width: 250px; font-size:11pt;\" type=\"submit\" value=\"Get all books\">");
        pw.println("</form>");

        pw.println("<form style=\"text-align: center;\" action=\"books_add\" method=\"post\">");
        pw.println("<input style=\"height:30px; width: 250px; font-size:11pt;\" type=\"text\" id=\"bookName\" name=\"bookName\" placeholder=\"name\">");
        pw.println("<input style=\"height:30px; width: 250px; font-size:11pt;\" type=\"text\" id=\"yearOfPublication\" name=\"yearOfPublication\" placeholder=\"year of publication\" pattern=\"[0-9]+\">");
        pw.println("<input style=\"height:30px; width: 250px; font-size:11pt;\" type=\"text\" id=\"authors\" name=\"authors\" placeholder=\"authors separated by ', '\">");
        pw.println("<input style=\"height:30px; width: 100px; font-size:11pt;\" type=\"submit\" value=\"save\">");
        pw.println("</form>");

        pw.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!exceptions.isEmpty()){
            BookServlet.printExceptionPage(resp, exceptions);
            return;
        }
        try {
            BookDto bookDto = new BookDto()
                    .setName(req.getParameter("bookName"))
                    .setYearOfPublication(Integer.valueOf(req.getParameter("yearOfPublication")))
                    .setAuthors(Arrays.asList(req.getParameter("authors").split(", ")));

            bookService.save(bookDto);
        } catch (SQLException | WrongDataException | ClassNotFoundException e) {
            e.printStackTrace();
            exceptions.add(e);
            BookServlet.printExceptionPage(resp, exceptions);
        } finally {
            doGet(req, resp);
        }
    }
}
