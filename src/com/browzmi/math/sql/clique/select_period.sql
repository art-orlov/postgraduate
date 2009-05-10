select
	step, COUNT(*)
from
	v_hour
group by
	step
into outfile 'temp.txt'