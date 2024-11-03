--liquibase formatted sql
--changeset jchojdak:2
INSERT INTO `roles` (`name`) VALUES ('ROLE_USER');
INSERT INTO `roles` (`name`) VALUES ('ROLE_ADMIN');

INSERT INTO `users` (`active`, `blocked`, `email`, `first_name`, `last_name`, `mobile`, `password`, `registered_at`, `username`)
VALUES (1, 0, 'admin@admin.com', 'Admin', 'Admin', '123456789', '$2a$10$jbM7jT.I7oVyPM93QNha/.nvPMdx/hGIRmw6TfmzoUx08tyG2LQ8.', "2024-11-03 16:54:56.280587", 'admin');

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES (1, 2);