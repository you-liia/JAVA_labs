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
import java.util.List;

@WebServlet("/books")
public class BookServlet extends HttpServlet {
    private final List<Throwable> exceptions = new ArrayList<>();

    private final BookService bookService;

    public BookServlet() {
        this.bookService = new BookServiceImpl();
    }

    public static void printExceptionPage(HttpServletResponse resp, List<Throwable> exceptionList) throws IOException {
        PrintWriter pw = resp.getWriter();

        pw.println("<html><head><meta charset=\"utf-8\"><title>Lab2</title></head><body>");
        pw.println("<h2>Exception page</h2>");
        for (Throwable t : exceptionList) {
            pw.print(String.format("<h3>%s", t.getClass().getName()));
            if (!t.getMessage().isEmpty()) {
                pw.print(String.format(": %s", t.getMessage()));
            }
            pw.println("</h3>");
        }
        pw.println("</body></html>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!exceptions.isEmpty()){
            printExceptionPage(resp, exceptions);
            return;
        }
        try {
            PrintWriter pw = resp.getWriter();
            List<BookDto> books = bookService.getAll();

            pw.println("<html><head><meta charset=\"utf-8\"><title>Lab2</title><style> table, th, td { border: 1px solid white; border-collapse: collapse;} th, td { background-color: #96D4D4; height: 30px;}</style></head><body align=\"center\" style=\"background-color: #eadecf\">");
            pw.println("<form action=\"books\" method=\"post\">");
            pw.println("<input style=\"height:30px; width: 250px; font-size:11pt;\" type=\"submit\" value=\"Get books with few authors\">");
            pw.println("</form>");
            if (books.isEmpty()) {
                pw.println("<p>Books were not found</p>");
            } else {
                pw.println("<table style=\"width:50%; text-align:center;\" align=\"center\">");
                pw.println("<tr><th style=\"width:33%\">Name</th><th style=\"width:33%\">Authors</th><th>Year of publication</th></tr>");
                pw.println("<br>");
                for (BookDto b : books) {
                    pw.println(String.format("<tr><td>%s</td><td>%s</td><td>%d</td></tr>", b.getName(), String.join(", ", b.getAuthors()), b.getYearOfPublication()));
                }
                pw.println("</table>");
            }

            pw.println("<form action=\"books_add\" method=\"get\">");
            pw.println("<input style=\"height:30px; background-color: #d9d8d8; width: 140px; margin-top: 25px; font-size:11pt;\" type=\"submit\" value=\"Add new book\">");
            pw.println("</form>");

            pw.println("</body></html>");
        } catch (ClassNotFoundException | SQLException | WrongDataException e) {
            exceptions.add(e);
            printExceptionPage(resp, exceptions);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!exceptions.isEmpty()){
            printExceptionPage(resp, exceptions);
            return;
        }
        try {
            PrintWriter pw = resp.getWriter();
            List<BookDto> books = bookService.getBooksWithFewAuthors();

            pw.println("<html><head><meta charset=\"utf-8\"><title>Lab2</title><style> table, th, td { border: 1px solid white; border-collapse: collapse;} th, td { background-color: #e69898; height: 30px;}</style></head><body align=\"center\" style=\"background-color: #eadecf\">");
            pw.println("<form action=\"books\" method=\"get\">");
            pw.println("<input style=\"height:30px; width: 250px; font-size:11pt;\" type=\"submit\" value=\"Get all books\">");
            pw.println("</form>");
            if (books.isEmpty()) {
                pw.println("<p>Books with few authors were not found</p>");
            } else {
                pw.println("<table style=\"text-align:center; width:50%;\" align=\"center\">");
                pw.println("<tr><th style=\"width:33%\">Name</th><th style=\"width:33%\">Authors</th><th>Year of publication</th></tr>");
                pw.println("<br>");
                for (BookDto b : books) {
                    pw.println(String.format("<tr><td>%s</td><td>%s</td><td>%d</td></tr>", b.getName(), String.join(", ", b.getAuthors()), b.getYearOfPublication()));
                }
                pw.println("</table>");
            }
            pw.println("</body></html>");
        } catch (ClassNotFoundException | SQLException | WrongDataException e) {
            exceptions.add(e);
            printExceptionPage(resp, exceptions);
        }

    }
}
