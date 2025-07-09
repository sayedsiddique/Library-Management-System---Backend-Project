package com.example.demo.controller.member;

import com.example.demo.dto.book.BookDTO;
import com.example.demo.dto.borrowTransaction.BorrowTransactionDTO;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.service.book.BookService;
import com.example.demo.service.borrowTransaction.BorrowTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members/{id}/borrowed-books")
@Tag(name = "Members", description = "Member management operations")
public class MemberBorrowTransactionController {
    private final BorrowTransactionService borrowTransactionService;

    public MemberBorrowTransactionController(BorrowTransactionService borrowTransactionService) {
        this.borrowTransactionService = borrowTransactionService;
    }

    @GetMapping()
    @Operation(summary = "Get all member's transactions", description = "Retrieve a list of all member's transactions")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
    })
    public ResponseEntity<ApiResponse<List<BorrowTransactionDTO>>> getMemberBorrowTransactions(@PathVariable("id")Long id) {
        List<BorrowTransactionDTO> borrowTransactions = borrowTransactionService.getMemberBorrowTransactions(id);
        ApiResponse<List<BorrowTransactionDTO>> response = new ApiResponse<>("Transactions retrieved successfully", "success", HttpStatus.OK.value(), borrowTransactions);
        return new ResponseEntity<ApiResponse<List<BorrowTransactionDTO>>>(response, HttpStatus.OK);
    }
}
