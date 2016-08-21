# GDUFE_JW_API
广财教务系统非官方API  
通过模拟登陆实现查分数、查课表、教学计划，若你想要在App、网站中实现查分则可使用。

# USAGE
 - 各操作均需先登录，登陆用了教务的 `default2.aspx` 避开验证码。  
 - 查成绩和课表分两种：
查当前学期成绩和查指定学期成绩，当前学期成绩是登陆后直接Get到的信息，比获取指定学期成绩更容易，对教务系统访问次数也更少。
 - 在客户端使用前可以先用`TestParm.java`（记得填学号密码）或者允许GET请求后浏览器测试下，相当于客户端
 - 修改代码前建议先看看 `src/help.md` 的说明

**如需查课表给用户最好选择查课程介绍而不是直接查课表：**  

	1. 5,6,7节相同课这种情况在课程介绍里是一个项而在课表里却是两个项，选课程介绍方便给不同课程填充不同颜色。  
	2. 课程介绍在教务系统上先于课表公布


## 登陆 / 其他操作均需先登录

	POST http://localhost:8080/jw/main.do
	参数 action=login&uname=学号&upwd=密码

返回
	
	字符串："登陆成功" or "登陆失败"

## 查当前学期课表

	POST http://localhost:8080/jw/main.do
	参数 action=curSemesterKeBiao

返回Json数组格式的课表，注意type可能为空，因为教务系统中有些课是没有显示课程类型的

	[
	    {
	        "name": "数据结构",
	        "type": "必修",
	        "time": "周一第1,2节{第1-16周}",
	        "teacher": "Demo-匿名",
	        "location": "拓新楼(SS1)135"
	    },
	    {
	        "name": "可视化程序设计",
	        "type": "任选",
	        "time": "周二第1,2节{第1-16周}",
	        "teacher": "Demo-匿名1",
	        "location": "励学楼(SJ1)401"
	    },
	    {
	        "name": "婚姻家庭纠纷法律实务",
	        "type": "",
	        "time": "周四第1,2节{第1-16周}",
	        "teacher": "Demo-匿名2",
	        "location": "拓新楼(SS1)335"
	    }
	]

失败则返回

	[]

可能是未登录，也可能是教务系统Hold不住了

## 查当前学期成绩

	POST http://localhost:8080/jw/main.do
	参数 action=curSemesterScore

返回Json数组格式的成绩，包含学分、平时成绩、卷面成绩和总评

	[
	    {
	        "courseName": "就业指导",
	        "credit": "1.0",
	        "regularScore": "85",
	        "paperScore": "83",
	        "totalScore": "84"
	    },
	    {
	        "courseName": "软件工程课程设计",
	        "credit": "2.0",
	        "regularScore": "97",
	        "paperScore": "95",
	        "totalScore": "96"
	    }
	]

## 查当前学期课程介绍

	POST http://localhost:8080/jw/main.do
	参数 action=curSemesterJieShao

返回

	[
	    {
	        "name": "形势与政策",
	        "type": "必修",
	        "time": "7,11,15(5,6)",
	        "teacher": "匿名233",
	        "location": "2-107"
	    }
	]

## 查指定学期课表

	POST http://localhost:8080/jw/main.do
	参数 action=keBiao&xuenian=2014-2015&xueqi=1
	xuenian是学年，格式：20xx-20xx，有效参数：入学起的4年，如2013年入学，则2013-2014(大一)、2014-2015、2015-2016、2016-2017(大四)
	xueqi为学期，有效参数：1、2、全部

返回格式同查当前学期课表

## 查指定学期成绩

	POST http://localhost:8080/jw/main.do
	参数 action=score&xuenian=2015-2016&xueqi=全部
	xuenian是学年，格式：20xx-20xx，有效参数：入学起的4年
	xueqi为学期，有效参数：1、2、全部

返回格式同查当前学期成绩

# LICENSE

**The MIT license**

该API可用于学习、发布查课表/成绩工具，可商用，但若被用作不正当用途，如利用用户的教务系统账号密码非法获取他人身份信息，造成的后果与原作者无关。

