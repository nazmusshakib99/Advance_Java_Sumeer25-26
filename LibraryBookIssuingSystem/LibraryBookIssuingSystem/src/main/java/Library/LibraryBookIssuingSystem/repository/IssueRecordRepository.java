package Library.LibraryBookIssuingSystem.repository;

import Library.LibraryBookIssuingSystem.repository.mapper.IssueRecordMapper;
import com.example.library.domain.LibraryDomain;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class IssueRecordRepository {

    private final JdbcTemplate jdbcTemplate;

    private final IssueRecordMapper issueRecordMapper =
            new IssueRecordMapper();

    public IssueRecordRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // =========================================================
    // SAVE
    // =========================================================

    public com.example.library.domain.LibraryDomain.IssueRecord save(
            com.example.library.domain.LibraryDomain.IssueRecord issue
    ) {

        String sql = """
                INSERT INTO issue_records
                (
                    book_id,
                    member_name,
                    member_email,
                    issue_date,
                    due_date,
                    return_date,
                    status
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            PreparedStatement ps =
                    connection.prepareStatement(
                            sql,
                            Statement.RETURN_GENERATED_KEYS
                    );

            ps.setLong(1, issue.getBookId());
            ps.setString(2, issue.getMemberName());
            ps.setString(3, issue.getMemberEmail());
            ps.setObject(4, issue.getIssueDate());
            ps.setObject(5, issue.getDueDate());
            ps.setObject(6, issue.getReturnDate());
            ps.setString(7, issue.getStatus());

            return ps;

        }, keyHolder);

        if (keyHolder.getKey() != null) {
            issue.setId(keyHolder.getKey().longValue());
        }

        return issue;
    }

    // =========================================================
    // FIND ALL
    // =========================================================

    public List<LibraryDomain.IssueRecord> findAll(
            String status,
            String email
    ) {

        StringBuilder sql = new StringBuilder(
                """
                SELECT id,
                       book_id,
                       member_name,
                       member_email,
                       issue_date,
                       due_date,
                       return_date,
                       status
                FROM issue_records
                WHERE 1=1
                """
        );

        List<Object> args = new ArrayList<>();

        if (status != null && !status.isBlank()) {

            if ("OVERDUE".equalsIgnoreCase(status)) {

                sql.append("""
                        AND status = 'ISSUED'
                        AND due_date < CURRENT_DATE
                        """);

            } else {

                sql.append(" AND status = ?");
                args.add(status.toUpperCase());
            }
        }

        if (email != null && !email.isBlank()) {

            sql.append(" AND LOWER(member_email) = LOWER(?)");
            args.add(email);
        }

        sql.append(" ORDER BY id DESC");

        return jdbcTemplate.query(
                sql.toString(),
                issueRecordMapper,
                args.toArray()
        );
    }

    // =========================================================
    // FIND BY ID
    // =========================================================

    public Optional<LibraryDomain.IssueRecord> findById(
            Long id
    ) {

        String sql = """
                SELECT id,
                       book_id,
                       member_name,
                       member_email,
                       issue_date,
                       due_date,
                       return_date,
                       status
                FROM issue_records
                WHERE id = ?
                """;

        List<LibraryDomain.IssueRecord> records =
                jdbcTemplate.query(
                        sql,
                        issueRecordMapper,
                        id
                );

        return records.stream().findFirst();
    }

    // =========================================================
    // FIND BY ID FOR UPDATE
    // =========================================================

    public Optional<LibraryDomain.IssueRecord> findByIdForUpdate(
            Long id
    ) {

        String sql = """
                SELECT id,
                       book_id,
                       member_name,
                       member_email,
                       issue_date,
                       due_date,
                       return_date,
                       status
                FROM issue_records
                WHERE id = ?
                FOR UPDATE
                """;

        List<LibraryDomain.IssueRecord> records =
                jdbcTemplate.query(
                        sql,
                        issueRecordMapper,
                        id
                );

        return records.stream().findFirst();
    }

    // =========================================================
    // UPDATE
    // =========================================================

    public int update(
            LibraryDomain.IssueRecord issue
    ) {

        String sql = """
                UPDATE issue_records
                SET member_name = ?,
                    member_email = ?,
                    due_date = ?,
                    status = ?,
                    return_date = ?
                WHERE id = ?
                """;

        return jdbcTemplate.update(
                sql,
                issue.getMemberName(),
                issue.getMemberEmail(),
                issue.getDueDate(),
                issue.getStatus(),
                issue.getReturnDate(),
                issue.getId()
        );
    }

    // =========================================================
    // DELETE
    // =========================================================

    public int deleteById(Long id) {

        String sql = """
                DELETE FROM issue_records
                WHERE id = ?
                """;

        return jdbcTemplate.update(sql, id);
    }

    // =========================================================
    // MEMBER ALREADY HAS THIS BOOK
    // =========================================================

    public boolean memberHasBook(
            Long bookId,
            String memberEmail
    ) {

        String sql = """
                SELECT COUNT(*)
                FROM issue_records
                WHERE book_id = ?
                  AND LOWER(member_email) = LOWER(?)
                  AND status = 'ISSUED'
                """;

        Integer count =
                jdbcTemplate.queryForObject(
                        sql,
                        Integer.class,
                        bookId,
                        memberEmail
                );

        return count != null && count > 0;
    }

    // =========================================================
    // COUNT MEMBER OUTSTANDING BOOKS
    // =========================================================

    public int countOutstandingBooks(
            String memberEmail
    ) {

        String sql = """
                SELECT COUNT(*)
                FROM issue_records
                WHERE LOWER(member_email) = LOWER(?)
                  AND status = 'ISSUED'
                """;

        Integer count =
                jdbcTemplate.queryForObject(
                        sql,
                        Integer.class,
                        memberEmail
                );

        return count == null ? 0 : count;
    }
}
