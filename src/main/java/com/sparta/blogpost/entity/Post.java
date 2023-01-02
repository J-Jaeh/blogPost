package com.sparta.blogpost.entity;

import com.sparta.blogpost.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "POST_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
//    @ManyToOne
    private String username;

    @Column(nullable = false)
    private String content;

    private Long likeCount;
    // @JsonIgnore 필드 레벨에서 무시 될 수 있는 속성
    // 데이터를 주고 받을 때 해당 데이터는 결과창에서 응답값에 보이지 않는다.
//    @JsonIgnore
//    @Column(nullable = false)
//    private String password;

    // 댓글을 다 가져오기 위해서
    // 댓글이 게시글을 참조하는 관계 설정
    // 하지 않아도 되기는하는데.. OneToMany로 이 유저가 삭제되었을때
    // 같이 삭제하기 위해서로 삭제하지 않아도 데이터 조회할때는 불러오는 로직이 없으니 상관은 없으나
    // 쓸데없이 많다면 문제가 될수있으니 삭제해준다.
    // 5년이 지나면 삭제의 경우 뭐.. 5년동안 데이터가 사용되지 않으면 삭제된다라는
    // 로직이나.. 언제 삭제되었는지는 넣어두지 않았을까
    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
    private final List<PostLike> postLikeList = new ArrayList<>();

    //게시글 참조하는 User관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User users;


    //양방향에 데이터를 저장해주기 위해서 사용한 메소드
    //그런데 오류나서 죽여뒀다.
    public void addUser(User user){
        this.users =user;
        users.getPosts().add(this);
    }
    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.users = user;
    }

    // 게시글 작성시 입력
    // 요소 1
    public Post(PostRequestDto requestDto, String username, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = username;
        this.users = user;
    }
    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
    // 게시글 작성시 입력
    // 요소 2
    public Post(PostRequestDto requestDto, String username) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = username;
    }
    public boolean isWriter(String username){
        if(Objects.equals(this.username, username)){
            return true;
        }
        return false;
    }
}