package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.dto.ScrollResult;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Blog;
import com.hmdp.entity.Follow;
import com.hmdp.entity.User;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IFollowService;
import com.hmdp.service.IUserService;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import com.sun.net.httpserver.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hmdp.utils.RedisConstants.BLOG_LIKED_KEY;
import static com.hmdp.utils.RedisConstants.FEED_KEY;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

    @Autowired
    private IFollowService followService;

    @Override
    public Result queryHotBlog(Integer current) {
        // 根据用户查询
        Page<Blog> page = query()
                .orderByDesc("liked")
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        // 查询用户
        records.forEach(blog -> {
            this.queryBlogUser(blog);
            this.isBlogLiked(blog);
        });
        return Result.ok(records);
    }

    @Override
    public Result queryBlogById(Long id) {
        // 1. 查詢blog
        Blog blog = getById(id);
        if (blog == null) {
            return Result.fail("筆記不存在!");
        }
        // 2. 查詢blog有關的用戶
        queryBlogUser(blog);
        // 3. 查詢blog是否點讚
        isBlogLiked(blog);
        return Result.ok(blog);
    }

    private void isBlogLiked(Blog blog) {
        // 1. 獲取登陸用戶
        UserDTO user = UserHolder.getUser();
        if (user == null) {
            // 用戶未登入，無須查詢是否點讚
            return;
        }
        Long userId = user.getId();
        // 2. 判斷當前登陸用戶是否已經點讚
//        Boolean isMember = stringRedisTemplate.opsForSet().isMember(BLOG_LIKED_KEY + blog.getId(), userId.toString());
        Double score = stringRedisTemplate.opsForZSet().score(BLOG_LIKED_KEY + blog.getId(), userId.toString());
        blog.setIsLike(score != null);
    }

    @Override
    public Result likeBlog(Long id) {
        // 1. 獲取登陸用戶
        Long userId = UserHolder.getUser().getId();
        // 2. 判斷當前登陸用戶是否已經點讚
//        Boolean isMember = stringRedisTemplate.opsForSet().isMember(BLOG_LIKED_KEY + id, userId.toString());
        Double score = stringRedisTemplate.opsForZSet().score(BLOG_LIKED_KEY + id, userId.toString());
        if (score == null) {
            // 3. 如果未點讚，可以點讚
            // 3.1. 資料庫點讚 + 1
            boolean isSuccess = update().setSql("liked = liked + 1").eq("id", id).update();
            // 3.2. 保存用戶到 Redis 的 set 集合 zadd key value score
            if (isSuccess) {
//                stringRedisTemplate.opsForSet().add(BLOG_LIKED_KEY + id, userId.toString());
                stringRedisTemplate.opsForZSet().add(BLOG_LIKED_KEY + id, userId.toString(), System.currentTimeMillis());
            }
        } else {
            // 4. 如果以點，取消點讚
            // 4.1. 資料庫點讚數 -1
            boolean isSuccess = update().setSql("liked = liked - 1").eq("id", id).update();
            // 4.2. 把用戶從 Redis 的 set 集合移除
//            stringRedisTemplate.opsForSet().remove(BLOG_LIKED_KEY + id, userId.toString());
            stringRedisTemplate.opsForZSet().remove(BLOG_LIKED_KEY + id, userId.toString());
        }
        return Result.ok();
    }

    @Override
    public Result queryBlogLikes(Long id) {
        // 1. 查詢 top5 的點讚用戶 zrange key 0 4
        Set<String> top5 = stringRedisTemplate.opsForZSet().range(BLOG_LIKED_KEY + id, 0, 4);
        if (top5 == null || top5.isEmpty()) {
            return Result.ok(Collections.emptyList());
        }
        // 2. 解析出其中的用戶id
        List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
        String idStr = StrUtil.join(",", ids);
        // 3. 根據用戶id查詢用戶 WHERE id IN (5, 1) ORDER BY FIELD(id, 5 ,1)
        List<UserDTO> userDTOS = userService.query()
                .in("id", ids)
                .last("ORDER BY FIELD(id," + idStr + ")").list()
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                .collect(Collectors.toList());
        // 4. 返回
        return Result.ok(userDTOS);
    }

    @Override
    public Result saveBlog(Blog blog) {
        // 1. 获取登录用户
        UserDTO user = UserHolder.getUser();
        blog.setUserId(user.getId());
        // 2. 保存探店筆記
        boolean isSuccess = save(blog);
        if (!isSuccess) {
            return Result.ok("新增筆記失敗");
        }
        // 3. 查詢筆記作者的所有粉絲 select * from tb_follow where follow_user_id = ?
        List<Follow> follows = followService.query().eq("follow_user_id", user.getId()).list();
        // 4. 推祟筆記 id 給所有粉絲
        for (Follow follow : follows) {
            // 4.1 獲取粉絲id
            Long userId = follow.getUserId();
            // 4.2 推送
            String key = FEED_KEY + userId;
            stringRedisTemplate.opsForZSet().add(key, blog.getId().toString(), System.currentTimeMillis());
        }
        // 5. 返回id
        return Result.ok(blog.getId());
    }

    @Override
    public Result queryBlogOfFollow(Long max, Integer offset) {
        // 1. 獲取當前用戶
        Long userId = UserHolder.getUser().getId();
        // 2. 查詢收件箱 ZREVERANGEBYSCORE key Max Min LIMIT offset count
        String key = FEED_KEY + userId;
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(key, 0, max, offset, 2);
        // 3. 非空判斷
        if (typedTuples == null || typedTuples.isEmpty()) {
            return Result.ok();
        }
        // 4. 解析資料: blogId、minTime(時間戳)、offset
        List<Long> ids = new ArrayList<>(typedTuples.size());
        long minTime = 0;
        int count = 1;
        for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
            // 4.1 獲取 id
            ids.add(Long.valueOf(tuple.getValue()));
            // 4.2 獲取分數(時間戳)
            long time = tuple.getScore().longValue();
            if (time == minTime) {
                count++;
            } else {
                minTime = time;
                count = 1;
            }
        }
        // 5. 根據 id 查詢 blog
        String idStr = StrUtil.join(",", ids);
        List<Blog> blogs = query().in("id", ids).last("ORDER BY FIELD(id," + idStr + ")").list();

        for (Blog blog : blogs) {
            // 5.1 查詢blog有關的用戶
            queryBlogUser(blog);
            // 5.2 查詢blog是否點讚
            isBlogLiked(blog);
        }

        // 6. 封裝並返回
        ScrollResult r = new ScrollResult();
        r.setList(blogs);
        r.setOffset(count);
        r.setMinTime(minTime);

        return Result.ok(r);
    }

    private void queryBlogUser(Blog blog) {
        Long userId = blog.getUserId();
        User user = userService.getById(userId);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
    }
}
