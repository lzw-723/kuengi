package fun.lzwi.service;

import fun.lzwi.bean.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookshelfService {
    // 单例模式
    private static BookshelfService bookshelfService = new BookshelfService();

    private BookshelfService() {
    }

    public static BookshelfService getInstance() {
        return bookshelfService;
    }

    private final ObservableList<Book> books = FXCollections.observableArrayList();

    // 添加书籍
    // 删除书籍
    // 修改书籍
    // 查询书籍
    public void add(Book book) {
        books.add(book);
    }

    public void delete(Book book) {
    }

    public void update(Book book) {
    }

    public void query(Book book) {
    }

    public ObservableList<Book> observableList() {
        return books;
    }

    /**
     * 查询书籍总数
     * 
     * @return
     */
    public int count() {
        return books.size();
    }

}
