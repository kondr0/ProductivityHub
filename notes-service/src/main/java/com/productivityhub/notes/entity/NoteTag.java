package com.productivityhub.notes.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "note_tags", schema = "notes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class NoteTag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(length = 16)
    private String color;
}
