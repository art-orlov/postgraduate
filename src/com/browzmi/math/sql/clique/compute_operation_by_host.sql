insert into
    :operationTable (step, user_id, url_id, value)
select
	:step as step,
	user_id,
	host_id,
	count(user_id) as value
from
	:sourceTable as s
left join
	url as u
on
	s.url_id = u.id
group by
	s.user_id, u.host_id, step
