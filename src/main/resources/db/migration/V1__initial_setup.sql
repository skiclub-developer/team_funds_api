-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Erstellungszeit: 27. Okt 2018 um 14:46
-- Server-Version: 5.7.23
-- PHP-Version: 7.2.8

SET
SQL_MODE
=
"NO_AUTO_VALUE_ON_ZERO";
SET
time_zone
=
"+00:00";

--
-- Datenbank: `team_funds`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `penalties`
--

CREATE TABLE `penalties`
(
  `id`                int(11) NOT NULL,
  `penalty_name`      varchar(255) NOT NULL,
  `penalty_cost`      int(11) NOT NULL,
  `case_of_beer_cost` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `penalties`
--

INSERT INTO `penalties` (`id`, `penalty_name`, `penalty_cost`, `case_of_beer_cost`)
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

CREATE TABLE `users`
(
  `id`                int(11) NOT NULL,
  `name`              varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `current_penalties` int(11) NOT NULL,
  `case_of_beer`      int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `users`
--

INSERT INTO `users` (`id`, `name`, `current_penalties`, `case_of_beer`)
VALUES
       (1, 'Patrick', -4, -4),
       (2, 'Robin', -29, -6),
       (3, 'Jansi', 6, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user_penalties`
--

CREATE TABLE `user_penalties`
(
  `id`         int(11) NOT NULL,
  `user_id`    int(11) NOT NULL,
  `penalty_id` int(11) NOT NULL,
  `amount`     int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `user_penalties`
--

INSERT INTO `user_penalties` (`id`, `user_id`, `penalty_id`, `amount`)
VALUES
       (1, 1, 1, 27),
       (2, 2, 2, 9),
       (3, 1, 2, 4),
       (4, 3, 2, 2),
       (5, 3, 1, 2),
       (6, 1, 5, 1),
       (7, 1, 23, 1),
       (8, 2, 1, 10);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `penalties`
--
ALTER TABLE `penalties`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_name_index` (`name`);

--
-- Indizes für die Tabelle `user_penalties`
--
ALTER TABLE `user_penalties`
  ADD PRIMARY KEY (`id`),
  ADD KEY `penalty_foreign_key` (`penalty_id`),
  ADD KEY `user_foreign_key` (`user_id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `penalties`
--
ALTER TABLE `penalties`
  MODIFY `id` int (11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT für Tabelle `users`
--
ALTER TABLE `users`
  MODIFY `id` int (11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT für Tabelle `user_penalties`
--
ALTER TABLE `user_penalties`
  MODIFY `id` int (11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `user_penalties`
--
ALTER TABLE `user_penalties`
  ADD CONSTRAINT `penalty_foreign_key` FOREIGN KEY (`penalty_id`) REFERENCES `penalties` (`id`),
  ADD CONSTRAINT `user_foreign_key` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
