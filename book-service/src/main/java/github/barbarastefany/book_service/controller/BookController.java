package github.barbarastefany.book_service.controller;

import github.barbarastefany.book_service.model.Book;
import github.barbarastefany.book_service.proxy.CambioProxy;
import github.barbarastefany.book_service.repository.BookRepository;
import github.barbarastefany.book_service.response.Cambio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("book-service")
public class BookController {

    @Autowired
    private Environment environment;

    @Autowired
    private BookRepository repository;

    @Autowired
    private CambioProxy proxy;

    @GetMapping("/book-service/{id}/{currency}")
    public Book getBook(
            @PathVariable Long id,
            @PathVariable String currency
    ) {

        Book book = repository
                .findById(id)
                .orElseThrow((
                ) -> new RuntimeException("Book not found"));

        Cambio cambio = proxy.getCambio(book.getPrice(), "USD", currency);

        String PORT = environment.getProperty("local.server.port");
        book.setEnvironment(
                "Book port"
                        + PORT
                        + "Cambio Port"
                        + cambio.getEnvironment()
        );
        book.setPrice(cambio.getConvertedValue().doubleValue());
        book.setCurrency(currency);

        return book;
    }

}