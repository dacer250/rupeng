package com.rupeng.util;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 短信验证码工具类
 */
public class SMSUtils {

    private static final Logger logger = LogManager.getLogger(SMSUtils.class);

    private static final String SESSION_KEY_SMS_CODE = "sessionKeySMSCode";//表示邮件验证码
    private static final String SESSION_KEY_SMS_PHONE = "sessionKeySMSPhone";//表示邮件收件人
    private static final String SESSION_KEY_SMS_TIME = "sessionKeySMSTime";//表示邮件发送时间
    private static final String SESSION_KEY_SMS_REMAIN_CHECK_COUNT = "sessionKeySMSRemainCheckCount";//表示剩余验证次数

    public static final int CHECK_RESULT_TRUE = 1;//表示验证成功
    public static final int CHECK_RESULT_FLASE = 2;//表示验证失败
    public static final int CHECK_RESULT_INVALID = 3;//表示验证码失效，需要重新发送

    public static int checkSMSCode(HttpSession session, String phone, String requestCode) {
        if (phone == null || requestCode == null) {
            logger.debug("检查短信验证码失败，phone{}，requestCode：{}", phone, requestCode);
            return CHECK_RESULT_FLASE;
        }
        String sessionPhone = (String) session.getAttribute(SESSION_KEY_SMS_PHONE);
        if (!phone.equals(sessionPhone)) {
            logger.debug("检查短信验证码失败，phone{}，requestCode：{}，sessionPhone：{}", phone, requestCode, sessionPhone);
            return CHECK_RESULT_FLASE;
        }
        Long sendTime = (Long) session.getAttribute(SESSION_KEY_SMS_TIME);
        //验证码过期失效
        if (sendTime - System.currentTimeMillis() > 1000 * 60 * 30) {
            logger.debug("检查短信验证码失败，验证码过期，phone：{}，requestCode：{}", phone, requestCode);
            return CHECK_RESULT_INVALID;
        }

        String sessionCode = (String) session.getAttribute(SESSION_KEY_SMS_CODE);
        Integer remainCount = (Integer) session.getAttribute(SESSION_KEY_SMS_REMAIN_CHECK_COUNT);
        
        //如果验证码不匹配
        if (!requestCode.equalsIgnoreCase(sessionCode)) {

            remainCount--;
            //没有剩余验证次数了
            if (remainCount <= 0) {
                session.removeAttribute(SESSION_KEY_SMS_CODE);
                session.removeAttribute(SESSION_KEY_SMS_REMAIN_CHECK_COUNT);
                session.removeAttribute(SESSION_KEY_SMS_TIME);
                session.removeAttribute(SESSION_KEY_SMS_PHONE);
                logger.debug("检查短信验证码失败，验证码错误，phone：{}，requestCode：{}，sessionCode：{}，remainCount：{}", phone,
                        requestCode, sessionCode, remainCount);
                return CHECK_RESULT_INVALID;
            } else {
                session.setAttribute(SESSION_KEY_SMS_REMAIN_CHECK_COUNT, remainCount);
                logger.debug("检查短信验证码失败，验证码错误，phone：{}，requestCode：{}，sessionCode：{}，remainCount：{}", phone,
                        requestCode, sessionCode, remainCount);
                return CHECK_RESULT_FLASE;
            }
        }

        //验证码匹配
        session.removeAttribute(SESSION_KEY_SMS_CODE);
        session.removeAttribute(SESSION_KEY_SMS_REMAIN_CHECK_COUNT);
        session.removeAttribute(SESSION_KEY_SMS_TIME);
        session.removeAttribute(SESSION_KEY_SMS_PHONE);

        logger.debug("检查邮件验证码成功，phone：{}，requestCode：{}，sessionCode：{}，remainCount：{}", phone, requestCode, sessionCode,
                remainCount);
        return CHECK_RESULT_TRUE;
    }

    public static void sendSMSCode(HttpSession session, String url, String username, String appKey, String template,
            String phone) {
        // 得到一个随机的5位数字的验证码
        String smsCode = new Random().nextInt(90000) + 10000 + "";
        StringBuilder builder = new StringBuilder();
        builder.append(url).append("?userName=").append(username);
        builder.append("&appKey=").append(appKey);
        builder.append("&templateId=").append(template);
        builder.append("&code=").append(smsCode);
        builder.append("&phoneNum=").append(phone);

        try {
            String result = HttpUtils.get(builder.toString());
            SMSResult smsResult = JsonUtils.toBean(result, SMSResult.class);
            if (smsResult.getCode() == 0) {

                session.setAttribute(SESSION_KEY_SMS_CODE, smsCode);
                session.setAttribute(SESSION_KEY_SMS_PHONE, phone);
                session.setAttribute(SESSION_KEY_SMS_TIME, System.currentTimeMillis());
                session.setAttribute(SESSION_KEY_SMS_REMAIN_CHECK_COUNT, 5);

                logger.debug("发送短信验证码成功，phone：{}，code：{}", phone, smsCode);
            } else {
                throw new RuntimeException("发送短信验证码失败，发送结果为：" + result);
            }
        } catch (Exception e) {
            logger.debug("发送短信验证码失败，phone：{}，code：{}", phone, smsCode, e);
            throw new RuntimeException("发送短信验证码失败", e);
        }
    }

    @SuppressWarnings("unused")
    private static class SMSResult {
        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
