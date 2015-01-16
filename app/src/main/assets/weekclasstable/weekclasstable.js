var hours = ["08","08", "10", "10", "12", "13", "14", "15", "16", "17", "19", "20", "21"];
var mins = ["00", 50, "05", 55, 30, 20, 30, 20, 35, 25, 30, 20, 10];
var main = function(){
    // 显示的天数
    var showCount = 7;
    // 可视范围大小
    var fullWidth = window.innerWidth;
    var fullHeight = window.innerHeight;
    if(fullHeight==0){
        fullHeight=document.body.clientHeight;
    }
    Android.debug(document.body.clientHeight);
    // 顶部高度
    $('#first-row').width(fullWidth);
    $(".right_top_block").width(fullWidth*0.9285714);
    var topHeight = (fullHeight/15) * 2 * (1/2) - $("hr").height()>=35?(fullHeight/15) * 2 * (1/2) - $("hr").height():35;
    // 块大小
    var blockHeight = ((fullHeight - topHeight - $("hr").height())/7.2);
    var blockWidth = (fullWidth*0.9285714)/showCount;
    //Android.debug('full width is '+fullWidth);
    //Android.debug('body width is '+$('body').width());
    //Android.debug('container width is:'+$('#container').width());
    //Android.debug('#first-row width is '+$('#first-row').width());
   // Android.debug('$(".right_top_block").width() return'+$(".right_top_block").width());
    // 设置顶部相关
    var $first_row = $("#margin-height");
    $first_row.height(topHeight);
    $(".day_block").width(blockWidth);
    $(".top").height(topHeight);
    $(".right_top_block").height(topHeight);
    $(".eng").css("margin-top", (topHeight - $(".eng").height() - $(".num").height())/2);
    // 设置节数相关
    $(".class_index").height(blockHeight);
    $(".class_index").css("line-height",blockHeight+"px");

    // 颜色
    var colors = new Array("#fa7886", "#38d3a9", "#34ced9", "#fcdc36","#64baff","#af92d7");

    $(".classtable_class").html("");
    for(var i = 1; i <= 7; i++) {
        var itemX = blockWidth*(i-1);
        // 获取一天课表
        var mess = null;
        mess = Android.getDayLesson(i);
        var obj = JSON.parse(mess);

        var count = parseInt(obj.count);
        if(count == 0) {
            for(var j = 0; j < 13; j++){
                var itemY = blockHeight*(j);
                $(".classtable_class").append(
                "<div class='class_item' style='top: "+itemY+"px;"+
                " left: "+itemX+"px; width: "+blockWidth+"px; height: "+blockHeight+"px; border: #E4E4E4 dashed 1pt; border-top: 0; border-right: 0;'></div>");
            }
            continue;
        }
        var allNodes = "";
        for (var j = 0; j < count; j++) {
            var nodes = obj.day_class[j].node.split(",");
            var itemY = blockHeight*(parseInt(nodes[0])-1);

            for(var k = 0; k < nodes.length; k ++) {
                allNodes += "," + nodes[k] + ",";
            }
            //Android.debug("node:"+(parseInt(nodes[0])+i));
            var startTime = hours[parseInt(nodes[0])-1]+":"+mins[parseInt(nodes[0])-1];
            $class = $("<div id='"+obj.day_class[j].id+"' class='class_item' style='top: "+itemY+"px;"+
                                       " left:"+itemX+"px; width: "+blockWidth+"px; height: "+blockHeight*nodes.length+"px;"+
                                       " background-color: "+colors[parseInt(parseInt(nodes[0])+i)%colors.length]+"; '></div>");
            $class_item=$("<div id=item"+obj.day_class[j].id+">"+startTime+"<br/>"+obj.day_class[j].classname+"<br/>"+obj.day_class[j].location+"</div>");
            $class.html($class_item);
            $(".classtable_class").append($class);
            var mt = (blockHeight*nodes.length - $class_item.height() ) /2;
            $class_item.css("margin-top", mt+'px');
        };
        //Android.debug(allNodes);
        for(var j = 0; j < 13; j++){
            if(allNodes.indexOf(","+(j+1)+",") > -1)
                continue;
            var itemY = blockHeight*(j);
            $(".classtable_class").append(
            "<div class='class_item' style='top: "+itemY+"px;"+
            " left: "+itemX+"px; width: "+blockWidth+"px; height: "+blockHeight+"px; border: #E4E4E4 dashed 1pt; border-top: 0; border-right: 0;'></div>");
        }

    }

}

function hideBody(){
    $("body").hide();

}

function toTop(){
    window.scrollTo(0,0);
}
main();
