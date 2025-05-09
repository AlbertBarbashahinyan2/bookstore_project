package com.example.demospring1.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "award",
        indexes = @Index(name = "idx_award_name", columnList = "name"))
@Setter
@Getter
@ToString
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "award", cascade = CascadeType.PERSIST)
    private List<BookAward> awards;

}
