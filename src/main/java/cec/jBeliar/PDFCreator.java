package cec.jBeliar;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;


public class PDFCreator {

    private PDFData data;
    private PDFConfig config;
    private PDFStyles style;

    private String fileName;

    public PDFCreator() {
        this.data = new PDFData("Raport");
        this.config = new PDFConfig("admin", "admin", "title");
        this.style = new PDFStyles(BaseColor.WHITE);
    }
    public PDFCreator(PDFData data) {
        this();
        this.data = data;
    }
    public PDFCreator(PDFData data, PDFConfig config) {
        this();
        this.data = data;
        this.config = config;
    }
    public PDFCreator(PDFData data, PDFStyles style) {
        this();
        this.data = data;
        this.style = style;
    }
    public PDFCreator(PDFData data, PDFConfig config, PDFStyles style) {
        this();
        this.data = data;
        this.config = config;
        this.style = style;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void create() {

        Rectangle pageSize = new Rectangle(PageSize.A4);
        pageSize.setBackgroundColor(style.getDocumentBackGroundColor());
        Document document = new Document(pageSize);

        Font bigFont = new Font(Font.FontFamily.HELVETICA  , 25, Font.BOLD);
        Paragraph titleParagraph = new Paragraph();
        Paragraph dateParagraph = new Paragraph();
        Paragraph main = new Paragraph();

        // add Title
        titleParagraph.add(new Chunk(this.data.getTitle(), bigFont));
        titleParagraph.add(Chunk.NEWLINE);
        titleParagraph.add(Chunk.NEWLINE);
        titleParagraph.add(Chunk.NEWLINE);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);

        // add Date
        if (data.isDate()) {

            LocalDate currentDate = LocalDate.now();
            Chunk date = new Chunk();

            dateParagraph.add(new Chunk(currentDate.toString()));
            dateParagraph.setAlignment(Element.ALIGN_RIGHT);

            main.add(date);
        }

        // add sentences
        for (String sentence : this.data.getSentences()) {
            if(sentence.equals("-")){
                main.add(new Chunk(new LineSeparator()));
            }
            else {
                main.add(new Chunk(sentence));
            }
            main.add(Chunk.NEWLINE);
        }

        // add picture
        //Image image = Image.getInstance(image);

        try {

            log("Savinig raport to a file.");

            PdfWriter.getInstance(document, new FileOutputStream(this.fileName + ".pdf"));

            document.open();
            document.addCreator(config.getCreator());
            document.addAuthor(config.getAuthor());
            document.addTitle(config.getTitle());
            document.add(titleParagraph);
            document.add(dateParagraph);
            document.add(main);
            document.close();

            log("File is created properly.");

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void log(String message) {
        System.out.println(message);
    }
}
