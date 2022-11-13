package com.idempotent.token.service.util.impl;

import com.idempotent.token.service.util.TokenUtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TokenUtilServiceImpl implements TokenUtilService {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    /**
     * 存入 Redis 的 Token 键的前缀
     */
    private static final String IDEMPOTENT_TOKEN_PREFIX = "idempotent_token:";

    /**
     * 创建 Token 存入 Redis，并返回该 Token
     *
     * @param value 用于辅助验证的 value 值
     * @return 生成的 Token 串
     */
    @Override
    public String genToken(String value) {
        String token = UUID.randomUUID().toString();

        String key = IDEMPOTENT_TOKEN_PREFIX + token;

        redisTemplate.opsForValue().set(key, value, 5, TimeUnit.MINUTES);

        return token;
    }

    /**
     * 验证 Token 正确性
     *
     * @param token token 字符串
     * @param value value 存储在Redis中的辅助验证信息
     * @return 验证结果
     */
    @Override
    public Boolean validToken(String token, String value) {
        // 设置 Lua 脚本，其中 KEYS[1] 是 key，KEYS[2] 是 value
        String luaScript = "if redis.call('get', KEYS[1]) == KEYS[2] " +
                " then " +
                " return redis.call('del', KEYS[1]) " +
                " else " +
                " return 0" +
                " end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);

        //根据前缀拼接redis key
        String key = IDEMPOTENT_TOKEN_PREFIX + token;

        //执行lua脚本
        Long result = redisTemplate.execute(redisScript, Arrays.asList(key, value));

        //根据redis的执行结果判断，如果返回结果不为空，则表示是第一次请求，如果返回的结果为空，则表示已经请求过了
        //因为请求完之后，会将redis中对应的token删除
        if (result != null && result != 0L) {
            log.info("valid token successful, token = {}, key = {}, value = {}", token, key, value);
            return true;
        }

        log.info("valid token fail, token = {}, key = {}, value = {}", token, key, value);
        return false;
    }
}
