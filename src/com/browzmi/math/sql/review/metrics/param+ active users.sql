select count(user_id) from (
select user_id, sum(recomend) / sum(browse) r from
(select
	user_id,
	1 browse,
	if(exists(
		select
			*
		from
			action
		where
			user_id = b.user_id
			and
			url_id = b.url_id
			and
			date between b.date and date_add(date, interval 1 hour)
	) , 1, 0) recomend
from
	browsing b
	) tmp1
group by user_id
having user_id in (select user_id from friend)
) tmp2
where r > 0.1