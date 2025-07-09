package com.example.demo.jobs.borrowTransaction;

import com.example.demo.dto.borrowTransaction.BorrowTransactionDTO;
import com.example.demo.jobs.mail.MailJob;
import com.example.demo.model.borrowTransaction.BorrowTransaction;
import com.example.demo.repository.borrowTransaction.BorrowTransactionRepository;
import com.example.demo.service.borrowTransaction.BorrowTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BorrowTransactionJob {
    @Autowired
    private BorrowTransactionService borrowTransactionService;

    @Autowired
    private MailJob mailJob;

    @Scheduled(cron = "0 59 23 * * ?")
    public void sendDueDateReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<BorrowTransactionDTO> transactions = borrowTransactionService.getTransactionsByDueDate(tomorrow);

        for (BorrowTransactionDTO transaction : transactions) {
            String to = transaction.memberEmail();
            String subject = "Reminder: Item due tomorrow!";
            String body = "Hi,\n\nThis is a reminder that the item '" + transaction.bookTitle() +
                    "' is due on " + transaction.dueDate() + ".\n\nPlease return it on time.\n\nThanks.";

            mailJob.sendEmail(to, subject, body);
        }

        System.out.println("Sent " + transactions.size() + " due date reminder(s) for " + tomorrow);
    }
}
