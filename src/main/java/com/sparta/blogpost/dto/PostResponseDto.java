package com.sparta.blogpost.dto;
import com.sparta.blogpost.entity.Comment;
import com.sparta.blogpost.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto{
        private Long id;
        private String title;
        private String content;
        private String username;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        private List<CommentResponseDto> commentList;
        public PostResponseDto (Post post) {
            this.id = post.getId();
            this.createdDate = post.getCreatedDate();
            this.modifiedDate = post.getModifiedDate();
            this.title = post.getTitle();
            this.username = post.getUsername();
            this.content = post.getContent();
        }
        public PostResponseDto (Post post, List<CommentResponseDto> comment) {
            this.id = post.getId();
            this.createdDate = post.getCreatedDate();
            this.modifiedDate = post.getModifiedDate();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.username = post.getUsername();
            this.commentList = comment;
        }

    public void addCommentList(List<CommentResponseDto> commentList) {
        this.commentList = commentList;
    }
}

//여기도 왜 두개 오버로딩?