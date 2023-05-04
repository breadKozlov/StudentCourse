package by.kozlov.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "trainers", schema = "public")
@ToString(exclude = "courses")
@EqualsAndHashCode(exclude = "courses")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private String email;

    @Builder.Default
    @ManyToMany(mappedBy = "trainers")
    private List<Course> courses = new ArrayList<>();
}
