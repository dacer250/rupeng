<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% String ctxPath = request.getContextPath(); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="<%=ctxPath%>/css/style.css" />
<link rel="stylesheet" href="<%=ctxPath%>/css/index.css" />
<script type="text/javascript"
	src="<%=ctxPath%>/lib/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=ctxPath%>/lib/unslider.min.js"></script>
<script type="text/javascript" src="<%=ctxPath%>/js/index.js"></script>
<title>如鹏网——专注于大学生的IT职业技能在线学习社区</title>
</head>
<body>

	<!--登陆注册部分-->
	<div class="nav-bar container-fluid">
		<div class="container center">
			<div style="float: right;">
				<ul class="nav-list">
					<c:if test="${not empty user}">
					<li class="f12"><a href="<%=request.getContextPath()%>/user/userInfo.do">${(empty user.name)?user.email:user.name}</a></li>
					<li class="f12"><a href="<%=request.getContextPath()%>/user/logout.do">退出</a></li>
				</c:if>
				<c:if test="${empty user}">
					<li class="f12"><a href="<%=request.getContextPath()%>/user/login.do">登录</a></li>
					<li class="f12"><a href="<%=request.getContextPath()%>/user/register.do">注册</a></li>
				</c:if>
				</ul>
			</div>
		</div>
	</div>

	<!--主体-->
	<!--logo-->
	<div class="container center logo-box">
		<div class="fl logo">
			<a href="/" title="如鹏网"> <img alt="如鹏网"
				src="<%=ctxPath%>/images/logo.png">
			</a>
		</div>
		<div class="fl slogan">
			<h1 class="f16">专注于大学生就业的在线教育</h1>
		</div>
		<a href="javascript:;" id="btn-menu" class="btn-menu mobile fr">菜单</a>
	</div>


	<!--导航-->
	<div class="container center nav-box">
		<nav>
			<ul id="nav-list" class="nav-list">
				<li class="all"><a class="f18" href="#" title="全部课程">全部课程</a>
					<ul class="sub-list">
						<li><a href="/topics/javaeecourses/index.shtml"
							title="Java训练营"> <img alt="Java训练营"
								src="<%=ctxPath%>/images/85x85-java.png" />
								<h2>Java训练营</h2> <span>有趣、逗逼</span> <span>SSH、SSM架构通吃</span>
						</a></li>
						<li title=".Net训练营"><a href="/xlynet.shtml" title=".Net训练营">
								<img alt=".Net训练营"
								src="<%=ctxPath%>/images/85x85-net.png" />
								<h2>.Net训练营</h2> <span>剖析.Net内部原理</span> <span>讲解大网站开发技术</span>
						</a></li>
						<li title="C语言训练营"><a href="/News/10/338.shtml"
							title="C语言训练营"> <img alt="C语言训练营"
								src="<%=ctxPath%>/images/85x85-c.png" />
								<h2>C语言训练营</h2> <span>课余时间不浪费</span> <span>学点有用的</span>
						</a></li>
					</ul></li>
				<li><a class="f16" href="/News/10/165.shtml" title="0元学">“0元学”</a></li>
				<li><a class="f16" href="/News/13/list_1.shtml" title="就业喜报">就业喜报</a></li>
				<li><a class="f16" href="/aboutus.shtml" title="关于如鹏">关于如鹏</a></li>
				<li><a class="f16" href="/wholearning.shtml" title="他们正在学">他们正在学</a></li>
			</ul>
		</nav>
	</div>

	<div class="container center">
		<!--图片轮播-->
		<div class="slider-box" id="slider-box">
			<ul>
				<li
					style="background: url(<%=ctxPath%>/images/slider1.png) 50% 50% no-repeat;">
					<a href="/News/10/338.shtml" target="_blank"></a>
				</li>
				<li
					style="background: url(<%=ctxPath%>/images/slider2.png) 50% 50% no-repeat;">
					<a href="/News/9/357.shtml" target="_blank"></a>
				</li>
				<li
					style="background: url(<%=ctxPath%>/images/slider3.png) 50% 50% no-repeat;">
					<a href="/News/9/368.shtml" target="_blank"></a>
				</li>
			</ul>
		</div>

		<!--如鹏消息-->
		<div class="mess-box">
			<div class="mess-title">如鹏消息</div>
			<div class="mess-list">
				<ul class="list-icon">
					<li><a href="/News/9/368.shtml" target="_blank">信哥哥、信同事、信自己，所以我们来了</a>
					</li>
					<li><a href="/News/13/363.shtml" target="_blank">如鹏老师最高兴的时刻是……</a>
					</li>
					<li><a href="/News/9/357.shtml" target="_blank">揭露那些对如鹏的抹黑</a>
					</li>
					<li><a href="http://www.rupeng.com/Courses/Chapter/496"
						style="color: red" target="_blank">甲骨文放弃Java，我们怎么办</a></li>
					<li><a href="/News/9/373.shtml" target="_blank">如鹏训练营0820开班典礼</a>
					</li>
					<li><a href="/News/9/267.shtml" target="_blank">如鹏网出席全国青少年智创大会</a>
					</li>
					<li><a href="/News/9/310.shtml" target="_blank">感动！病床上手写的编程考试题</a>
					</li>
					<li><a href="/News/14/304.shtml" target="_blank">三十岁，java这条路，我才开始</a>
					</li>
					<li><a href="/News/9/302.shtml" target="_blank">如鹏网到中科院研究生院做讲座</a>
					</li>
					<li><a href="/News/13/295.shtml" target="_blank">应届生平均薪资8016元</a>
					</li>
				</ul>
			</div>
			<div class="mess-more">
				<a href="/News/9/list_1.shtml" class="more">更多&gt;&gt;</a>
			</div>
		</div>

		<!--加入方式-->
		<div class="join-box clearboth">
			<div class="fl consult">
				<a class="fw600 f15" target="_blank"
					href="http://crm2.qq.com/portalpage/wpa.php?uin=800110472&aty=0&a=0&curl=&ty=1"
					title="咨询课程">咨询课程</a>
			</div>
			<div class="fl ml-7">
				<a href="/yuema.shtml"><img alt="如鹏微信、微博、QQ群"
					src="<%=ctxPath%>/images/232x125-1.jpg" /></a>
			</div>
			<div class="fl ml-7">
				<a
					href="http://list.qq.com/cgi-bin/qf_invite?id=46c1bf5f1319f463adb1148d723dcfd77caf5158ad81c7c6"
					target="_blank"><img alt=""
					src="<%=ctxPath%>/images//232x125-2.jpg" /></a>
			</div>
			<div class="fl ml-7">
				<a href="/News/10/269.shtml"><img alt=""
					src="<%=ctxPath%>/images//232x125-3.jpg" /></a>
			</div>
			<div class="fl ml-7">
				<a href="/News/14/247.shtml"><img alt=""
					src="<%=ctxPath%>/images//232x125-4.jpg" /></a>
			</div>
		</div>
	</div>

	<!--课程中心-->
	<div class="lesson-box container-fluid">
		<div class="container center">
			<div class="title-box">
				<i></i>
				<h2 class="f22">课程中心</h2>
			</div>
			<div class="content">
				<ul>
					<li><a href="/topics/javaeecourses/index.shtml"><img
							alt="" src="<%=ctxPath%>/images/371x208-java.jpg" /></a>
						<div class="desc">
							<h3>学起来最轻松、最有趣的JavaEE就业课程</h3>
							<a class="btn btn-strong"
								href="/topics/javaeecourses/index.shtml">查看详情</a> <a
								class="btn btn-weak"
								href="http://crm2.qq.com/portalpage/wpa.php?uin=800110472&aty=0&a=0&curl=&ty=1">开班时间</a>
						</div></li>
					<li><a href="/xlynet.shtml"><img alt=""
							src="<%=ctxPath%>/images/371x208-net.jpg" /></a>
						<div class="desc">
							<h3>国内唯一讲解大型互联网架构的.Net课程！</h3>
							<a class="btn btn-strong" href="/xlynet.shtml">查看详情</a> <a
								class="btn btn-weak"
								href="http://crm2.qq.com/portalpage/wpa.php?uin=800110472&aty=0&a=0&curl=&ty=1">开班时间</a>
						</div></li>
					<li><a href="/xlyandroid.shtml"><img alt=""
							src="<%=ctxPath%>/images/371x208-android.jpg" /></a>
						<div class="desc">
							<h3>移动互联网时代，学Android不需要理由</h3>
							<a class="btn btn-strong" href="/xlyandroid.shtml">查看详情</a> <a
								class="btn btn-weak"
								href="http://crm2.qq.com/portalpage/wpa.php?uin=800110472&aty=0&a=0&curl=&ty=1">开班时间</a>
						</div></li>
				</ul>
			</div>
		</div>
	</div>

	<div class="free-box container center">
		<div class="title-box">
			<i></i>
			<h2 class="f22">
				<a href="freecourses.shtml">免费课程</a>
			</h2>
			<a href="freecourses.shtml"
				style="float: right; color: #57a9e2; font-size: 12px;">更多&gt;&gt;</a>
		</div>
		<div class="content">
			<ul class="lesson-list">
				<li>
					<div class="img">
						<span class="tip">入门课程</span> <a href="/Courses/Index/66"><img
							alt="" src="<%=ctxPath%>/images/220x123-1.jpg"></a>
					</div>
					<div class="desc">
						<h3>C语言也能干大事之游戏开发</h3>
						<div class="num join-num">
							<i></i> <span><span courseid="66">2417</span>人正在学习</span>
						</div>
						<a class="btn btn-strong" href="/Courses/Index/66">立即查看</a>
					</div>
				</li>
				<li>
					<div class="img">
						<span class="tip">答疑解惑</span> <a href="/Courses/Chapter/471"><img
							alt="" src="<%=ctxPath%>/images/220x123-2.jpg"></a>
					</div>
					<div class="desc">
						<h3>.Net为什么就业这么火</h3>
						<div class="num join-num">
							<i></i> <span><span chapterid="471">12368</span>人正在学</span>
						</div>
						<a class="btn btn-strong" href="/Courses/Chapter/471">立即查看</a>
					</div>
				</li>
				<li>
					<div class="img">
						<span class="tip">最新视频</span> <a href="/Courses/Index/51"><img
							alt="" src="<%=ctxPath%>/images/220x123-3.jpg"></a>
					</div>
					<div class="desc">
						<h3>史上最有趣、最易懂的Java入门教程</h3>
						<div class="num join-num">
							<i></i> <span><span courseid="51">4897</span>人正在学</span>
						</div>
						<a class="btn btn-strong" href="/Courses/Index/51">立即学习</a>
					</div>
				</li>
				<li>
					<div class="img">
						<span class="tip">真知灼见</span> <a href="/Courses/Chapter/496"><img
							alt="" src="<%=ctxPath%>/images/220x123-4.png"></a>
					</div>
					<div class="desc">
						<h3>听说Oracle要放弃Java了？我们怎么办？</h3>
						<div class="num join-num">
							<i></i> <span><span chapterid="496">4649</span>人正在学</span>
						</div>
						<a class="btn btn-strong" href="/Courses/Chapter/496">立即学习</a>
					</div>
				</li>
				<li>
					<div class="img">
						<span class="tip">学点东西</span> <a href="/Courses/Chapter/497"><img
							alt="" src="<%=ctxPath%>/images/220x123-5.png"></a>
					</div>
					<div class="desc">
						<h3>校园一卡通的原理是什么？有什么漏洞可以利用？可以用C语言开发？</h3>
						<div class="num join-num">
							<i></i> <span><span chapterid="497">1258</span>人正在学</span>
						</div>
						<a class="btn btn-strong" href="/Courses/Chapter/497">立即学习</a>
					</div>
				</li>
				<li>
					<div class="img">
						<span class="tip">最热视频</span> <a href="/Courses/Index/12"><img
							alt="" src="<%=ctxPath%>/images/220x123-6.jpg"></a>
					</div>
					<div class="desc">
						<h3>C语言也能干大事第三版</h3>
						<div class="num join-num">
							<i></i> <span><span courseid="12">12701</span>人正在学</span>
						</div>
						<a class="btn btn-strong" href="/Courses/Index/12">立即学习</a>
					</div>
				</li>
				<li>
					<div class="img">
						<span class="tip">高大上</span> <a href="/Courses/Chapter/299"><img
							alt="" src="<%=ctxPath%>/images/220x123-9.jpg"></a>
					</div>
					<div class="desc">
						<h3>你想知道大型互联网站用的什么技术吗？</h3>
						<div class="num join-num">
							<i></i> <span><span chapterid="299">12550</span>人正在学</span>
						</div>
						<a class="btn btn-strong" href="/Courses/Chapter/299">立即学习</a>
					</div>
				</li>
				<li>
					<div class="img">
						<span class="tip">深入教程</span> <a href="/Courses/Index/34"><img
							alt="" src="<%=ctxPath%>/images/220x123-7.jpg"></a>
					</div>
					<div class="desc">
						<h3>C语言也能干大事进阶篇：C语言开发网站</h3>
						<div class="num join-num">
							<i></i> <span><span courseid="34">6247</span>人正在学</span>
						</div>
						<a class="btn btn-strong" href="/Courses/Index/34">立即学习</a>
					</div>
				</li>
				<li>
					<div class="img">
						<span class="tip">答疑解惑</span> <a href="/Courses/Chapter/379"><img
							alt="" src="<%=ctxPath%>/images/220x123-8.jpg"></a>
					</div>
					<div class="desc">
						<h3>大二开始找工作</h3>
						<div class="num join-num">
							<i></i> <span><span chapterid="379">2967</span>人正在学</span>
						</div>
						<a class="btn btn-strong" href="/Courses/Chapter/379">立即学习</a>
					</div>
				</li>
				<li>
					<div class="img">
						<span class="tip">答疑解惑</span> <a href="/News/9/13.shtml"><img
							alt="" src="<%=ctxPath%>/images/220x123-10.jpg"></a>
					</div>
					<div class="desc">
						<h3>你学习中遇到的疑惑都可以在这里解决</h3>
						<div class="num join-num">
							<i></i> <span>百万人受益</span>
						</div>
						<a class="btn btn-strong" href="/News/9/13.shtml">立即查看</a>
					</div>
				</li>
			</ul>
		</div>
	</div>

	<div class="container-fluid article-box">
		<div class="container center">
			<div class="news-1 news-box fl">
				<div class="tit-box">
					<h3>
						<a href="/News/9/13.shtml">解惑文章</a>
					</h3>
					<a class="more" href="/News/9/13.shtml">更多&gt;&gt; </a>
				</div>
				<div class="content">
					<ul class="list-icon">
						<li><a href="/News/8/260.shtml">
								<div class="tit">[答疑解惑]选择做程序员是对还是错</div>
								<div class="view">
									<i></i> <span>热门阅读</span>
								</div>
						</a></li>
						<li><a href="/News/8/15.shtml">
								<div class="tit">【解惑】这么多技术我该怎么学</div>
								<div class="view">
									<i></i> <span>热门阅读</span>
								</div>
						</a></li>
						<li><a href="/News/8/16.shtml">
								<div class="tit">【解惑】计算机学习速成法</div>
								<div class="view">
									<i></i> <span>热门阅读</span>
								</div>
						</a></li>
						<li><a href="/News/8/39.shtml">
								<div class="tit">【解惑】我该怎么选择？选择就是放弃</div>
								<div class="view">
									<i></i> <span>热门阅读</span>
								</div>
						</a></li>
						<li><a href="/News/8/43.shtml">
								<div class="tit">量变到质变的感觉及时间安排的原则</div>
								<div class="view">
									<i></i> <span>热门阅读</span>
								</div>
						</a></li>
						<li><a href="/News/8/46.shtml">
								<div class="tit">一切编程语言都是纸老虎</div>
								<div class="view">
									<i></i> <span>热门阅读</span>
								</div>
						</a></li>
					</ul>
				</div>
			</div>

			<div class="news-2 news-box fr">
				<div class="tit-box">
					<h3>
						<a href="/News/7/list_1.shtml">招聘信息</a><small>JOB</small>
					</h3>
					<a class="more" href="/News/7/list_1.shtml">更多&gt;&gt; </a>
				</div>
				<div class="content">
					<ul>
						<li><a href="/News/9/308.shtml">如鹏网招聘.Net老师、Java老师、班主任</a></li>
						<li><a href="/News/7/271.shtml">厦门停开心来如鹏招聘Java、.Net、Android程序员</a></li>
						<li><a href="/News/7/278.shtml">佛山八戒印刷网络公司来如鹏招聘.Net工程师</a></li>
						<li><a href="/News/7/237.shtml">广州、郑州、青岛三家公司到如鹏网招聘.net工程师</a></li>
						<li><a href="/News/7/230.shtml">郑州云计划来如鹏招聘Java、.Net、Android工程师</a></li>
						<li><a href="/News/7/229.shtml">上海匡正来如鹏网招聘Android工程师</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<!-- 页脚 -->
	<div id="footerDiv" class="footer">
		<div class="top container-fluid">
			<div class="container center">
				<div class="left-box">
					<ul>
						<li><a href="#" title="加入我们">加入我们</a></li>
						<li><a href="#" title="联系我们">联系我们</a></li>
						<li><a href="#" title="付款方式">付款方式</a></li>
						<li><a href="#" title="服务条款">服务条款</a></li>
						<li><a href="#" title="版权声明">版权声明</a></li>
						<li><a href="#" title="友情链接">友情链接</a></li>
						<li><a>热线电话：010-67877100</a></li>
						<li><a>24小时热线：15910679760</a></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="bottom container-fluid">
			<div class="container center">
				<div>
					®北京如鹏信息科技有限公司2008-2016 <a href="http://www.miitbeian.gov.cn/"
						target="_blank">京ICP备14048059号-2</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>