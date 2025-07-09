package com.example.demo.controller.borrowTransaction;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.borrowTransaction.BorrowTransactionDTO;
import com.example.demo.request.borrowTransaction.BorrowTransactionRequest;
import com.example.demo.service.borrowTransaction.BorrowTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
@Tag(name = "Borrow Transactions", description = "Borrow transactions operations")
public class BorrowTransactionController {
    private final BorrowTransactionService borrowTransactionService;
    public BorrowTransactionController(BorrowTransactionService borrowTransactionService) {
        this.borrowTransactionService = borrowTransactionService;
    }

    @PostMapping("borrow")
    @Operation(summary = "Borrow a book", description = "Borrow a book")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Book borrowed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Borrow conflict: Member is not active, book is unavailable, member has reached borrow limit, or member already borrowed this book")
    })
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
    @Operation(summary = "Get all transactions", description = "Retrieve a list of all transactions")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
    })
    public ResponseEntity<ApiResponse<List<BorrowTransactionDTO>>> getTransactions() {
        List<BorrowTransactionDTO> borrowTransactions = borrowTransactionService.getTransactions();
        ApiResponse<List<BorrowTransactionDTO>> response = new ApiResponse<>("Transactions retrieved successfully", "success", HttpStatus.OK.value(), borrowTransactions);
        return new ResponseEntity<ApiResponse<List<BorrowTransactionDTO>>>(response, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get a transaction by ID", description = "Retrieve details of a specific transaction by its ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transaction retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    public ResponseEntity<ApiResponse<BorrowTransactionDTO>> getTransaction(@PathVariable Long id) {
        BorrowTransactionDTO borrowTransaction = borrowTransactionService.getTransaction(id);
        ApiResponse<BorrowTransactionDTO> response = new ApiResponse<>("Transaction retrieved successfully", "success", HttpStatus.OK.value(), borrowTransaction);
        return new ResponseEntity<ApiResponse<BorrowTransactionDTO>>(response, HttpStatus.OK);
    }
}
