delete from
	favorite_tag
where
	favorite_id in (select id from favorite where user_id not in (select user_id from browsing));

delete from
	favorite
where
	user_id not in (select user_id from browsing);

insert into
	action
select
	null id, min(date), user_id, url_id, 'comment' type
from
	`comment` o
where
	operation = 'add'
	and
	not exists (select * from action where user_id = o.user_id and url_id = o.url_id)
group by
	user_id, url_id;

insert into
	action
select
	null id, date, user_id, url_id, 'favorite' type
from
	`favorite` o
where
	operation = 'add'
	and
	not exists (select * from action where user_id = o.user_id and url_id = o.url_id)
group by
	user_id, url_id;

insert into
	action
select
	null id, date, user_id, url_id, 'rate' type
from
	`rate` o
where
	operation = 'add'
	and
	not exists (select * from action where user_id = o.user_id and url_id = o.url_id)
group by
	user_id, url_id;
