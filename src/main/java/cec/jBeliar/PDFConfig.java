package cec.jBeliar;


public class PDFConfig {
    private String creator;
    private String author;
    private String title;

    public PDFConfig(String creator, String author, String title) {
        this.creator = creator;
        this.author = author;
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }
}
