create table universe.constellation (
	id bigint primary key generated always as identity,
	name text,
	orientation text check (orientation in ('north', 'south')) not null
);

comment on table universe.constellation is 'Созвездия';
comment on column universe.constellation.id is 'Идентификатор';
comment on column universe.constellation.name is 'Наименование';
comment on column universe.constellation.orientation is 'Ориентация (полушарие)';

insert into universe.constellation (name, orientation) values ('Crux', 'south');

create table universe.star (
	id bigint primary key generated always as identity,
	name text,
	visual_brightness numeric(5, 2),
	absolute_magnitude int,
	metallicity	numeric,
	radius bigint,
	id_constellation bigint references universe.constellation (id)
);

comment on table universe.star is 'Звезды';
comment on column universe.star.id is 'Идентификатор';
comment on column universe.star.name is 'Наименование';
comment on column universe.star.visual_brightness is 'Видимая яркость';
comment on column universe.star.absolute_magnitude is 'Абсолютная звездная величина';
comment on column universe.star.metallicity is 'Металличность';
comment on column universe.star.radius is 'Экваториальный радиус';
comment on column universe.star.id_constellation is 'Видимое положение на небе (созвездие)';