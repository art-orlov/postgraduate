create function `fnComputeDay`(date datetime)
    returns datetime
begin
    declare result datetime;

    set result = date_add(date, interval 12 hour);
    set result = date_sub(result, interval (3600 * extract(hour from result) + 60 * extract(minute from result) + extract(second from result)) second);

    return result;
end;
