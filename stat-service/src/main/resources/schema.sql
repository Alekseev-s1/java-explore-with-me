-- DROP TABLE IF EXISTS endpoint_hits;

CREATE TABLE IF NOT EXISTS endpoint_hits (
    hit_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app VARCHAR,
    uri VARCHAR,
    ip VARCHAR,
    timestamp TIMESTAMP WITHOUT TIME ZONE
);