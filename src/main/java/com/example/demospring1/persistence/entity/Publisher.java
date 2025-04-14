package com.example.demospring1.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "publisher",
        indexes = @Index(name = "idx_publisher_name", columnList = "name"))
@Setter
@Getter
@ToString
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "publisher")
    private List<BookPublisher> publishers;
}
