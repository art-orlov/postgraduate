select
	count(distinct user_id)
from
	friend
where
	user_id in (select user_id from action);


/*select
	count(distinct user_id)
from
	browsing
where
	user_id in (select user_id from friend)
	and
	user_id in (select user_id from action);*/