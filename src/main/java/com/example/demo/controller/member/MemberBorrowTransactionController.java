package com.example.demo.controller.member;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.borrowTransaction.BorrowTransactionDTO;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.book.BookService;
import com.example.demo.service.borrowTransaction.BorrowTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members/{id}/borrowed-books")
public class MemberBorrowTransactionController {
    private final BorrowTransactionService borrowTransactionService;

    public MemberBorrowTransactionController(BorrowTransactionService borrowTransactionService) {
        this.borrowTransactionService = borrowTransactionService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<BorrowTransactionDTO>>> getMemberBorrowTransactions(@PathVariable("id")Long id) {
        List<BorrowTransactionDTO> borrowTransactions = borrowTransactionService.getMemberBorrowTransactions(id);
        ApiResponse<List<BorrowTransactionDTO>> response = new ApiResponse<>("Transactions retrieved successfully", "success", HttpStatus.OK.value(), borrowTransactions);
        return new ResponseEntity<ApiResponse<List<BorrowTransactionDTO>>>(response, HttpStatus.OK);
    }
}
