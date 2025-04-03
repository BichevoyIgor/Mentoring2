package ru.bichevoy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bichevoy.entity.Author;
import ru.bichevoy.entity.Book;
import ru.bichevoy.entity.Customer;
import ru.bichevoy.entity.RentDetail;
import ru.bichevoy.exception.BaseException;
import ru.bichevoy.service.LibraryService;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private final LibraryService libraryService;

    @RequestMapping
    public String showMainPage(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("author", new Author());
        model.addAttribute("customer", new Customer());
        return "form";
    }

    @RequestMapping("/showBook")
    public String showInfoBook(@RequestParam("title") String title, Model model) {
        List<Book> foundedBook = libraryService.findBookByTitle(title);
        model.addAttribute("books", foundedBook);
        return "book_info";
    }

    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book book,
                          @RequestParam("authorsInput") String authorsInput,
                          Model model) {
        try {
            libraryService.addBook(book, authorsInput);
            List<Book> bookList = List.of(book);
            model.addAttribute("books", bookList);
            return "book_info";
        } catch (BaseException e) {
            return "oops";
        }
    }

    @PostMapping("/addCustomer")
    public String addCustomer(@ModelAttribute Customer customer, Model model) {
        try {
            libraryService.addCustomer(customer);
            model.addAttribute("customer", customer);
            return "customer_info";
        } catch (BaseException e) {
            return "oops";
        }
    }

    @RequestMapping("/showBookFromAPI")
    public String showBookFromGoogle(@RequestParam("title") String title, Model model) {
        try {
            List<Book> books = libraryService.findBookInAPI(title);
            model.addAttribute("books", books);
            return "book_info";
        } catch (BaseException e) {
            return "oops";
        }
    }

    @RequestMapping("/showInfoCustomer")
    public String showClientInfo(@RequestParam("id") long id, Model model) {
        try {
            Optional<Customer> customer = libraryService.findCustomer(id);
            model.addAttribute("customer", customer.orElse(null));
            if (customer.isPresent()) {
                List<RentDetail> rentDetails = libraryService.findRentDetails(id);
                model.addAttribute("rentDetails", rentDetails);
            }
            return "customer_info";
        } catch (BaseException e) {
            return "oops";
        }
    }

    @PostMapping("/rentBook")
    public String rentBook(@RequestParam("book_title") String title,
                           @RequestParam("customer_id") long id,
                           Model model) {
        try {
            Optional<RentDetail> rentDetail = libraryService.rentBook(title, id);
            model.addAttribute("rentDetail", rentDetail.orElse(null));
            return "rent_detail";
        } catch (BaseException e) {
            return "oops";
        }
    }

    @PostMapping("/endRent")
    public String endRent(@RequestParam("rent_id") long book_id, @RequestParam("customer_id") long customerId) {
        try {
            libraryService.endRent(book_id);
            return "redirect:/showInfoCustomer?id=" + customerId;
        } catch (BaseException e) {
            return "oops";
        }
    }

    @GetMapping("history_rent")
    public String historyRent(@RequestParam("id") long customerId, Model model) {
        try {
            Optional<Customer> customer = libraryService.findCustomer(customerId);
            model.addAttribute("customer", customer.orElse(null));
            if (customer.isPresent()) {
                List<RentDetail> rentDetails = libraryService.findAllRentDetails(customerId);
                model.addAttribute("rentDetails", rentDetails);
            }
            return "customer_info";
        } catch (BaseException e) {
            return "oops";
        }
    }
}
