CREATE TABLE `team_funds`.`user_penalty_beer_payments`
(
  `id`      INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `amount`  INT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

ALTER TABLE `user_penalty_beer_payments`
  ADD CONSTRAINT `user_penalty_beer_payments_foreign_key_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);