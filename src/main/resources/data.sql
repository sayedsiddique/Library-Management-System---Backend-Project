-- Insert sample authors (5 authors)
-- Insert sample authors (5 authors)
INSERT INTO `authors` (`name`, `email`, `bio`, `birth_date`, `created_at`, `updated_at`) VALUES
('J.K. Rowling', 'jkrowling@example.com', 'British author, best known for the Harry Potter series.', '1965-07-31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('George Orwell', 'georgeorwell@example.com', 'English novelist and essayist, journalist and critic.', '1903-06-25', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jane Austen', 'janeausten@example.com', 'English novelist known primarily for her six major novels.', '1775-12-16', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mark Twain', 'marktwain@example.com', 'American writer, humorist, and lecturer.', '1835-11-30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Harper Lee', 'harperlee@example.com', 'American novelist best known for her novel To Kill a Mockingbird.', '1926-04-28', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample books (15 books)
INSERT INTO `books` (`title`, `isbn`, `publication_year`, `available_copies`, `total_copies`, `created_at`, `updated_at`) VALUES
('Harry Potter and the Philosopher''s Stone', '9780747532743', 1997, 5, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('1984', '9780451524935', 1949, 3, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Pride and Prejudice', '9781503290563', 1813, 4, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('The Adventures of Tom Sawyer', '9780486400770', 1876, 6, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('To Kill a Mockingbird', '9780061120084', 1960, 2, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('The Great Gatsby', '9780743273565', 1925, 4, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Murder on the Orient Express', '9780062693662', 1934, 5, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Romeo and Juliet', '9780743477116', 1597, 6, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('War and Peace', '9781400079988', 1869, 2, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('The Catcher in the Rye', '9780316769488', 1951, 3, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Brave New World', '9780060850524', 1932, 4, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('The Grapes of Wrath', '9780143039433', 1939, 2, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('The Hobbit', '9780261102217', 1937, 4, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Wuthering Heights', '9780141439556', 1847, 5, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('The Picture of Dorian Gray', '9780141439570', 1890, 2, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample members (30 members)
INSERT INTO `members` (`name`, `email`, `phone`, `status`, `membership_date`, `created_at`, `updated_at`) VALUES
('Alice Johnson', 'alice.johnson@example.com', '123-456-7890', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bob Smith', 'bob.smith@example.com', '234-567-8901', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Charlie Brown', 'charlie.brown@example.com', '345-678-9012', 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('David Green', 'david.green@example.com', '456-789-0123', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Eva Adams', 'eva.adams@example.com', '567-890-1234', 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Frank Harris', 'frank.harris@example.com', '678-901-2345', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Grace Lee', 'grace.lee@example.com', '789-012-3456', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hannah Wilson', 'hannah.wilson@example.com', '890-123-4567', 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ian Scott', 'ian.scott@example.com', '901-234-5678', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jack Thomas', 'jack.thomas@example.com', '012-345-6789', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Karen Lopez', 'karen.lopez@example.com', '123-456-7890', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Liam Walker', 'liam.walker@example.com', '234-567-8901', 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Megan Harris', 'megan.harris@example.com', '345-678-9012', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Nina Young', 'nina.young@example.com', '456-789-0123', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Oscar King', 'oscar.king@example.com', '567-890-1234', 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Paul Martinez', 'paul.martinez@example.com', '678-901-2345', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Quincy Roberts', 'quincy.roberts@example.com', '789-012-3456', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Rachel White', 'rachel.white@example.com', '890-123-4567', 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sam Carter', 'sam.carter@example.com', '901-234-5678', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tina Clark', 'tina.clark@example.com', '012-345-6789', 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ursula Nelson', 'ursula.nelson@example.com', '123-456-7890', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Vince Cooper', 'vince.cooper@example.com', '234-567-8901', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Will Foster', 'will.foster@example.com', '345-678-9012', 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Xander Young', 'xander.young@example.com', '456-789-0123', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Yara Morgan', 'yara.morgan@example.com', '567-890-1234', 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Zane Mitchell', 'zane.mitchell@example.com', '678-901-2345', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Adam Foster', 'adam.foster@example.com', '789-012-3456', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ben Allen', 'ben.allen@example.com', '890-123-4567', 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cleo Ross', 'cleo.ross@example.com', '901-234-5678', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dina Long', 'dina.long@example.com', '012-345-6789', 'INACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Eli King', 'eli.king@example.com', '123-456-7890', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert author-book relationships (randomized with 1-3 authors per book)
-- Harry Potter and the Philosopher's Stone - J.K. Rowling
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (1, 1);
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (1, 3);

-- 1984 - George Orwell
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (2, 2);

-- Pride and Prejudice - Jane Austen
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (3, 3);

-- The Adventures of Tom Sawyer - Mark Twain
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (4, 4);

-- To Kill a Mockingbird - Harper Lee
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (5, 5);

-- The Great Gatsby - F. Scott Fitzgerald
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (6, 1);

-- Murder on the Orient Express - Agatha Christie
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (7, 4);

-- Romeo and Juliet - William Shakespeare
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (8, 1);
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (8, 2);

-- War and Peace - Leo Tolstoy
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (9, 3);

-- The Catcher in the Rye - J.D. Salinger
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (10, 4);

-- Brave New World - Aldous Huxley
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (11, 2);

-- The Grapes of Wrath - John Steinbeck
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (12, 5);

-- The Hobbit - J.R.R. Tolkien
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (13, 1);

-- Wuthering Heights - Emily Brontë
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (14, 3);

-- The Picture of Dorian Gray - Oscar Wilde
INSERT INTO `author_book` (`book_id`, `author_id`) VALUES (15, 4);
