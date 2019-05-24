package com.app.pipelinesurvey.utils;

import com.supermap.data.Point;
import com.supermap.data.Point2D;
import com.supermap.data.Recordset;

/**
 * @Author HaiRun
 * @Time 2019/3/28.15:15
 * 点到线最短的距离 返回最短距离的线的id
 */

public class PtToLrLUtils {
    public static PtToLrLUtils m_ins = null;

    public static PtToLrLUtils Ins() {
        if (m_ins == null) {
            m_ins = new PtToLrLUtils();
        }
        return m_ins;
    }


    public double pointToLine(Recordset recordset, Point2D point2D) {
        double length = 0;
        double a, b, c;
        double x0 =0.0, y0 =0.0, x1 =0.0, y1 =0.0, x2 =0.0, y2 =0.0;
        x0 = point2D.getX();
        y0 = point2D.getY();
        x1 = recordset.getDouble("startLongitude");
        y1 = recordset.getDouble("startLatitude");
        x2 = recordset.getDouble("endLongitude");
        y1 = recordset.getDouble("endLatitude");

        // 线段的长度
        a = lineSpace(x1, y1, x2, y2);

        // (x1,y1)到点的距离
        b = lineSpace(x1, y1, x0, y0);

        // (x2,y2)到点的距离
        c = lineSpace(x2, y2, x0, y0);

        //点在位置在起点或者终点
        if (c <= 0.000001 || b <= 0.000001) {

            length = 0;

            return length;

        }

        //线长度
        if (a <= 0.000001) {

            length = b;

            return length;

        }
        //直角
        if (c * c == a * a + b * b) {

            length = b;

            return length;

        }

        //直角
        if (b * b == a * a + c * c) {

            length = c;

            return length;

        }

        // 半周长
        double p = (a + b + c) / 2;

        // 海伦公式求面积
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));

        // 返回点到线的距离（利用三角形面积公式求高） 底 * 高 /2
        length = 2 * s / a;

        return length;

    }


    /**
     *   计算两点之间的距离
     */
    private double lineSpace(double x1, double y1, double x2, double y2) {
        double lineLength = 0;
        lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

        return lineLength;
    }
}
