--liquibase formatted sql
--changeset jchojdak:3

-- Example users
INSERT INTO `users` (`id`, `active`, `blocked`, `email`, `first_name`, `last_name`, `mobile`, `password`, `registered_at`, `username`) VALUES
    (2, b'1', b'0', 'olivia.garcia@mail.com', 'Olivia', 'Garcia', '+16596160740', 'HQK4nvOsWWua', '2024-02-03 20:43:26.000000', 'olivia_21'),
    (3, b'1', b'0', 'emma.martinez@test.org', 'Emma', 'Martinez', '+96267940191', 'u6iw5cKfQAVO', '2024-06-08 17:03:56.000000', 'emma_22'),
    (4, b'1', b'0', 'ava.lopez@example.com', 'Ava', 'Lopez', '+19377934116', 'uUz$if#GqR97', '2024-03-12 13:35:21.000000', 'ava_23'),
    (5, b'1', b'0', 'mason.wilson@example.com', 'Mason', 'Wilson', '+28769655972', '(107r%)xq0iA', '2024-01-21 18:24:41.000000', 'mason_24'),
    (6, b'1', b'0', 'elijah.smith@test.org', 'Elijah', 'Smith', '+64532728373', 'sdjrvEpubly#', '2024-08-10 04:49:01.000000', 'elijah_25'),
    (7, b'1', b'0', 'elijah.lopez@example.com', 'Elijah', 'Lopez', '+99611888556', '83B1TQhrytgT', '2024-11-01 20:59:09.000000', 'elijah_26'),
    (8, b'1', b'0', 'sophia.garcia@example.com', 'Sophia', 'Garcia', '+66686838801', 'REG37CL6OR83', '2024-05-14 23:38:33.000000', 'sophia_27'),
    (9, b'1', b'0', 'noah.garcia@example.com', 'Noah', 'Garcia', '+35768871676', 'oA%u*v6i0anu', '2024-09-08 19:43:41.000000', 'noah_28'),
    (10, b'1', b'0', 'elijah.jones@test.org', 'Elijah', 'Jones', '+40934801323', '(6BgQuPK^6F%', '2024-09-15 02:26:18.000000', 'elijah_29'),
    (11, b'1', b'0', 'emma.lopez@example.com', 'Emma', 'Lopez', '+53479027325', '@4ud*#bFG2n4', '2024-05-13 21:32:44.000000', 'emma_30'),
    (12, b'1', b'0', 'noah.davis@example.com', 'Noah', 'Davis', '+49745425328', 'h@cfv37Q)#o1', '2024-03-26 07:43:06.000000', 'noah_31'),
    (13, b'1', b'0', 'olivia.martinez@example.com', 'Olivia', 'Martinez', '+50451711597', '!357L3lO&e!H', '2024-03-11 13:56:14.000000', 'olivia_32'),
    (14, b'1', b'0', 'lucas.jones@mail.com', 'Lucas', 'Jones', '+31493635609', '7^#elbaV(mKs', '2024-06-01 18:00:46.000000', 'lucas_33'),
    (15, b'1', b'0', 'isabella.brown@mail.com', 'Isabella', 'Brown', '+35245217526', '0Q3KPtEIAm6h', '2024-03-15 07:29:45.000000', 'isabella_34'),
    (16, b'1', b'0', 'emma.martinez@test.org', 'Emma', 'Martinez', '+21579857850', 'X9J$nj5&5VYw', '2024-10-23 11:09:31.000000', 'emma_35'),
    (17, b'1', b'0', 'mason.brown@example.com', 'Mason', 'Brown', '+79164710772', 'hDPZR$Y&fD&j', '2024-05-29 19:50:46.000000', 'mason_36'),
    (18, b'1', b'0', 'elijah.brown@example.com', 'Elijah', 'Brown', '+96835559626', ')0M7eLCv6iZK', '2024-03-30 01:58:06.000000', 'elijah_37'),
    (19, b'1', b'0', 'sophia.jones@test.org', 'Sophia', 'Jones', '+26128822458', '01nU#1eBDmue', '2024-12-07 01:44:26.000000', 'sophia_38'),
    (20, b'1', b'0', 'emma.brown@test.org', 'Emma', 'Brown', '+51676538639', 'LeAv%uXV9(GG', '2024-03-12 14:57:03.000000', 'emma_39'),
    (21, b'1', b'0', 'olivia.davis@test.org', 'Olivia', 'Davis', '+88352997707', '^PY6%dpWyP!b', '2024-08-14 20:47:44.000000', 'olivia_40');

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
    (2, 1),
    (3, 1),
    (4, 1),
    (5, 1),
    (6, 1),
    (7, 1),
    (8, 1),
    (9, 1),
    (10, 1),
    (11, 1),
    (12, 1),
    (13, 1),
    (14, 1),
    (15, 1),
    (16, 1),
    (17, 1),
    (18, 1),
    (19, 1),
    (20, 1),
    (21, 1);

-- Example categories
INSERT INTO `categories` (`id`, `description`, `name`) VALUES
    (1, 'All about technology', 'Technology'),
    (2, 'All about business and finance', 'Business'),
    (3, 'All about healthy lifestyle', 'Health'),
    (4, 'All about travel', 'Travel'),
    (5, 'All about movies', 'Movies');

-- Example posts
INSERT INTO `posts` (`id`, `content`, `created_at`, `title`, `updated_at`, `author_id`, `category_id`) VALUES
    -- Technology
    (1, 'Hi! I have a quick question, how do I center a div in html? :)', '2024-11-14 20:26:47.000000', 'How to center div in HTML?', '2024-11-14 20:26:47.000000', '15', '1'),
    (2, 'What are the newest trends in AI, and how can beginners dive into machine learning?', '2024-11-15 10:00:00.000000', 'Your First Steps into AI and Machine Learning', '2024-11-15 10:00:00.000000', '2', '1'),
    (3, 'Could someone explain the key differences between Java and JavaScript?', '2024-11-16 14:30:00.000000', 'Java vs. JavaScript: What’s the Difference?', '2024-11-16 14:30:00.000000', '3', '1'),
    (4, 'Which programming languages are most popular for web development this year?', '2024-11-17 08:15:00.000000', 'Best Programming Languages for Web Development in 2024', '2024-11-17 08:15:00.000000', '4', '1'),

    -- Business
    (5, 'What financial strategies should startups focus on in 2024?', '2024-11-15 11:00:00.000000', 'Financial Tips for Startups in 2024', '2024-11-15 11:00:00.000000', '5', '2'),
    (6, 'What are the key elements of a winning business proposal?', '2024-11-16 13:00:00.000000', 'How to Write a Business Proposal That Works', '2024-11-16 13:00:00.000000', '6', '2'),
    (7, 'Where should you invest during an economic downturn?', '2024-11-17 16:00:00.000000', 'Smart Investments During Tough Times', '2024-11-17 16:00:00.000000', '7', '2'),

    -- Health
    (8, 'What are the benefits of switching to a plant-based diet?', '2024-11-15 09:30:00.000000', 'Why You Should Try a Plant-Based Diet', '2024-11-15 09:30:00.000000', '8', '3'),
    (9, 'Can anyone suggest effective home workouts for busy professionals?', '2024-11-16 12:45:00.000000', 'Home Workouts for Busy People', '2024-11-16 12:45:00.000000', '9', '3'),
    (10, 'How can meditation improve mental well-being?', '2024-11-17 18:30:00.000000', 'The Power of Meditation for Mental Health', '2024-11-17 18:30:00.000000', '10', '3'),

    -- Travel
    (11, 'What are the best solo travel destinations in 2024?', '2024-11-15 15:45:00.000000', 'Top Solo Travel Spots for 2024', '2024-11-15 15:45:00.000000', '11', '4'),
    (12, 'How can you save money while traveling abroad?', '2024-11-16 17:00:00.000000', 'Traveling on a Budget: Money-Saving Tips', '2024-11-16 17:00:00.000000', '12', '4'),
    (13, 'What are the best hidden gems to discover in popular tourist spots?', '2024-11-17 20:00:00.000000', 'Discovering Hidden Gems in Popular Destinations', '2024-11-17 20:00:00.000000', '13', '4'),

    -- Movies
    (14, 'Which movies should you not miss this December?', '2024-11-15 14:30:00.000000', 'Top Must-See Movies in December 2024', '2024-11-15 14:30:00.000000', '14', '5'),
    (15, 'Can anyone recommend some underrated indie films?', '2024-11-16 19:15:00.000000', 'Underrated Indie Movies You Need to See', '2024-11-16 19:15:00.000000', '15', '5'),
    (16, 'How has CGI changed the way films are made?', '2024-11-17 21:45:00.000000', 'The Impact of CGI on Modern Cinema', '2024-11-17 21:45:00.000000', '16', '5');

-- Post ID: 1
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('Try using <center> </center> if you want to make your content stand out, it\'s a simple trick. :)', '2024-11-14 20:31:49.000000', '2024-11-14 20:31:49.000000', '7', '1');

-- Post ID: 2
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('Great article! If you\'re just starting, Python with TensorFlow and Keras is a good way to dive into AI.', '2024-11-15 10:10:00.000000', '2024-11-15 10:10:00.000000', '10', '2'),
    ('AI can feel overwhelming at first, but just take it step by step. Working with real datasets is really helpful for learning.', '2024-11-15 10:20:00.000000', '2024-11-15 10:20:00.000000', '11', '2');

-- Post ID: 3
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('Java and JavaScript are totally different. Java is used for backend stuff, while JavaScript is for making websites interactive!', '2024-11-16 14:35:00.000000', '2024-11-16 14:35:00.000000', '12', '3'),
    ('A quick way to remember: Java is more complex, used for bigger projects, while JavaScript makes websites come alive with interactivity.', '2024-11-16 14:40:00.000000', '2024-11-16 14:40:00.000000', '13', '3');

-- Post ID: 4
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('React.js is definitely one of the top choices for web development. It’s fast and has a huge community for support!', '2024-11-17 08:20:00.000000', '2024-11-17 08:20:00.000000', '14', '4'),
    ('Node.js is great for building fast and scalable backends, especially if you already know JavaScript.', '2024-11-17 08:30:00.000000', '2024-11-17 08:30:00.000000', '15', '4');

-- Post ID: 5
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('Building a steady cash flow and having an emergency fund is key to navigating tough financial times.', '2024-11-15 11:10:00.000000', '2024-11-15 11:10:00.000000', '16', '5'),
    ('If you\'re looking to raise capital, consider options like equity crowdfunding or reaching out to angel investors.', '2024-11-15 11:15:00.000000', '2024-11-15 11:15:00.000000', '17', '5');

-- Post ID: 6
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('When writing a business proposal, make sure it’s clear and to the point. Explain the problem, your solution, and why you’re the best choice.', '2024-11-16 13:10:00.000000', '2024-11-16 13:10:00.000000', '18', '6'),
    ('Adding case studies or success stories can really make your proposal stand out and build trust.', '2024-11-16 13:20:00.000000', '2024-11-16 13:20:00.000000', '19', '6');

-- Post ID: 7
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('During tough times, it’s smart to invest in stable assets like real estate or blue-chip stocks. Diversifying is really important.', '2024-11-17 16:10:00.000000', '2024-11-17 16:10:00.000000', '2', '7'),
    ('Gold and precious metals are also good for protecting your money against inflation and market ups and downs.', '2024-11-17 16:20:00.000000', '2024-11-17 16:20:00.000000', '3', '7');

-- Post ID: 8
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('A plant-based diet is great not only for your health but also for the environment! It’s a win-win.', '2024-11-15 09:40:00.000000', '2024-11-15 09:40:00.000000', '4', '8'),
    ('Going plant-based has seriously improved my energy and digestion. You should definitely give it a try!', '2024-11-15 09:50:00.000000', '2024-11-15 09:50:00.000000', '5', '8');

-- Post ID: 9
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('Try adding some quick HIIT workouts to your day. They’re effective and you can easily squeeze them into a busy schedule!', '2024-11-16 13:00:00.000000', '2024-11-16 13:00:00.000000', '2', '9'),
    ('Resistance bands are perfect for building strength at home, and they take up almost no space at all.', '2024-11-16 13:10:00.000000', '2024-11-16 13:10:00.000000', '6', '9');

-- Post ID: 10
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('Meditation has made a huge difference for me in managing stress. I really recommend giving it a try, especially mindfulness techniques.', '2024-11-17 18:40:00.000000', '2024-11-17 18:40:00.000000', '2', '10'),
    ('I can’t believe how much better I feel after just a few minutes of meditation each day. It really helps with focus too!', '2024-11-17 18:45:00.000000', '2024-11-17 18:45:00.000000', '11', '10');

-- Post ID: 11
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('Japan is hands down one of the safest and most fascinating solo travel spots. Definitely worth checking out!', '2024-11-15 15:50:00.000000', '2024-11-15 15:50:00.000000', '4', '11'),
    ('I’ve always wanted to visit New Zealand for solo hiking. The landscapes there are just breathtaking!', '2024-11-15 15:55:00.000000', '2024-11-15 15:55:00.000000', '13', '11');

-- Post ID: 12
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('Booking flights early and looking into budget airlines is one of the best ways to save money. Also, consider staying in hostels or Airbnb.', '2024-11-16 17:10:00.000000', '2024-11-16 17:10:00.000000', '4', '12'),
    ('Take the local bus or train instead of taxis – it’s not only cheaper but a great way to experience a new place.', '2024-11-16 17:20:00.000000', '2024-11-16 17:20:00.000000', '15', '12');

-- Post ID: 13
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('I love finding hidden gems like local cafes and markets. You’ll always find the best food and coolest souvenirs there!', '2024-11-17 20:10:00.000000', '2024-11-17 20:10:00.000000', '15', '13'),
    ('In places like Paris, I always try to visit the smaller museums. They’re way less crowded and much more intimate.', '2024-11-17 20:20:00.000000', '2024-11-17 20:20:00.000000', '12', '13');

-- Post ID: 14
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('I\'m so excited for the new sci-fi movie dropping this December. It looks like it’s going to blow everyone’s mind!', '2024-11-15 14:40:00.000000', '2024-11-15 14:40:00.000000', '14', '14'),
    ('I\'m looking forward to the next installment in the Marvel series. December’s going to be a great month for movie lovers!', '2024-11-15 14:45:00.000000', '2024-11-15 14:45:00.000000', '17', '14');

-- Post ID: 15
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('If you haven’t seen "The Farewell," it’s an amazing indie film with so much heart. Definitely check it out!', '2024-11-16 19:20:00.000000', '2024-11-16 19:20:00.000000', '2', '15'),
    ('I’d recommend "Everything Everywhere All at Once" too! It’s a crazy rollercoaster of a movie with a unique take on the multiverse.', '2024-11-16 19:30:00.000000', '2024-11-16 19:30:00.000000', '3', '15');

-- Post ID: 16
INSERT INTO `comments` (`content`, `created_at`, `updated_at`, `author_id`, `post_id`) VALUES
    ('CGI has totally changed how we enjoy movies, especially in action scenes. It opens up so many possibilities for creativity.', '2024-11-17 21:50:00.000000', '2024-11-17 21:50:00.000000', '15', '16'),
    ('Don’t get me wrong, CGI is awesome, but I still have a soft spot for practical effects. There’s something about the realism that CGI can’t quite match.', '2024-11-17 22:00:00.000000', '2024-11-17 22:00:00.000000', '8', '16');