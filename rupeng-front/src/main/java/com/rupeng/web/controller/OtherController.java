package com.rupeng.web.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Setting;
import com.rupeng.pojo.User;
import com.rupeng.service.SettingService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.EmailUtils;
import com.rupeng.util.ImageCodeUtils;
import com.rupeng.util.JedisUtils;
import com.rupeng.util.JsonUtils;
import com.rupeng.util.SMSUtils;
import com.rupeng.util.UploadUtils;

@Controller
@MultipartConfig//处理上传文件需要此注解
public class OtherController {
	@Autowired
	private SettingService settingService;
	
	@RequestMapping("/")
	public ModelAndView index(){
		return new ModelAndView("index");
	}
	
	@RequestMapping("/imageCode.do")
	public void imageCode(HttpServletRequest request,HttpServletResponse response){
		ImageCodeUtils.sendImageCode(request.getSession(), response);
	}
	
	@RequestMapping("/smsCode.do")
	public @ResponseBody AjaxResult smsCode(String phone,HttpServletRequest request){
		if (CommonUtils.isEmpty(phone)) {
			return AjaxResult.errorInstance("手机号不能为空");
		}
		if (!CommonUtils.isPhone(phone)) {
			return AjaxResult.errorInstance("手机号格式错误");
		}
		
		Setting url = settingService.selectByName("sms_url");
		Setting username = settingService.selectByName("sms_username");
		Setting appKey = settingService.selectByName("sms_app_key");
		Setting template = settingService.selectByName("sms_code_template");
		
		//发送短信验证码
		SMSUtils.sendSMSCode(request.getSession(), url.getValue(), username.getValue(), appKey.getValue(), template.getValue(), phone);
		return AjaxResult.successInstance("短信验证码已发送");
	}
	
	@RequestMapping("/emailCode.do")
	public @ResponseBody AjaxResult emailCode(String email,HttpServletRequest request){
		if (CommonUtils.isEmpty(email)) {
			return AjaxResult.errorInstance("邮箱不能为空");
		}
		if (!CommonUtils.isEmail(email)) {
			return AjaxResult.errorInstance("邮箱格式错误");
		}
		
		/*
		Setting smtpServer = settingService.selectByName("email_smtp_host");
		Setting username = settingService.selectByName("email_username");
		Setting password = settingService.selectByName("email_password");
		Setting from = settingService.selectByName("email_from");
		
		//发送邮箱验证码
		EmailUtils.sendEmailCode(request.getSession(), smtpServer.getValue(), username.getValue(), password.getValue(), from.getValue(), email);
		*/
		
		Setting url = settingService.selectByName("sendcloud_email_url");
		Setting apiUser = settingService.selectByName("sendcloud_email_api_user");
		Setting apiKey = settingService.selectByName("sendcloud_email_api_key");
		Setting from = settingService.selectByName("sendcloud_email_from");
		Setting templateInvokeName = settingService.selectByName("sendcloud_email_code_template");
		
		EmailUtils.sendEmailCodeBySendCloud(request.getSession(), url.getValue(), apiUser.getValue(), apiKey.getValue(), from.getValue(), email, templateInvokeName.getValue());
		
		return AjaxResult.successInstance("邮箱验证码已发送");
	}
	
	//获取新消息通知
	@RequestMapping("/notification.do")
	public @ResponseBody AjaxResult notificationList(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		if (user==null) {
			return AjaxResult.errorInstance("用户未登录");
		}
		
		//获得当前登录用户所有的通知消息，并且把通知消息从redis里面删除掉
		Set<String> notifications = JedisUtils.smembersAndDel("notification_"+user.getId());
		
		//没有通知消息就返回null
		if (CommonUtils.isEmpty(notifications)) {
			return AjaxResult.successInstance(null);
		}
		
		//json转换为对象
		List<Object> list = new ArrayList<>();
		for (String string : notifications) {
			list.add(JsonUtils.toBean(string, Object.class));
		}
		
		return AjaxResult.successInstance(list);
	}
	
	//响应uediter的配置文件
	@RequestMapping(value="/upload.do",method=RequestMethod.GET)
	public ModelAndView uploadConfig(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		request.getRequestDispatcher("/lib/ueditor/jsp/config.json").forward(request, response);
		return null;
	}
	
	//处理上传请求
	@RequestMapping(value="/upload.do",method=RequestMethod.POST)
	public @ResponseBody Map<String, String> uploadSubmit(MultipartFile upfile,HttpServletRequest request) throws Exception{
		//创建目标文件
		String extension = UploadUtils.getExtension(upfile.getOriginalFilename());//文件后缀,带"."
		String parentDir = request.getServletContext().getRealPath("/upload");
		File parentFile = new File(parentDir);
		if (!parentFile.exists()) {
			parentFile.mkdirs();//不存在就创建父文件夹
		}
		String child = UUID.randomUUID().toString()+extension;//保存的文件名
		File file = new File(parentFile, child);
		upfile.transferTo(file);//先把上传文件保存在本地,上传七牛云后再删除
	
		//上传到七牛云
		Setting bucket = settingService.selectByName("qiniu_upload_bucket");
		Setting secretKey = settingService.selectByName("qiniu_upload_secretKey");
		Setting accessKey = settingService.selectByName("qiniu_upload_accessKey");
		UploadUtils.uploadFileByQiniuyun(file, accessKey.getValue(), secretKey.getValue(), bucket.getValue());
	
		file.delete();//删除本地保存的目标文件
		
		//响应信息
		Map<String, String> result = new HashMap<>();
		result.put("state", "SUCCESS");
		result.put("original", upfile.getOriginalFilename());
		result.put("title", child);
		result.put("type", extension);
		result.put("url", "/"+child);
		return result;
	}
}
