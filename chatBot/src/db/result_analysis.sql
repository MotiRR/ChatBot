CREATE TABLE IF NOT EXISTS result_analysis
(
    id          integer PRIMARY KEY AUTOINCREMENT,
    id_sentence integer,
    verb        VARCHAR(512),
    noun        VARCHAR(512),
    swear       VARCHAR(512),
    xxx         VARCHAR(512),
    spam        VARCHAR(512),
    secret      VARCHAR(512),
    FOREIGN KEY (id_sentence) REFERENCES sentences (id)
);