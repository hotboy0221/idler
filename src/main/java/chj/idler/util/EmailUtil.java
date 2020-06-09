package chj.idler.util;

import chj.idler.service.model.UserModel;
import chj.idler.service.model.VideoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailUtil {
    @Value("${spring.mail.username}")
    private String userName;
    @Value("${server.weburl}")
    private String weburl;
    private String nickname="Idler";
    @Autowired
    private JavaMailSender mailSender;

    private final String ENCODING="UTF-8";

    public void newVideoNotify(String [] toWho, VideoModel videoModel)throws MessagingException{
        StringBuilder sb=new StringBuilder();
        sb.append("<a title='点击观看' href='"+videoModel.getUrl()+"' style='color:orange;font-size:3em;display:block;'>"+videoModel.getTitle()+"</a>");
        sb.append("<img src='"+videoModel.getPicture()+"' style='width:380px'>");
        sendHtmlMail("您追的【"+videoModel.getName()+"】更新啦！",sb.toString(),toWho);
    }

    public void activateUser(String registerToken, UserModel userModel)throws MessagingException{
        StringBuilder sb=new StringBuilder();
        sb.append("<h1>尊敬的"+userModel.getUsername()+"你好</h1>");
        sb.append("<p>您需要在五分钟内<a style=\"color:orange;\" href=\""+weburl+"/users/register?token="+registerToken+"\">点击链接</a>来激活您的账号，若非本人操作请忽略本邮件</p>");
        sendHtmlMail("您正在进行注册",sb.toString(),new String[]{userModel.getEmail()});
    }
    public void sendHtmlMail(String subject, String content, String[] toWho) throws MessagingException{

        //检验参数：邮件主题、收件人、邮件内容必须不为空才能够保证基本的逻辑执行
        if(subject == null||toWho == null||toWho.length == 0||content == null){
            throw new RuntimeException("模板邮件无法继续发送，因为缺少必要的参数！");
        }
        //html
        MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true,ENCODING);
            //设置邮件的基本信息
            handleBasicInfo(helper,subject,content,toWho);
            //发送邮件
            mailSender.send(mimeMessage);
    }

    public void handleBasicInfo(MimeMessageHelper mimeMessageHelper,String subject,String content,String[] toWho)throws MessagingException{
            //设置发件人

            mimeMessageHelper.setFrom(nickname+'<'+userName+'>');
            //设置邮件的主题
            mimeMessageHelper.setSubject(subject);
            //设置邮件的内容
            mimeMessageHelper.setText(content,true);
            //设置邮件的收件人
            mimeMessageHelper.setTo(toWho);
    }

}
