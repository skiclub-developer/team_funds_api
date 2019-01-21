ALTER TABLE user_penalty_payments
  ADD COLUMN paid_at timestamp;

ALTER TABLE user_penalty_beer_payments
  ADD COLUMN paid_at timestamp;