insert into
	:relationTable (step, user_id1, user_id2, value)
(select
	cnt1.step,
	cnt1.user_id as user_id1,
	cnt2.user_id as user_id2,
	sum(cnt1.value + cnt2.value) as value
from
(select * from :sourceTable) as cnt1,
(select * from :sourceTable) as cnt2
where
	cnt1.user_id < cnt2.user_id
	and
	cnt1.url_id = cnt2.url_id
	and
	cnt1.step = cnt2.step
group by
	step, user_id1, user_id2)