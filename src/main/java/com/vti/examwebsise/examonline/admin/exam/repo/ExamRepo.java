package com.vti.examwebsise.examonline.admin.exam.repo;
import com.vti.examwebsise.examonline.common.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepo extends JpaRepository<Exam,Integer> {

}
