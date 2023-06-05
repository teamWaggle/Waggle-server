package com.example.waggle.service.member;

import com.example.waggle.dto.member.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface MemberService {
    MemberDto signIn(SignInDto signInDto);
    MemberDto signUp(SignUpDto signUpDto);
    void signOut(HttpSession session);
}
