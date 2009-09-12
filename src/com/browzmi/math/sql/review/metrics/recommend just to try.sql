select count(user_id) from (
select
	user_id,
	count(dt) cdt
from (
select
	user_id,
	date(date) dt
from
	action
group by
	user_id,
	dt
) tmp1
group by
	user_id
) tmp2
where cdt = 1;

select count(distinct user_id) from (
	select
		user_id, count(id) a_count
	from
		action
	group by
		user_id
) tmp1
where
	a_count = 1;