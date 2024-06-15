package com.example.library.controller;

import com.example.library.model.Book;
import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.repo.UserRepo;
import com.example.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final UserRepo userRepo;

    @GetMapping
    public String profilePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login?logout";
        }

        String username = principal.getName();
        User user = userRepo.findByUsername(username);

        if (user == null) {
            return "redirect:/login?logout";
        }

        List<Book> allBooks = bookService.findAllBooks();
        List<Book> userBooks = bookService.findBooksByUser(user);

        model.addAttribute("isAdmin", user.getRoles().contains(Role.ADMIN));
        model.addAttribute("user", user);
        model.addAttribute("books", allBooks);
        model.addAttribute("userBooks", userBooks);

        return "profile";
    }

    @GetMapping("/add")
    public String addBookForm(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("book", new Book());
            return "add-book";
        }
        return "redirect:/login";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepo.findByUsername(username);
        book.setUser(user);
        bookService.saveBook(book);
        return "redirect:/profile";
    }
}