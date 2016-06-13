package cec.jBeliar;

import com.itextpdf.text.BaseColor;


public class PDFStyles {

    private BaseColor backgroundColor;

    public PDFStyles(BaseColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public BaseColor getDocumentBackGroundColor() {
        return backgroundColor;
    }
}
