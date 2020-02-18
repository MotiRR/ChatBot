CREATE TABLE IF NOT EXISTS user_lock
(
    id_user             integer,
    date_of_lock        DateTime,
    date_of_unlock      DateTime,
    type_of_restriction varchar(25),
    reason              varchar(255)
);