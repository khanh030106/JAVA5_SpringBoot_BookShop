package assignment.java5.ass_self.Services;

import assignment.java5.ass_self.Entities.Book;
import assignment.java5.ass_self.Repository.BookRepository;
import org.springframework.data.domain.Page;
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

    public Page<Book> topSoldOut(int index, int pageSize){
        Pageable pageable = PageRequest.of(index, pageSize);
        return bookRepository.findTopSoldOut(pageable);
    }

    public List<Book> find20Books(){
        Pageable pageable = PageRequest.of(0, 20);
        return bookRepository.findAllBooksActive(pageable).getContent();
    }

    public Page<Book> findAllBooks(int index, int pageSize){
        Pageable pageable = PageRequest.of(index, pageSize);
        return bookRepository.findAllBooksActive(pageable);
    }

    public List<Book> find25PromotionBooks(){
        Pageable pageable = PageRequest.of(0, 25);
        return bookRepository.findPromotionBooks(pageable).getContent();
    }

    public Page<Book> findAllPromotionBooks(int index, int pageSize){
        Pageable pageable = PageRequest.of(index, pageSize);
        return bookRepository.findPromotionBooks(pageable);
    }

    public List<Book> filterBooks(
            String price,
            Long category,
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
                Long.valueOf(emptyToNull(String.valueOf(category))),
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

    public Book findBookWithId(Long id){
        return bookRepository.findById(id).orElse(null);
    }
}
