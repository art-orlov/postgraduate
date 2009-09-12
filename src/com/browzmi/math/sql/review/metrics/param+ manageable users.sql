select count(user_id) from (
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
) tmp2
where (good / total) > 0.1