package com.sparta.blogpost.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column
//    private Long Like_count;

    @Column
    private String username;
    @Column
    private Long postId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Post posts;

    @ManyToOne(fetch = FetchType.LAZY)
    private User users;
    public PostLike(Post post, User user){
        this.posts = post;
        this.users = user;
    }
}
