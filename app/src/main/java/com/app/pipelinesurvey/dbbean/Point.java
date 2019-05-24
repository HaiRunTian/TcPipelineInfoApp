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
public class Point {
    @Id(autoincrement = true)
    private long id; //
    private String PointEncod;//管线点编码
    private String Map_Name;//图幅号
    private String PipeType;//管线种类
    private String Exp_Num;//物探点号
    private String Map_Num;//图上点号
    private String Sur_Num;//图上点号
    private String Feature;//点特征
    private String Subsid;//附属物
    private long X;//平面横坐标
    private long Y;//平面纵坐标
    private double Surf_H;//地面高程
    private String Offset;//管偏
    private String WellSize;//窨井规格
    private double WellDeep;//窨井深度
    private double WellWater;//窨井水深
    private double WellMud;//窨井淤泥
    private String WellCoverShape;//井盖形状
    private String WellCoverSize; //井盖尺寸
    private String WellCoverMaterial;//井盖材质
    private String ExpMethod;//探查方法
    private String Road;//道路名称
    private Date Exp_Date;//探查日期
    private String Exp_Unit;//探查单位
    private String Remark;//备注
    private String Picture;//照片
    private String Puzzle;//疑难问题
    private String symbol; //符号专题图作用点专题图表达式
    private String Explain;//说明内容（用于在图上展文本）
    private String State;//状态
    private int Pipes;//管点多少个连接方向
    private int LowDeep;//最低深度
    private int UpDeep;//最高深度
    private double ExpX;//物探点号坐标X
    private double ExpY;//物探点号坐标Y
    private double MapX;//图上点号坐标X
    private double MapY;//图上点号坐标Y
    private String ExpGroup;//物探组长
    private String SurGroup;//测量组长
    private String InsGroup;//内业组长
    private double Angle;//符号旋转角度
    private double Temp1;//临时字段1
    private double Temp2;//临时字段2
    private double Temp3;//临时字段3
    private Date Import_Date;//数据导入或添加时间
    private double PsCheQiX;//图上点号坐标X
    private double PsCheQiY;//图上点号坐标Y
    private double ExpCheQiX;//
    private double ExpCheQiY;//
    @Generated(hash = 1707198646)
    public Point(long id, String PointEncod, String Map_Name, String PipeType,
            String Exp_Num, String Map_Num, String Sur_Num, String Feature,
            String Subsid, long X, long Y, double Surf_H, String Offset,
            String WellSize, double WellDeep, double WellWater, double WellMud,
            String WellCoverShape, String WellCoverSize, String WellCoverMaterial,
            String ExpMethod, String Road, Date Exp_Date, String Exp_Unit,
            String Remark, String Picture, String Puzzle, String symbol,
            String Explain, String State, int Pipes, int LowDeep, int UpDeep,
            double ExpX, double ExpY, double MapX, double MapY, String ExpGroup,
            String SurGroup, String InsGroup, double Angle, double Temp1,
            double Temp2, double Temp3, Date Import_Date, double PsCheQiX,
            double PsCheQiY, double ExpCheQiX, double ExpCheQiY) {
        this.id = id;
        this.PointEncod = PointEncod;
        this.Map_Name = Map_Name;
        this.PipeType = PipeType;
        this.Exp_Num = Exp_Num;
        this.Map_Num = Map_Num;
        this.Sur_Num = Sur_Num;
        this.Feature = Feature;
        this.Subsid = Subsid;
        this.X = X;
        this.Y = Y;
        this.Surf_H = Surf_H;
        this.Offset = Offset;
        this.WellSize = WellSize;
        this.WellDeep = WellDeep;
        this.WellWater = WellWater;
        this.WellMud = WellMud;
        this.WellCoverShape = WellCoverShape;
        this.WellCoverSize = WellCoverSize;
        this.WellCoverMaterial = WellCoverMaterial;
        this.ExpMethod = ExpMethod;
        this.Road = Road;
        this.Exp_Date = Exp_Date;
        this.Exp_Unit = Exp_Unit;
        this.Remark = Remark;
        this.Picture = Picture;
        this.Puzzle = Puzzle;
        this.symbol = symbol;
        this.Explain = Explain;
        this.State = State;
        this.Pipes = Pipes;
        this.LowDeep = LowDeep;
        this.UpDeep = UpDeep;
        this.ExpX = ExpX;
        this.ExpY = ExpY;
        this.MapX = MapX;
        this.MapY = MapY;
        this.ExpGroup = ExpGroup;
        this.SurGroup = SurGroup;
        this.InsGroup = InsGroup;
        this.Angle = Angle;
        this.Temp1 = Temp1;
        this.Temp2 = Temp2;
        this.Temp3 = Temp3;
        this.Import_Date = Import_Date;
        this.PsCheQiX = PsCheQiX;
        this.PsCheQiY = PsCheQiY;
        this.ExpCheQiX = ExpCheQiX;
        this.ExpCheQiY = ExpCheQiY;
    }
    @Generated(hash = 1977038299)
    public Point() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getPointEncod() {
        return this.PointEncod;
    }
    public void setPointEncod(String PointEncod) {
        this.PointEncod = PointEncod;
    }
    public String getMap_Name() {
        return this.Map_Name;
    }
    public void setMap_Name(String Map_Name) {
        this.Map_Name = Map_Name;
    }
    public String getPipeType() {
        return this.PipeType;
    }
    public void setPipeType(String PipeType) {
        this.PipeType = PipeType;
    }
    public String getExp_Num() {
        return this.Exp_Num;
    }
    public void setExp_Num(String Exp_Num) {
        this.Exp_Num = Exp_Num;
    }
    public String getMap_Num() {
        return this.Map_Num;
    }
    public void setMap_Num(String Map_Num) {
        this.Map_Num = Map_Num;
    }
    public String getSur_Num() {
        return this.Sur_Num;
    }
    public void setSur_Num(String Sur_Num) {
        this.Sur_Num = Sur_Num;
    }
    public String getFeature() {
        return this.Feature;
    }
    public void setFeature(String Feature) {
        this.Feature = Feature;
    }
    public String getSubsid() {
        return this.Subsid;
    }
    public void setSubsid(String Subsid) {
        this.Subsid = Subsid;
    }
    public long getX() {
        return this.X;
    }
    public void setX(long X) {
        this.X = X;
    }
    public long getY() {
        return this.Y;
    }
    public void setY(long Y) {
        this.Y = Y;
    }
    public double getSurf_H() {
        return this.Surf_H;
    }
    public void setSurf_H(double Surf_H) {
        this.Surf_H = Surf_H;
    }
    public String getOffset() {
        return this.Offset;
    }
    public void setOffset(String Offset) {
        this.Offset = Offset;
    }
    public String getWellSize() {
        return this.WellSize;
    }
    public void setWellSize(String WellSize) {
        this.WellSize = WellSize;
    }
    public double getWellDeep() {
        return this.WellDeep;
    }
    public void setWellDeep(double WellDeep) {
        this.WellDeep = WellDeep;
    }
    public double getWellWater() {
        return this.WellWater;
    }
    public void setWellWater(double WellWater) {
        this.WellWater = WellWater;
    }
    public double getWellMud() {
        return this.WellMud;
    }
    public void setWellMud(double WellMud) {
        this.WellMud = WellMud;
    }
    public String getWellCoverShape() {
        return this.WellCoverShape;
    }
    public void setWellCoverShape(String WellCoverShape) {
        this.WellCoverShape = WellCoverShape;
    }
    public String getWellCoverSize() {
        return this.WellCoverSize;
    }
    public void setWellCoverSize(String WellCoverSize) {
        this.WellCoverSize = WellCoverSize;
    }
    public String getWellCoverMaterial() {
        return this.WellCoverMaterial;
    }
    public void setWellCoverMaterial(String WellCoverMaterial) {
        this.WellCoverMaterial = WellCoverMaterial;
    }
    public String getExpMethod() {
        return this.ExpMethod;
    }
    public void setExpMethod(String ExpMethod) {
        this.ExpMethod = ExpMethod;
    }
    public String getRoad() {
        return this.Road;
    }
    public void setRoad(String Road) {
        this.Road = Road;
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
    public String getRemark() {
        return this.Remark;
    }
    public void setRemark(String Remark) {
        this.Remark = Remark;
    }
    public String getPicture() {
        return this.Picture;
    }
    public void setPicture(String Picture) {
        this.Picture = Picture;
    }
    public String getPuzzle() {
        return this.Puzzle;
    }
    public void setPuzzle(String Puzzle) {
        this.Puzzle = Puzzle;
    }
    public String getSymbol() {
        return this.symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getExplain() {
        return this.Explain;
    }
    public void setExplain(String Explain) {
        this.Explain = Explain;
    }
    public String getState() {
        return this.State;
    }
    public void setState(String State) {
        this.State = State;
    }
    public int getPipes() {
        return this.Pipes;
    }
    public void setPipes(int Pipes) {
        this.Pipes = Pipes;
    }
    public int getLowDeep() {
        return this.LowDeep;
    }
    public void setLowDeep(int LowDeep) {
        this.LowDeep = LowDeep;
    }
    public int getUpDeep() {
        return this.UpDeep;
    }
    public void setUpDeep(int UpDeep) {
        this.UpDeep = UpDeep;
    }
    public double getExpX() {
        return this.ExpX;
    }
    public void setExpX(double ExpX) {
        this.ExpX = ExpX;
    }
    public double getExpY() {
        return this.ExpY;
    }
    public void setExpY(double ExpY) {
        this.ExpY = ExpY;
    }
    public double getMapX() {
        return this.MapX;
    }
    public void setMapX(double MapX) {
        this.MapX = MapX;
    }
    public double getMapY() {
        return this.MapY;
    }
    public void setMapY(double MapY) {
        this.MapY = MapY;
    }
    public String getExpGroup() {
        return this.ExpGroup;
    }
    public void setExpGroup(String ExpGroup) {
        this.ExpGroup = ExpGroup;
    }
    public String getSurGroup() {
        return this.SurGroup;
    }
    public void setSurGroup(String SurGroup) {
        this.SurGroup = SurGroup;
    }
    public String getInsGroup() {
        return this.InsGroup;
    }
    public void setInsGroup(String InsGroup) {
        this.InsGroup = InsGroup;
    }
    public double getAngle() {
        return this.Angle;
    }
    public void setAngle(double Angle) {
        this.Angle = Angle;
    }
    public double getTemp1() {
        return this.Temp1;
    }
    public void setTemp1(double Temp1) {
        this.Temp1 = Temp1;
    }
    public double getTemp2() {
        return this.Temp2;
    }
    public void setTemp2(double Temp2) {
        this.Temp2 = Temp2;
    }
    public double getTemp3() {
        return this.Temp3;
    }
    public void setTemp3(double Temp3) {
        this.Temp3 = Temp3;
    }
    public Date getImport_Date() {
        return this.Import_Date;
    }
    public void setImport_Date(Date Import_Date) {
        this.Import_Date = Import_Date;
    }
    public double getPsCheQiX() {
        return this.PsCheQiX;
    }
    public void setPsCheQiX(double PsCheQiX) {
        this.PsCheQiX = PsCheQiX;
    }
    public double getPsCheQiY() {
        return this.PsCheQiY;
    }
    public void setPsCheQiY(double PsCheQiY) {
        this.PsCheQiY = PsCheQiY;
    }
    public double getExpCheQiX() {
        return this.ExpCheQiX;
    }
    public void setExpCheQiX(double ExpCheQiX) {
        this.ExpCheQiX = ExpCheQiX;
    }
    public double getExpCheQiY() {
        return this.ExpCheQiY;
    }
    public void setExpCheQiY(double ExpCheQiY) {
        this.ExpCheQiY = ExpCheQiY;
    }


}
