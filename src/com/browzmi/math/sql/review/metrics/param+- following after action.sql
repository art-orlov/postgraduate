select avg (good / total) from (
select user_id, count(user_id) total, sum(good_try) good from (
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
			a.user_id in (select user_id2 from friend where user_id = b.user_id)
	), 1, 0) good_try
from
	browsing b
where
	user_id in (select user_id from friend)
) tmp1
group by user_id
) tmp2;

select avg (good / total) from (
select user_id, count(user_id) total, sum(good_try) good from (
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
) tmp1
group by user_id
) tmp2;

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

/*
select avg (good / total) from (
select user_id, count(user_id) total, sum(good_try) good from (
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
			a.user_id in (select user_id2 from friend where user_id = b.user_id)
	), 1, 0) good_try
from
	browsing b
where
	b.user_id in (select user_id from friend)
	and
	b.operation = 'add'
	and
	b.date between '2008-07-01' and '2008-08-01'
) tmp1
group by user_id
) tmp2;

select avg (good / total) from (
select user_id, count(user_id) total, sum(good_try) good from (
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
	b.operation = 'add'
	and
	b.date between '2008-07-01' and '2008-08-01'
) tmp1
group by user_id
) tmp2;

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
	b.operation = 'add'
	and
	b.date between '2008-07-01' and '2008-08-01'
) tmp1;
*/