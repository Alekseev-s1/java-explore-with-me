DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS compilation_event;
DROP TABLE IF EXISTS compilations;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS categories;

CREATE TABLE categories
(
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE compilations
(
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(255),
    pinned BOOLEAN
);

CREATE TABLE locations
(
    location_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat DOUBLE PRECISION,
    lon DOUBLE PRECISION
);

CREATE TABLE users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(512) UNIQUE
);

CREATE TABLE events
(
    event_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR(255),
    annotation VARCHAR(1024),
    description VARCHAR(2048),
    state VARCHAR(40),
    initiator_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    category_id BIGINT REFERENCES categories (category_id),
    created_on TIMESTAMP WITHOUT TIME ZONE,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    paid BOOLEAN,
    request_moderation BOOLEAN,
    confirmed_requests BIGINT,
    participant_limit BIGINT,
    views BIGINT,
    location_id BIGINT REFERENCES locations (location_id) ON DELETE CASCADE
);

CREATE TABLE requests
(
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id BIGINT REFERENCES events (event_id) ON DELETE CASCADE,
    requester_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE,
    status VARCHAR(40)
);

CREATE TABLE comments (
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    comment_text TEXT NOT NULL,
    author_id BIGINT REFERENCES users (user_id) ON DELETE SET NULL,
    event_id BIGINT REFERENCES events (event_id) ON DELETE CASCADE,
    state VARCHAR(40),
    created_on TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE compilation_event
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id BIGINT REFERENCES events (event_id) ON DELETE CASCADE,
    compilation_id BIGINT REFERENCES compilations (compilation_id) ON DELETE CASCADE
);

