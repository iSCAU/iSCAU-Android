package cn.scau.scautreasure;

import cn.scau.scautreasure.ui.Widget;

/**
 * 本类只是为了兼容低版本的桌面小插件，由于已经现版本的桌面小插件类 .ui.Widget
 * 命令路径不同与2012年的版本，升级到本版本后，用户插件会消失，避免用户重新添加，
 * 实现本类并集成.ui.Widget ，使命名路径与老版本一致。实际代码依然位于.ui.Widget.
 * <p/>
 * User: special
 * Date: 13-10-13
 * Time: 上午10:56
 * Mail: specialcyci@gmail.com
 */
public class WidgetProvider extends Widget {

}
