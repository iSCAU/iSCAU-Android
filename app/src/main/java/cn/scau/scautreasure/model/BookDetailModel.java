package cn.scau.scautreasure.model;

import java.util.List;

/**
 * 书籍详细;
 * User: special
 * Date: 13-8-18
 * Time: 下午9:52
 * Mail: specialcyci@gmail.com
 */
//        "索书号" => "serial_number",
//        "文献类型" => "document_type",
//        "条码号" => "barcode_number",
//        "年卷期" => "year_title",
//        "校区—馆藏地" => "collection_place",
//        "书刊状态" => "books_status",
public class BookDetailModel {

    private String serial_number;
    private String document_type;
    private String barcode_number;
    private String year_title;
    private String collection_place;
    private String books_status;

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

    public String getBarcode_number() {
        return barcode_number;
    }

    public void setBarcode_number(String barcode_number) {
        this.barcode_number = barcode_number;
    }

    public String getYear_title() {
        return year_title;
    }

    public void setYear_title(String year_title) {
        this.year_title = year_title;
    }

    public String getCollection_place() {
        return collection_place;
    }

    public void setCollection_place(String collection_place) {
        this.collection_place = collection_place;
    }

    public String getBooks_status() {
        return books_status;
    }

    public void setBooks_status(String books_status) {
        this.books_status = books_status;
    }

    @Override
    public String toString() {
        return "BookDetailModel{" +
                "serial_number='" + serial_number + '\'' +
                ", document_type='" + document_type + '\'' +
                ", barcode_number='" + barcode_number + '\'' +
                ", year_title='" + year_title + '\'' +
                ", collection_place='" + collection_place + '\'' +
                ", books_status='" + books_status + '\'' +
                '}';
    }

    public class DetailList{

        private List<BookDetailModel> details;

        public List<BookDetailModel> getDetails() {
            return details;
        }

        public void setDetails(List<BookDetailModel> details) {
            this.details = details;
        }
    }
}
