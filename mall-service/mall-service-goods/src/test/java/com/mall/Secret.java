package com.mall;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Secret {
    private static final Logger logger = LoggerFactory.getLogger(Secret.class);
    /** 加密的算法，这个算法是默认的，一定要与你的配置一致 */
    private static final String ALGORITHM_INFO = "PBEWithMD5AndDES";
    /** 加密的密钥 */
    private static final String SECRET_KEY = "ZQY!XJ^n4j8zM";

    @Test
    public void encrypt() {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        config.setAlgorithm(ALGORITHM_INFO);
        config.setPassword(SECRET_KEY);
        standardPBEStringEncryptor.setConfig(config);
        //对数据进行加密
        String encrypt = standardPBEStringEncryptor.encrypt("");

        logger.info(encrypt);
    }
}
