select avg(per_day) from (
select
	count(*) per_day
from
	browsing
group by
	user_id, date(date)
) tmp1