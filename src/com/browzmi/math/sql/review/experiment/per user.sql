select
	(unix_timestamp(max_d) - unix_timestamp(min_d)) / 60 inter,
	max_d,
	min_d,
	(select count(distinct url_id) from browsing where operation = 'add' and user_id = b.user_id) unique_urls,
	(select count(*) from browsing where operation = 'add' and user_id = b.user_id) urls,
	(select count(distinct url_id) from action where user_id = b.user_id) unique_urls,
	(select count(*) from action where user_id = b.user_id) urls,
	(
		select avg(diff) from (
		select
			(unix_timestamp(end_date) - unix_timestamp(start_date)) / 60 diff,
			user_id
		from (
		select
			b1.date end_date,
			(select max(date) from browsing b2 where b2.user_id = b1.user_id and b2.url_id = b1.url_id and b2.window_id = b1.window_id and b2.operation = 'add' and b2.date < b1.date) start_date,
			b1.user_id
		from
			browsing b1
		where
			b1.operation = 'remove'
		) tmp1
		) tmp2
		where
			tmp2.user_id = b.user_id
	) on_page,
	b.user_id
from
(select
	max(date) max_d,
	min(date) min_d,
	user_id
from
	browsing
where
	operation = 'add'
group by
	user_id) b;
