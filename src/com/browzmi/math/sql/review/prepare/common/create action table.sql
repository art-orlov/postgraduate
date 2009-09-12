delete from
	favorite_tag
where
	favorite_id in (select id from favorite where user_id not in (select user_id from browsing));

delete from
	favorite
where
	user_id not in (select user_id from browsing);

drop table if exists action;

create table `action` (
	`id` bigint(20) not null auto_increment,
	`date` datetime not null,
	`user_id` char(22) not null,
	`url_id` bigint(20) not null,
	`type` enum('favorite', 'comment', 'rate', 'clip') not null,
	primary key (`id`),
	key `url_id-user_id-date` (`url_id`, `user_id`, `date`)
);

insert into
	action
select
	null id, date, user_id, url_id, 'comment' type
from
	`comment`
where
	operation = 'add';

insert into
	action
select
	null id, date, user_id, url_id, 'favorite' type
from
	`favorite`
where
	operation = 'add';

insert into
	action
select
	null id, date, user_id, url_id, 'rate' type
from
	`rate`
where
    operation = 'add';

insert into
	action
select
	null id, date, user_id, url_id, 'clip' type
from
	`clip`
where
    operation = 'add';
