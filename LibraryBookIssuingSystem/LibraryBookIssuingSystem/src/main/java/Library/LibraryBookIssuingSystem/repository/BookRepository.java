package Library.LibraryBookIssuingSystem.repository.mapper;

import Library.LibraryBookIssuingSystem.domain.LibraryDomain;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<LibraryDomain> {

    @Override
    public LibraryDomain.Book mapRow(
            ResultSet rs,
            int rowNum
    ) throws SQLException {

        LibraryDomain.Book book = new LibraryDomain.Book();

        book.setId(rs.getLong("id"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));

        // Convert database genre string to Genre enum
        String genre = rs.getString("genre");

        if (genre != null) {
            book.setGenre(
                    LibraryDomain.Genre.valueOf(
                            genre.toUpperCase()
                    )
            );
        }

        book.setTotalCopies(
                rs.getInt("total_copies")
        );

        book.setAvailableCopies(
                rs.getInt("available_copies")
        );

        book.setPublishedYear(
                rs.getInt("published_year")
        );

        book.setPrice(
                rs.getBigDecimal("price")
        );

        return book;
    }
}