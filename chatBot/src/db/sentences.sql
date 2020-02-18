CREATE TABLE IF NOT EXISTS sentences
(
    id         integer PRIMARY KEY AUTOINCREMENT,
    id_message integer,
    sentence   VARCHAR(512),
    FOREIGN KEY (id_message) REFERENCES messages (id)
);
