$(document).ready(function(){
    var token = window.localStorage["token"];
			if(token == "undefined"){
				window.location.href="a_login.html";
				return false;
    		}
    showVideoList();
})
function showVideoList(){
    $.ajax({
        type: "GET",
        contentType:"application/x-www-form-urlencoded",
        dataType: "json",
        url: "http://"+myHost+"/videos",
        xhrFields:{withCredentials:true},
        beforeSend: function(request) {
            request.setRequestHeader("token", window.localStorage["token"]);
        },
        success:function(result){
            if(result.data.errCode==20003){
                window.location.href="a_login.html";
            }
            if(result.succeed){
            result.data.forEach(data => {
                addVideo(data);
            });
          }
        },
        error:function(result){
            bootbox.alert({
                title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                message:"服务器繁忙",
                backdrop:true
            })
        }
    })

}
function addVideo(data){
    var v="<div class='col-xs-6 col-sm-4 col-md-3'><div class='thumbnail search-thumbnail'>";
    v+="<input type='hidden' name='video_id' value='"+data.id+"'>";
    v+="<span name='tags' class='search-promotion label label-success arrowed-in arrowed-in-right'>"+data.tags+"</span>";
    v+="<i  class='delete fa fa-times red fa-2x' aria-hidden='true'></i>"
    v+="<img class='media-object' name='picture' style='width: 100%;height:180px;'src='"+data.picture+"' />";
    v+="<div class='caption'><div class='clearfix'>";
    if(data.source==3)
    v+="<span name='source' class='pull-right label label-grey info-label'>优酷</span>";
    else if(data.source==2)
    v+="<span name='source' class='pull-right label label-grey info-label'>爱奇艺</span>";
    else if(data.source==1)
    v+="<span name='source' class='pull-right label label-grey info-label'>腾讯视频</span>";
    else 
    v+="<span name='source' class='pull-right label label-grey info-label'>未知</span>";
    v+="<div class='pull-left bigger-110 one-line' style='max-width: 60%;'>";
    if(data.status==3)
    v+="<img name='status' src='./image/yu.png' >";
    else if(data.status==1)
    v+="<img name='status' src='./image/vip.png'>";
    else if(data.status==2)
    v+="<img name='status' src='./image/pre.png' >";
    v+="<a href='"+data.url+"' name='title' target='_blank'>"+data.title+"</a>";
    v+="</div></div>";
    v+="<h3 class='search-title'>";
    v+="<a href='"+data.detailUrl+"' name='name' class='orange' style='font-size: 22px;'>"+data.name+"</a>";
    v+="</h3>";
    v+="<p class='two-line' title="+data.description+">"+data.description+"</p>";
    v+="<button type='button' name='sub' value='"+data.sub+"' class='btn btn-default btn-round btn-white submit'>";
    if(data.sub==1)
    v+="<i class='ace-icon fa fa-check-circle-o light-green' aria-hidden='true'>已订阅通知</i>";
    else
    v+="<i class='ace-icon fa fa-times-circle-o light-red' aria-hidden='true'>未订阅通知</i>";
    v+="</button></div></div></div>";
    $("#video_list").append(v);
}
var video;
$('#search').on('click', function (e) {
    $('#add').attr("disabled",false);
    $('#search').attr("disabled", true);
    $('#search>i').removeClass("fa-search");
    $('#search>i').addClass("fa-spinner fa-spin");
    var url = $('#search_url').val().trim();
    $.ajax({
        type: "GET",
        contentType:"application/x-www-form-urlencoded",
        dataType: "json",
        url: "http://"+myHost+"/videos/search",
        xhrFields:{withCredentials:true},
        headers:{
            token:window.localStorage["token"]
        },
        data: {
            url: url
        },
        success: function (result) {
            if (result.succeed == true) {
                var data = result.data;
                video=data;
                $("#video_info input[name='video_id']").val(data.id);
                $("#video_info img[name='image']").attr("src", data.image);
                $("#video_info a[name='name']").text(data.name);
                $("#video_info a[name='name']").attr("title",data.name);
                $("#video_info span[name='score']").text(data.score);
                $("#video_info span[name='type']").text(data.type);
                $("#video_info span[name='tags']").text(data.tags);
                $("#video_info span[name='name2']").text(data.name2);
                $("#video_info span[name='name2']").attr("title",data.name2);
                if (data.source == 0) {
                    $("#video_info span[name='source']").text("未知");
                } else if (data.source == 1) {
                    $("#video_info span[name='source']").text("腾讯视频");
                } else if (data.source == 2) {
                    $("#video_info span[name='source']").text("爱奇艺");
                } else if (data.source == 3) {
                    $("#video_info span[name='source']").text("优酷");
                }

                $("#video_info div[name='description']").text(data.description);
                if (data.now) {
                    if (data.finish==0) {
                        $("#video_info span[name='now']").text("已更新至" + data.now + "集");
                    } else {
                        $("#video_info span[name='now']").text("全" + data.now + "集");
                    }
                }
                $("#video_info a[name='url']").attr("href", data.url);
                $("#video_info a[name='url']").text(data.title);
                $("#video_info a[name='url']").attr("title",data.title);

                if (data.status == 0) {
                    $("#video_info img[name='status']").attr("src", "")
                } else if (data.status == 1) {
                    $("#video_info img[name='status']").attr("src", "./image/yu.png")
                } else if (data.status == 2) {
                    $("#video_info img[name='status']").attr("src", "./image/vip.png")
                }
                $('form#video_info').css("display", "block");
            } else {
                bootbox.alert({
                    title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                    message:result.data.errCode + "_" + result.data.errMsg,
                })
                video=undefined
            }
            $('#search>i').removeClass("fa-spinner fa-spin");
            $('#search>i').addClass("fa-search");
            $('#search').attr("disabled", false);
        },
        error: function (result) {
            bootbox.alert({
                title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                message:"服务器繁忙",
                backdrop:true
            })
            video=undefined
            $('#search>i').removeClass("fa-spinner fa-spin");
            $('#search>i').addClass("fa-search");
            $('#search').attr("disabled", false);
        }
    })
    return false;
})
$("#reset").on('click', function (e) {
    searchReset();
    return false;
})
function searchReset() {
    $('#add').attr("disabled",false);
    $('form#video_info').css("display", "none");
    $('#search_url').val("");
    $("#video_info input[name='video_id']").val("");
    $("#video_info img[name='image']").attr("src", "");
    $("#video_info a[name='url']").attr("href", "");
    $("#video_info a[name='url']").text("");
    $("#video_info a[name='url']").attr("title","");
    $("#video_info a[name='name']").text("");
    $("#video_info a[name='name']").attr("title","");
    $("#video_info span[name='score']").text("");
    $("#video_info span[name='type']").text("");
    $("#video_info span[name='tags']").text("");
    $("#video_info span[name='name2']").text("");
    $("#video_info span[name='name2']").attr("title","");
    $("#video_info span[name='source']").text("");
    $("#video_info span[name='description']").text("");
    $("#video_info span[name='now']").text("");
    $("#video_info img[name='status']").attr("src", "")
}
$("#add").on("click",function(e){
    var me=$(this);
    me.attr("disabled",true);
    var videoId=$("#video_info input[name='video_id']").val();
    if(!/^[1-9][0-9]*$/.test(videoId)){
        bootbox.alert({
            title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
            message: "请输入网址并搜索",
            backdrop:true
        })
    }
    $.ajax({
        type: "POST",
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        url: "http://" + myHost + "/videos/"+videoId+"/users",
        xhrFields: { withCredentials: true },
        headers: {
            token: window.localStorage["token"],
        },
        success: function (result) {
            if (result.succeed) {
                bootbox.alert({
                    title: "提示",
                    message: "追剧成功",
                })
                // 添加新内容到list
            if(video!=undefined){
                video.sub=1;
                addVideo(video);
                me.attr("disabled",false);
            }
            } else if(result.data.errCode==30002){
                bootbox.alert({
                    title: "提示",
                    message: "视频已订阅",
                    backdrop: true
                })
            }
            else {
                bootbox.alert({
                    title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                    message: result.data.errCode + "_" + result.data.errMsg,
                })
            }
        },
        error: function (result) {
            bootbox.alert({
                title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                message: "服务器繁忙",
                backdrop:true
            })
            me.attr("disabled",false);
        }
    })
})
$("#video_list").on("click","button[name='sub']",function(e){
    $(this).attr("disabled",true);
    var videoId=$(this).parent().parent().children("input[name='video_id']").val();
    var value=$(this).val();
    if(value==1){
        value=0;
        $(this).children("i").removeClass("fa-check-circle-o light-green");
        $(this).children("i").addClass("fa-times-circle-o light-red");
        $(this).children("i").text("未订阅通知");
    }else if(value==0){
        value=1;
        $(this).children("i").removeClass("fa-times-circle-o light-red");
        $(this).children("i").addClass("fa-check-circle-o light-green");
        $(this).children("i").text("已订阅通知");
    }else{
        return;
    }
    $(this).val(value);
    var me=$(this);
    var subAjax=$.ajax({
        type: "PATCH",
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        url: "http://" + myHost + "/videos/"+videoId+"/users",
        xhrFields: { withCredentials: true },
        headers:{
            token: window.localStorage["token"],
        },
        data: {
            sub:value
        },
        success: function (result) {
            if (result.succeed) {
                
            } else {
                bootbox.alert({
                    title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                    message: result.data.errCode + "_" + result.data.errMsg,
                })
                setTimeout("window.location.reload()",2000);
            }
            me.attr("disabled",false);
        },
        error: function (result) {
            bootbox.alert({
                title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                message: "服务器繁忙",
                backdrop: true
            })
            me.attr("disabled",false);
        }
    })
    
    return false;
})
$("#video_list").on("click","i.delete",function(e){
    var videoId=$(this).parent().children("input[name='video_id']").val();
    var me=$(this);
    bootbox.confirm({ 
        size: "small",
        title:"提示",
        message: "确认要删除吗？",
        callback: function(result){
            if(result){
                $.ajax({
                    type: "DELETE",
                    contentType: "application/x-www-form-urlencoded",
                    dataType: "json",
                    url: "http://" + myHost + "/videos/"+videoId+"/users",
                    xhrFields: { withCredentials: true },
                    headers: {
                        token: window.localStorage["token"],
                    },
                    success: function (result) {
                        if (result.succeed) {
                            bootbox.alert({
                                title: "提示",
                                message: "删除成功",
                                backdrop: true
                            })
                            me.parent().parent().remove(); 
                        } else {
                            bootbox.alert({
                                title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                                message: result.data.errCode + "_" + result.data.errMsg,
                            })
                            setTimeout("window.location.reload()",2000);
                        }
                    },
                    error: function (result) {
                        bootbox.alert({
                            title: "<i class='fa fa-exclamation-triangle' aria-hidden='true' style='color: #f92c2c;margin-right: 10px;'></i>错误",
                            message: "服务器繁忙",
                            backdrop: true
                        })
                    }
                })
            }
         }
    })
    return false;
})