/*
какой процент рекомендаций друзей я посещаю
*/

select user_id, sum(visit) / sum(recommendation) from (
select
	f.user_id,
	1 recommendation,
	if (
		exists(
			select
				*
			from
				browsing
			where
				user_id = f.user_id
				and
				url_id = a.user_id
				and
				date between a.date and date_add(a.date, interval 1 day)
		)
	, 1, 0) visit
from
	action a,
	friend f
where
	f.user_id2 = a.user_id
	and
	f.date < a.date
	/*and
	f.user_id2 <> '3macPOgUabR2ZBjWafWWls'
	and
	f.user_id <> '3macPOgUabR2ZBjWafWWls'*/
) tmp1
group by user_id
