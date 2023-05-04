package by.kozlov;

import by.kozlov.entity.Course;
import by.kozlov.entity.Student;
import by.kozlov.entity.StudentProfile;
import by.kozlov.entity.Trainer;
import by.kozlov.utils.HibernateUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
class HibernateRunnerTest {

    @Test
    public void createAndSaveSomeCourses() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var course = Course.builder().name("Java Enterprise").description("Power course of Java EE, Spring and Hibernate")
                .cost(1000.12).build();

        var course1 = Course.builder().name("Java Basic").description("Power course of Java SE and it fundamental rules")
                .cost(856.35).build();

        var course2 = Course.builder().name("Android Development").description("Course about develop of application on Android")
                .cost(900.3).build();

        session.save(course);
        session.save(course1);
        session.save(course2);

        session.getTransaction().commit();
    }

    @Test
    public void createAndAddSomeStudents() {

        var FIND_BY_NAME = "FROM Course C WHERE C.name = :name";

        var session = HibernateUtil.getSession();
        session.beginTransaction();

        var student1 = Student.builder().name("Pavel").surname("Ivanov").age(33)
                .email("killer@tut.by").build();
        var student2 = Student.builder().name("Sergei").surname("Petrov").age(13)
                .email("pups@tut.by").build();
        var student3 = Student.builder().name("Olga").surname("Smirnova").age(23)
                .email("kipa@gmail.com").build();
        var student4 = Student.builder().name("Vladimir").surname("Sidorov").age(87)
                .email("sliva@yandex.ru").build();
        var student5 = Student.builder().name("Petr").surname("Chikov").age(44)
                .email("kopito@mail.ru").build();
        var student6 = Student.builder().name("Alexei").surname("Suslov").age(91)
                .email("pixel@gmail.com").build();
        var student7 = Student.builder().name("Oleg").surname("Pipetkin").age(33)
                .email("rufik@tut.by").build();
        var student8 = Student.builder().name("Pavel").surname("Liliputov").age(33)
                .email("aurora@mail.ru").build();

        var course1 = (Course)session.createQuery(FIND_BY_NAME).setParameter("name","Java Enterprise")
                .list().stream().findFirst().orElseThrow();
        var course2 = (Course)session.createQuery(FIND_BY_NAME).setParameter("name","Java Basic")
                .list().stream().findFirst().orElseThrow();
        var course3 = (Course)session.createQuery(FIND_BY_NAME).setParameter("name","Android Development")
                .list().stream().findFirst().orElseThrow();

        List<Student> studentList1 = new ArrayList<>();
        studentList1.add(student1);
        studentList1.add(student2);
        studentList1.add(student3);
        studentList1.add(student4);
        studentList1.forEach(course1::addStudent);
        List<Student> studentList2 = new ArrayList<>();
        studentList2.add(student5);
        studentList2.add(student6);
        studentList2.forEach(course2::addStudent);
        List<Student> studentList3 = new ArrayList<>();
        studentList3.add(student7);
        studentList3.add(student8);
        studentList3.forEach(course3::addStudent);

        session.saveOrUpdate(course1);
        session.saveOrUpdate(course2);
        session.saveOrUpdate(course3);

        session.getTransaction().commit();
    }

    @Test
    public void createAndAddStudentsProfiles() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var students = session.createQuery("FROM Student",Student.class).list();

        for(Student student: students) {

            var average = new Random().nextDouble(1,10.1);
            var scale = Math.pow(10,1);
            var result = Math.ceil(average * scale) / scale;
            var studentProfile = StudentProfile.builder().averageScore(result)
                    .student(student).build();

            session.save(studentProfile);
        }

        session.getTransaction().commit();
    }
    @Test
    public void checkOneToOne() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var student = Student.builder().name("Kirill").surname("Ivanov").age(30)
                .email("golum@tut.by").build();

        var course = Course.builder().name("Spring").description("Power course of Spring")
                .cost(345.15).build();

        course.addStudent(student);

        var studentProfile = StudentProfile.builder().averageScore(8.1).build();

        log.info(student.toString());

        session.save(course);
        studentProfile.setStudent(student);

        log.info(student.toString());
        session.getTransaction().commit();
    }

    @Test
    public void checkManyToOne() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var course = Course.builder().name("Java ME").description("Power course of Java ME")
                .cost(345.15).build();

        var student1 = Student.builder().name("Yulia").surname("Petrova").age(30)
                .email("gol@tut.by").build();

        var student2 = Student.builder().name("Igor").surname("Kuplinov").age(14)
                .email("holka@gmail.com").build();

        log.info(course.getStudents().toString());

        session.save(course);
        course.addStudent(student1);
        course.addStudent(student2);

        log.info(course.getStudents().toString());
        session.getTransaction().commit();
    }

    @Test
    public void findAllStudents() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var students = session.createQuery("FROM Student S WHERE S.course.name = :name",Student.class)
                .setParameter("name","Java Enterprise").list();

        System.out.println(students.toString());
        session.getTransaction().commit();
    }

    @Test
    public void deleteAllStudents() {

        var score = 6.0;

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var students = session.createQuery("FROM Student S WHERE S.studentProfile.averageScore < :score",Student.class)
                .setParameter("score",score).list();

        System.out.println(students.toString());

        for (Student student: students) {
            session.delete(student);
        }

        session.getTransaction().commit();
    }

    @Test
    public void addStudent(){

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var course = (Course)session.createQuery("FROM Course C WHERE C.name = :name")
                .setParameter("name","Java Enterprise").list().stream().findFirst().orElseThrow();

        var student = Student.builder().name("Pavel").surname("Kozlov").age(78).email("meta@tut.by").build();

        course.addStudent(student);
        session.getTransaction().commit();
    }

    @Test
    public void deleteCourseJavaEE() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var course = (Course)session.createQuery("FROM Course C WHERE C.name = :name")
                .setParameter("name","Java Enterprise").list().stream().findFirst().orElseThrow();


        session.delete(course);
        session.getTransaction().commit();
    }

    @Test
    public void checkManyToMany() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var trainer1 = Trainer.builder().name("Igor").surname("Ivanov").age(43).build();
        var trainer2 = Trainer.builder().name("Ivan").surname("Hilton").age(32).build();

        var course1 = Course.builder().name("Java Enterprise").description("Power course of Java EE, Spring and Hibernate")
                .cost(1000.12).build();
        var course2 = Course.builder().name("PHP Enterprise").description("Power course of PHP")
                .cost(1000.12).build();

        course1.addTrainer(trainer1);
        course1.addTrainer(trainer2);
        course2.addTrainer(trainer1);

        session.save(course1);
        session.save(course2);
        session.save(trainer1);
        session.save(trainer2);

        session.getTransaction().commit();

    }

    @Test
    public void updateCourse() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var testCourse = Course.builder().name("Java Enterprise").description("Power course of Java EE, Spring and Hibernate")
                .cost(1000.12).build();

        session.createQuery("UPDATE Course C SET C.name = :name, C.description = :description, C.cost = :cost"
        + " WHERE C.id = :id")
                .setParameter("name",testCourse.getName())
                .setParameter("description",testCourse.getDescription())
                .setParameter("cost",testCourse.getCost())
                .setParameter("id",13L)
                .executeUpdate();

        var coursesBeforeUp = session.createQuery("FROM Course C ORDER BY C.id",Course.class).list();
        System.out.println(coursesBeforeUp);

        var course = session.get(Course.class,13L);
        course.setName("Swimming");
        course.setDescription("Nice course for people, who likes to swim");

        session.update(course);
        var coursesAfterUp = session.createQuery("FROM Course C ORDER BY C.id",Course.class).list();

        System.out.println(coursesAfterUp);

        session.getTransaction().commit();
    }

    @Test
    public void deleteCourse() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var testCourse = Course.builder().name("Something course").description("Power course for dreamers")
                .cost(100055.12).build();

        session.save(testCourse);
        session.flush();

        var coursesBeforeDelete = session.createQuery("FROM Course C ORDER BY C.id",Course.class).list();
        System.out.println(coursesBeforeDelete);

        var testCourseDelete = session.createQuery("FROM Course C WHERE C.name = :name",Course.class)
                        .setParameter("name",testCourse.getName()).list().stream().findFirst().orElseThrow();

        System.out.println(testCourseDelete);
        session.delete(testCourseDelete);
        session.flush();

        var coursesAfterDelete = session.createQuery("FROM Course C ORDER BY C.id",Course.class).list();

        System.out.println(coursesAfterDelete);

        session.getTransaction().commit();

    }



}