package by.kozlov.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "student_profile", schema = "public")
public class StudentProfile {

    @Id
    @Column(name = "id_student")
    private Long id;
    @Column(name = "average_score")
    private Double averageScore;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id_student")
    private Student student;

    public void setStudent(Student student) {
        student.setStudentProfile(this);
        this.student = student;
    }

}
