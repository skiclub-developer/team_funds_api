create table user_penalty_beer_payments
(
  id      serial not null,
  user_id int    not null
    constraint user_penalty_beer_payments_users_id_fk
      references users (id),
  amount  int    not null
);

create unique index user_penalty_beer_payments_id_uindex
  on user_penalty_beer_payments (id);

alter table user_penalty_beer_payments
  add constraint user_penalty_beer_payments_pk
    primary key (id);

