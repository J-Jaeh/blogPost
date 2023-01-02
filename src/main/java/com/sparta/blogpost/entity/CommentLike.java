package com.sparta.blogpost.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//
//    @Column
//    private Long Like_count;

    @Column
    private String username;
    @Column
    private Long commentId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comments;
    @ManyToOne(fetch = FetchType.LAZY)
    private User users;
    public CommentLike(Comment comment, User user) {
        this.comments = comment;
        this.users = user;
    }
}
