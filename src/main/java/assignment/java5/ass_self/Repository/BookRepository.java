package assignment.java5.ass_self.Repository;

import assignment.java5.ass_self.Entities.Book;
import assignment.java5.ass_self.Entities.PromotionBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("""
SELECT DISTINCT b FROM Book b WHERE b.isDeleted = false AND b.isActive = true ORDER BY b.soldCount DESC
""")
    Page<Book> findTopSoldOut(Pageable pageable);

    @Query("""
SELECT b FROM Book b
WHERE b.isDeleted = false AND b.isActive = true
""")
    Page<Book> findAllBooksActive(Pageable pageable);

    @Query("""
    SELECT DISTINCT b FROM Book b
    LEFT JOIN b.bookCategories bc
    LEFT JOIN bc.categoryID c
    LEFT JOIN b.bookAuthors ba
    LEFT JOIN ba.authorID a
    WHERE (:category IS NULL OR c.id = :category)
      AND (:author IS NULL OR a.authorName = :author)
      AND (:min IS NULL OR b.price >= :min)
      AND (:max IS NULL OR b.price <= :max)
      AND b.isDeleted = false
      AND b.isActive = true
""")

    Page<Book> filterBooks(
            @Param("category") Long category,
            @Param("author") String author,
            @Param("min") BigDecimal min,
            @Param("max") BigDecimal max,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT b FROM Book b
    LEFT JOIN FETCH b.bookAuthors ba
    LEFT JOIN FETCH ba.authorID
    LEFT JOIN FETCH b.bookImages bi
    LEFT JOIN FETCH b.publisherID
    WHERE b.id = :id
    """)
    Book findBookByIdWithId(@Param("id") Long id);

    @Query("""
    SELECT DISTINCT b FROM Book b
    JOIN  b.bookCategories bc
    WHERE bc.categoryID = (
    SELECT bc.categoryID FROM BookCategory bc
    JOIN  bc.bookID b
    WHERE b.id = :id
    )
""")
    Page<Book> findBookWithCate(@Param("id") Long id, Pageable pageable);

    @Query("""
    SELECT DISTINCT b FROM Book b
    JOIN PromotionBook pb ON b.id = pb.id.bookID
    JOIN pb.promotionID p
    WHERE p.startDate <= CURRENT_TIMESTAMP AND p.endDate >= CURRENT_TIMESTAMP
    AND b.isDeleted = false AND b.isActive = true
    """)
    Page<Book> findPromotionBooks(Pageable pageable);

    @Query("""
    SELECT b FROM Book b WHERE b.id = :id
""")
    public Book findBookById(@Param("id") Long id);
}
