package com.rupeng.web.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rupeng.pojo.Card;
import com.rupeng.pojo.ClassesUser;
import com.rupeng.pojo.Question;
import com.rupeng.pojo.User;
import com.rupeng.service.ClassesUserService;
import com.rupeng.service.QuestionService;
import com.rupeng.service.UserCardService;
import com.rupeng.service.UserService;
import com.rupeng.util.AjaxResult;
import com.rupeng.util.CommonUtils;
import com.rupeng.util.EmailUtils;
import com.rupeng.util.ImageCodeUtils;
import com.rupeng.util.SMSUtils;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ClassesUserService classesUserService;
	@Autowired
	private UserCardService userCardService;
	@Autowired
	private QuestionService questionService;
	
	
	//注册页
	@RequestMapping(value="/register.do",method=RequestMethod.GET)
	public ModelAndView registerPage(){
		return new ModelAndView("user/register");
	}
	
	/**
	 * 邮箱验证码验证,用户注册
	 * @param email
	 * @param password
	 * @param emailCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/register.do",method=RequestMethod.POST)
	public ModelAndView registerSubmit(String email,String password,String emailCode,HttpServletRequest request){
		if (!CommonUtils.isEmail(email)) {
			return new ModelAndView("user/register","message","邮箱格式错误");
		}
		if (!CommonUtils.isLengthEnough(password, 6)) {
			return new ModelAndView("user/register","message","密码长度不能小于6位");
		}
		int result = EmailUtils.checkEmailCode(request.getSession(), email, emailCode);
		
		if (result == EmailUtils.CHECK_RESULT_FLASE) {
			return new ModelAndView("user/register","message","验证码错误");
		}else if (result == EmailUtils.CHECK_RESULT_INVALID) {
			return new ModelAndView("user/register","message","验证码超时");
		}
		
		//邮箱唯一性验证
		User user = new User();
		user.setEmail(email);
		if (userService.isExisted(user)) {
			return new ModelAndView("user/register","message","邮箱已经被注册");
		}
		
		user.setCreateTime(new Date());
		String passwordSalt = UUID.randomUUID().toString();
		user.setPasswordSalt(passwordSalt);
		user.setPassword(CommonUtils.calculateMD5(passwordSalt+password));
		userService.insert(user);
		return new ModelAndView("user/registerSuccess");
	}
	
	//登录页
	@RequestMapping(value="/login.do",method=RequestMethod.GET)
	public ModelAndView loginPage(){
		return new ModelAndView("user/login");
	}
	
	@RequestMapping(value="/login.do",method=RequestMethod.POST)
	public ModelAndView loginSubmit(String loginName,String password,String imageCode,HttpServletRequest request,HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView("user/login");
		
		 //检查验证码
        if (!ImageCodeUtils.checkImageCode(request.getSession(), imageCode)) {
            request.setAttribute("message", "验证码错误");
            return modelAndView;
        }
        //检查登录名是否是邮箱或者手机号
        if (!CommonUtils.isEmail(loginName) && !CommonUtils.isPhone(loginName)) {
            request.setAttribute("message", "请输入正确的邮箱或者手机号");
            return modelAndView;
        }
        //检查密码
        if (!CommonUtils.isLengthEnough(password, 6)) {
            request.setAttribute("message", "密码至少6位");
            return modelAndView;
        }
		
		//登录名 邮箱?手机?
        User user = userService.login(loginName,password);
        //登录成功
        
        if (user == null) {
            request.setAttribute("message", "登录名或者密码错误");
            return modelAndView;
        }
        //设置session
		request.getSession().setAttribute("user", user);
		
		//添加cookie，方便自动登录
        int maxAge = 60 * 60 * 24 * 14;//14天

        Cookie loginNameCookie = new Cookie("loginName", loginName);
        loginNameCookie.setMaxAge(maxAge);
        loginNameCookie.setPath("/");
        response.addCookie(loginNameCookie);

        Cookie passwordCookie = new Cookie("password", user.getPassword());
        passwordCookie.setMaxAge(maxAge);
        passwordCookie.setPath("/");
        response.addCookie(passwordCookie);

        //重定向到首页
        modelAndView.setViewName("redirect:/");
        return modelAndView;
	}
	
	//退出登录
	@RequestMapping("/logout.do")
	public ModelAndView logout(HttpServletRequest request,HttpServletResponse response){
		request.getSession().invalidate();
		
		//删除cookie，取消自动登录
        int maxAge = 0;//立即删除
        
        Cookie loginNameCookie = new Cookie("loginName", "");
        loginNameCookie.setMaxAge(maxAge);
        loginNameCookie.setPath("/");
        response.addCookie(loginNameCookie);

        Cookie passwordCookie = new Cookie("password", "");
        passwordCookie.setMaxAge(maxAge);
        passwordCookie.setPath("/");
        response.addCookie(passwordCookie);

		return new ModelAndView("redirect:/user/login.do");
	}
	
	//个人中心页
	@RequestMapping("/userInfo.do")
	public ModelAndView userInfoPage(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		if (user==null) {
			return new ModelAndView("redirect:/user/login.do");
		}
		
		return new ModelAndView("user/userInfo");
	}
	
	//修改个人信息
	@RequestMapping("/update.do")
	public @ResponseBody AjaxResult update(String name,String school,Boolean isMale,HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		user = userService.selectOne(user.getId());
		
		user.setName(name);
		user.setSchool(school);
		user.setIsMale(isMale);
		
		userService.update(user);
		
		//更新session中的user
		request.getSession().setAttribute("user", user);
		
		return AjaxResult.successInstance("保存成功");
	}
	
	//绑定手机号
	@RequestMapping("/bindPhone.do")
	public @ResponseBody AjaxResult update(String phone,String smsCode,HttpServletRequest request){
		if (!CommonUtils.isPhone(phone)) {
			return AjaxResult.errorInstance("手机号格式错误");
		}
		
		int result = SMSUtils.checkSMSCode(request.getSession(), phone, smsCode);
		
		if (result==SMSUtils.CHECK_RESULT_FLASE) {
			return AjaxResult.errorInstance("验证码错误");
		}else if (result==SMSUtils.CHECK_RESULT_INVALID) {
			return AjaxResult.errorInstance("验证码已失效，请重新点击发送");
		}
		
		User user = (User) request.getSession().getAttribute("user");
		user = userService.selectOne(user.getId());
		user.setPhone(phone);
		userService.update(user);
		
		//更新session中的user
		request.getSession().setAttribute("user", user);
		
		return AjaxResult.successInstance("绑定手机号成功");
	}
	
	//修改密码
	@RequestMapping(value="/passwordUpdate.do",method=RequestMethod.GET)
	public ModelAndView passwordUpdatePage(){
		return new ModelAndView("user/passwordUpdate");
	}
	@RequestMapping(value="/passwordUpdate.do",method=RequestMethod.POST)
	public @ResponseBody AjaxResult passwordUpdateSubmit(String password,String newpassword,HttpServletRequest request){
		if (!CommonUtils.isLengthEnough(password, 6)) {
			return AjaxResult.errorInstance("原密码长度不能小于6位");
		}
		if (!CommonUtils.isLengthEnough(newpassword, 6)) {
			return AjaxResult.errorInstance("新密码长度不能小于6位");
		}
		
		//检查原密码是否正确
        //从session中取出user，使用其id查询数据库，可避免session中的user信息不是最新的情况，当然如果可以保证session中的user信息总是和数据库中的一致，也可以使用session中的user
		User user = (User) request.getSession().getAttribute("user");
		user = userService.selectOne(user.getId());
		if (!user.getPassword().equalsIgnoreCase(CommonUtils.calculateMD5(user.getPasswordSalt()+password))) {
			return AjaxResult.errorInstance("原密码错误");
		}
		
		user.setPassword(CommonUtils.calculateMD5(user.getPasswordSalt()+newpassword));
		userService.update(user);
		
		//更新session中的user
        request.getSession().setAttribute("user", user);
        
		return AjaxResult.successInstance("密码修改成功");
	}
	
	//找回密码
	@RequestMapping(value="/passwordRetrieve.do",method=RequestMethod.GET)
	public ModelAndView passwordRetrievePage(){
		return new ModelAndView("user/passwordRetrieve");
	}
	
	@RequestMapping(value="/passwordRetrieve.do",method=RequestMethod.POST)
	public ModelAndView passwordRetrieveSubmit(String password,String email,String emailCode,HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView("user/passwordRetrieve");
		
		if (!CommonUtils.isEmail(email)) {
			modelAndView.addObject("message", "邮箱格式错误");
			return modelAndView;
		}
		if (!CommonUtils.isLengthEnough(password, 6)) {
			modelAndView.addObject("message", "密码长度不能小于6位");
			return modelAndView;
		}
		
		int result = EmailUtils.checkEmailCode(request.getSession(), email, emailCode);
		if (result==EmailUtils.CHECK_RESULT_FLASE) {
			modelAndView.addObject("message", "验证码错误");
			return modelAndView;
		}else if (result==EmailUtils.CHECK_RESULT_INVALID) {
			modelAndView.addObject("message", "验证码已失效，请重新点击发送");
			return modelAndView;
		}
		
		User user = new User();
		user.setEmail(email);
		user = userService.selectOne(user);
		if (user == null) {
            request.setAttribute("message", "此邮箱没有被注册");
            return modelAndView;
        }
        user.setPassword(CommonUtils.calculateMD5(user.getPasswordSalt() + password));
        userService.update(user);
		
		modelAndView.setViewName("user/passwordRetrieveSuccess");
		return modelAndView;
	}
	
	//学习中心
	@RequestMapping("/studentHome.do")
	public ModelAndView studentHome(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		
		ClassesUser classesUser = new ClassesUser();
		classesUser.setUserId(user.getId());
		//判断当前用户是否加入班级
		if (!classesUserService.isExisted(classesUser)) {
			return new ModelAndView("message","message","还没有加入任何班级");
		}
		
		//把当前用户所拥有的学习卡查询出来
		List<Card> cards = userCardService.selectSecondListByFirstId(user.getId());
		request.setAttribute("cards", cards);
		
		//当前用户(学生)提问的所有未解决问题以及回复的所有未解决问题
		List<Question> questionList = questionService.selectUnresolvedQuestionByStudentId(user.getId());
		
		request.setAttribute("questionList", questionList);
		return new ModelAndView("user/studentHome");
	}
	
	//教学中心
	@RequestMapping("/teacherHome.do")
	public ModelAndView teacherHome(HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
	
		if (user.getIsTeacher()==null || user.getIsTeacher() == false) {
			return new ModelAndView("message","message","您不是老师,不能进入教学中心");
		}
		
		//查询老师所在所有班级的所有学生提出的所有未解决的问题
		List<Question> questionList = questionService.selectUnresolvedQuestionByTeacherId(user.getId());
		
		return new ModelAndView("user/teacherHome","questionList",questionList);
	}
}
