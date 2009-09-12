delete from 
	browsing
where
	url_id = 1
	or
	operation = 'remove';

delete from
	favorite_tag
where
	favorite_id in (select id from favorite where url_id = 1 or operation <> 'add');

delete from
    favorite
where
    favorite.operation <> 'add'
    or
    favorite.url_id = 1;

delete from rate where url_id = 1;

delete from comment where url_id = 1;