package com.imooc.passbook.merchantplatform.service.impl;

import com.imooc.passbook.merchantplatform.constants.Constants;
import com.imooc.passbook.merchantplatform.service.ITokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 商户上传 token 的 service
 *
 * - 上传的 token 要：
 *   1. 以文件的形式存到指定目录的商户目录下
 *   2. 存到 redis 中
 */

@Slf4j
@Service
@AllArgsConstructor
public class TokenServiceImpl implements ITokenService {

    private final StringRedisTemplate redisTemplate;  // Redis 客户端

    @Override
    public void uploadTokenFile(String merchantsId, String passTemplateId, MultipartFile file) {
        try {
            File directory = new File(Constants.TOKEN_DIR + merchantsId);  // 不同的商户存放 token 的目录不同
            if (!directory.exists()) {  // 若目录不存在则创建
                directory.mkdir();
                log.info("Create directory: {}", Constants.TOKEN_DIR + merchantsId);
            }

            Path path = Paths.get(Constants.TOKEN_DIR, merchantsId, passTemplateId);  // 确保目录存在之后开始创建文件
            Files.write(path, file.getBytes());

            saveTokenInRedis(path, passTemplateId);  // 最后将 token 文件存入 redis（以 passTemplateId 为 key）
        } catch (IOException ex) {
            log.error("[Token upload]");
        }
    }

    private void saveTokenInRedis(Path path, String key) {
        Set<String> tokens;

        try (Stream<String> stream = Files.lines(path)) {  // 加载文件中的每一行到一个 Set 中去重（∵ token 不能有重复的）
            tokens = stream.collect(Collectors.toSet());
        } catch (IOException ex) {
            log.error("[saveTokenToRedis] failed to load the token file");
            return;
        }

        if (!CollectionUtils.isEmpty(tokens)) {
            redisTemplate.execute((RedisCallback<Object>) connection -> {  // 将 lambda 强转为 RedisCallback 类型
                for (String token : tokens)
                    connection.sAdd(key.getBytes(), token.getBytes());  // 将 tokens 逐个存入 redis（用 passTemplate id 作为 key）
                return null;
            });
        }
    }
}
