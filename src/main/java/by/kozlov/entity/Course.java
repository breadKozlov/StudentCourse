package by.kozlov.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(exclude = {"students","trainers"})
@EqualsAndHashCode(exclude = {"students","trainers"})
@Table(name = "courses", schema = "public")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double cost;
    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Student> students = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "trainers_courses",
    joinColumns = @JoinColumn(name = "id_course"),
    inverseJoinColumns = @JoinColumn(name = "id_trainer"))
    private List<Trainer> trainers = new ArrayList<>();


    public void addTrainer(Trainer trainer) {
        trainers.add(trainer);
        trainer.getCourses().add(this);
    }

    public void addStudent(Student student) {
        students.add(student);
        student.setCourse(this);
    }
}
