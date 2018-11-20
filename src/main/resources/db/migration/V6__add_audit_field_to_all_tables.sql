ALTER TABLE user_penalties
  ADD COLUMN changed_by varchar;

ALTER TABLE user_penalty_payments
  ADD COLUMN changed_by varchar;

ALTER TABLE user_penalty_beer_payments
  ADD COLUMN changed_by varchar;