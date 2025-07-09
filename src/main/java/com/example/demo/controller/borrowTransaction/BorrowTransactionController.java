package com.example.demo.controller.borrowTransaction;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.borrowTransaction.BorrowTransactionDTO;
import com.example.demo.request.borrowTransaction.BorrowTransactionRequest;
import com.example.demo.service.borrowTransaction.BorrowTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
public class BorrowTransactionController {
    private final BorrowTransactionService borrowTransactionService;
    public BorrowTransactionController(BorrowTransactionService borrowTransactionService) {
        this.borrowTransactionService = borrowTransactionService;
    }

    @PostMapping("borrow")
    public ResponseEntity<ApiResponse<BorrowTransactionDTO>> borrowBook(@Valid @RequestBody BorrowTransactionRequest borrowTransactionRequest) {
        BorrowTransactionDTO borrowTransaction = borrowTransactionService.borrowBook(borrowTransactionRequest);
        ApiResponse<BorrowTransactionDTO> response = new ApiResponse<>("Book borrowed successfully", "success", HttpStatus.CREATED.value(), borrowTransaction);
        return new ResponseEntity<ApiResponse<BorrowTransactionDTO>>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("{id}/return")
    public ResponseEntity<ApiResponse<BorrowTransactionDTO>> returnBook(@PathVariable Long id) {
        BorrowTransactionDTO borrowTransaction = borrowTransactionService.returnBook(id);
        ApiResponse<BorrowTransactionDTO> response = new ApiResponse<>("Book returned successfully", "success", HttpStatus.OK.value(), borrowTransaction);
        return new ResponseEntity<ApiResponse<BorrowTransactionDTO>>(response, HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<BorrowTransactionDTO>>> getTransactions() {
        List<BorrowTransactionDTO> borrowTransactions = borrowTransactionService.getTransactions();
        ApiResponse<List<BorrowTransactionDTO>> response = new ApiResponse<>("Transactions retrieved successfully", "success", HttpStatus.OK.value(), borrowTransactions);
        return new ResponseEntity<ApiResponse<List<BorrowTransactionDTO>>>(response, HttpStatus.OK);
    }
    
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<BorrowTransactionDTO>> getTransaction(@PathVariable Long id) {
        BorrowTransactionDTO borrowTransaction = borrowTransactionService.getTransaction(id);
        ApiResponse<BorrowTransactionDTO> response = new ApiResponse<>("Transaction retrieved successfully", "success", HttpStatus.OK.value(), borrowTransaction);
        return new ResponseEntity<ApiResponse<BorrowTransactionDTO>>(response, HttpStatus.OK);
    }
}
