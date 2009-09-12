delete from
	browsing
where
	date not between date('2008-04-01') and date('2008-05-16');

delete from favorite_tag;

delete from
	favorite
where
	date not between date('2008-04-01') and date('2008-05-16');

delete from
	comment
where
	date not between date('2008-04-01') and date('2008-05-16');

delete from
	rate
where
	date not between date('2008-04-01') and date('2008-05-16');

delete from friend where user_id not in (
    select user_id from browsing
);

delete from friend where user_id2 not in (
    select user_id from browsing
);