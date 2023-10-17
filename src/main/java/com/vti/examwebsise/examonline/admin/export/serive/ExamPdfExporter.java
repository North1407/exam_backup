package com.vti.examwebsise.examonline.admin.export.serive;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.vti.examwebsise.examonline.common.entity.Exam;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;


public class ExamPdfExporter extends AbstractExporter {

    public void export(List<Exam> listUsers, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response, "application/pdf", ".pdf", "exams_");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph paragraph = new Paragraph("List of exam", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        table.setWidths(new float[] {1.2f, 3.5f, 3.0f, 3.0f, 3.0f, 3.0f});

        writeTableHeader(table);
        writeTableData(table, listUsers);

        document.add(table);

        document.close();

    }

    private void writeTableData(PdfPTable table, List<Exam> listUsers) {
        for (Exam exam : listUsers) {
            table.addCell(String.valueOf(exam.getId()));
            table.addCell(exam.getUser().getUsername());
            if(exam.getTopic()==null) {
                table.addCell("Random");
            }else {
                table.addCell(String.valueOf(exam.getTopic().getName()));
            }
            table.addCell(String.valueOf(exam.getMark()));
            table.addCell(String.valueOf(exam.getStartTime()));
            table.addCell(String.valueOf(exam.getEndTime()));

        }
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Username", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Topic", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Mark", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Start Time", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("End Time", font));
        table.addCell(cell);
    }

}