package com.sparta.blogpost.dto;

import com.sparta.blogpost.entity.User;
import com.sparta.blogpost.enums.UserRoleEnum;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String password;
    private UserRoleEnum role;
    private List<PostResponseDto> posts;
    private List<CommentResponseDto> commentList;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }
    public void addPostList(List<PostResponseDto> postList) {
        this.posts = postList;
    }
    public void addCommentList(List<CommentResponseDto> commentList) {
        this.commentList = commentList;
    }
}
