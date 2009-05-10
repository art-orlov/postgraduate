/*
hosts:
	youtube			34
	google			8
	facebook		35
	browzmi.bogspot 36
	browzmi			1

urls:
	http://www.youtube.com/			111
	http://www.google.com/			91
	http://www.facebook.com/		112
	http://browzmi.blogspot.com/	113
	http://www.browzmi.com/welcome/	114

select * from url where host_id = 36;
*/
select count(*) from favorite where url_id = 114 and operation = 'add';