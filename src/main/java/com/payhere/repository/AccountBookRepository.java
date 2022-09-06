package com.payhere.repository;

import com.payhere.model.AccountBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long> {
    List<AccountBook> findAllByUsernameOrderByModifiedAtDesc(String username);
    Optional<AccountBook> findByIdAndUsername(Long id, String username);
    List<AccountBook> findByUsernameAndMemoContaining(String keyword, String username);
    List<AccountBook> findByUsernameAndIncomeContaining (String keyword,String username);
    List<AccountBook> findByUsernameAndExpenseContaining (String keyword, String username);
}
