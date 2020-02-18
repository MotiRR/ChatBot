CREATE TABLE IF NOT EXISTS session
(
    id                integer PRIMARY KEY AUTOINCREMENT,
    sessionId         varchar(1024) NOT NULL,
    userId            integer       NOT NULL,
    creationTimestamp DateTime      NOT NULL,
    isClosed          BOOLEAN       NOT NULL,
    closedTimestamp   DateTime,
    UNIQUE (sessionId),
    FOREIGN KEY (userId) REFERENCES user (id)
);