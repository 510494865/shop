var E3MALL = {
	checkLogin : function(){
		var _ticket = $.cookie("token");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url:"http://localhost:8080/Mall/user/token/"+ _ticket,
		//	dataType : "jsonp",
			type : "GET",
			success : function(data){
				
				var username = data.username;
				var html = username + "&nbsp;&nbsp;<a href=\"http://localhost:8080/Mall/user/logout\" class=\"link-logout\">退出</a>";
				$("#loginbar").html(html);
				
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	E3MALL.checkLogin();
});