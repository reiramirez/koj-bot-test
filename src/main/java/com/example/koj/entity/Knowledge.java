package com.example.koj.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity(name = "t_knowledge")
@EqualsAndHashCode
public class Knowledge {
    @Id
    @Column(name = "ID")
    Integer id;
    @Column(name = "Question")
    String question;
    @Column(name = "Answer")
    String answer;
}
