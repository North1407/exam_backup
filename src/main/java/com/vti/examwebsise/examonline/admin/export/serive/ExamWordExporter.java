package com.vti.examwebsise.examonline.admin.export.serive;

import com.vti.examwebsise.examonline.common.entity.Answer;
import com.vti.examwebsise.examonline.common.entity.Question;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ExamWordExporter {
    public void exportDocx(List<Question> questions, String topicName, HttpServletResponse response) {
        try {
            XWPFDocument document = new XWPFDocument();
            setTitleParagraph(document, topicName);
            int questionNumber = 1;
            for (Question question : questions) {
                setQuestionParagraph(question, document, questionNumber);
                setAnswersParagraph(question.getAnswers(), document);
                questionNumber++;
            }
            export(response, document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setTitleParagraph(XWPFDocument document, String topicName) {
        XWPFParagraph titleParagraph = document.createParagraph();
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setBold(true);
        titleRun.setFontSize(16);
        titleRun.setText("VTI Exam Test" + ("".equals(topicName)? "": " - " + topicName));
    }
    private void setQuestionParagraph(Question question, XWPFDocument document, int questionNumber) {
        XWPFParagraph questionParagraph = document.createParagraph();
        String header;
        String code = null;
        if (question.getContent().contains("<p>")) {
            header = question.getRawContent().split("\\r\\n")[0];
            code = question.getRawContent().split(header)[1];
        } else {
            header = question.getRawContent();
        }
        XWPFRun questionRun = questionParagraph.createRun();
        questionRun.setText("Câu hỏi " + questionNumber + ": " + header);
        questionRun.setFontFamily("Arial"); // Đặt font chữ
        questionRun.setFontSize(13);
        questionRun.setBold(true);
        if (code != null) {
            XWPFRun questionRun1 = questionParagraph.createRun();
            questionRun1.setText(code);
            questionRun1.setFontFamily("Arial"); // Đặt font chữ
            questionRun1.setFontSize(11);
        }
        if (question.getTrueAnswer() > 1) {
            XWPFRun questionRun2 = questionParagraph.createRun();
            questionRun2.setFontSize(11);
            questionRun2.setItalic(true);
            questionRun2.setText("(Có nhiều đáp án đúng)");
            questionRun2.addCarriageReturn();
        }
    }
    private void setAnswersParagraph(List<Answer> answers, XWPFDocument document) {

        char answerNumber = 'A';
        XWPFParagraph answersParagraph = document.createParagraph();
        XWPFRun answersRun = answersParagraph.createRun();
        Iterator<Answer> iterator = answers.iterator();

        while (iterator.hasNext()) {
            Answer answer = iterator.next();
            answersRun.setText(answerNumber + ") " + answer.getContent());
            answerNumber++;
            answersRun.addCarriageReturn();
        }
    }
    private void export(HttpServletResponse response, XWPFDocument document) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        document.write(byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String timestamp = dateFormatter.format(new Date());
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=exam" + timestamp + ".docx");
        response.setContentLength(byteArray.length);
        response.getOutputStream().write(byteArray);
    }
}
