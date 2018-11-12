ALTER TABLE user_penalties
  ADD COLUMN changed_by TEXT;

ALTER TABLE user_penalty_payments
  ADD COLUMN changed_by TEXT;

ALTER TABLE user_penalty_beer_payments
  ADD COLUMN changed_by TEXT;