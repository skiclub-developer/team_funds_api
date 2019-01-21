create table penalties
(
  id                serial,
  penalty_name      varchar(255) not null,
  penalty_cost      int          not null,
  case_of_beer_cost int          not null
);

create unique index penalties_id_uindex
  on penalties (id);

alter table penalties
  add constraint penalties_pk
    primary key (id);

--
-- Daten für Tabelle `penalties`
--

INSERT INTO penalties (id, penalty_name, penalty_cost, case_of_beer_cost)
VALUES
       (1, 'tunnel', 1, 0),
       (2, 'runde', 1, 0),
       (3, '20kontakte', 1, 0),
       (4, '20kontakteFehler', 2, 0),
       (5, 'roteKarte', 25, 1),
       (6, 'platzVerweisMeckern', 50, 0),
       (7, 'gelbRotMeckern', 50, 0),
       (8, 'gelbMeckern', 10, 0),
       (9, 'trainerHinterBande', 25, 1),
       (10, 'unentschuldigtNichtZumSpiel', 50, 0),
       (11, 'abmeldungInDerGruppe', 5, 0),
       (12, 'pflichtSpielPunktTrainer', 5, 0),
       (13, 'pflichtSpielVerloren', 2, 0),
       (14, 'sacheVergessen', 2, 0),
       (15, 'ausrüstungVergessen', 5, 0),
       (16, 'falscherEinwurf', 2, 0),
       (17, 'gasangriff', 5, 0),
       (18, 'unentschuldigtTraining', 10, 1),
       (19, 'platzbeschwerde', 1, 0),
       (20, 'schussAufTennisplatz', 2, 0),
       (21, 'foto', 1, 0),
       (22, 'pinkelnInDusche', 5, 0),
       (23, 'torschussSeitenaus', 0, 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `users`
--

CREATE TABLE users
(
  id                int          NOT NULL,
  name              varchar(255) NOT NULL,
  current_penalties int          NOT NULL DEFAULT 0,
  case_of_beer      int          NOT NULL DEFAULT 0
);

create unique index users_id_uindex
  on users (id);

alter table users
  add constraint users_pk
    primary key (id);

--
-- Daten für Tabelle `users`
--

INSERT INTO users (id, name)
VALUES
       (1, 'Katze'),
       (2, 'Christian'),
       (3, 'Daniel'),
       (4, 'Zachi'),
       (5, 'Giaco'),
       (6, 'Horst'),
       (7, 'Ingo'),
       (8, 'Jan'),
       (9, 'Jansi'),
       (10, 'Jens'),
       (11, 'Joel'),
       (12, 'Dete'),
       (13, 'Julian'),
       (14, 'Ken'),
       (15, 'Celo'),
       (16, 'Matze'),
       (17, 'Schnitzke'),
       (18, 'Hecki'),
       (19, 'Kocki'),
       (20, 'Ricardo'),
       (21, 'Robin'),
       (22, 'Thomas'),
       (23, 'Schaddi'),
       (24, 'Tobi'),
       (25, 'Max'),
       (26, 'Spölle'),
       (27, 'Patti');

create table user_penalties
(
  id         serial not null,
  user_id    int    not null
    constraint user_penalties_users_id_fk
      references users (id),
  penalty_id int    not null
    constraint user_penalties_penalties_id_fk
      references penalties (id),
  amount     int    not null
);

create unique index user_penalties_id_uindex
  on user_penalties (id);

alter table user_penalties
  add constraint user_penalties_pk
    primary key (id);

