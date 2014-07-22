$(document).ready(function(){
    // 显示的天数
    var showCount = 7;
    // 可视范围大小
    var fullWidth = window.innerWidth;
    var fullHeight = window.innerHeight;
    // 顶部高度
    var topHeight = (fullHeight/15) * 2 * (1/2) - $("hr").height();
    // 块大小
    var blockHeight = ((fullHeight - topHeight - $("hr").height())/13);
    var blockWidth = $(".right_top_block").width()/showCount;

    // 设置顶部相关
    $(".day_block").width(blockWidth);
    $(".top").height(topHeight);
    $(".right_top_block").height(topHeight);
    $(".eng").css("margin-top", (topHeight - $(".eng").height() - $(".num").height())/2);

    $(".middle").width(fullWidth);
    // 设置节数相关
    $(".class_index").height(blockHeight);
    $(".class_index").css("padding-top", ( blockHeight - parseInt($(".class_index").css("font-size")) ) / 2);

    // 颜色
    var colors = new Array("#F63393", "#5EA2F7", "#5EF7AD", "#F75E65");

    $(".classtable_class").html("");
    for(var i = 1; i <= 7; i++) {
        var itemX = blockWidth*(i-1);
        // 获取一天课表
        var mess = null;
        mess = Android.getDayLesson(i);
        var obj = eval ("(" + mess + ")");

        var count = parseInt(obj.count);
        if(count == 0) {
            for(var j = 0; j < 13; j++){
                var itemY = blockHeight*(j);
                $(".classtable_class").html($(".classtable_class").html() +
                "<div class='class_item' style='top: "+itemY+"px;"+
                " left: "+itemX+"px; width: "+blockWidth+"px; height: "+blockHeight+"px; border: #E4E4E4 solid 1pt; border-top: 0; border-right: 0;'></div>");
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

            $(".classtable_class").html($(".classtable_class").html() +
                "<div id='"+obj.day_class[j].id+"' class='class_item' style='top: "+itemY+"px;"+
                " left:"+itemX+"px; width: "+blockWidth+"px; height: "+blockHeight*nodes.length+"px;"+
                " background-color: "+colors[parseInt(Math.random()*colors.length)]+"; '>"+
                "<div id=item"+obj.day_class[j].id+">"+obj.day_class[j].classname+"</div></div>");
            var mt = ($("#"+obj.day_class[j].id).height() - $("#item"+obj.day_class[j].id).height() ) /2;
            $("#item"+obj.day_class[j].id).css("margin-top", mt);
        };
        Android.debug(allNodes);
        for(var j = 0; j < 13; j++){
            if(allNodes.indexOf(","+(j+1)+",") > -1)
                continue;
            var itemY = blockHeight*(j);
            $(".classtable_class").html($(".classtable_class").html() +
            "<div class='class_item' style='top: "+itemY+"px;"+
            " left: "+itemX+"px; width: "+blockWidth+"px; height: "+blockHeight+"px; border: #E4E4E4 solid 1pt; border-top: 0; border-right: 0;'></div>");
        }
    }
});