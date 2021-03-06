package com.rupeng.web.tag;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.rupeng.pojo.Question;
import com.rupeng.pojo.QuestionAnswer;
import com.rupeng.pojo.User;
import com.rupeng.util.CommonUtils;

public class QuestionAnswersTag extends SimpleTagSupport{
	private Question question;//问题
	private List<QuestionAnswer> rootAnswers;//根答案及其子答案
	private User user;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	
	public List<QuestionAnswer> getRootAnswers() {
		return rootAnswers;
	}

	public void setRootAnswers(List<QuestionAnswer> rootAnswers) {
		this.rootAnswers = rootAnswers;
	}


	@Override
	public void doTag() throws JspException, IOException {
		user = (User) getJspContext().getAttribute("user", PageContext.SESSION_SCOPE);
				
		//生成一段html文本
		StringBuilder builder = new StringBuilder();
		builder.append("<ul class=\"media-list\">");
		
		process(builder, rootAnswers);
		
		builder.append("</ul>");
		
		//把这段html文本输出到浏览器
		getJspContext().getOut().append(builder);
	}
	
	private void process(StringBuilder builder,List<QuestionAnswer> questionAnswers){
		//迭代结束条件
		if (CommonUtils.isEmpty(questionAnswers)) {
			return;
		}
		
		for (QuestionAnswer questionAnswer : questionAnswers) {
			builder.append("<div class=\"media\">");
			builder.append("	<div class=\"pull-left\">");
			builder.append("		<span class=\"label label-success\">").append(questionAnswer.getUsername()).append("</span>");
			builder.append("	</div>");
			builder.append("	<div class=\"media-body\">");
			builder.append("		<div>");
			builder.append("			<span>").append(simpleDateFormat.format(questionAnswer.getCreateTime())).append("</span>&nbsp;&nbsp;&nbsp;");
			if (question.getIsResolved()==null || question.getIsResolved()==false) {
				builder.append("			<a class=\"btn btn-xs btn-default\" reanswer=\"").append(questionAnswer.getId()).append("\">补充回答、追问</a>");
				if (user.getId()==question.getUserId()||(user.getIsTeacher()!=null&&user.getIsTeacher())) {
					builder.append("			<a class=\"btn btn-xs btn-default\" adopt=\"").append(questionAnswer.getId()).append("\">采纳</a>");
				}
			}else{
				if (questionAnswer.getIsAdopted() != null && questionAnswer.getIsAdopted()) {
                    PageContext pageContext = (PageContext) getJspContext();
                    builder.append("          <img width=\"100\" src=\"" + pageContext.getServletContext().getContextPath() + "/images/correct.png\" />");
                }
			}
			builder.append("		</div>");
			builder.append("		<div style=\"background-color: #F9F9F9;\">");
			builder.append("			<p>").append(questionAnswer.getContent()).append("</p>");
			builder.append("		</div>");
			//递归调用,拼接直接子答案
			process(builder, questionAnswer.getChildrenAnswerList());
			
			builder.append("	</div>");
			builder.append("</div>");
		}
		
	}
}
