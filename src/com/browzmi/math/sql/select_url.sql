select
    *
from
    url
where
    host_id = :HOST_ID
    and
    file_hash = :FILE_HASH
    and
    file = :FILE