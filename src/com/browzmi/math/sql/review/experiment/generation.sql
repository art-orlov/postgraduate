select count(distinct user_id) from browsing;

select count(*) from url u where exists (select * from browsing where url_id = u.id);

select count(*) from browsing where operation = 'add';

select count(*) from action;

select count(distinct url_id) from action;

select avg(diff) from (
select
	(unix_timestamp(end_date) - unix_timestamp(start_date)) / 60 diff
from (
select
	b.date end_date,
	(select max(date) from browsing b2 where b2.user_id = b.user_id and b2.url_id = b.url_id and b2.window_id = b.window_id and b2.operation = 'add' and b2.date < b.date) start_date
from
	browsing b
where
	b.operation = 'remove'
) tmp1
) tmp2;

select avg(uc) from (
select count(distinct user_id) uc from browsing group by url_id) tmp1;

select sum(good_try) / count(*) from (
select
	user_id,
	if (exists(
		select
			*
		from
			action a
		where
			a.url_id = b.url_id
			and
			a.date between date_sub(b.date, interval 1 day) and b.date
			and
			a.user_id <> b.user_id
	), 1, 0) good_try
from
	browsing b
where
	operation = 'add'
) tmp1;

select
	s1.c,
	sum(s2.c_count)
from
(
	select c, count(*) c_count from (
		select count(distinct user_id) c from browsing group by url_id
	) tmp1
	group by c
) s1
,
(
	select c, count(*) c_count from (
		select count(distinct user_id) c from browsing group by url_id
	) tmp2
	group by c
) s2
where
	s2.c >= s1.c
group by
	s1.c;

/*
select avg(c) from (
select
	count(*) c
from
	browsing
where
	operation = 'add'
group by
	user_id, url_id) tmp1;

select
	s1.c,
	sum(s2.c_count)
from
(
	select c, count(*) c_count from (
			select count(*) c from browsing where operation = 'add' group by user_id, url_id
	) tmp1
	group by c
) s1
,
(
	select c, count(*) c_count from (
			select count(*) c from browsing where operation = 'add' group by user_id, url_id
	) tmp2
	group by c
) s2
where
	s2.c <= s1.c
group by
	s1.c;

select
	s1.c,
	sum(s2.c_count)
from
(
	select c, count(*) c_count from (
		select count(distinct user_id) c from action group by url_id
	) tmp1
	group by c
) s1
,
(
	select c, count(*) c_count from (
		select count(distinct user_id) c from action group by url_id
	) tmp2
	group by c
) s2
where
	s2.c <= s1.c
group by
	s1.c;

select
	s1.c,
	sum(s2.c_count)
from
(
	select c, count(*) c_count from (
			select count(*) c from action group by user_id, url_id
	) tmp1
	group by c
) s1
,
(
	select c, count(*) c_count from (
			select count(*) c from action group by user_id, url_id
	) tmp2
	group by c
) s2
where
	s2.c <= s1.c
group by
	s1.c;
*/