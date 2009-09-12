delete from
    friend
where
	user_id = 'MMTQE2Ku7HJKBO6TKJenUC'
	or
	user_id2 = 'MMTQE2Ku7HJKBO6TKJenUC';

delete from
	friend
where
	user_id not in (select user_id from browsing)
	or
	user_id2 not in (select user_id from browsing);
