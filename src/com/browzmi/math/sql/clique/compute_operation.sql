insert into
    :operationTable (step, user_id, url_id, value)
select
	:step as step,
	user_id,
	url_id,
	count(user_id) as value
from
	:sourceTable
group by
	user_id, url_id, step
