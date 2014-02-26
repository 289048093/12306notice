package lizhao.util;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.sun.org.apache.xerces.internal.impl.PropertyManager;

public final class MailUtil {
	/**
	 * 默认构造方法
	 */
	private MailUtil(){
	};

	/**
	 * 线程异步发送邮件
	 * 
	 */

	public static void sendMail(String subject, String content, String email) {
		Thread sendMailThread = new SendMailThread(subject, content, email);
		sendMailThread.start();
	}

	public static void sendMail(String subject, String content, String email, Boolean force) {
		Thread sendMailThread = new SendMailThread(subject, content, email, force);
		sendMailThread.start();
	}

	/**
	 * 非线程发送邮件
	 * 
	 * @param subject
	 * @param content
	 * @param email
	 * @param force
	 *            是否强制发送邮件
	 */
	public static void sendMailSynchron(String subject, String content, String email, Boolean force) {
		// 先判断是否开启发邮件功能，如果未开启，直接退出。
//		String enable = "1";// PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_ENABLE);
//		if (!force && !enable.equals("0")) {
//			return;
//		}
		// 邮件服务器发送代码。
		try {
			JavaMailSenderImpl javaMailSenderImpl = getMailSender();
			MimeMessage msg = javaMailSenderImpl.createMimeMessage();
			MimeMessageHelper helper;
			helper = new MimeMessageHelper(msg, false, "utf-8");
			String from = "52673406@qq.com";//PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_FROM);
//			if (StringUtil.isBlank(from)) {
//				throw new MessagingException("发送地址不能为空");
//			}
//			if (StringUtil.isBlank(email)) {
//				throw new MessagingException("收信地址不能为空");
//			}
//			if (StringUtil.isBlank(subject)) {
//				throw new MessagingException("主题不能为空");
//			}
//			if (StringUtil.isBlank(content)) {
//				throw new MessagingException("内容不能为空");
//			}
			helper.setFrom(from);
			helper.setTo(email);
			helper.setSubject(subject);
			helper.setText(content, true);
			javaMailSenderImpl.send(msg);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	/**
	 * 获取邮件发送器
	 * 
	 * 发送邮件错误
	 * 
	 * @return
	 * @throws MessagingException
	 *             邮件发送异常
	 */
	private static JavaMailSenderImpl getMailSender() throws MessagingException {
		String password = "chao123jing";//PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_PASSWORD, true);
		String username = "52673406@qq.com";//PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_USERNAME, true);
		String host = "smtp.qq.com";//PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_HOST, true);
		String port = "25";//PropertyManager.getInstance().getDbProperty(PropertyManager.DB_EMAIL_PORT, true);
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.auth", true);
//		if (StringUtil.isBlank(password)) {
//			throw new MessagingException("邮件密码为空");
//		}
//		if (StringUtil.isBlank(username)) {
//			throw new MessagingException("邮件用户名为空");
//		}
//		if (StringUtil.isBlank(host)) {
//			throw new MessagingException("主机为空");
//		}
//		if (StringUtil.isBlank(port)) {
//			throw new MessagingException("端口为空");
//		}
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		javaMailSenderImpl.setPassword(password);
		javaMailSenderImpl.setUsername(username);
		javaMailSenderImpl.setHost(host);
		javaMailSenderImpl.setPort(Integer.parseInt(port));
		//解决 javax.mail.MessagingException: 501 Syntax: HELO hostname
//		String hostIp = PropertyManager.getInstance().getXMLProperty(PropertyManager.XML_HOST);
//		javaMailProperties.put("mail.smtp.localhost", hostIp);
		javaMailSenderImpl.setJavaMailProperties(javaMailProperties);
		return javaMailSenderImpl;
	}
}

class SendMailThread extends Thread {
	private String subject;
	private String content;
	private String email;
	private Boolean force = false;

	public SendMailThread(String subject, String content, String email){
		this.subject = subject;
		this.content = content;
		this.email = email;
	}

	public SendMailThread(String subject, String content, String email, Boolean force){
		this.subject = subject;
		this.content = content;
		this.email = email;
		this.force = force;
	}

	@Override
	public void run() {
		MailUtil.sendMailSynchron(subject, content, email, force);
	}
	public static void main(String[] args) throws InterruptedException {
        MailUtil.sendMailSynchron("test", "这是一个测试", "289048093@qq.com",true);
        Thread.sleep(5000);
    }
}
