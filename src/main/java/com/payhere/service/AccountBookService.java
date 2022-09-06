package com.payhere.service;

import com.payhere.dto.requestDto.AccountBookRequestDto;
import com.payhere.dto.responseDto.AccountBookResponseDto;
import com.payhere.model.AccountBook;
import com.payhere.repository.AccountBookRepository;
import com.payhere.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountBookService {
    private final AccountBookRepository accountBookRepository;

    // 가계부 생성
    public ResponseEntity createAccountBook(AccountBookRequestDto requestDto, UserDetailsImpl userDetails) {
        AccountBook accountBook = AccountBook.builder()
                .username(userDetails.getUsername())
                .memo(requestDto.getMemo())
                .income(requestDto.getIncome())
                .expense(requestDto.getExpense())
                .build();
        accountBookRepository.save(accountBook);

        return new ResponseEntity("등록성공", HttpStatus.OK);
    }


    // 해당 유저가 작성한 가계부 전체 조회
    public List<AccountBookResponseDto> getAccountBooks(UserDetailsImpl userDetails) {
        List<AccountBook> accountBooks = accountBookRepository.findAllByUsernameOrderByModifiedAtDesc(userDetails.getUsername());
        return makeList(accountBooks);
    }

    // 특정 가계부 조회
    public AccountBookResponseDto getAccountBook(Long id, UserDetailsImpl userDetails) {
        AccountBook accountBook = accountBookRepository.findByIdAndUsername(id, userDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("해당 가계부가 없습니다.")
        );
        return AccountBookResponseDto.fromAccountBook(accountBook);
    }


    // AccountBook -> AccountBookResponseDto
    public List<AccountBookResponseDto> makeList(List<AccountBook> accountBookList) {
        List<AccountBookResponseDto> accountBookResponseDtos = new ArrayList<>();
        for (AccountBook accountBook : accountBookList) {
            AccountBookResponseDto accountBookResponseDto = AccountBookResponseDto.fromAccountBook(accountBook);
            accountBookResponseDtos.add(accountBookResponseDto);
        }
        return accountBookResponseDtos;
    }

    // 가계부 수정
    @Transactional
    public ResponseEntity updateAccountBook(Long id, AccountBookRequestDto requestDto, String username) {
        AccountBook accountBook = accountBookRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 가계부가 없습니다.")
        );
        if(accountBook.getUsername().equals(username)) {
            accountBook.update(requestDto, id);
            accountBookRepository.save(accountBook);
            return new ResponseEntity("수정성공", HttpStatus.OK);
        }else{
            throw new IllegalArgumentException("해당 가계부를 수정할 권한이 없습니다.");
        }
    }

    //중복 검색 방지용 출처: https://howtodoinjava.com/java8/java-stream-distinct-examples/
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    // 가계부 검색
    public List<AccountBookResponseDto> searchAccountBookByKeyword(String keyword, UserDetailsImpl userDetails) {
        List<AccountBook> accountBooksByMemo = accountBookRepository.findByUsernameAndMemoContaining(keyword, userDetails.getUsername());
        List<AccountBook> accountBooksByIncome = accountBookRepository.findByUsernameAndIncomeContaining(keyword, userDetails.getUsername());
        List<AccountBook> accountBooksByExpense = accountBookRepository.findByUsernameAndExpenseContaining(keyword, userDetails.getUsername());
        List<AccountBookResponseDto> accountBookResponseDtos = new ArrayList<>();
        accountBookResponseDtos.addAll(makeList(accountBooksByMemo));
        accountBookResponseDtos.addAll(makeList(accountBooksByIncome));
        accountBookResponseDtos.addAll(makeList(accountBooksByExpense));

        return accountBookResponseDtos.stream()
                .filter(distinctByKey(p -> p.getId())) // 중복되는 id값 제거
                .collect(Collectors.toList());
    }

    // 가계부 삭제
    public ResponseEntity deleteAccountBook(Long id, String username) {
        AccountBook accountBook = accountBookRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 가계부가 없습니다.")
        );
        if(accountBook.getUsername().equals(username)) {
            accountBookRepository.delete(accountBook);
            return new ResponseEntity("삭제성공", HttpStatus.OK);
        }else{
            throw new IllegalArgumentException("해당 가계부를 삭제할 권한이 없습니다.");
        }
    }
}
