package com.payhere.dto.requestDto;

import lombok.Getter;

@Getter
public class AccountBookRequestDto {
    private String memo;
    private int income;
    private int expense;
}
