CREATE TABLE IF NOT EXISTS gallery
(
    id      integer PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    photo   BLOB    NOT NULL,
    UNIQUE (user_id),
    FOREIGN KEY (user_id) REFERENCES user (id)
);