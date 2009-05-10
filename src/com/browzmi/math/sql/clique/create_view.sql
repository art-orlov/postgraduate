insert into
    view (user_id, date, url_id)
select
	user_id,
	date,
	url_id
from
	browsing
where
	operation = 'add'
	and
	date between :FROM_DATE and :TO_DATE
