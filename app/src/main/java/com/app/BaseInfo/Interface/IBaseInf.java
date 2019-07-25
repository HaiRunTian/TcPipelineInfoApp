package com.app.BaseInfo.Interface;

import com.supermap.data.GeoStyle;
import com.supermap.mapping.ThemeLabel;
import com.supermap.mapping.ThemeUnique;

/**
 * Created by Los on 2018-06-15 14:09.
 */

public interface IBaseInf {
    /**
     *
     * @return 对象风格
     */
     GeoStyle GetStyle();

    /**
     *
     * @param preStr
     * @param index 设置ID
     */
     void setId(String preStr,int index);

    /**
     *  创建统一标签专题图
     */
     ThemeLabel createThemeLabel();

    /**
     *  创建单值专题图
     */
     ThemeUnique createThemeUnique();

    /**
     *  创建默认单值专题图
     */
     ThemeUnique createDefaultThemeUnique();

}
