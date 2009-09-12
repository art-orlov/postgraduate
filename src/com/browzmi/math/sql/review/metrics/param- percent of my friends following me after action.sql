select avg (f_good/f_total) from (
select
	date,
	user_id,
	url_id,
	(select count(id) from friend where a.user_id = user_id) f_total,
	(
		select
			count(id)
		from
			friend f
		where
			a.user_id = f.user_id
			and
			exists (
				select * from browsing b2
				where
					b2.url_id = a.url_id
					and
					b2.user_id = f.user_id2
					and
					b2.date between a.date and date_add(a.date, interval 1 day)
			)
	) f_good
from
	action a
where
	a.user_id in (select user_id from friend)
	and
	a.date between date('2008-04-01') and date('2008-06-15')
) tmp1
