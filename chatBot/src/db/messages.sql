CREATE TABLE IF NOT EXISTS messages
(
    id           integer PRIMARY KEY AUTOINCREMENT,
    user_id      integer,
    date_of_send DateTime,
    message      VARCHAR(1024)
);

