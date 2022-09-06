package com.payhere.model;

import com.payhere.dto.requestDto.AccountBookRequestDto;
import com.payhere.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class AccountBook extends Timestamped{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String username;

    @Column
    private String memo;

    @Column
    private int totalAsset;

    @Column
    private int income;

    @Column
    private int expense;

    public AccountBook(AccountBookRequestDto requestDto, UserDetailsImpl userDetails, int totalAsset) {
        this.username = userDetails.getUsername();
        this.memo = requestDto.getMemo();
        this.totalAsset = totalAsset + requestDto.getIncome() - requestDto.getExpense();
        this.income = requestDto.getIncome();
        this.expense = requestDto.getExpense();
    }

    public void update(AccountBookRequestDto requestDto, Long id) {
        this.memo = requestDto.getMemo();
        this.income = requestDto.getIncome();
        this.expense = requestDto.getExpense();
        this.id = id;
    }
}
