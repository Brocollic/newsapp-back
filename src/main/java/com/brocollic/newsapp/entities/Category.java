package com.brocollic.newsapp.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "label cannot be null and it's trimmed length must greater than zero.")
    @Size(max = 30, message = "label cannot be longer than 30 characters.")
    @Column(unique = true)
    private String label;

    @NotBlank(message = "description cannot be null and it's trimmed length must greater than zero.")
    @Size(max = 250, message = "description cannot be longer than 250 characters.")
    private String description;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Article> articles;

}
