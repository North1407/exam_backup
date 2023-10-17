package com.vti.examwebsise.examonline.common.entity;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "exams")
@NoArgsConstructor
@AllArgsConstructor
public class Exam extends IdBasedEntity implements Comparable<Exam> {
    @Column(nullable = false)
    private float mark;

    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "exams_answers",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "answer_id")
    )
    private List<Answer> answers = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "exams_questions",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    List<Question> questions = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public int compareTo(Exam o) {
        return o.getId() - this.id;
    }

    public String toString() {
        return "Exam [mark=" + mark + ", startTime=" + startTime + ", endTime=" + endTime + ", topic=" + questions.get(0).getTopic() + "]";
    }
}