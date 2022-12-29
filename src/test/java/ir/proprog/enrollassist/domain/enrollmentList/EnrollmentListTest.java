package ir.proprog.enrollassist.domain.enrollmentList;

import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.section.ExamTime;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import ir.proprog.enrollassist.domain.studyRecord.Grade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class EnrollmentListTest {

    @Mock
    Student student;

    EnrollmentList enrollmentList;

    @BeforeEach
    public void init() throws Exception {
        student = mock(Student.class, withSettings().useConstructor("1234", GraduateLevel.Undergraduate.name()));
        enrollmentList = new EnrollmentList("list", student);

        Section section1 = new Section(new Course("1234567", "CA", 3, GraduateLevel.Undergraduate.name()),
                "1", new ExamTime("2022-12-21T13:00", "2022-12-21T16:00"), null);
        Section section2 = new Section(new Course("2234567", "CB", 2, GraduateLevel.Undergraduate.name()),
                "2", new ExamTime("2022-12-22T13:00", "2022-12-22T16:00"), null);
        Section section3 = new Section(new Course("3234567", "CC", 1, GraduateLevel.Undergraduate.name()),
                "3", new ExamTime("2022-12-23T08:00", "2022-12-23T11:00"), null);

        enrollmentList.addSections(section1, section2, section3);
    }

    @AfterEach
    public void cleanUp() {
        enrollmentList = null;
    }

    @Test
    public void checkValidGPALimit_no_violation_test() throws Exception {
        Section section1 = new Section(new Course("4234567", "CD", 1, GraduateLevel.Undergraduate.name()),
                "4", new ExamTime("2022-12-21T17:00", "2022-12-21T19:00"), null);
        Section section2 = new Section(new Course("5234567", "CE", 4, GraduateLevel.Undergraduate.name()),
                "5", new ExamTime("2022-12-21T16:00", "2022-12-21T19:00"), null);
        Section section3 = new Section(new Course("6234567", "CF", 2, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);

        enrollmentList.addSections(section1, section2, section3);

        when(student.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        when(student.calculateGPA()).thenReturn(new Grade(0.0D));

        assertEquals(0, enrollmentList.checkValidGPALimit().size());
    }

    @Test
    public void checkValidGPALimit_min_credit_violation_test() throws Exception {
        when(student.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        when(student.calculateGPA()).thenReturn(new Grade(0.0D));

        assertEquals(1, enrollmentList.checkValidGPALimit().size());
    }

    @Test
    public void checkValidGPALimit_max_credit_violation_test() throws Exception {
        Section section1 = new Section(new Course("4234567", "CD", 1, GraduateLevel.Undergraduate.name()),
                "4", new ExamTime("2022-12-21T17:00", "2022-12-21T19:00"), null);
        Section section2 = new Section(new Course("5234567", "CE", 4, GraduateLevel.Undergraduate.name()),
                "5", new ExamTime("2022-12-21T16:00", "2022-12-21T19:00"), null);
        Section section3 = new Section(new Course("6234567", "CF", 2, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);
        Section section4 = new Section(new Course("7234567", "CG", 4, GraduateLevel.Undergraduate.name()),
                "5", new ExamTime("2022-12-21T16:00", "2022-12-21T19:00"), null);
        Section section5 = new Section(new Course("8234567", "CH", 4, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);

        enrollmentList.addSections(section1, section2, section3, section4, section5);

        when(student.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        when(student.calculateGPA()).thenReturn(new Grade(0.0D));

        assertEquals(1, enrollmentList.checkValidGPALimit().size());
    }

    @Test
    public void checkValidGPALimit_max_credit_violation_gpa_14_test() throws Exception {
        Section section1 = new Section(new Course("4234567", "CD", 1, GraduateLevel.Undergraduate.name()),
                "4", new ExamTime("2022-12-21T17:00", "2022-12-21T19:00"), null);
        Section section2 = new Section(new Course("5234567", "CE", 4, GraduateLevel.Undergraduate.name()),
                "5", new ExamTime("2022-12-21T16:00", "2022-12-21T19:00"), null);
        Section section3 = new Section(new Course("6234567", "CF", 2, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);
        Section section4 = new Section(new Course("7234567", "CG", 4, GraduateLevel.Undergraduate.name()),
                "5", new ExamTime("2022-12-21T16:00", "2022-12-21T19:00"), null);
        Section section5 = new Section(new Course("8234567", "CH", 4, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);

        enrollmentList.addSections(section1, section2, section3, section4, section5);

        when(student.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        when(student.calculateGPA()).thenReturn(new Grade(13.5D));

        assertEquals(1, enrollmentList.checkValidGPALimit().size());
    }

    @Test
    public void checkValidGPALimit_max_credit_violation_gpa_10_test() throws Exception {
        Section section1 = new Section(new Course("4234567", "CD", 1, GraduateLevel.Undergraduate.name()),
                "4", new ExamTime("2022-12-21T17:00", "2022-12-21T19:00"), null);
        Section section2 = new Section(new Course("5234567", "CE", 4, GraduateLevel.Undergraduate.name()),
                "5", new ExamTime("2022-12-21T16:00", "2022-12-21T19:00"), null);
        Section section3 = new Section(new Course("6234567", "CF", 2, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);
        Section section4 = new Section(new Course("7234567", "CG", 4, GraduateLevel.Undergraduate.name()),
                "5", new ExamTime("2022-12-21T16:00", "2022-12-21T19:00"), null);
        Section section5 = new Section(new Course("8234567", "CH", 4, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);

        enrollmentList.addSections(section1, section2, section3, section4, section5);

        when(student.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        when(student.calculateGPA()).thenReturn(new Grade(10.5D));

        assertEquals(1, enrollmentList.checkValidGPALimit().size());
    }

    @Test
    public void checkValidGPALimit_max_credit_violation_gpa_17_test() throws Exception {
        Section section1 = new Section(new Course("4234567", "CD", 1, GraduateLevel.Undergraduate.name()),
                "4", new ExamTime("2022-12-21T17:00", "2022-12-21T19:00"), null);
        Section section2 = new Section(new Course("5234567", "CE", 4, GraduateLevel.Undergraduate.name()),
                "5", new ExamTime("2022-12-21T16:00", "2022-12-21T19:00"), null);
        Section section3 = new Section(new Course("6234567", "CF", 2, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);
        Section section4 = new Section(new Course("7234567", "CG", 4, GraduateLevel.Undergraduate.name()),
                "5", new ExamTime("2022-12-21T16:00", "2022-12-21T19:00"), null);
        Section section5 = new Section(new Course("8234567", "CH", 4, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);
        Section section6 = new Section(new Course("8234567", "CH", 4, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);

        enrollmentList.addSections(section1, section2, section3, section4, section5, section6);

        when(student.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        when(student.calculateGPA()).thenReturn(new Grade(19.5D));

        assertEquals(1, enrollmentList.checkValidGPALimit().size());
    }

    @Test
    public void checkValidGPALimit_min_credit_violation_master_test() throws Exception {
        when(student.getGraduateLevel()).thenReturn(GraduateLevel.Masters);
        when(student.calculateGPA()).thenReturn(new Grade(0.0D));

        assertEquals(1, enrollmentList.checkValidGPALimit().size());
    }

    @Test
    public void checkValidGPALimit_max_credit_violation_master_test() throws Exception {
        Section section1 = new Section(new Course("4234567", "CD", 1, GraduateLevel.Undergraduate.name()),
                "4", new ExamTime("2022-12-21T17:00", "2022-12-21T19:00"), null);
        Section section2 = new Section(new Course("5234567", "CE", 4, GraduateLevel.Undergraduate.name()),
                "5", new ExamTime("2022-12-21T16:00", "2022-12-21T19:00"), null);
        Section section3 = new Section(new Course("6234567", "CF", 2, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);

        enrollmentList.addSections(section1, section2, section3);

        when(student.getGraduateLevel()).thenReturn(GraduateLevel.Masters);
        when(student.calculateGPA()).thenReturn(new Grade(0.0D));

        assertEquals(1, enrollmentList.checkValidGPALimit().size());
    }

    @Test
    public void checkValidGPALimit_violation_undergrad_test() throws Exception {
        Section section1 = new Section(new Course("4234567", "CD", 1, GraduateLevel.Undergraduate.name()),
                "4", new ExamTime("2022-12-21T17:00", "2022-12-21T19:00"), null);
        Section section2 = new Section(new Course("5234567", "CE", 4, GraduateLevel.Undergraduate.name()),
                "5", new ExamTime("2022-12-21T16:00", "2022-12-21T19:00"), null);
        Section section3 = new Section(new Course("6234567", "CF", 2, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);
        Section section4 = new Section(new Course("7234567", "CG", 4, GraduateLevel.Undergraduate.name()),
                "5", new ExamTime("2022-12-21T16:00", "2022-12-21T19:00"), null);
        Section section5 = new Section(new Course("8234567", "CH", 4, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);
        Section section6 = new Section(new Course("8234567", "CH", 4, GraduateLevel.Undergraduate.name()),
                "6", new ExamTime("2022-12-21T14:00", "2022-12-21T17:00"), null);

        enrollmentList.addSections(section1, section2, section3, section4, section5, section6);

        when(student.getGraduateLevel()).thenReturn(GraduateLevel.Undergraduate);
        when(student.calculateGPA()).thenReturn(new Grade(0.0D));
        when(student.getTotalTakenCredits()).thenReturn(10);

        assertEquals(2, enrollmentList.checkValidGPALimit().size());
    }

}