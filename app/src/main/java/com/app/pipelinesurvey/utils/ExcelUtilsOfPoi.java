package com.app.pipelinesurvey.utils;

import android.util.SparseArray;

import com.app.BaseInfo.Data.BaseFieldInfos;
import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.LineFieldFactory;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.BaseInfo.Data.PointFieldFactory;
import com.app.pipelinesurvey.bean.DetectionInfo;
import com.app.pipelinesurvey.config.SuperMapConfig;
import com.app.utills.LogUtills;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 使用poi 库生成excel表 读取excel表
 * HSSFWorkbook() 2003 ~20007
 * XSSFWorkbook(); 2007以上
 *
 * @author HaiRun
 * @time 2019/6/19.14:42
 */
public class ExcelUtilsOfPoi {
//    private static String[] title = new String[]{"序号", "检测日期", "点号", "完成工作量", "检测方法", "检测图幅", "作业组长", "组员1", "组员2", "备注"};

    /**
     * 创建excel表
     *
     * @return
     */
    private static Workbook createWorkbook() {
        //xls
        return new HSSFWorkbook();
    }


    /**
     * 初始化 当天检测记录表
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/6/26  18:00
     */
    public static void initExcelLogSheet(String fileName, List<DetectionInfo> list) {
        int[] width = new int[]{5, 11, 13, 20, 30, 8, 8, 8, 8, 8};
//        fileName = SuperMapConfig.DEFAULT_DATA_PATH + SuperMapConfig.DEFAULT_DATA_EXCEL_PATH + "/检测记录表.xls";
        FileOutputStream outputStream = null;
        try {
            FileInputStream is = new FileInputStream(new File(fileName));
            Workbook workbook = new HSSFWorkbook(is);
            SparseArray<CellStyle> borderedStyle = createBorderedStyle(workbook);
            Sheet sheet = workbook.getSheetAt(0);
//            for (int i = 0; i < width.length; i++) {
//                sheet.setColumnWidth(i, width[i] * 256);
//            }
            //设置复杂excel表头
            setCellStyle(sheet, list.get(0));
            //填入数据
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    DetectionInfo info = list.get(i);
                    LogUtills.i(info.toString());
                    Row row = sheet.createRow(10 + i);
                    row.setHeight((short) (256 * 1.2));
                    Cell cell = row.createCell(0);
                    cell.setCellStyle(borderedStyle.get(3));
                    cell.setCellValue(info.getSerialNum());
                    cell = row.createCell(1);
                    cell.setCellStyle(borderedStyle.get(3));
                    cell.setCellValue(info.getDetectionDate());

                    cell = row.createCell(2);
                    cell.setCellStyle(borderedStyle.get(3));
                    String pointName = info.getPointName();
                    cell.setCellValue(pointName);

                    cell = row.createCell(3);
                    String workload = info.getWorkload();
                    cell.setCellStyle(borderedStyle.get(3));
                    cell.setCellValue(workload);
                    cell = row.createCell(4);
                    cell.setCellStyle(borderedStyle.get(3));
                    cell.setCellValue(info.getDetectionMethod());
                    cell = row.createCell(5);
                    cell.setCellStyle(borderedStyle.get(3));
                    cell.setCellValue(info.getDetectionMap());
                    cell = row.createCell(6);
                    cell.setCellStyle(borderedStyle.get(3));
                    cell.setCellValue(info.getGroupLeader());
                    cell = row.createCell(7);
                    cell.setCellStyle(borderedStyle.get(3));
                    cell.setCellValue(info.getGroupMember1());
                    cell = row.createCell(8);
                    cell.setCellStyle(borderedStyle.get(3));
                    cell.setCellValue(info.getGroupMember2());
                    cell = row.createCell(9);
                    cell.setCellStyle(borderedStyle.get(3));
                }
            }
            outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 设置复制excel表头
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/1  14:25
     */
    private static void setCellStyle(Sheet sheet, DetectionInfo info) {

        //第五行1
        Row row5 = sheet.createRow(4);
        Cell cell5 = row5.createCell(0);
        cell5.setCellValue("工程编码：" + info.getPrjCode());
        mergingCells(sheet, CellRangeAddress.valueOf("$A$5:$C$5"));

        //第五行 2
        Cell cell52 = row5.createCell(3);
        cell52.setCellValue("工程名称：" + info.getPrjName());
        mergingCells(sheet, CellRangeAddress.valueOf("$D$5:$E$5"));

        //第五行 3
        Cell cell53 = row5.createCell(5);
        cell53.setCellValue("工程地点：" + info.getPrjSite());
        mergingCells(sheet, CellRangeAddress.valueOf("$F$5:$H$5"));

        //第五行 4
        Cell cell54 = row5.createCell(8);
        cell54.setCellValue("原始记录文件：");
        mergingCells(sheet, CellRangeAddress.valueOf("$I$5:$J$5"));

        //第六行1
        Row row6 = sheet.createRow(5);
        Cell cell61 = row6.createCell(0);
        cell61.setCellValue("仪器名称：" + info.getApparatusName1());
        mergingCells(sheet, CellRangeAddress.valueOf("$A$6:$C$6"));
        //第六行2
        Cell cell62 = row6.createCell(3);
        cell62.setCellValue("仪器编号：" + info.getApparatusCode1());
        //第六行3
        Cell cell63 = row6.createCell(4);
        cell63.setCellValue("仪器名称：" + info.getApparatusName2() + "");
        //第六行4
        Cell cell64 = row6.createCell(5);
        cell64.setCellValue("仪器编号：" + info.getApparatusCode2() + "");
        mergingCells(sheet, CellRangeAddress.valueOf("$F$6:$H$6"));
        //第六行5
        Cell cell65 = row6.createCell(8);
        cell65.setCellValue(info.getOriginal() + "");
        mergingCells(sheet, CellRangeAddress.valueOf("$I$6:$J$6"));

        //第七行1
        Row row7 = sheet.createRow(6);
        Cell cell71 = row7.createCell(0);
        cell71.setCellValue("检测标准：" + info.getDetectionStandard());
        mergingCells(sheet, CellRangeAddress.valueOf("$A$7:$E$7"));
        //第七行2
        Cell cell72 = row7.createCell(5);
        cell72.setCellValue("备注：" + info.getRemark() + "");
        mergingCells(sheet, CellRangeAddress.valueOf("$F$7:$J$7"));

        //第8行
        Row row8 = sheet.createRow(7);
        Cell cell81 = row8.createCell(0);
        cell81.setCellValue("检测标准：" + info.getDetectionStandard());
        mergingCells(sheet, CellRangeAddress.valueOf("$A$8:$E$8"));
        //第七行2
        Cell cell82 = row8.createCell(5);
        cell82.setCellValue("备注：" + info.getRemark() + "");
        mergingCells(sheet, CellRangeAddress.valueOf("$F$8:$J$8"));

    }

    /**
     * 设置复制excel表头
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/1  14:25
     */
/*    private static void setCellStyle(SparseArray<CellStyle> borderedStyle, Sheet sheet, DetectionInfo info) {
        //第一行
        Row row1 = sheet.createRow(0);
        row1.setHeight((short) (256 * 1.6));
        Cell cell = row1.createCell(0);
        cell.setCellStyle(borderedStyle.get(0));
        cell.setCellValue("广州市天驰测绘技术有限公司");
        mergingCells(sheet, CellRangeAddress.valueOf("$A$1:$J$1"));
        //第二行
        Row row2 = sheet.createRow(1);
        row2.setHeight((short) (256 * 1.6));
        Cell cell2 = row2.createCell(0);
        cell2.setCellStyle(borderedStyle.get(0));
        cell2.setCellValue("现 场 检 测 记 录 表");
        mergingCells(sheet, CellRangeAddress.valueOf("$A$2:$J$2"));
        //第三行
        Row row3 = sheet.createRow(2);
        row3.setHeight((short) (256 * 1.6));
        Cell cell3 = row3.createCell(0);
        cell3.setCellStyle(borderedStyle.get(0));
        cell3.setCellValue("管线探测事业部");
        mergingCells(sheet, CellRangeAddress.valueOf("$A$3:$J$3"));
        //第四行
        Row row4 = sheet.createRow(3);
        row4.setHeight((short) (256 * 1.2));
        Cell cell4 = row4.createCell(0);
        cell4.setCellStyle(borderedStyle.get(4));
        cell4.setCellValue("格式编码：TCIVA A1 LT5 18 07");
        mergingCells(sheet, CellRangeAddress.valueOf("$A$4:$D$4"));

        Cell cell42 = row4.createCell(4);
        cell42.setCellStyle(borderedStyle.get(4));
        cell42.setCellValue("发文编号：");
        mergingCells(sheet, CellRangeAddress.valueOf("$E$4:$H$4"));

        Cell cell43 = row4.createCell(8);
        cell43.setCellStyle(borderedStyle.get(4));
        cell43.setCellValue("第  页/共  页");
        mergingCells(sheet, CellRangeAddress.valueOf("$I$4:$J$4"));
        //第五行1
        Row row5 = sheet.createRow(4);
        row5.setHeight((short) (256 * 1.2));
        setCloumLine(borderedStyle, row5);
        Cell cell5 = row5.createCell(0);
        cell5.setCellStyle(borderedStyle.get(1));
        cell5.setCellValue("工程编码：" + info.getPrjCode());
        mergingCells(sheet, CellRangeAddress.valueOf("$A$5:$C$5"));

        //第五行 2
        Cell cell52 = row5.createCell(3);
        cell52.setCellStyle(borderedStyle.get(1));
        cell52.setCellValue("工程名称：" + info.getPrjName());
        mergingCells(sheet, CellRangeAddress.valueOf("$D$5:$E$5"));

        //第五行 3
        Cell cell53 = row5.createCell(5);
        cell53.setCellStyle(borderedStyle.get(1));
        cell53.setCellValue("工程地点：" + info.getPrjSite());
        mergingCells(sheet, CellRangeAddress.valueOf("$F$5:$H$5"));

        //第五行 4
        Cell cell54 = row5.createCell(8);
        cell54.setCellStyle(borderedStyle.get(1));
        cell54.setCellValue("原始记录文件：");
        mergingCells(sheet, CellRangeAddress.valueOf("$I$5:$J$5"));

        //第六行1
        Row row6 = sheet.createRow(5);
        row6.setHeight((short) (256 * 1.2));
        Cell cell61 = row6.createCell(0);
        setCloumLine(borderedStyle, row6);
        cell61.setCellStyle(borderedStyle.get(1));
        cell61.setCellValue("仪器名称：" + info.getApparatusName1());
        mergingCells(sheet, CellRangeAddress.valueOf("$A$6:$C$6"));
        //第六行2
        Cell cell62 = row6.createCell(3);
        cell62.setCellStyle(borderedStyle.get(1));
        cell62.setCellValue("仪器编号：" + info.getApparatusCode1());
//        mergingCells(sheet, CellRangeAddress.valueOf("$D$6:$E$6"));
        //第六行3
        Cell cell63 = row6.createCell(4);
        cell63.setCellStyle(borderedStyle.get(1));
        cell63.setCellValue("仪器名称：" + info.getApparatusName2() + "");
//        mergingCells(sheet, CellRangeAddress.valueOf("$F$6:$G$6"));
        //第六行4
        Cell cell64 = row6.createCell(5);
        cell64.setCellStyle(borderedStyle.get(1));
        cell64.setCellValue("仪器编号：" + info.getApparatusCode2() + "");
        mergingCells(sheet, CellRangeAddress.valueOf("$F$6:$H$6"));
        //第六行5
        Cell cell65 = row6.createCell(8);
        cell65.setCellStyle(borderedStyle.get(1));
        cell65.setCellValue(info.getOriginal() + "");
        mergingCells(sheet, CellRangeAddress.valueOf("$I$6:$J$6"));
        //第七行1
        Row row7 = sheet.createRow(6);
        row7.setHeight((short) (256 * 1.2));
        setCloumLine(borderedStyle, row7);
        Cell cell71 = row7.createCell(0);
        cell71.setCellValue("检测标准：" + info.getDetectionStandard());
        mergingCells(sheet, CellRangeAddress.valueOf("$A$7:$E$7"));
        cell71.setCellStyle(borderedStyle.get(1));
        //第七行2
        Cell cell72 = row7.createCell(5);
        cell72.setCellStyle(borderedStyle.get(1));
        cell72.setCellValue("备注：" + info.getRemark() + "");
        mergingCells(sheet, CellRangeAddress.valueOf("$F$7:$J$7"));

        //第8行
        Row row8 = sheet.createRow(7);
        row8.setHeight((short) (256 * 1.2));
        setCloumLine(borderedStyle, row8);
        Cell cell81 = row8.createCell(0);
        cell81.setCellStyle(borderedStyle.get(1));
        cell81.setCellValue("检测标准：" + info.getDetectionStandard());
        mergingCells(sheet, CellRangeAddress.valueOf("$A$8:$E$8"));
        //第七行2
        Cell cell82 = row8.createCell(5);
        cell82.setCellStyle(borderedStyle.get(1));
        cell82.setCellValue("备注：" + info.getRemark() + "");
        mergingCells(sheet, CellRangeAddress.valueOf("$F$8:$J$8"));

        //第九行
        Row row9 = sheet.createRow(8);
        row9.setHeight((short) (256 * 1.2));
        setCloumLine(borderedStyle, row9);
        Cell cell9 = row9.createCell(0);
        cell9.setCellStyle(borderedStyle.get(2));
        cell9.setCellValue("项 目 详 情 记 录 表");
        mergingCells(sheet, CellRangeAddress.valueOf("$A$9:$J$9"));

        //第十行
        Row row10 = sheet.createRow(9);
        row10.setHeight((short) (256 * 1.2));
        for (int i = 0; i < title.length; i++) {
            Cell cell10 = row10.createCell(i);
            cell10.setCellStyle(borderedStyle.get(2));
            cell10.setCellValue(title[i]);
        }
    }*/
    private static void setCloumLine(SparseArray<CellStyle> borderedStyle, Row row) {
        for (int i = 0; i < 10; i++) {
            row.createCell(i).setCellStyle(borderedStyle.get(1));
        }
    }

    /**
     * 方法描述：初始化Excel表头
     *
     * @param colNameP  点表字段集合
     * @param colNameL  线表字段集合
     * @param pointName 点表名字
     * @param lineName  线表名字
     * @Params : fileName excel文件名
     * @author :HaiRun
     * @date :2019/6/19  16:27
     */
    public static void initExcel(String fileName, List<String> colNameP, List<String> colNameL, String pointName, String lineName) {
        FileOutputStream outputStream = null;
        Workbook workbook = null;
        try {
            workbook = createWorkbook();
            SparseArray<CellStyle> borderedStyle = createBorderedStyle(workbook);

            //建立新的point sheet对象 excel表单
            Sheet sheetPoint = workbook.createSheet(pointName);
            //在sheet里创建第一行，参数为行索引  0~65535直接
            Row row1 = sheetPoint.createRow(0);
            //创建单元格 0~255
            for (int i = 0; i < colNameP.size(); i++) {
                Cell cell = row1.createCell(i);
                cell.setCellStyle(borderedStyle.get(2));
                cell.setCellValue(colNameP.get(i));
            }
            //建立新的point sheet对象 excel表单
            Sheet sheetLine = workbook.createSheet(lineName);
            //在sheet里创建第一行，参数为行索引  0~65535直接
            Row row = sheetLine.createRow(0);
            //创建单元格 0~255
            for (int i = 0; i < colNameL.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(borderedStyle.get(2));
                cell.setCellValue(colNameL.get(i));
            }

            outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
        } catch (Exception e) {
            LogUtills.e("ExcelUtilsOfPoi ---------" + e.toString());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 方法描述：初始化Excel表头
     * 外检模式
     * @param colNameP  点表字段集合
     * @param colNameL  线表字段集合
     * @param pointName 点表名字
     * @param lineName  线表名字
     * @Params : fileName excel文件名
     * @author :HaiRun
     * @date :2019/6/19  16:27
     */
    public static void initExcelOfOut(String fileName, List<String> colNameP, List<String> colNameL,List<String> colNamePoint
            ,List<String> colNameLine, String pointName, String lineName, String point, String line) {
        FileOutputStream outputStream = null;
        Workbook workbook = null;
        try {
            workbook = createWorkbook();
            SparseArray<CellStyle> borderedStyle = createBorderedStyle(workbook);

            //建立新的point sheet对象 excel表单
            Sheet sheetPoint = workbook.createSheet(pointName);
            //在sheet里创建第一行，参数为行索引  0~65535直接
            Row row1 = sheetPoint.createRow(0);
            //创建单元格 0~255
            for (int i = 0; i < colNameP.size(); i++) {
                Cell cell = row1.createCell(i);
                cell.setCellStyle(borderedStyle.get(2));
                cell.setCellValue(colNameP.get(i));
            }
            //建立新的line sheet对象 excel表单
            Sheet sheetLine = workbook.createSheet(lineName);
            //在sheet里创建第一行，参数为行索引  0~65535直接
            Row row = sheetLine.createRow(0);
            //创建单元格 0~255
            for (int i = 0; i < colNameL.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(borderedStyle.get(2));
                cell.setCellValue(colNameL.get(i));
            }
            //创建第三张表
            Sheet point2 = workbook.createSheet(point);
            //在sheet里创建第一行，参数为行索引  0~65535直接
            Row row2 = point2.createRow(0);
            //创建单元格 0~255
            for (int i = 0; i < colNamePoint.size(); i++) {
                Cell cell = row2.createCell(i);
                cell.setCellStyle(borderedStyle.get(2));
                cell.setCellValue(colNamePoint.get(i));
            }

            //创建第四张表
            Sheet line2 = workbook.createSheet(line);
            //在sheet里创建第一行，参数为行索引  0~65535直接
            Row row3 = line2.createRow(0);
            //创建单元格 0~255
            for (int i = 0; i < colNameLine.size(); i++) {
                Cell cell = row3.createCell(i);
                cell.setCellStyle(borderedStyle.get(2));
                cell.setCellValue(colNameLine.get(i));
            }

            outputStream = new FileOutputStream(fileName);
            workbook.write(outputStream);
        } catch (Exception e) {
            LogUtills.e("ExcelUtilsOfPoi ---------" + e.toString());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @param :sheetIndex 表下标
     * @param :           fileName 文件名称
     * @params : list  数据集合
     * @author :HaiRun
     * @date :2019/6/19  20:15
     */
    public static <T> void writeObjListToExcel(int sheetIndex, List<T> list, String fileName) {
        FileInputStream is = null;
        FileOutputStream os = null;
        Workbook wb = null;
        String extString = fileName.substring(fileName.lastIndexOf("."));
        try {
            is = new FileInputStream(new File(fileName));
            if (".xls".equals(extString)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {

            } else {
                wb = null;
            }
            if (wb != null) {
                SparseArray<CellStyle> borderedStyle = createBorderedStyle(wb);
                Sheet sheet = wb.getSheetAt(sheetIndex);
                ArrayList<String> list1 = null;
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    list1 = (ArrayList<String>) list.get(i);
                    int size2 = list1.size();
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < size2; j++) {
                        Cell cell = row.createCell(j);
                        cell.setCellStyle(borderedStyle.get(3));
                        cell.setCellValue(list1.get(j));
                    }
                }
            }
            os = new FileOutputStream(fileName);
            wb.write(os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 设置表格的内容到四边的距离,表格四边的颜色
     * <p>
     * 对齐方式：
     * 水平： setAlignment();
     * 竖直：setVerticalAlignment()
     * 四边颜色：
     * 底边：  cellStyle.setBottomBorderColor()
     * <p>
     * 四边边距：
     * <p>
     * 填充：
     * <p>
     * 缩进一个字符：
     * setIndention()
     * <p>
     * 内容类型：
     * setDataFormat()
     *
     * @param workbook
     * @return
     */
    private static SparseArray<CellStyle> createBorderedStyle(Workbook workbook) {
        SparseArray<CellStyle> array = new SparseArray<>();
        //第一 二 三行标题
        CellStyle cellStyle0 = workbook.createCellStyle();
        Font font0 = creatFont(workbook);
        font0.setFontHeightInPoints((short) 14);
        font0.setFontName("黑体");
        font0.setBoldweight((short) 5);
        font0.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle0.setFont(font0);
        cellStyle0.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle0.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        array.put(0, cellStyle0);

        //第 五 六 七 八 十行
        CellStyle cellStyle1 = workbook.createCellStyle();
        Font font1 = creatFont(workbook);
        font1.setFontName("宋体");
        font1.setFontHeightInPoints((short) 10);
        font1.setBoldweight((short) 5);
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle1.setFont(font1);
        cellStyle1.setAlignment(CellStyle.ALIGN_LEFT);
        cellStyle1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        setCellStyle(cellStyle1);
        array.put(1, cellStyle1);

        //第九行
        CellStyle cellStyle2 = workbook.createCellStyle();
        Font font2 = creatFont(workbook);
        font2.setFontName("宋体");
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font2.setFontHeightInPoints((short) 10);
        font2.setBoldweight((short) 1);
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle2.setFont(font2);
        cellStyle2.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        setCellStyle(cellStyle2);
        array.put(2, cellStyle2);

        //第十一行
        CellStyle cellStyle3 = workbook.createCellStyle();
        //对齐
        cellStyle3.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle3.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font font3 = workbook.createFont();
        font3.setFontName("宋体");
        font3.setColor(Font.COLOR_NORMAL);
        cellStyle3.setFont(font3);
        setCellStyle(cellStyle3);
        array.put(3, cellStyle3);

        //第 五 行
        CellStyle cellStyle4 = workbook.createCellStyle();
        Font font4 = creatFont(workbook);
        font4.setFontHeightInPoints((short) 10);
        font4.setBoldweight((short) 5);
        font4.setFontName("宋体");
        font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle4.setFont(font1);
        cellStyle4.setAlignment(CellStyle.ALIGN_LEFT);
        cellStyle4.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        array.put(4, cellStyle4);

        //第 五 行
        CellStyle cellStyle5 = workbook.createCellStyle();
        cellStyle5.setBottomBorderColor((short) 13);
        cellStyle5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        array.put(5, cellStyle5);

        return array;
    }

    /**
     * 设置单元格格式
     *
     * @Params :
     * @author :HaiRun
     * @date :2019/7/2  17:34
     */
    private static void setCellStyle(CellStyle cellStyle3) {
        //重新设置单元格的四边颜色
        BorderStyle thin = BorderStyle.THIN;
        short blackColor_Index = IndexedColors.BLACK.getIndex();
        cellStyle3.setBottomBorderColor(blackColor_Index);
        cellStyle3.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle3.setTopBorderColor(blackColor_Index);
        cellStyle3.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle3.setRightBorderColor(blackColor_Index);
        cellStyle3.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle3.setRightBorderColor(blackColor_Index);
        cellStyle3.setBorderLeft(CellStyle.BORDER_THIN);
    }


    /**
     * 创建Font
     * <p>
     * 注意点：excle工作簿中字体最大限制为32767，应该重用字体，而不是为每个单元格都创建字体。
     * <p>
     * 其API:
     * setBold():设置粗体
     * setFontHeightInPoints():设置字体的点数
     * setColor():设置字体颜色
     * setItalic():设置斜体
     *
     * @param workbook
     * @return
     */
    private static Font creatFont(Workbook workbook) {
        Font font = workbook.createFont();
        font.setColor(Font.COLOR_NORMAL);
        return font;
    }

    /**
     * 设置Sheet表
     *
     * @param sheet
     */
    private static void setSheet(Sheet sheet) {
        // turn off gridlines（关闭网络线）
        sheet.setDisplayGridlines(false);
        sheet.setPrintGridlines(false);
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);

    }

    /**
     * 获取点线表数据量
     *
     * @params :file excel文件名
     * @author :HaiRun
     * @date :2019/6/19  20:24
     */
    public static int getExcelRows(File file, int sheet) {
        int rows = 0;
        FileInputStream is = null;
        Workbook workbook = null;
        try {
            is = new FileInputStream(file);
            workbook = new HSSFWorkbook(is);
            Sheet sheetAt = workbook.getSheetAt(sheet);
            //获取最大行
            rows = sheetAt.getPhysicalNumberOfRows() - 1;
        } catch (Exception e) {
            e.getMessage();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return rows;
    }

    /**
     * @param : sheet excel表下标
     * @params :file  文件名
     * @author :HaiRun
     * @date :2019/6/19  20:27
     */
    public static List<Map<String, Object>> readExcelDataToBean(File file, int sheet) {
        ArrayList<Map<String, Object>> pInfos = new ArrayList<>();
        FileInputStream is = null;
        Workbook workbook = null;
        try {
            is = new FileInputStream(file);
            workbook = new HSSFWorkbook(is);
            //点表
            Sheet sheetPoint = workbook.getSheetAt(sheet);
//            BaseFieldPInfos infos = null;
            //获取表的行数
            int rowsP = sheetPoint.getPhysicalNumberOfRows();
            //获取第一行
            Row row = sheetPoint.getRow(0);
            //获取第一行列数
            int columnCount = row.getPhysicalNumberOfCells();
            LogUtills.i("行数", rowsP + "------------" + columnCount);
            //遍历行数
            for (int i = 1; i < rowsP; i++) {
                Map<String, Object> map = new HashMap<>();
                Row row1 = sheetPoint.getRow(i);
                LogUtills.i("长度", columnCount + "======" + row1.getPhysicalNumberOfCells());
                //遍历列数
                for (int j = 0; j < columnCount; j++) {
                    try {
                        String cell = row.getCell(j).toString();
                        map.put(row.getCell(j).toString(), row1.getCell(j).toString());
                    } catch (Exception e) {
                        map.put(row.getCell(j).toString(), "");
                        LogUtills.e("字段值", row.getCell(j).toString() + "======");
                    }
                }
                pInfos.add(map);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtills.e("FileNotFoundException" + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtills.e("IOException" + e.toString());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return pInfos;
    }

    /**
     * 合并cell单元格
     * <p>
     * CellRangeAddress构造器中参数：
     * 参数1：first row(0-based)
     * 参数2：last row(0-based)
     * 参数3：first column(0-based)
     * 参数4：last column(0-based)
     *
     * @param sheet
     * @param cellRangeAddress
     */
    private static void mergingCells(Sheet sheet, CellRangeAddress cellRangeAddress) {
        sheet.addMergedRegion(cellRangeAddress);
    }


}
