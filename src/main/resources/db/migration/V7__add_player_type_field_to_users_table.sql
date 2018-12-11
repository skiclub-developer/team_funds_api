ALTER TABLE users
  ADD COLUMN type VARCHAR(255) DEFAULT 'PLAYER';

UPDATE users
set type = 'COACH'
WHERE name = 'Thomas';

UPDATE users
set type = 'COACH'
WHERE name = 'Joel';

UPDATE users
set type = 'ADVISOR'
where name = 'Ken';

UPDATE users
set type = 'ADVISOR'
where name = 'Horst';
