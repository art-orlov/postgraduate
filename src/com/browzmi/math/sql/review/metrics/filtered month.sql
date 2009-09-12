select avg(per_day) from (
select
	date(date), weekday(date), count(distinct user_id) per_day
from
	browsing
where
	date between '2008-11-01' and '2008-12-01'
	and
	weekday(date) < 5
group by
	date(date)
) tmp1;

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
	b.date between '2008-11-01' and '2008-12-01'
	and
	weekday(b.date) < 5
) tmp1
group by user_id
) tmp2;
