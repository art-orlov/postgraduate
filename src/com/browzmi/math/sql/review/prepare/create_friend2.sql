drop table if exists friend2;

create table friend2 (
	`id` bigint(20) NOT NULL auto_increment,
	`user_id` char(22) NOT NULL,
	`user_id2` char(22) NOT NULL,
	PRIMARY KEY  (`id`),
    KEY `user_id-user_id2` (`user_id`, `user_id2`)
);

insert into friend2
select
	null id,
	user_id,
	user_id2
from
	friend
where
	user_id in (
		select
			user_id
		from
			friend
		group by
			user_id
		having
			count(id) > 1
	);

drop table if exists friend3;

create table friend3 (
	`id` bigint(20) NOT NULL auto_increment,
	`user_id` char(22) NOT NULL,
	`user_id2` char(22) NOT NULL,
	PRIMARY KEY  (`id`),
    KEY `user_id-user_id2` (`user_id`, `user_id2`)
);

insert into friend3
select
	null id,
	user_id,
	user_id2
from
	friend2 f
where
	exists (select * from friend2 where user_id = f.user_id2 and user_id2 = f.user_id);