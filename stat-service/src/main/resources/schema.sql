DROP TABLE IF EXISTS endpoint_hits;

CREATE TABLE endpoint_hits
(
    hit_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app VARCHAR(255),
    uri VARCHAR(255),
    ip VARCHAR(16),
    timestamp TIMESTAMP WITHOUT TIME ZONE
);