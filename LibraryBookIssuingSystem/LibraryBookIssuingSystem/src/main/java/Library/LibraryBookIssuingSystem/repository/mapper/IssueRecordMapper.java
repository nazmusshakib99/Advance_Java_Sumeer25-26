package Library.LibraryBookIssuingSystem.repository.mapper;

import com.example.library.domain.LibraryDomain;

import javax.swing.tree.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IssueRecordMapper implements RowMapper<LibraryDomain.IssueRecord> {

    @Override
    public LibraryDomain.IssueRecord mapRow(
            ResultSet rs,
            int rowNum
    ) throws SQLException {

        LibraryDomain.IssueRecord issue =
                new LibraryDomain.IssueRecord();

        issue.setId(rs.getLong("id"));
        issue.setBookId(rs.getLong("book_id"));
        issue.setMemberName(rs.getString("member_name"));
        issue.setMemberEmail(rs.getString("member_email"));

        if (rs.getDate("issue_date") != null) {
            issue.setIssueDate(
                    rs.getDate("issue_date").toLocalDate()
            );
        }

        if (rs.getDate("due_date") != null) {
            issue.setDueDate(
                    rs.getDate("due_date").toLocalDate()
            );
        }

        if (rs.getDate("return_date") != null) {
            issue.setReturnDate(
                    rs.getDate("return_date").toLocalDate()
            );
        }

        issue.setStatus(rs.getString("status"));

        return issue;
    }
}
