package com.vti.examwebsise.examonline.admin.question.controller;

import com.vti.examwebsise.examonline.admin.level.service.LevelService;
import com.vti.examwebsise.examonline.common.entity.Level;
import com.vti.examwebsise.examonline.common.entity.Question;
import com.vti.examwebsise.examonline.common.entity.Topic;
import com.vti.examwebsise.examonline.admin.setting.service.SettingService;
import com.vti.examwebsise.examonline.admin.export.serive.ExamWordExporter;
import com.vti.examwebsise.examonline.admin.question.service.QuestionService;
import com.vti.examwebsise.examonline.admin.topic.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/manage")
public class QuestionController {
    private String defaultRedirectURL = "redirect:/manage/questions";
    @Autowired
    private QuestionService questionService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private LevelService levelService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/questions")
    public String getAllQuestions(Model model, String keyword, String status, HttpSession session
    ) {
        List<Topic> topics = topicService.getAllTopics();
        List<Question> questions;
        if (status == null && session.getAttribute("questions") != null && keyword == null) {
            questions = (List<Question>) session.getAttribute("questions");
        } else {
            questions = questionService.getAllQuestions(keyword);
        }

        List<Level> levels = levelService.getAllLevels();
        int numQ = settingService.getNumberOfQuestion();

        // Lưu danh sách câu hỏi vào session
        session.setAttribute("questions", questions);
        model.addAttribute("questions", questions);
        model.addAttribute("numQ", new int[numQ]);
        model.addAttribute("levels", levels);
        model.addAttribute("keyword", keyword);
        model.addAttribute("topics", topics);
        return "admins/questions/questions";
    }

    @GetMapping("/questions/export")
    public void exportQuestions(String keyword, String topicName, String levelName, int numQ, HttpServletResponse response, HttpSession session) {
        // Lấy danh sách câu hỏi từ session
        List<Question> currentQuestions = (List<Question>) session.getAttribute("questions");
        List<Question> questions = questionService.exportQuestions(currentQuestions, topicName, levelName, keyword);
        ExamWordExporter exporter = new ExamWordExporter();
        exporter.exportDocx(questions.subList(0, Math.min(numQ, questions.size())), topicName, response);
    }

    @GetMapping("/questions/new")
    public String newQuestion(Model model) {
        List<Topic> topics = topicService.getAllTopics();
        List<Level> levels = levelService.getAllLevels();
        model.addAttribute("question", new Question());
        model.addAttribute("topics", topics);
        model.addAttribute("levels", levels);
        model.addAttribute("title", "New Question");
        return "admins/questions/question_form";
    }

    @GetMapping("/questions/{id}/enabled/{status}")
    public String enableQuestion(@PathVariable("id") Integer id, @PathVariable("status") boolean status, RedirectAttributes re) {
        questionService.enableQuestion(id, status);
        re.addFlashAttribute("message", "The question ID " + id + " has been " + (status ? "enabled" : "disabled") + " successfully");
        return defaultRedirectURL + "?status=change";
    }

    @PostMapping("/questions/save")
    public String saveQuestion(Question question, @RequestParam("answerIDs") String[] answerIds, @RequestParam("answerContents") String[] answerContents,
                               @RequestParam("answerCorrects") String[] ansCorrects, RedirectAttributes re) {
        try {
            questionService.save(question, answerIds, answerContents, ansCorrects);
        } catch (RuntimeException e) {
            re.addFlashAttribute("dangerMessage", "Answer is used in Exam");
            return defaultRedirectURL;
        }
        re.addFlashAttribute("message", "The question has been saved successfully");
        return defaultRedirectURL + "?status=change";
    }

    @GetMapping("/questions/edit/{id}")
    public String editQuestion(@PathVariable("id") Integer id, Model model) {
        Question question = questionService.getQuestionById(id);
        List<Topic> topics = topicService.getAllTopics();
        List<Level> levels = levelService.getAllLevels();
        model.addAttribute("question", question);
        model.addAttribute("topics", topics);
        model.addAttribute("levels", levels);
        model.addAttribute("title", "Edit Question(ID: " + id + ")");
        return "admins/questions/question_form";
    }

    @GetMapping("/questions/delete/{id}")
    public String deleteQuestion(@PathVariable("id") Integer id, RedirectAttributes re) {
        try {
            questionService.deleteQuestion(id);
        } catch (RuntimeException e) {
            re.addFlashAttribute("dangerMessage", "Question with id " + id + " is used in Exam");
            return defaultRedirectURL;
        }
        re.addFlashAttribute("message", "The question ID " + id + " has been deleted successfully");
        return defaultRedirectURL + "?status=change";
    }
}
