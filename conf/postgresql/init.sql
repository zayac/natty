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

CREATE TABLE album
(
    id          serial CONSTRAINT id_pr_album primary key,
    name        varchar(255) not null,
    year        date
);

CREATE TABLE artists_genres
(
    artist_id  	integer references artist(id) on delete cascade,
    genre_id  	integer references genre(id) on delete restrict,
    CONSTRAINT pr_pair_art_gen primary key (artist_id, genre_id)
);

CREATE TABLE albums_artists
(
    album_id    integer references album(id) on delete cascade,
    artist_id   integer references artist(id) on delete restrict,
    CONSTRAINT pr_pair_alb_art primary key (album_id, artist_id)
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





CREATE TABLE widget_type
(
    id          serial CONSTRAINT id_pr_widget_type primary key,
	name        varchar(255) not null,
	classname	varchar(255) not null
);

CREATE TABLE gui_properties
(
    id          serial CONSTRAINT id_pr_gui_props primary key,
    parent_id   integer references gui_properties(id),
    wtype       integer references widget_type(id),
	style		varchar(255)
);

CREATE TABLE content_header
(
    content_id  integer references gui_properties(id),
    header      varchar(255) not null,
    CONSTRAINT  pr_key_cont primary key (content_id)
);

CREATE TABLE panel_content
(
    panel_id    integer references gui_properties(id),
	ord_number  integer,
    content_id  integer references gui_properties(id),
    CONSTRAINT  pr_pair_pan_cont primary key (panel_id, content_id)
);

CREATE TABLE label
(
    id          integer references gui_properties(id),
    text        varchar
);

INSERT INTO widget_type (id, name, classname) VALUES
(   1, 'label',				'WLabel'),
(   2, 'vertical panel',	'WVerticalPanel'),
(   3, 'tab panel',			'WTabPanel'),
(   4, 'genres list',		'WGenresList'),
(   5, 'text box',			'WTextBox'),
(   6, 'basic button',		'WBasicButton'),
(   7, 'horizontal panel',	'WHorizontalPanel'),
(   8, 'track list',		'WTrackList'),
(   9, 'text cell list',	'WTextCellList'),
(  10, 'image',				'WImage'),
(  11, 'artist list',		'WArtistList'),
(  12, 'album list',		'WAlbumList'),
(  13, 'player',			'WPlayer'),
(  14, 'flow panel',		'WFlowPanel');


INSERT INTO gui_properties (id, parent_id, wtype, style) VALUES
(   0,	null,	2,	'root'),
(   1,	0,		1,	'header'),
(   2,	0,	   14,	'search_container'),
(   3,	2,		5,	'search'),
(   4,	2,		6,	'search_button'),
(   5,	0,		7,	'lists_container'),
(   6,	5,		4,	'genre_list'),
(   7,	5,		8,	'track_list'),
(   8,	0,		1,	'copyright'),
(   9,  5,	   11,	'artist_list'),
(  10,  5,     12,  'album_list'),
(  11,  0,	   13,  'player');

INSERT INTO label (id, text) VALUES
(	1,	'<h1>&#9835;atty</h1><h3>enjoying music</h3>'),
(	3,	'search'),
(	4,	'find'),
(	8,	'MIPT frtk. 2011');

INSERT INTO panel_content (panel_id, ord_number, content_id) VALUES
(	0,	0,	1),
(	0,	1, 11),
(	0,	2,	2),
(	0,	3,	5),
(	0,	4,	8),
(	2,	0,	3),
(	2,	1,	4),
(	5,	0,	6),
(	5,	1,	9),
(	5,	2, 10),
(	5,	3,	7);
