package org.tp;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2021/7/1
 */
public class AliyunSmsSenderImpl implements SmsSender {

    private SmsProperties.SmsMessage smsMessage;

    public AliyunSmsSenderImpl(SmsProperties.SmsMessage smsProperties) {
        this.smsMessage = smsProperties;
    }

    @Override
    public boolean send(String message) {
        System.out.println(smsMessage.toString()+"开始发送短信==》短信内容："+message);
        return true;
    }
}
