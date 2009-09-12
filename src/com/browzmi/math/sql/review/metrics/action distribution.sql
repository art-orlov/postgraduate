select avg(a_count), min(a_count), max(a_count) from
(select user_id, count(id) a_count from action group by user_id) tmp1;


select per_day , count(user_id) u_count from (
select
	user_id, count(*) per_day
from
	action
group by
	user_id, date(date)
) tmp1
group by
	per_day
order by
	per_day;


select count(user_id) u_count, a_count from
(select
	user_id, count(id) a_count
from
	action
group by
	user_id) tmp1
group by
	a_count;


select
	s1.a_count,
	sum(s2.u_count)
from

(select count(user_id) u_count, a_count from
(select
	user_id, count(id) a_count
from
	action
group by
	user_id) tmp1
group by
	a_count) s1
,
(select count(user_id) u_count, a_count from
(select
	user_id, count(id) a_count
from
	action
group by
	user_id) tmp1
group by
	a_count) s2

where
	s2.a_count <= s1.a_count
group by
	s1.a_count;
