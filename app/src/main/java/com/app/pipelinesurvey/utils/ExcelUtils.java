package com.app.pipelinesurvey.utils;

import android.content.Context;

import com.app.BaseInfo.Data.BaseFieldInfos;
import com.app.BaseInfo.Data.BaseFieldLInfos;
import com.app.BaseInfo.Data.BaseFieldPInfos;
import com.app.BaseInfo.Data.POINTTYPE;
import com.app.utills.LogUtills;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
 * 写入EXCEL
 */
public class ExcelUtils {
    public static WritableFont arial14font = null;
    public static WritableCellFormat arial14format = null;
    public static WritableFont arial10font = null;
    public static WritableCellFormat arial10format = null;
    public static WritableFont arial12font = null;
    public static WritableCellFormat arial12format = null;
    public final static String UTF8_ENCODING = "UTF-8";
    public final static String GBK_ENCODING = "GBK";

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
                ArrayList<String> list =  null;
                int size = objList.size();
                for (int j = 0; j < size; j++) {
                   list = (ArrayList<String>) objList.get(j);
                   int _size = list.size();
                    for (int i = 0; i < _size; i++) {
                        sheet.addCell(new Label(i, j + 1, list.get(i),
                                arial12format));
                    }
                }
                writebook.write();
                //	Toast.makeText(c, "导出到手机存储成功", Toast.LENGTH_SHORT).show();
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
     * 从Excel表中导入数据
     *
     * @param f
     * @param con
     * @param sheet
     * @return
     */
    public static List<BaseFieldInfos> read2DB(File f, int sheet, Context con) {
        ArrayList<BaseFieldInfos> _pInfos = new ArrayList<BaseFieldInfos>();
        try {
            Workbook course = null;
            course = Workbook.getWorkbook(f);
            Cell cell = null;
            if (sheet == 0) { //点表
                Sheet _sheetP = course.getSheet(sheet);
                BaseFieldPInfos _info = null;
                int _rowsP = _sheetP.getRows();
                for (int i = 1; i < _rowsP; i++) {
                     _info = new BaseFieldPInfos();
                    cell = _sheetP.getCell(0,i);
                    _info.buildingStructures = cell.getContents();
                    cell = _sheetP.getCell(1,i);
                    _info.depth = cell.getContents();
                    cell = _sheetP.getCell(2,i);
                    _info.endDirDepth = cell.getContents();
                    cell = _sheetP.getCell(3,i);
                    _info.expGroup = cell.getContents();
                    cell = _sheetP.getCell(4,i);
                    _info.exp_Date = cell.getContents();
                    cell = _sheetP.getCell(5,i);
                    _info.exp_Num = cell.getContents();
                    cell = _sheetP.getCell(6,i);
                    _info.feature = cell.getContents();
                    cell = _sheetP.getCell(7,i);
                    _info.id = cell.getContents();
                    cell = _sheetP.getCell(8,i);
                    _info.latitude = Double.parseDouble(cell.getContents());
                    cell = _sheetP.getCell(9,i);
                    _info.longitude = Double.parseDouble(cell.getContents());
                    cell = _sheetP.getCell(10,i);
                    _info.picture = cell.getContents();
                    cell = _sheetP.getCell(11,i);
                    _info.pipeOffset = cell.getContents();
                    cell = _sheetP.getCell(12,i);
                    _info.puzzle = cell.getContents();
                    cell = _sheetP.getCell(13,i);
                    _info.remark = cell.getContents();
                    cell = _sheetP.getCell(14,i);
                    _info.road = cell.getContents();
                    cell = _sheetP.getCell(15,i);
                    _info.serialNum = Integer.parseInt(cell.getContents());
                    cell = _sheetP.getCell(16,i);
                    _info.situation = cell.getContents();
                    cell = _sheetP.getCell(17,i);
                    _info.startDirDepth = cell.getContents();
                    cell = _sheetP.getCell(18,i);
                    _info.state = cell.getContents();
                    cell = _sheetP.getCell(19,i);
                    _info.subsid = cell.getContents();
                    cell = _sheetP.getCell(20,i);
                    _info.surf_H = cell.getContents();
                    cell = _sheetP.getCell(21,i);
                    _info.symbol = cell.getContents();
                    cell = _sheetP.getCell(22,i);
                    _info.symbolExpression = cell.getContents();
                    cell = _sheetP.getCell(23,i);
                    _info.symbolID = Integer.parseInt(cell.getContents());
                    cell = _sheetP.getCell(24,i);
                    _info.symbolSizeX = Double.parseDouble(cell.getContents());
                    cell = _sheetP.getCell(25,i);
                    _info.symbolSizeY = Double.parseDouble(cell.getContents());
                    cell = _sheetP.getCell(26,i);
                    _info.wellCoverMaterial = cell.getContents();
                    cell = _sheetP.getCell(27,i);
                    _info.wellCoverSize = cell.getContents();
                    cell = _sheetP.getCell(28,i);
                    _info.wellDeep = cell.getContents();
                    cell = _sheetP.getCell(29,i);
                    _info.wellMud = cell.getContents();
                    cell = _sheetP.getCell(30,i);
                    _info.wellSize = cell.getContents();
                    cell = _sheetP.getCell(31,i);
                    _info.wellWater = cell.getContents();
                    cell = _sheetP.getCell(32,i);
                    _info.code = cell.getContents();
                    cell = _sheetP.getCell(33,i);
                    _info.datasetName = cell.getContents();
                    cell = _sheetP.getCell(34,i);
                    _info.pipeType = cell.getContents();
                    cell = _sheetP.getCell(35,i);
                    _info.rangeExpression = Double.parseDouble(cell.getContents());
                    cell = _sheetP.getCell(36,i);
                    _info.shortCode = cell.getContents();
                    cell = _sheetP.getCell(37,i);
                    _info.submitName = cell.getContents();
                    cell = _sheetP.getCell(38,i);
                    _info.sysId = Integer.parseInt(cell.getContents());
                    cell = _sheetP.getCell(39,i);
                    _info.type = POINTTYPE.valueOf(cell.getContents());
                    _pInfos.add(_info);
                }

            } else if (sheet == 1) { //线表

                Sheet _sheetL = course.getSheet(sheet);
                BaseFieldLInfos _infoL = null;
                int _rowsL = _sheetL.getRows();
                for (int i = 1; i < _rowsL; i++) {
                     _infoL = new BaseFieldLInfos();
                    cell = _sheetL.getCell(0,i);
                    _infoL.belong = cell.getContents();
                    cell = _sheetL.getCell(1,i);
                    _infoL.benDeep = cell.getContents();
                    cell = _sheetL.getCell(2,i);
                    _infoL.benExpNum = cell.getContents();
                    cell = _sheetL.getCell(3,i);
                    _infoL.burialDifference = cell.getContents();
                    cell = _sheetL.getCell(4,i);
                    _infoL.buried = cell.getContents();
                    cell = _sheetL.getCell(5,i);
                    _infoL.cabNum = cell.getContents();
                    cell = _sheetL.getCell(6,i);
                    _infoL.d_S = cell.getContents();
                    cell = _sheetL.getCell(7,i);
                    _infoL.endDeep = cell.getContents();
                    cell = _sheetL.getCell(8,i);
                    _infoL.endExpNum = cell.getContents();
                    cell = _sheetL.getCell(9,i);
                    _infoL.endLatitude = Double.parseDouble(cell.getContents());
                    cell = _sheetL.getCell(10,i);
                    _infoL.endLongitude = Double.parseDouble(cell.getContents());
                    cell = _sheetL.getCell(11,i);
                    _infoL.exp_Date = cell.getContents();
                    cell = _sheetL.getCell(12,i);
                    _infoL.holeDiameter = cell.getContents();
                    cell = _sheetL.getCell(13,i);
                    _infoL.id = cell.getContents();
                    cell = _sheetL.getCell(14,i);
                    _infoL.labelTag = cell.getContents();
                    cell = _sheetL.getCell(15,i);
                    _infoL.material = cell.getContents();
                    cell = _sheetL.getCell(16,i);
                    _infoL.pipeLength = cell.getContents();
                    cell = _sheetL.getCell(17,i);
                    _infoL.pipeSize = cell.getContents();
                    cell = _sheetL.getCell(18,i);
                    _infoL.pressure = cell.getContents();
                    cell = _sheetL.getCell(19,i);
                    _infoL.puzzle = cell.getContents();
                    cell = _sheetL.getCell(20,i);
                    _infoL.remark = cell.getContents();
                    cell = _sheetL.getCell(21,i);
                    _infoL.rowXCol = cell.getContents();
                    cell = _sheetL.getCell(22,i);
                    _infoL.startLatitude = Double.parseDouble(cell.getContents());
                    cell = _sheetL.getCell(23,i);
                    _infoL.startLongitude = Double.parseDouble(cell.getContents());
                    cell = _sheetL.getCell(24,i);
                    _infoL.state = cell.getContents();
                    cell = _sheetL.getCell(25,i);
                    _infoL.totalHole = cell.getContents();
                    cell = _sheetL.getCell(26,i);
                    _infoL.usedHole = cell.getContents();
                    cell = _sheetL.getCell(27,i);
                    _infoL.voltage = cell.getContents();
                    cell = _sheetL.getCell(28,i);
                    _infoL.code = cell.getContents();
                    cell = _sheetL.getCell(29,i);
                    _infoL.datasetName = cell.getContents();
                    cell = _sheetL.getCell(30,i);
                    _infoL.pipeType= cell.getContents();
                    cell = _sheetL.getCell(31,i);
                    _infoL.rangeExpression = Double.parseDouble(cell.getContents());
                    cell = _sheetL.getCell(32,i);
                    _infoL.shortCode = cell.getContents();
                    cell = _sheetL.getCell(33,i);
                    _infoL.submitName = cell.getContents();
                    cell = _sheetL.getCell(34,i);
                    _infoL.sysId = Integer.parseInt(cell.getContents());
                    cell = _sheetL.getCell(35,i);
                    _infoL.type = POINTTYPE.valueOf(cell.getContents());
                    _pInfos.add(_infoL);
                }
            }
            course.close();
            //sheet.getRows() 获取excel总行数，即总数量

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
			LogUtills.i("excel",e.getMessage());
        }

        return _pInfos;
    }


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
