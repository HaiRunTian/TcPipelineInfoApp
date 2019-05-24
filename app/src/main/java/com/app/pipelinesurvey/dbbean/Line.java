package com.app.pipelinesurvey.dbbean;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;


/**
 * @Author HaiRun
 * @Time 2019/2/14.15:46
 */
@Entity
public class Line {
    @Id(autoincrement = true)
    private long id;
    private String PlineEncod; //管线段编码
    private String PipeType; //管线点号（起点点号）
    private String BenExpNum; //管线点号（起点点号）
    private String EndExpNum; //连接点号（终点点号）
    private String Material; //管线材料
    private String Buried; //埋设方式
    private String D_S; //断面尺寸
    private String RowXCol; //行X列
    private int BenDeep; //起点埋深
    private int EndDeep; //终点埋深
    private int TotalHole; //总孔数
    private int UsedHole; //已用孔数
    private int CabNum; //总根数
    private String Voltage; //电压
    private String Pressure; //电压
    private String HoleDiameter; //孔径
    private Date Bulld_Date; //埋设日期
    private String Belong; //权属单位
    private String Remark; //备注
    private String Explain; //说明内容（用于在图上展文本）
    private Date Exp_Date; //探查时间
    private String Exp_Unit; //探查单位
    private String State; //状态
    private String Road; //道路名称
    private double BenX; //起点平面横坐标X
    private double BenY; //起点平面纵坐标Y
    private double BenH; //起点地面高程H
    private double EndX; //终点平面横坐标X
    private double EndY; //终点平面纵坐标Y
    private double EndH; //终点地面高程H
    private double NoteX; //管线注记坐标X
    private double NoteY; //管线注记坐标Y
    private boolean IsDrawNoteText; //是否展绘注记文本
    private String Temp1; //临时字段1
    private String Temp2; //临时字段2
    private String Temp3; //临时字段3
    private String Import_Date; //数据导入或添加时间
    private double PsCheQiBenX; //图上点号坐标X
    private double PsCheQiBenY; //图上点号坐标Y
    private double PsCheQiEndX; //图上点号坐标
    private double  PsCheQiEndY; //图上点号坐标
    private String BenExpMethod; //
    private String EndExpMethod; //
    private String PsCheQiBenLeftRight; //
    private String PsCheQiEndLeftRight; //
    @Generated(hash = 1885702403)
    public Line(long id, String PlineEncod, String PipeType, String BenExpNum,
            String EndExpNum, String Material, String Buried, String D_S,
            String RowXCol, int BenDeep, int EndDeep, int TotalHole, int UsedHole,
            int CabNum, String Voltage, String Pressure, String HoleDiameter,
            Date Bulld_Date, String Belong, String Remark, String Explain,
            Date Exp_Date, String Exp_Unit, String State, String Road, double BenX,
            double BenY, double BenH, double EndX, double EndY, double EndH,
            double NoteX, double NoteY, boolean IsDrawNoteText, String Temp1,
            String Temp2, String Temp3, String Import_Date, double PsCheQiBenX,
            double PsCheQiBenY, double PsCheQiEndX, double PsCheQiEndY,
            String BenExpMethod, String EndExpMethod, String PsCheQiBenLeftRight,
            String PsCheQiEndLeftRight) {
        this.id = id;
        this.PlineEncod = PlineEncod;
        this.PipeType = PipeType;
        this.BenExpNum = BenExpNum;
        this.EndExpNum = EndExpNum;
        this.Material = Material;
        this.Buried = Buried;
        this.D_S = D_S;
        this.RowXCol = RowXCol;
        this.BenDeep = BenDeep;
        this.EndDeep = EndDeep;
        this.TotalHole = TotalHole;
        this.UsedHole = UsedHole;
        this.CabNum = CabNum;
        this.Voltage = Voltage;
        this.Pressure = Pressure;
        this.HoleDiameter = HoleDiameter;
        this.Bulld_Date = Bulld_Date;
        this.Belong = Belong;
        this.Remark = Remark;
        this.Explain = Explain;
        this.Exp_Date = Exp_Date;
        this.Exp_Unit = Exp_Unit;
        this.State = State;
        this.Road = Road;
        this.BenX = BenX;
        this.BenY = BenY;
        this.BenH = BenH;
        this.EndX = EndX;
        this.EndY = EndY;
        this.EndH = EndH;
        this.NoteX = NoteX;
        this.NoteY = NoteY;
        this.IsDrawNoteText = IsDrawNoteText;
        this.Temp1 = Temp1;
        this.Temp2 = Temp2;
        this.Temp3 = Temp3;
        this.Import_Date = Import_Date;
        this.PsCheQiBenX = PsCheQiBenX;
        this.PsCheQiBenY = PsCheQiBenY;
        this.PsCheQiEndX = PsCheQiEndX;
        this.PsCheQiEndY = PsCheQiEndY;
        this.BenExpMethod = BenExpMethod;
        this.EndExpMethod = EndExpMethod;
        this.PsCheQiBenLeftRight = PsCheQiBenLeftRight;
        this.PsCheQiEndLeftRight = PsCheQiEndLeftRight;
    }
    @Generated(hash = 1133511183)
    public Line() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getPlineEncod() {
        return this.PlineEncod;
    }
    public void setPlineEncod(String PlineEncod) {
        this.PlineEncod = PlineEncod;
    }
    public String getPipeType() {
        return this.PipeType;
    }
    public void setPipeType(String PipeType) {
        this.PipeType = PipeType;
    }
    public String getBenExpNum() {
        return this.BenExpNum;
    }
    public void setBenExpNum(String BenExpNum) {
        this.BenExpNum = BenExpNum;
    }
    public String getEndExpNum() {
        return this.EndExpNum;
    }
    public void setEndExpNum(String EndExpNum) {
        this.EndExpNum = EndExpNum;
    }
    public String getMaterial() {
        return this.Material;
    }
    public void setMaterial(String Material) {
        this.Material = Material;
    }
    public String getBuried() {
        return this.Buried;
    }
    public void setBuried(String Buried) {
        this.Buried = Buried;
    }
    public String getD_S() {
        return this.D_S;
    }
    public void setD_S(String D_S) {
        this.D_S = D_S;
    }
    public String getRowXCol() {
        return this.RowXCol;
    }
    public void setRowXCol(String RowXCol) {
        this.RowXCol = RowXCol;
    }
    public int getBenDeep() {
        return this.BenDeep;
    }
    public void setBenDeep(int BenDeep) {
        this.BenDeep = BenDeep;
    }
    public int getEndDeep() {
        return this.EndDeep;
    }
    public void setEndDeep(int EndDeep) {
        this.EndDeep = EndDeep;
    }
    public int getTotalHole() {
        return this.TotalHole;
    }
    public void setTotalHole(int TotalHole) {
        this.TotalHole = TotalHole;
    }
    public int getUsedHole() {
        return this.UsedHole;
    }
    public void setUsedHole(int UsedHole) {
        this.UsedHole = UsedHole;
    }
    public int getCabNum() {
        return this.CabNum;
    }
    public void setCabNum(int CabNum) {
        this.CabNum = CabNum;
    }
    public String getVoltage() {
        return this.Voltage;
    }
    public void setVoltage(String Voltage) {
        this.Voltage = Voltage;
    }
    public String getPressure() {
        return this.Pressure;
    }
    public void setPressure(String Pressure) {
        this.Pressure = Pressure;
    }
    public String getHoleDiameter() {
        return this.HoleDiameter;
    }
    public void setHoleDiameter(String HoleDiameter) {
        this.HoleDiameter = HoleDiameter;
    }
    public Date getBulld_Date() {
        return this.Bulld_Date;
    }
    public void setBulld_Date(Date Bulld_Date) {
        this.Bulld_Date = Bulld_Date;
    }
    public String getBelong() {
        return this.Belong;
    }
    public void setBelong(String Belong) {
        this.Belong = Belong;
    }
    public String getRemark() {
        return this.Remark;
    }
    public void setRemark(String Remark) {
        this.Remark = Remark;
    }
    public String getExplain() {
        return this.Explain;
    }
    public void setExplain(String Explain) {
        this.Explain = Explain;
    }
    public Date getExp_Date() {
        return this.Exp_Date;
    }
    public void setExp_Date(Date Exp_Date) {
        this.Exp_Date = Exp_Date;
    }
    public String getExp_Unit() {
        return this.Exp_Unit;
    }
    public void setExp_Unit(String Exp_Unit) {
        this.Exp_Unit = Exp_Unit;
    }
    public String getState() {
        return this.State;
    }
    public void setState(String State) {
        this.State = State;
    }
    public String getRoad() {
        return this.Road;
    }
    public void setRoad(String Road) {
        this.Road = Road;
    }
    public double getBenX() {
        return this.BenX;
    }
    public void setBenX(double BenX) {
        this.BenX = BenX;
    }
    public double getBenY() {
        return this.BenY;
    }
    public void setBenY(double BenY) {
        this.BenY = BenY;
    }
    public double getBenH() {
        return this.BenH;
    }
    public void setBenH(double BenH) {
        this.BenH = BenH;
    }
    public double getEndX() {
        return this.EndX;
    }
    public void setEndX(double EndX) {
        this.EndX = EndX;
    }
    public double getEndY() {
        return this.EndY;
    }
    public void setEndY(double EndY) {
        this.EndY = EndY;
    }
    public double getEndH() {
        return this.EndH;
    }
    public void setEndH(double EndH) {
        this.EndH = EndH;
    }
    public double getNoteX() {
        return this.NoteX;
    }
    public void setNoteX(double NoteX) {
        this.NoteX = NoteX;
    }
    public double getNoteY() {
        return this.NoteY;
    }
    public void setNoteY(double NoteY) {
        this.NoteY = NoteY;
    }
    public boolean getIsDrawNoteText() {
        return this.IsDrawNoteText;
    }
    public void setIsDrawNoteText(boolean IsDrawNoteText) {
        this.IsDrawNoteText = IsDrawNoteText;
    }
    public String getTemp1() {
        return this.Temp1;
    }
    public void setTemp1(String Temp1) {
        this.Temp1 = Temp1;
    }
    public String getTemp2() {
        return this.Temp2;
    }
    public void setTemp2(String Temp2) {
        this.Temp2 = Temp2;
    }
    public String getTemp3() {
        return this.Temp3;
    }
    public void setTemp3(String Temp3) {
        this.Temp3 = Temp3;
    }
    public String getImport_Date() {
        return this.Import_Date;
    }
    public void setImport_Date(String Import_Date) {
        this.Import_Date = Import_Date;
    }
    public double getPsCheQiBenX() {
        return this.PsCheQiBenX;
    }
    public void setPsCheQiBenX(double PsCheQiBenX) {
        this.PsCheQiBenX = PsCheQiBenX;
    }
    public double getPsCheQiBenY() {
        return this.PsCheQiBenY;
    }
    public void setPsCheQiBenY(double PsCheQiBenY) {
        this.PsCheQiBenY = PsCheQiBenY;
    }
    public double getPsCheQiEndX() {
        return this.PsCheQiEndX;
    }
    public void setPsCheQiEndX(double PsCheQiEndX) {
        this.PsCheQiEndX = PsCheQiEndX;
    }
    public double getPsCheQiEndY() {
        return this.PsCheQiEndY;
    }
    public void setPsCheQiEndY(double PsCheQiEndY) {
        this.PsCheQiEndY = PsCheQiEndY;
    }
    public String getBenExpMethod() {
        return this.BenExpMethod;
    }
    public void setBenExpMethod(String BenExpMethod) {
        this.BenExpMethod = BenExpMethod;
    }
    public String getEndExpMethod() {
        return this.EndExpMethod;
    }
    public void setEndExpMethod(String EndExpMethod) {
        this.EndExpMethod = EndExpMethod;
    }
    public String getPsCheQiBenLeftRight() {
        return this.PsCheQiBenLeftRight;
    }
    public void setPsCheQiBenLeftRight(String PsCheQiBenLeftRight) {
        this.PsCheQiBenLeftRight = PsCheQiBenLeftRight;
    }
    public String getPsCheQiEndLeftRight() {
        return this.PsCheQiEndLeftRight;
    }
    public void setPsCheQiEndLeftRight(String PsCheQiEndLeftRight) {
        this.PsCheQiEndLeftRight = PsCheQiEndLeftRight;
    }
}
