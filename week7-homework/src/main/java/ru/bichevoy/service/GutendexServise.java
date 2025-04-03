package ru.bichevoy.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bichevoy.entity.Book;
import ru.bichevoy.exception.BaseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GutendexServise {
    private final AuthorService authorService;
    private final BookService bookService;
    private final AuthorBookService authorBookService;

    private final String URL_API = "https://gutendex.com/books?search=";

    public List<Book> findBookByTitle(String titleBook) {
        List<Book> foundedBooks = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(getJson(titleBook).toString());
            JsonNode items = rootNode.path("results");
            for (JsonNode item : items) {
                String foundedTitle = item.path("title").asText();
                Book book = new Book();
                List<String> genre = unpackGenre(item);

                String description = item.path("summaries").toString().replaceAll("\\\\", "")
                        .replaceAll("\\\"]", "")
                        .replaceAll("\\[\\\"\\\"", "");
                book.setTitle(foundedTitle);
                book.setDescription(description);
                book.setGenre(String.join(", ", genre));

                List<String> authorsList = unpackAuthors(item);
                book.setAuthors(new ArrayList<>());
                for (String name : authorsList) {
                    book.getAuthors().add(authorService.getOrAddAuthor(name));
                }
                foundedBooks.add(book);
                bookService.add(book);
                authorBookService.addAuthorBook(book.getAuthors(), book);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }
        return foundedBooks;
    }

    private StringBuilder getJson(String titleBook) throws IOException {
        URL url = new URL(URL_API + URLEncoder.encode(titleBook, StandardCharsets.UTF_8));
        URLConnection connection = url.openConnection();
        BufferedReader jsonResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = jsonResponse.readLine()) != null) {
            content.append(inputLine);
        }
        return content;
    }

    private List<String> unpackGenre(JsonNode rootNode) {
        List<String> unpackGenreList = new ArrayList<>();
        JsonNode bookshelvesNode = rootNode.path("bookshelves");
        for (JsonNode jsonNode : bookshelvesNode) {
            String text = jsonNode.asText();
            if (text.startsWith("Browsing: ")) {
                unpackGenreList.add(text.replace("Browsing: ", ""));
            } else {
                unpackGenreList.add(jsonNode.path("Browsing: ").asText().replaceAll(",", ""));
            }
        }
        return unpackGenreList;
    }

    private List<String> unpackAuthors(JsonNode rootNode) {
        List<String> unpackAuthorsList = new ArrayList<>();
        JsonNode bookshelvesNode = rootNode.path("authors");
        for (JsonNode jsonNode : bookshelvesNode) {
            String[] authorName = jsonNode.path("name").asText().split(", ");
            if (authorName.length >= 2) {
                unpackAuthorsList.add(authorName[1] + " " + authorName[0]);
            } else {
                unpackAuthorsList.add(authorName[0]);
            }
        }
        return unpackAuthorsList;
    }
}
