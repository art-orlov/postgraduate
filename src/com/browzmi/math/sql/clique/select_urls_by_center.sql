select 
	url_id
from
	(select COUNT(user_id) as cnt, url_id from :operationTable where step = :step and user_id in (:center) group by url_id) as subq
where
	subq.cnt >= :centerSize
