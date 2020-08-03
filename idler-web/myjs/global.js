let myHost="127.0.0.1:8080"
$(".logout").on("click",function(e){

    bootbox.confirm({ 
        size: "small",
        title:"提示",
        message: "确认要退出登录吗？",
        callback: function(result){
            if(result){
                window.localStorage.removeItem("token");
                window.location.href="a_login.html"
            }
         }
    })
   
})