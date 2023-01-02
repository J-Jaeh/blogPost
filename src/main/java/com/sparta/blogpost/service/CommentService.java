package com.sparta.blogpost.service;


import com.sparta.blogpost.dto.*;
import com.sparta.blogpost.entity.Comment;
import com.sparta.blogpost.entity.Post;
import com.sparta.blogpost.entity.User;
import com.sparta.blogpost.enums.UserRoleEnum;
import com.sparta.blogpost.jwt.JwtUtil;
import com.sparta.blogpost.repository.CommentRepository;
import com.sparta.blogpost.repository.PostRepository;
import com.sparta.blogpost.repository.UserRepository;
import io.jsonwebtoken.Claims;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    /*
        두개의 저장소(Post, User) Repository 가 필요한 이유.. 어떤 게시글인지 알고 연결해야하고
        현재 사용자가 권한이 있는지도 알아야하니 연관된 두가지 저장소가 필요한다.
    */
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;


    //댓글 생성
    @Transactional
    public ResponseDto<CommentResponseDto> createComment(CommentRequestDto requestDto, User user, Long id) {
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//        // 토큰이 있는 경우에만 글 작성 가능
//        if (token != null) {
//            // 토큰 검증
//            if (jwtUtil.validateToken(token)) {
//                // 토큰에서 사용자 정보 가져오기
//                claims = jwtUtil.getUserInfoFromToken(token);
//            }else {
//                return new ResponseDto<>("사용자 정보가 만료되었습니다.", 400);
//            }
//            Optional<User> user = userRepository.findByUsername(claims.getSubject());
//            if(user.isEmpty()) {
//                return new ResponseDto<>("해당 게시글에 수정 대한 권한이 없습니다.", 400);
//            }
//        Optional<Post> post = postRepository.findById(id);
//        if(post.isEmpty()) {
////                return new MessageResponseDto("존재하지 않는 게시글입니다.", HttpStatus.FAILED_DEPENDENCY.value());
//            return new ResponseDto<>("존재하지 않는 게시글입니다.", 400);
//        }
        Post post = postRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("이미 삭제된 게시글입니다.")
        );
        Comment comment = new Comment(requestDto, user.getUsername());
        comment.addUserAndPost(user, post);
        commentRepository.save(comment);
        return new ResponseDto<>(new CommentResponseDto(comment));
//        }else {
//            return new ResponseDto<>("존재하지 않는 토큰입니다.", 400);
//        }
    }


    //댓글 수정
    @Transactional
    public MessageResponseDto updateComment(CommentRequestDto requestDto, User user, Long id) {
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//        if (token != null) {
//            if (jwtUtil.validateToken(token)) {
//                claims = jwtUtil.getUserInfoFromToken(token);
//            } else {
//                return new MessageResponseDto("토큰이 틀렸습니다.", 400);
//            }
//            Optional<User> user = userRepository.findByUsername(claims.getSubject());
//            if (user.isEmpty()) {
//                return new MessageResponseDto("해당 게시글에 수정 대한 권한이 없습니다.", 400);
//            }
            //id만 가져가니까. 로그인하면 그냥 제거하고 수정한다.
//        Optional<Comment> comment = commentRepository.findById(id);
//        if(comment.isEmpty()) {
//            MessageResponseDto msg = new MessageResponseDto("해당 게시글이 없습니다.", 400);
//            return msg;
//        }
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 삭제되어 존재하지 않습니다.")
        );
        if (user.getRole() == UserRoleEnum.ADMIN || comment.isWriter(user.getUsername())) {
            comment.update(requestDto);
            MessageResponseDto msg = new MessageResponseDto("업데이트 성공", HttpStatus.OK.value());
            return msg;
        }
//        return null;
        MessageResponseDto msg = new MessageResponseDto("토큰이 만료되었습니다.", HttpStatus.FAILED_DEPENDENCY.value());
        return msg;
    }


    //댓글 삭제
    @Transactional
    public MessageResponseDto deleteComment(User user, Long id){
//        String token = jwtUtil.resolveToken(request);
//        Claims claims;
//
//        if (token != null) {
//            if (jwtUtil.validateToken(token)) {
//                claims= jwtUtil.getUserInfoFromToken(token);
//            }else {
//                throw new IllegalArgumentException("token error");
//            }
//            //토큰에서 가져온 사용자 정보를 사용하여 DB 조회 -> 로그인 안했으면 로그인 하라고 메시지
//            //id만 가져가니까. 로그인하면 그냥 제거하고 수정한다.
//            Optional<User> user = userRepository.findByUsername(claims.getSubject());
//            if(user.isEmpty()) {
//                MessageResponseDto msg = new MessageResponseDto("해당 게시글에 수정 대한 권한이 없습니다.", 400);
//                return msg;
//            }
//        Optional<Comment> comment = commentRepository.findById(id);
//        if(comment.isEmpty()) {
//            MessageResponseDto msg = new MessageResponseDto("해당 게시글이 없습니다.", 400);
//            return msg;
//        }
//        if (comment.get().isWriter(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)){
//            commentRepository.deleteById(comment.get().getId());
//            MessageResponseDto msg = new MessageResponseDto("삭제 성공", HttpStatus.OK.value());
//            return msg;
//        }
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 삭제되어 존재하지 않습니다.")
        );
        if (comment.isWriter(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN)){
            commentRepository.deleteById(comment.getId());
            MessageResponseDto msg = new MessageResponseDto("삭제 성공", HttpStatus.OK.value());
            return msg;
        }

        MessageResponseDto msg = new MessageResponseDto("삭제실패", HttpStatus.FAILED_DEPENDENCY.value());
        return msg;
    }
}
