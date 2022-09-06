package com.payhere.controller;

import com.payhere.dto.requestDto.AccountBookRequestDto;
import com.payhere.dto.responseDto.AccountBookResponseDto;
import com.payhere.security.UserDetailsImpl;
import com.payhere.service.AccountBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountBookController {
    private final AccountBookService accountBookService;

    // 가계부 생성
    @PostMapping("/api/accountBook")
    public ResponseEntity createAccountBook(@RequestBody AccountBookRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(accountBookService.createAccountBook(requestDto, userDetails));
    }

    // 해당 유저가 작성한 가계부 전체 조회
    @GetMapping("/api/accountBooks")
    public ResponseEntity getAccountBooks(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(accountBookService.getAccountBooks(userDetails));
    }

    // 특정 가계부 조회
    @GetMapping("/api/accountBook/{id}")
    public ResponseEntity getAccountBook(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(accountBookService.getAccountBook(id, userDetails));
    }

    // 검색어(memo, income, expense)로 가계부 조회
    @GetMapping("/api/accountBook/search")
    public ResponseEntity<List<AccountBookResponseDto>> searchAccountBook(@RequestParam(required = false) String keyword, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(keyword != null) {
            return ResponseEntity.ok().body(accountBookService.searchAccountBookByKeyword(keyword, userDetails));
        } else {
            return ResponseEntity.ok().body(accountBookService.getAccountBooks(userDetails));
        }
    }

    // 가계부 수정
    @PutMapping("/api/accountBook/{id}")
    public ResponseEntity updateAccountBook(@PathVariable Long id, @RequestBody AccountBookRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(accountBookService.updateAccountBook(id, requestDto, userDetails.getUsername()));
    }

    // 가계부 삭제
    @DeleteMapping("/api/accountBook/{id}")
    public ResponseEntity deleteAccountBook(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(accountBookService.deleteAccountBook(id, userDetails.getUsername()));
    }
}
