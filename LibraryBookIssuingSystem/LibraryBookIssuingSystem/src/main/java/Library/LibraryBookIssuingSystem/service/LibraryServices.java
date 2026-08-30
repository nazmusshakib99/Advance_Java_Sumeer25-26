package Library.LibraryBookIssuingSystem.service;

@Service
public class LibraryServices {

    private final BookRepository bookRepository;
    private final IssueRecordRepository issueRecordRepository;

    public LibraryServices(
            BookRepository bookRepository,
            IssueRecordRepository issueRecordRepository
    ) {
        this.bookRepository = bookRepository;
        this.issueRecordRepository = issueRecordRepository;
    }

    // =========================================================
    // CREATE BOOK
    // =========================================================

    public LibraryDomain.Book createBook(
            LibraryDomain.BookRequest request
    ) {

        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "A book with this ISBN already exists"
            );
        }

        LibraryDomain.Book book =
                new LibraryDomain.Book();

        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setTotalCopies(request.getTotalCopies());

        // Server calculates this.
        book.setAvailableCopies(
                request.getTotalCopies()
        );

        book.setPublishedYear(
                request.getPublishedYear()
        );

        book.setPrice(request.getPrice());

        return bookRepository.save(book);
    }

    // =========================================================
    // GET BOOKS
    // =========================================================

    public List<LibraryDomain.Book> getBooks(
            LibraryDomain.Genre genre,
            Boolean available
    ) {

        return bookRepository.findAll(
                genre,
                available
        );
    }

    // =========================================================
    // GET BOOK BY ID
    // =========================================================

    public LibraryDomain.Book getBookById(Long id) {

        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Book not found with id: " + id
                        )
                );
    }

    // =========================================================
    // UPDATE BOOK
    // =========================================================

    public LibraryDomain.Book updateBook(
            Long id,
            LibraryDomain.BookRequest request
    ) {

        LibraryDomain.Book existing =
                getBookById(id);

        // Check duplicate ISBN only when ISBN belongs
        // to another book.
        bookRepository.findByIsbn(request.getIsbn())
                .ifPresent(book -> {

                    if (!book.getId().equals(id)) {

                        throw new ApiException(
                                HttpStatus.CONFLICT,
                                "Another book already uses this ISBN"
                        );
                    }
                });

        int issuedCopies =
                bookRepository.countIssuedCopies(id);

        if (request.getTotalCopies() < issuedCopies) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Total copies cannot be lower than currently issued copies: "
                            + issuedCopies
            );
        }

        existing.setIsbn(request.getIsbn());
        existing.setTitle(request.getTitle());
        existing.setAuthor(request.getAuthor());
        existing.setGenre(request.getGenre());
        existing.setTotalCopies(
                request.getTotalCopies()
        );

        /*
         * availableCopies is never accepted from client.
         *
         * Number of currently issued copies remains unchanged.
         *
         * available = new total - issued
         */
        existing.setAvailableCopies(
                request.getTotalCopies() - issuedCopies
        );

        existing.setPublishedYear(
                request.getPublishedYear()
        );

        existing.setPrice(
                request.getPrice()
        );

        bookRepository.update(existing);

        return existing;
    }

    // =========================================================
    // DELETE BOOK
    // =========================================================

    public void deleteBook(Long id) {

        getBookById(id);

        int issuedCopies =
                bookRepository.countIssuedCopies(id);

        if (issuedCopies > 0) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Book cannot be deleted because "
                            + issuedCopies
                            + " copy/copies are currently issued"
            );
        }

        bookRepository.deleteById(id);
    }

    // =========================================================
    // ISSUE BOOK
    // =========================================================

    @Transactional
    public LibraryDomain.IssueRecord issueBook(
            LibraryDomain.IssueRequest request
    ) {

        /*
         * Lock the book row so two users cannot issue
         * the same last available copy simultaneously.
         */
        LibraryDomain.Book book =
                bookRepository.findByIdForUpdate(request.getBookId())
                        .orElseThrow(() ->
                                new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "Book not found with id: "
                                                + request.getBookId()
                                )
                        );

        // -----------------------------------------------------
        // CHECK AVAILABLE COPIES
        // -----------------------------------------------------

        if (book.getAvailableCopies() <= 0) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "No available copies of this book"
            );
        }

        // -----------------------------------------------------
        // CHECK SAME MEMBER + SAME BOOK
        // -----------------------------------------------------

        if (issueRecordRepository.memberHasBook(
                request.getBookId(),
                request.getMemberEmail()
        )) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "This member already has an unreturned copy of this book"
            );
        }

        // -----------------------------------------------------
        // CHECK MAXIMUM 3 OUTSTANDING BOOKS
        // -----------------------------------------------------

        int outstanding =
                issueRecordRepository.countOutstandingBooks(
                        request.getMemberEmail()
                );

        if (outstanding >= 3) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Member cannot have more than 3 outstanding books"
            );
        }

        // -----------------------------------------------------
        // CREATE ISSUE RECORD
        // -----------------------------------------------------

        LibraryDomain.IssueRecord issue =
                new LibraryDomain.IssueRecord();

        issue.setBookId(request.getBookId());
        issue.setMemberName(request.getMemberName());
        issue.setMemberEmail(request.getMemberEmail());
        issue.setIssueDate(request.getIssueDate());
        issue.setDueDate(request.getDueDate());

        /*
         * The return date supplied by client is not used for
         * a new issue. A newly issued book is always unreturned.
         */
        issue.setReturnDate(null);
        issue.setStatus("ISSUED");

        issueRecordRepository.save(issue);

        // -----------------------------------------------------
        // DECREMENT AVAILABLE COPIES
        // -----------------------------------------------------

        bookRepository.decreaseAvailableCopies(
                book.getId()
        );

        return issue;
    }

    // =========================================================
    // GET ISSUE RECORDS
    // =========================================================

    public List<LibraryDomain.IssueRecord> getIssues(
            String status,
            String email
    ) {

        return issueRecordRepository.findAll(
                status,
                email
        );
    }

    // =========================================================
    // RETURN BOOK
    // =========================================================

    @Transactional
    public LibraryDomain.IssueRecord returnBook(
            Long id
    ) {

        LibraryDomain.IssueRecord issue =
                issueRecordRepository.findByIdForUpdate(id)
                        .orElseThrow(() ->
                                new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "Issue record not found with id: "
                                                + id
                                )
                        );

        // -----------------------------------------------------
        // ALREADY RETURNED
        // -----------------------------------------------------

        if ("RETURNED".equalsIgnoreCase(
                issue.getStatus()
        )) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "This issue record has already been returned"
            );
        }

        // -----------------------------------------------------
        // UPDATE ISSUE RECORD
        // -----------------------------------------------------

        issue.setReturnDate(
                LocalDate.now()
        );

        issue.setStatus("RETURNED");

        issueRecordRepository.markReturned(
                id,
                issue.getReturnDate()
        );

        // -----------------------------------------------------
        // INCREMENT AVAILABLE COPIES
        // -----------------------------------------------------

        bookRepository.increaseAvailableCopies(
                issue.getBookId()
        );

        return issueRecordRepository
                .findById(id)
                .orElse(issue);
    }

    // =========================================================
    // UPDATE ISSUE RECORD
    // =========================================================

    public LibraryDomain.IssueRecord updateIssue(
            Long id,
            LibraryDomain.IssueUpdateRequest request
    ) {

        LibraryDomain.IssueRecord existing =
                issueRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "Issue record not found with id: "
                                                + id
                                )
                        );

        if ("RETURNED".equalsIgnoreCase(
                existing.getStatus()
        )) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "A returned issue record cannot be updated"
            );
        }

        // -----------------------------------------------------
        // MEMBER NAME
        // -----------------------------------------------------

        if (request.getMemberName() != null
                && !request.getMemberName().isBlank()) {

            existing.setMemberName(
                    request.getMemberName()
            );
        }

        // -----------------------------------------------------
        // MEMBER EMAIL
        // -----------------------------------------------------

        if (request.getMemberEmail() != null
                && !request.getMemberEmail().isBlank()) {

            existing.setMemberEmail(
                    request.getMemberEmail()
            );
        }

        // -----------------------------------------------------
        // DUE DATE
        // -----------------------------------------------------

        if (request.getDueDate() != null) {

            if (request.getDueDate()
                    .isBefore(existing.getDueDate())) {

                throw new ApiException(
                        HttpStatus.CONFLICT,
                        "Due date may only be extended, never shortened"
                );
            }

            existing.setDueDate(
                    request.getDueDate()
            );
        }

        issueRecordRepository.update(existing);

        return existing;
    }

    // =========================================================
    // DELETE ISSUE RECORD
    // =========================================================

    public void deleteIssue(Long id) {

        LibraryDomain.IssueRecord issue =
                issueRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException(
                                        HttpStatus.NOT_FOUND,
                                        "Issue record not found with id: "
                                                + id
                                )
                        );

        if (!"RETURNED".equalsIgnoreCase(
                issue.getStatus()
        )) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "Issue record can only be deleted after the book is returned"
            );
        }

        issueRecordRepository.deleteById(id);
    }
}

There are three methods used above that we still need to add to BookRepository:

findByIdForUpdate
        decreaseAvailableCopies
increaseAvailableCopies
Add these methods to BookRepository:

// =========================================================
// FIND BOOK FOR UPDATE
// =========================================================

public Optional<LibraryDomain.Book> findByIdForUpdate(Long id) {

    String sql = """
            SELECT id,
                   isbn,
                   title,
                   author,
                   genre,
                   total_copies,
                   available_copies,
                   published_year,
                   price
            FROM books
            WHERE id = ?
            FOR UPDATE
            """;

    List<LibraryDomain.Book> books =
            jdbcTemplate.query(
                    sql,
                    bookMapper,
                    id
            );

    return books.stream().findFirst();
}

// =========================================================
// DECREASE AVAILABLE COPIES
// =========================================================

public int decreaseAvailableCopies(Long bookId) {

    String sql = """
            UPDATE books
            SET available_copies = available_copies - 1
            WHERE id = ?
              AND available_copies > 0
            """;

    return jdbcTemplate.update(sql, bookId);
}

// =========================================================
// INCREASE AVAILABLE COPIES
// =========================================================

public int increaseAvailableCopies(Long bookId) {

    String sql = """
            UPDATE books
            SET available_copies = available_copies + 1
            WHERE id = ?
              AND available_copies < total_copies
            """;

    return jdbcTemplate.update(sql, bookId);
}
