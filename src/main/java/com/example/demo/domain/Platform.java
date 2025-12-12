package com.example.demo.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "platform")
@Entity
@Getter @Setter @NoArgsConstructor
public class Platform {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "platform_id")
    private Long id;

    @Column(name = "name" , nullable = false)
    private String name; // 지상, 공중, 해상
}
