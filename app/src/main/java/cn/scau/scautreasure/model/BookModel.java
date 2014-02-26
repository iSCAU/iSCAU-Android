package cn.scau.scautreasure.model;

import java.util.List;

/**
 * 当前借阅，历史借阅的model;
 * User: special
 * Date: 13-8-18
 * Time: 下午10:00
 * Mail: specialcyci@gmail.com
 */
//        "题名" => "title",
//        "责任者" => "author",
//        "题名/责任者" => "title",
//        "出版信息" => "press",
//        "借阅日期" => "borrow_date",
//        "归还日期" => "return_date",
//        "应还日期" => "should_return_date",
//        "馆藏地" => "collection_place",
//        "续借量" => "renew_time",
//        "索书号" => "serial_number",
//        "文献类型" => "document_type",
//        "条码号" => "barcode_number",
public class BookModel {

    private String title;
    private String author;
    private String press;
    private String borrow_date;
    private String return_date;
    private String should_return_date;
    private int    renew_time;
    private String collection_place;
    private String barcode_number;
    private String serial_number;
    private String document_type;
    private String check_code;
    private String url;

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

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getBorrow_date() {
        return borrow_date;
    }

    public void setBorrow_date(String borrow_date) {
        this.borrow_date = borrow_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public String getShould_return_date() {
        return should_return_date;
    }

    public void setShould_return_date(String should_return_date) {
        this.should_return_date = should_return_date;
    }

    public String getCollection_place() {
        return collection_place;
    }

    public void setCollection_place(String collection_place) {
        this.collection_place = collection_place;
    }

    public String getBarcode_number() {
        return barcode_number;
    }

    public void setBarcode_number(String barcode_number) {
        this.barcode_number = barcode_number;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public String getCheck_code() {
        return check_code;
    }

    public void setCheck_code(String check_code) {
        this.check_code = check_code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRenew_time() {
        return renew_time;
    }

    public void setRenew_time(int renew_time) {
        this.renew_time = renew_time;
    }

    @Override
    public String toString() {
        return "BookModel{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", press='" + press + '\'' +
                ", borrow_date='" + borrow_date + '\'' +
                ", return_date='" + return_date + '\'' +
                ", should_return_date='" + should_return_date + '\'' +
                ", renew_time='" + renew_time + '\'' +
                ", collection_place='" + collection_place + '\'' +
                ", barcode_number='" + barcode_number + '\'' +
                ", serial_number='" + serial_number + '\'' +
                ", document_type='" + document_type + '\'' +
                ", check_code='" + check_code + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public class BookList{

        private int count;

        private List<BookModel> books;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<BookModel> getBooks() {
            return books;
        }

        public void setBooks(List<BookModel> books) {
            this.books = books;
        }
    }
}
