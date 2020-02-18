CREATE TABLE IF NOT EXISTS friends
(
    id        integer PRIMARY KEY AUTOINCREMENT,
    user_id   INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    UNIQUE (user_id),
    FOREIGN KEY (friend_id) REFERENCES user (id)
);