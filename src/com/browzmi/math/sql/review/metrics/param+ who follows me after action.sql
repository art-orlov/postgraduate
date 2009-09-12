select avg (f_good/f_total) from (
select
	date,
	user_id,
	url_id,
	(
		select
			count(distinct user_id)
		from
			browsing b2
		where
			b2.url_id = a.url_id
			and
			b2.date between a.date and date_add(a.date, interval 1 day)
	) f_total,
	(
		select
			count(id)
		from
			friend f
		where
			a.user_id = f.user_id
			and
			exists (
				select * from browsing b3
				where
					b3.url_id = a.url_id
					and
					b3.user_id = f.user_id2
					and
					b3.date between a.date and date_add(a.date, interval 1 day)
			)
	) f_good
from
	action a
where
	a.user_id in (select user_id from friend)
) tmp1;
