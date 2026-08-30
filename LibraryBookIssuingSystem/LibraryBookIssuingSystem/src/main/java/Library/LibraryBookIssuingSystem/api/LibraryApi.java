package Library.LibraryBookIssuingSystem.api;

@RestController
@RequestMapping("/api")
public class LibraryApi {

    private final LibraryServices libraryServices;

    public LibraryApi(LibraryServices libraryServices) {
        this.libraryServices = libraryServices;
    }

    // =========================================================
    // 1. POST /api/books
    // =========================================================

    @PostMapping("/books")
    public ResponseEntity<LibraryDomain.Book> createBook(
            @Valid
            @RequestBody
            LibraryDomain.BookRequest request
    ) {

        LibraryDomain.Book book =
                libraryServices.createBook(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(book);
    }

    // =========================================================
    // 2. GET /api/books
    //
    // Example:
    // /api/books?genre=SCIENCE&available=true
    // =========================================================

    @GetMapping("/books")
    public ResponseEntity<List<LibraryDomain.Book>> getBooks(
            @RequestParam(required = false)
            LibraryDomain.Genre genre,

            @RequestParam(required = false)
            Boolean available
    ) {

        return ResponseEntity.ok(
                libraryServices.getBooks(
                        genre,
                        available
                )
        );
    }

    // =========================================================
    // 3. GET /api/books/{id}
    // =========================================================

    @GetMapping("/books/{id}")
    public ResponseEntity<LibraryDomain.Book> getBook(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                libraryServices.getBookById(id)
        );
    }

    // =========================================================
    // 4. PUT /api/books/{id}
    // =========================================================

    @PutMapping("/books/{id}")
    public ResponseEntity<LibraryDomain.Book> updateBook(
            @PathVariable Long id,

            @Valid
            @RequestBody
            LibraryDomain.BookRequest request
    ) {

        return ResponseEntity.ok(
                libraryServices.updateBook(
                        id,
                        request
                )
        );
    }

    // =========================================================
    // 5. DELETE /api/books/{id}
    // =========================================================

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id
    ) {

        libraryServices.deleteBook(id);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
    // 6. POST /api/issue
    // =========================================================

    @PostMapping("/issue")
    public ResponseEntity<LibraryDomain.IssueRecord> issueBook(
            @Valid
            @RequestBody
            LibraryDomain.IssueRequest request
    ) {

        LibraryDomain.IssueRecord issue =
                libraryServices.issueBook(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(issue);
    }

    // =========================================================
    // 7. GET /api/issues
    //
    // Examples:
    // /api/issues?status=OVERDUE
    // /api/issues?email=x@y.com
    // /api/issues?status=OVERDUE&email=x@y.com
    // =========================================================

    @GetMapping("/issues")
    public ResponseEntity<List<LibraryDomain.IssueRecord>> getIssues(
            @RequestParam(required = false)
            String status,

            @RequestParam(required = false)
            String email
    ) {

        return ResponseEntity.ok(
                libraryServices.getIssues(
                        status,
                        email
                )
        );
    }

    // =========================================================
    // 8. PATCH /api/issues/{id}/return
    // =========================================================

    @PatchMapping("/issues/{id}/return")
    public ResponseEntity<LibraryDomain.IssueRecord> returnBook(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                libraryServices.returnBook(id)
        );
    }

    // =========================================================
    // 9. PUT /api/issues/{id}
    // =========================================================

    @PutMapping("/issues/{id}")
    public ResponseEntity<LibraryDomain.IssueRecord> updateIssue(
            @PathVariable Long id,

            @Valid
            @RequestBody
            LibraryDomain.IssueUpdateRequest request
    ) {

        return ResponseEntity.ok(
                libraryServices.updateIssue(
                        id,
                        request
                )
        );
    }

    // =========================================================
    // 10. DELETE /api/issues/{id}
    // =========================================================

    @DeleteMapping("/issues/{id}")
    public ResponseEntity<Void> deleteIssue(
            @PathVariable Long id
    ) {

        libraryServices.deleteIssue(id);

        return ResponseEntity.noContent().build();
    }
}
