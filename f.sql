CREATE TABLE jc_author
(
    author_id INTEGER PRIMARY KEY NOT NULL,
    author_name VARCHAR(255)
);
CREATE TABLE jc_book
(
    book_id INTEGER PRIMARY KEY NOT NULL,
    book_name VARCHAR(255)
);
CREATE TABLE jc_book_author
(
    book_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    CONSTRAINT pk PRIMARY KEY (book_id, author_id),
    CONSTRAINT jc_book__fk FOREIGN KEY (book_id) REFERENCES jc_book (book_id),
    CONSTRAINT jc_author___fk FOREIGN KEY (author_id) REFERENCES jc_author (author_id)
);

CREATE TABLE jc_city
(
    city_id INTEGER PRIMARY KEY NOT NULL,
    city_name VARCHAR(255),
    region_id BIGINT,
    CONSTRAINT jc_city_region_id_fkey FOREIGN KEY (region_id) REFERENCES jc_region (region_id)
);
CREATE TABLE jc_region
(
    region_id INTEGER PRIMARY KEY NOT NULL,
    region_name VARCHAR(255)
);
CREATE TABLE jc_region_standalone
(
    region_id INTEGER PRIMARY KEY NOT NULL,
    region_name VARCHAR(255)
);
-- CREATE TABLE jc_city_city_id_seq
-- (
--     sequence_name VARCHAR NOT NULL,
--     last_value BIGINT NOT NULL,
--     start_value BIGINT NOT NULL,
--     increment_by BIGINT NOT NULL,
--     max_value BIGINT NOT NULL,
--     min_value BIGINT NOT NULL,
--     cache_value BIGINT NOT NULL,
--     log_cnt BIGINT NOT NULL,
--     is_cycled BOOLEAN NOT NULL,
--     is_called BOOLEAN NOT NULL
-- );