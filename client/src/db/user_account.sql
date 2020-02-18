CREATE TABLE IF NOT EXISTS user_account
(
    id       integer PRIMARY KEY AUTOINCREMENT,
    login    varchar(32) NOT NULL,
    password varchar(32) NOT NULL,
    UNIQUE (login)
);