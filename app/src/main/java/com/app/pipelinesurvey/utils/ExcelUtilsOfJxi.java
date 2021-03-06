package com.app.pipelinesurvey.utils;

import android.content.Context;

import com.app.BaseInfo.Data.BaseFieldInfos;
import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.LineFieldFactory;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.BaseInfo.Data.PointFieldFactory;
import com.app.utills.LogUtills;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * 写入EXCEL  jxi
 *
 * @author HaiRun
 */
public class ExcelUtilsOfJxi {
    public static WritableFont arial14font = null;
    public static WritableCellFormat arial14format = null;
    public static WritableFont arial10font = null;
    public static WritableCellFormat arial10format = null;
    public static WritableFont arial12font = null;
    public static WritableCellFormat arial12format = null;
    public final static String UTF8_ENCODING = "UTF-8";
    public final static String GBK_ENCODING = "GBK";
    /**
     * 初始化excel
     */
    public static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14,
                    WritableFont.BOLD);
            arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
            arial10font = new WritableFont(WritableFont.ARIAL, 10,
                    WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GREY_25_PERCENT);
            arial12font = new WritableFont(WritableFont.ARIAL, 12);
            arial12format = new WritableCellFormat(arial12font);
            arial12format.setBorder(jxl.format.Border.ALL,
                    jxl.format.BorderLineStyle.THIN);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileName       excel 表名
     * @param colName        字段名
     * @param excelSheetName sheet名
     */
    public static void initExcel(String fileName, List<String> colName, List<String> colNameL, String excelSheetName, String excelSheetNameL) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(fileName);
            if (file.exists()) file.delete();
            if (!file.exists()) {
                file.createNewFile();
            }
            workbook = Workbook.createWorkbook(file);
            //点
            WritableSheet sheet = workbook.createSheet(excelSheetName, 0);
            sheet.addCell((WritableCell) new Label(0, 0, fileName,
                    arial14format));
            for (int col = 0; col < colName.size(); col++) {
                sheet.addCell(new Label(col, 0, colName.get(col), arial10format));
            }
            //线
            WritableSheet sheetL = workbook.createSheet(excelSheetNameL, 1);
            sheetL.addCell((WritableCell) new Label(0, 0, fileName,
                    arial14format));
            for (int col = 0; col < colNameL.size(); col++) {
                sheetL.addCell(new Label(col, 0, colNameL.get(col), arial10format));
            }
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 数据填写到excel表中
     *
     * @param sheetIndex
     * @param objList
     * @param fileName
     * @param c
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> void writeObjListToExcel(int sheetIndex, List<T> objList,
                                               String fileName, Context c) {
        if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                in = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName),
                        workbook);
                WritableSheet sheet = writebook.getSheet(sheetIndex);
                ArrayList<String> list = null;
                int size = objList.size();
                for (int j = 0; j < size; j++) {
                    list = (ArrayList<String>) objList.get(j);
                    int _size = list.size();
                    for (int i = 0; i < _size; i++) {
                        //初始化excel表字段名字
                        sheet.addCell(new Label(i, j + 1, list.get(i),
                                arial12format));
                    }
                }
                writebook.write();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * 获取点线表数据量
     *
     * @Author HaiRun
     * @Time 2019/6/10 . 11:13
     */
    public static int getExcelRows(File file, int sheet) {
        int rows = 0;
        try {
            Workbook course = null;
            course = Workbook.getWorkbook(file);
            Cell _cell = null;
            Sheet _sheet = course.getSheet(sheet);
            rows = _sheet.getRows() - 1;
            course.close();
            return rows;

        } catch (Exception e) {
            e.getStackTrace();
        }
        return rows;
    }

    /**
     * 从Excel表中导入数据
     *
     * @param f
     * @param
     * @param sheet
     * @return
     */
    public static  List<Map<String,Object>> read2DB(File f, int sheet) {
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            Workbook course = null;
            course = Workbook.getWorkbook(f);
            Cell cell = null;
//            if (sheet == 0) {
                //点表
                Sheet _sheetP = course.getSheet(sheet);
//                BaseFieldPInfos _info = null;
                int _rowsP = _sheetP.getRows();
                //excel字段数组
                Cell[] _cells = _sheetP.getRow(0);
                //装键值对
                Map<String,Object> map = null;
                for (int i = 1; i < _rowsP; i++) {
                    map = new HashMap<>();
                    Cell[] cells = _sheetP.getRow(i);
                    LogUtills.i("长度",_cells.length + "======" + cells.length);
                    for (int j = 0; j < _cells.length; j++) {
                        LogUtills.i("字段值",_cells[j].getContents() + "========" + cells[j].getContents());
                        map.put(_cells[j].getContents(),cells[j].getContents());

                    }
                    list.add(map);
                }
            course.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LogUtills.e("excel", e.getMessage());
        }
        return list;
    }


    /**
     * @Params :
     * @author :HaiRun
     * @date :2019/7/24  10:21
     */
    public static Object getValueByRef(Class cls, String fieldName) {
        Object value = null;
        fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName
                .substring(0, 1).toUpperCase());
        String getMethodName = "get" + fieldName;
        try {
            Method method = cls.getMethod(getMethodName);
            value = method.invoke(cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
