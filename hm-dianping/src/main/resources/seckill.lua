-- 1. 參數列表
-- 1.1 優惠券id
local voucherId = ARGV[1]
-- 1.2 用戶id
local userId = ARGV[2]

-- 2. 資料key
-- 2.1 庫存key
local stockKey = 'seckill:stock:' .. voucherId
-- 2.2 訂單key
local OrderKey = 'seckill:order:' .. voucherId

-- 3. 腳本業務
-- 3.1 判斷庫存是否充足 get stockKey
if (tonumber(redis.call('get',stockKey)) <= 0) then
    -- 3.2. 庫存不足，返回1
    return 1
end
-- 3.2 判斷用戶是否下單 SISMEMBER orderKey userId
if (redis.call('sismember', OrderKey, userId) == 1) then
    -- 3.3 存在，說明重複下單，返回2
    return 2
end
-- 3.4 扣庫存 incrby stockKey -1
redis.call('incrby', stockKey, -1)
-- 3.5 下單(保存用戶) sadd orderKey userId
redis.call('sadd', OrderKey, userId)
return 0
