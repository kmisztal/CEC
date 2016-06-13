package cec.jBeliar;

import java.util.ArrayList;
import java.util.List;


public class PDFData {
    private String title;
    private List<String> sentences;
    private boolean date;
    private String author;


    public PDFData() {
        sentences = new ArrayList<>();
    }

    public PDFData(String filename) {
        this();
        this.title = filename;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addSentence(String sentence) {
        this.sentences.add(sentence);
    }

    public List<String> getSentences() {
        return sentences;
    }

    public String get(int i) {
        return sentences.get(i);
    }

    public String getTitle() {
        return title;
    }
    public int getSize() {
        return sentences.size();
    }

    public void addDate() {
        this.date = true;
    }
    public boolean isDate() {
        return this.date;
    }
    public void addAuthor(String author) {
        this.author = author;
    }
    public void addLineSeparator() {
        sentences.add("-");
    }

}
