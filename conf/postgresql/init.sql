CREATE TABLE genre
(
    id          serial primary key,
    name        char(64) not null
);

CREATE TABLE artist
(
    id          serial primary key,
    name        char(64) not null
);

CREATE TABLE artists_genres
(
    artist_id  	integer references artist(id) on delete cascade,
    genre_id  	integer references genre(id) on delete restrict,
    primary key (artist_id, genre_id)
);

CREATE TABLE album
(
    id          serial primary key,
    name        char(64) not null,
    year        date
);

CREATE TABLE albums_genres
(
    album_id  	integer references album(id) on delete cascade,
    genre_id  	integer references genre(id) on delete restrict,
    primary key (album_id, genre_id)
);

CREATE TABLE track
(
    id          serial primary key,
    name        char(64) not null,
    year        date,
    url         varchar
);

CREATE TABLE tracks_artists
(
    track_id  	integer references track(id) on delete cascade,
    artist_id  	integer references artist(id) on delete restrict,
    primary key (track_id, artist_id)
);

CREATE TABLE tracks_albums
(
    track_id  	integer references track(id) on delete cascade,
    album_id  	integer references album(id) on delete restrict,
    primary key (track_id, album_id)
);

CREATE TABLE tracks_genres
(
    track_id  	integer references track(id) on delete cascade,
    genre_id  	integer references genre(id) on delete restrict,
    primary key (track_id, genre_id)
);

CREATE TABLE sequence /* table for autogeneration uniq identifiers of any entity */
(
    SEQ_NAME character(50) NOT NULL,
    SEQ_COUNT integer NULL, PRIMARY KEY (SEQ_NAME)
);
INSERT INTO sequence
(
    SEQ_NAME,
    SEQ_COUNT
) values ('SEQ_GEN', 1);
