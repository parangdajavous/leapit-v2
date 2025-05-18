package com.example.leapit.common.techstack;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "tech_stack_tb")
@Entity
public class TechStack {
    @Id
    private String code; // 예: java
}
