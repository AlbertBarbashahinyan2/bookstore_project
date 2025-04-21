package com.example.demospring1.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "setting",
        indexes = @Index(name = "idx_setting_name", columnList = "name"))
@Setter
@Getter
@ToString
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "setting", cascade = CascadeType.PERSIST)
    private List<BookSetting> settings;
}
