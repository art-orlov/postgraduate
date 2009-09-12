select avg(b_count), min(b_count), max(b_count) from (
    select
        user_id, count(id) b_count
    from
        browsing
    group by
        user_id
) tmp1;


select per_day, count(user_id) u_count from (
select
	user_id, count(*) per_day
from
	browsing
group by
	user_id, date(date)
) tmp1
group by
	per_day
order by
	per_day;


select
	s1.b_count,
	sum(s2.u_count)
from

(select count(user_id) u_count, b_count from
(select
	user_id, count(id) b_count
from
	browsing
group by
	user_id) tmp1
group by
	b_count) s1
,
(select count(user_id) u_count, b_count from
(select
	user_id, count(id) b_count
from
	browsing
group by
	user_id) tmp1
group by
	b_count) s2
where
	s2.b_count <= s1.b_count
group by
	s1.b_count;