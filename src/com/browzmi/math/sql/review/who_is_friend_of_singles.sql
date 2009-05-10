select
	count(distinct user_id)
from
	friend
where
	user_id2 in
(select
	user_id
from
	friend
group by
	user_id
having
	count(id) = 1)