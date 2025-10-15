package com.hmdp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_TYPE_KEY;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryShopType() {

        String key = CACHE_SHOP_TYPE_KEY;
        // 1. 從 Redis 查詢商鋪類型列表
        List<String> listJson = stringRedisTemplate.opsForList().range(key, 0, -1);
        // 2. 判斷是否存在
        if (!CollectionUtil.isEmpty(listJson)) {
            long start = System.currentTimeMillis();
            // 3. 存在,直接返回
            List<ShopType> shopTypes = listJson.stream().map(item -> JSONUtil.toBean(item, ShopType.class))
                    .sorted(Comparator.comparingInt(ShopType::getSort))
                    .collect(Collectors.toList());
            long end = System.currentTimeMillis();
            System.out.println("Redis_Time = " + (end - start) + "ms");
            return Result.ok(shopTypes);
        }
        long start = System.currentTimeMillis();
        // 4. 資料庫查詢商鋪類型列表
        List<ShopType> shopTypes = query().orderByAsc("sort").list();
        // 5. 不存在,返回錯誤
        if (CollectionUtil.isEmpty(shopTypes)) {
            return Result.fail("商鋪類行為空");
        }
        // 6. 資料存在, 寫入 Redis
        List<String> shopTypeJson = shopTypes.stream().sorted(Comparator.comparingInt(ShopType::getSort))
                .map(JSONUtil::toJsonStr)
                .collect(Collectors.toList());
        stringRedisTemplate.opsForList().leftPushAll(key, shopTypeJson);
        long end = System.currentTimeMillis();
        System.out.println("DB_Time = " + (end - start) + "ms");
        // 7. 存在,返回
        return Result.ok(shopTypes);
    }
}
