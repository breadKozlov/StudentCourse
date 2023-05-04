package by.kozlov.entity;


import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "students", schema = "public")
@ToString(exclude = {"course","studentProfile"})
@EqualsAndHashCode(exclude = {"course","studentProfile"})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private String email;
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "id_course")
    private Course course;
    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private StudentProfile studentProfile;
}
