-- 比較線程標示與鎖中的標示是否一致
if (redis.call('get', KEYS[1]) == ARGV[1]) then
    -- 釋放鎖 del key
    redis.call('del', KEYS[1])
end
return 0