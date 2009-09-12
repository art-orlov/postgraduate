/* 8% */
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
			b2.url_id = b.url_id
			and
			b2.date between b.date and date_add(b.date, interval 1 day)
	) f_total,
	(
		select
			count(id)
		from
			friend f
		where
			b.user_id = user_id
			and
			exists (
				select * from browsing b3
				where
					b3.url_id = b.url_id
					and
					b3.user_id = f.user_id2
					and
					b3.date between b.date and date_add(b.date, interval 1 day)
			)
	) f_good
from
	browsing b
where
	b.user_id in (select user_id from friend)
	and
	b.date between date('2008-04-01') and date('2008-06-15')
) tmp1
