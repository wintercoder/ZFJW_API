# 说明										
1. 教务系统登陆页地址的`(S(hpuxisy320bpi2iunqzwd445))/`下称为cookieId，是系统为了防止浏览器禁用Cookie而存在URL上的，每次都会变化，一段时间后失效。

	所以sessionId需要每次登陆前获取，获取方式：Get请求[http://jwxt2.gdufe.edu.cn:8080](http://jwxt2.gdufe.edu.cn:8080)会返回302跳转，header里的`location`有跳转链接，内含sessionId，需要禁止自动跳转才能获取。
	
	有sessionId之后登陆并用Cookie进行其他操作，全程同一个HttpClient对象会自动管理Cookie(4.x版本HttpClient)

2. 登陆前需要获取一次登陆页的`__VIEWSTATE`信息，__VIEWSTATE存储了当前视图结构，表示当前页面有什么东西，编码转换地址：[http://viewstatedecoder.azurewebsites.net/](http://viewstatedecoder.azurewebsites.net/)

3. 登陆后可以直接GET查当前学期课表、分数等，需要指定学期的话要获取当前学期课表/分数页面的__VIEWSTATE，再Post过去。

4. 登陆后基本上所有请求都含`xh=...&xm=...&gnmkdm=N121603`，xm是GBK/gb2312编码的姓名，gnmkdm固定值，这2个不是必要属性。

5. 代码中的成员变量URL均不包含学号，所以除登陆时外，`getAndSetViewState()`的url需带上学号。

6. `Base_URL`有改变的话（登陆时获取sessionId之后就改变）需要调用refreshUrlByBaseUrl()更新相关的URL。

# 特殊情况

如果从课表html里正则匹配课程时不对劲，则可能是这种情况

	<td>第3节</td><td align="center">人工智能基础<br>必修<br>1-16(3)<br>李某<br>4-110<br></td><td align="center" rowspan="2">创业基础<br>必修<br>1-16(3,4)<br>何某人<br>2-203<br></td><td align="center">&nbsp;</td><td align="center" rowspan="2">计算机图形学<br>1-16(3,4)<br>某A<br>4-205<br></td><td align="center">&nbsp;</td><td align="center">&nbsp;</td><td align="center">&nbsp;</td>
		</tr><tr style="height:50px;">
			<td>第4节</td><td align="center">&nbsp;</td><td align="center">&nbsp;</td><td align="center">&nbsp;</td><td align="center">&nbsp;</td><td align="center">&nbsp;</td>
		</tr><tr style="height:50px;">
			<td rowspan="4" style="width:1%;">下午</td><td>第5节</td><td align="center">&nbsp;</td><td align="center" rowspan="2">形势与政策<br>必修<br>7,11,15(5,6)<br>某B<br>2-107<br></td>
			
