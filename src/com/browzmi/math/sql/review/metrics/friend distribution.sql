select avg(f_count), min(f_count), max(f_count) from
(select user_id, count(id) f_count from friend group by user_id) tmp1;


select count(distinct user_id) from friend;


select
	s1.f_count,
	sum(s2.u_count)
from

(select count(user_id) u_count, f_count from
(select
	user_id, count(id) f_count
from
	friend
group by
	user_id) tmp1
group by
	f_count) s1
,
(select count(user_id) u_count, f_count from
(select
	user_id, count(id) f_count
from
	friend
group by
	user_id) tmp1
group by
	f_count) s2

where
	s2.f_count >= s1.f_count
group by
	s1.f_count;
