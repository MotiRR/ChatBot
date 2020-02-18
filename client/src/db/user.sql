CREATE TABLE IF NOT EXISTS user
(
    id          integer PRIMARY KEY NOT NULL,
    name        varchar(32)         NOT NULL,
    second_name varchar(32)         NOT NULL,
    last_name   varchar(32)         NOT NULL,
    age         integer     DEFAULT NULL,
    phone       varchar(32) DEFAULT NULL,
    email       varchar(32) DEFAULT NULL,
    is_blocked  integer DEFAULT 0,
    blocking_description varchar(1024),
    FOREIGN KEY (id) REFERENCES user_account (id)
);