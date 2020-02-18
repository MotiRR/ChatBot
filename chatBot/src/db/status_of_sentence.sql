CREATE TABLE IF NOT EXISTS status_of_sentence
(
    id          integer PRIMARY KEY AUTOINCREMENT,
    id_sentence integer,
    status      varchar(50),
    FOREIGN KEY (id_sentence) REFERENCES sentences (id)
);