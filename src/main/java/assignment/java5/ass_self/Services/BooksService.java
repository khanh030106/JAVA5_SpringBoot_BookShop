package assignment.java5.ass_self.Services;

import assignment.java5.ass_self.Entities.Book;
import assignment.java5.ass_self.Repository.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BooksService {
    private final BookRepository bookRepository;
    public BooksService (BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }


    private String emptyToNull(String v){
        return (v == null || v.isBlank()) ? null : v;
    }

    public List<Book> top20SoldOut(){
        Pageable pageable = PageRequest.of(0,20);
        return bookRepository.findTopSoldOut(pageable).getContent();
    }

    public List<Book> topSoldOut(){
        Pageable pageable = PageRequest.of(0, 50);
        return bookRepository.findTopSoldOut(pageable).getContent();
    }

    public List<Book> find30Books(){
        Pageable pageable = PageRequest.of(0, 30);
        return bookRepository.findAllBooksActive(pageable).getContent();
    }

    public List<Book> findAllBooks(){
        Pageable pageable = PageRequest.of(0, 50);
        return bookRepository.findAllBooksActive(pageable).getContent();
    }

    public List<Book> filterBooks(
            String price,
            String category,
            String author
    ){
        BigDecimal min = null;
        BigDecimal max = null;
        Pageable pageable = PageRequest.of(0, 50);

        if (price != null && !price.isBlank()) {
            switch (price) {
                case "0-100000" -> {
                    min = BigDecimal.ZERO;
                    max = BigDecimal.valueOf(100000);
                }
                case "100000-200000" -> {
                    min = BigDecimal.valueOf(100000);
                    max = BigDecimal.valueOf(200000);
                }
                case "200000+" -> {
                    min = BigDecimal.valueOf(200000);
                    max = BigDecimal.valueOf(999999999);
                }
            }
        }

        return bookRepository.filterBooks(
                emptyToNull(category),
                emptyToNull(author),
                min,
                max,
                pageable
        ).getContent();
    }

    @Transactional(readOnly = true)
    public Book findBookById(Long id){
        return bookRepository.findBookByIdWithId(id);
    }

    @Transactional(readOnly = true)
    public List<Book> findBookWithCate(Long id) {
        Pageable pageable = PageRequest.of(0, 20);
        if (id == null) {
            return List.of();
        } else {
            return bookRepository.findBookWithCate(id, pageable).getContent();
        }
    }
}
