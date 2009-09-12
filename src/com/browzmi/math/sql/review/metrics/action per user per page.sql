select avg(u_per_url) from (
	select
		count(*) u_per_url, min(date), max(date), user_id, url_id
	from
		action
	group by
		user_id, url_id
) tmp1;