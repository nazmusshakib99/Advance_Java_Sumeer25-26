package Library.LibraryBookIssuingSystem.domain;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LibraryDomain {

    // =========================================================
    // ENUM
    // =========================================================

    public enum Genre {
        FICTION,
        SCIENCE,
        HISTORY,
        TECHNOLOGY,
        BIOGRAPHY
    }

    // =========================================================
    // BOOK ENTITY
    // =========================================================

    public static class Book {

        private Long id;
        private String isbn;
        private String title;
        private String author;
        private Genre genre;
        private Integer totalCopies;
        private Integer availableCopies;
        private Integer publishedYear;
        private BigDecimal price;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Genre getGenre() {
            return genre;
        }

        public void setGenre(Genre genre) {
            this.genre = genre;
        }

        public Integer getTotalCopies() {
            return totalCopies;
        }

        public void setTotalCopies(Integer totalCopies) {
            this.totalCopies = totalCopies;
        }

        public Integer getAvailableCopies() {
            return availableCopies;
        }

        public void setAvailableCopies(Integer availableCopies) {
            this.availableCopies = availableCopies;
        }

        public Integer getPublishedYear() {
            return publishedYear;
        }

        public void setPublishedYear(Integer publishedYear) {
            this.publishedYear = publishedYear;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }

    // =========================================================
    // ISSUE RECORD ENTITY
    // =========================================================

    public static class IssueRecord {

        private Long id;
        private Long bookId;
        private String memberName;
        private String memberEmail;
        private LocalDate issueDate;
        private LocalDate dueDate;
        private LocalDate returnDate;
        private String status;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getBookId() {
            return bookId;
        }

        public void setBookId(Long bookId) {
            this.bookId = bookId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberEmail() {
            return memberEmail;
        }

        public void setMemberEmail(String memberEmail) {
            this.memberEmail = memberEmail;
        }

        public LocalDate getIssueDate() {
            return issueDate;
        }

        public void setIssueDate(LocalDate issueDate) {
            this.issueDate = issueDate;
        }

        public LocalDate getDueDate() {
            return dueDate;
        }

        public void setDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
        }

        public LocalDate getReturnDate() {
            return returnDate;
        }

        public void setReturnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    // =========================================================
    // BOOK REQUEST DTO
    // =========================================================

    public static class BookRequest {

        @NotBlank(message = "ISBN is required")
        @Pattern(
                regexp = "^\\d{3}-\\d{10}$",
                message = "ISBN must match the format 123-1234567890"
        )
        private String isbn;

        @NotBlank(message = "Title is required")
        @Size(
                min = 2,
                max = 120,
                message = "Title must contain between 2 and 120 characters"
        )
        private String title;

        @NotBlank(message = "Author is required")
        @Size(
                max = 60,
                message = "Author must not exceed 60 characters"
        )
        private String author;

        @NotNull(message = "Genre is required")
        private Genre genre;

        @NotNull(message = "Total copies is required")
        @Min(
                value = 1,
                message = "Total copies must be at least 1"
        )
        @Max(
                value = 500,
                message = "Total copies cannot exceed 500"
        )
        private Integer totalCopies;

        @NotNull(message = "Published year is required")
        @Min(
                value = 1450,
                message = "Published year must be at least 1450"
        )
        @Max(
                value = 2026,
                message = "Published year cannot exceed 2026"
        )
        private Integer publishedYear;

        @NotNull(message = "Price is required")
        @DecimalMin(
                value = "50.00",
                message = "Price must be at least 50.00"
        )
        @DecimalMax(
                value = "20000.00",
                message = "Price cannot exceed 20000.00"
        )
        @Digits(
                integer = 5,
                fraction = 2,
                message = "Price must have up to 5 integer digits and 2 decimal places"
        )
        private BigDecimal price;

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Genre getGenre() {
            return genre;
        }

        public void setGenre(Genre genre) {
            this.genre = genre;
        }

        public Integer getTotalCopies() {
            return totalCopies;
        }

        public void setTotalCopies(Integer totalCopies) {
            this.totalCopies = totalCopies;
        }

        public Integer getPublishedYear() {
            return publishedYear;
        }

        public void setPublishedYear(Integer publishedYear) {
            this.publishedYear = publishedYear;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }

    // =========================================================
    // ISSUE REQUEST DTO
    // =========================================================

    public static class IssueRequest {

        @NotNull(message = "Book ID is required")
        @Positive(message = "Book ID must be positive")
        private Long bookId;

        @NotBlank(message = "Member name is required")
        @Size(
                min = 3,
                max = 60,
                message = "Member name must contain between 3 and 60 characters"
        )
        private String memberName;

        @NotBlank(message = "Member email is required")
        @Email(message = "Member email must be a valid email address")
        private String memberEmail;

        @NotNull(message = "Issue date is required")
        @PastOrPresent(message = "Issue date cannot be in the future")
        private LocalDate issueDate;

        @NotNull(message = "Due date is required")
        @Future(message = "Due date must be in the future")
        private LocalDate dueDate;

        @PastOrPresent(message = "Return date cannot be in the future")
        private LocalDate returnDate;

        public Long getBookId() {
            return bookId;
        }

        public void setBookId(Long bookId) {
            this.bookId = bookId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberEmail() {
            return memberEmail;
        }

        public void setMemberEmail(String memberEmail) {
            this.memberEmail = memberEmail;
        }

        public LocalDate getIssueDate() {
            return issueDate;
        }

        public void setIssueDate(LocalDate issueDate) {
            this.issueDate = issueDate;
        }

        public LocalDate getDueDate() {
            return dueDate;
        }

        public void setDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
        }

        public LocalDate getReturnDate() {
            return returnDate;
        }

        public void setReturnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
        }
    }

    // =========================================================
    // ISSUE UPDATE DTO
    // =========================================================

    public static class IssueUpdateRequest {

        @Size(
                min = 3,
                max = 60,
                message = "Member name must contain between 3 and 60 characters"
        )
        private String memberName;

        @Email(message = "Member email must be a valid email address")
        private String memberEmail;

        @Future(message = "Due date must be in the future")
        private LocalDate dueDate;

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberEmail() {
            return memberEmail;
        }

        public void setMemberEmail(String memberEmail) {
            this.memberEmail = memberEmail;
        }

        public LocalDate getDueDate() {
            return dueDate;
        }

        public void setDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
        }
    }
}