select
	avg(u_count)
from (
	select count(distinct user_id) u_count from browsing group by date(date)
) tmp1