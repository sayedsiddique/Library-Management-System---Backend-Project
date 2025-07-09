package com.example.demo.controller;

import com.example.demo.jobs.borrowTransaction.BorrowTransactionJob;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestJobController {

    private final BorrowTransactionJob borrowTransactionJob;

    public TestJobController(BorrowTransactionJob borrowTransactionJob) {
        this.borrowTransactionJob = borrowTransactionJob;
    }

    @GetMapping("run-due-date-job")
    public String runDueDateJobNow() {
        borrowTransactionJob.sendDueDateReminders();
        return "Due date reminder job triggered successfully!";
    }
}
