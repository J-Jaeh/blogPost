package com.sparta.blogpost.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageResponseDto {
    private String message;
    private int statusCode;

    public MessageResponseDto(String message, int statusCode){
        this.message = message;
        this.statusCode = statusCode;
    }
    public MessageResponseDto(String message){
        this.message = message;
    }
}

//질문사항 1 왜 두개지 ?

//
