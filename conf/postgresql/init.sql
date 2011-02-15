CREATE TABLE genre
(
    id          serial CONSTRAINT id_pr_genre primary key,
    name        varchar(255) not null
);

CREATE TABLE artist
(
    id          serial CONSTRAINT id_pr_artist primary key,
    name        varchar(255) not null
);

CREATE TABLE artists_genres
(
    artist_id  	integer references artist(id) on delete cascade,
    genre_id  	integer references genre(id) on delete restrict,
    CONSTRAINT pr_pair_art_gen primary key (artist_id, genre_id)
);

CREATE TABLE album
(
    id          serial CONSTRAINT id_pr_album primary key,
    name        varchar(255) not null,
    year        date
);

CREATE TABLE albums_genres
(
    album_id  	integer references album(id) on delete cascade,
    genre_id  	integer references genre(id) on delete restrict,
    CONSTRAINT pr_pair_alb_gen primary key (album_id, genre_id)
);

CREATE TABLE track
(
    id          serial CONSTRAINT id_pr_track primary key,
    name        varchar(255) not null,
    year        date,
    url         varchar
);

CREATE TABLE tracks_artists
(
    track_id  	integer references track(id) on delete cascade,
    artist_id  	integer references artist(id) on delete restrict,
    CONSTRAINT pr_pair_tra_art primary key (track_id, artist_id)
);

CREATE TABLE tracks_albums
(
    track_id  	integer references track(id) on delete cascade,
    album_id  	integer references album(id) on delete restrict,
    CONSTRAINT pr_pair_tra_alb primary key (track_id, album_id)
);

CREATE TABLE tracks_genres
(
    track_id  	integer references track(id) on delete cascade,
    genre_id  	integer references genre(id) on delete restrict,
    CONSTRAINT pr_pair_tra_gen primary key (track_id, genre_id)
);

