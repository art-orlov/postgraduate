select count(user_id) u_count, d_count from (
	select user_id, count(dt) d_count from (
			select user_id, date(date) dt from browsing group by user_id, dt
	) tmp1
	group by user_id
) tmp2
group by d_count;


select avg(d_count) from (
	select user_id, count(dt) d_count from (
			select user_id, date(date) dt from browsing group by user_id, dt
	) tmp1
	group by user_id
) tmp2;


select
	s1.d_count,
	sum(s2.u_count)
from

(select count(user_id) u_count, d_count from (
	select user_id, count(dt) d_count from (
			select user_id, date(date) dt from browsing group by user_id, dt
	) tmp1
	group by user_id
) tmp2
group by d_count) s1
,
(select count(user_id) u_count, d_count from (
	select user_id, count(dt) d_count from (
			select user_id, date(date) dt from browsing group by user_id, dt
	) tmp1
	group by user_id
) tmp2
group by d_count) s2

where
	s2.d_count <= s1.d_count
group by
	s1.d_count;