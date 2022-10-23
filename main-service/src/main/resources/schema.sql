DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS compilation_event;
DROP TABLE IF EXISTS compilations;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS categories;

CREATE TABLE categories (
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR UNIQUE
);

CREATE TABLE compilations (
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR,
    pinned BOOLEAN
);

CREATE TABLE locations (
    location_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat DOUBLE PRECISION,
    lon DOUBLE PRECISION
);

CREATE TABLE users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR,
    email VARCHAR UNIQUE
);

CREATE TABLE events (
    event_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title VARCHAR,
    annotation VARCHAR,
    description VARCHAR,
    state VARCHAR,
    initiator_id BIGINT REFERENCES users(user_id) ON DELETE CASCADE,
    category_id BIGINT REFERENCES categories(category_id),
    created_on TIMESTAMP WITHOUT TIME ZONE,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    paid BOOLEAN,
    request_moderation BOOLEAN,
    confirmed_requests BIGINT,
    participant_limit BIGINT,
    views BIGINT,
    location_id BIGINT REFERENCES locations(location_id) ON DELETE CASCADE
);

CREATE TABLE requests (
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id BIGINT REFERENCES events(event_id) ON DELETE CASCADE,
    requester_id BIGINT REFERENCES users(user_id) ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE,
    status VARCHAR
);

CREATE TABLE compilation_event (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id BIGINT REFERENCES events(event_id) ON DELETE CASCADE,
    compilation_id BIGINT REFERENCES compilations(compilation_id) ON DELETE CASCADE
);
