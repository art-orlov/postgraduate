/*
�� ��� �����:
	125 / 367 = 34% ����������� ��������
	190 / 367 = 51% ���������� ������
� ����������� ���������:
	80  / 166 = 48% ����������� ��������
	108 / 166 = 65% ����������� �����
*/
select
	count(u.user_id)
from
	(select distinct user_id from browsing) u
where
	u.user_id in (select user_id from friend)
	and
	exists(
		select
			*
		from
			browsing b
		where
			b.user_id = u.user_id
			and
			exists(
				select
					*
				from
					action a
				where
					a.url_id = b.url_id
					and
					a.user_id <> b.user_id /* in (select user_id2 from friend where user_id = b.user_id)*/
					and
					a.date between date_sub(b.date, interval 1 day) and b.date
			)
	)
	and
	u.user_id in (select user_id from browsing where date between date('2008-04-01') and date('2008-06-15'))