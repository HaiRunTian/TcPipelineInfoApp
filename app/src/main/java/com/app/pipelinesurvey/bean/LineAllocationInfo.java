package com.app.pipelinesurvey.bean;

/**
 * Created by Kevin on 2019-03-19.
 */

public class LineAllocationInfo {
   int id;
   String name;
   String width;
   String colorName;
   String color;
   String symbolID;
   String shortCall;
   String type;
   String typeCode;
   String remark;
   String typeShortCall;
   String standard;
   String city;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getWidth() {
      return width;
   }

   public void setWidth(String width) {
      this.width = width;
   }

   public String getColorName() {
      return colorName;
   }

   public void setColorName(String colorName) {
      this.colorName = colorName;
   }

   public String getColor() {
      return color;
   }

   public void setColor(String color) {
      this.color = color;
   }

   public String getSymbolID() {
      return symbolID;
   }

   public void setSymbolID(String symbolID) {
      this.symbolID = symbolID;
   }

   public String getShortCall() {
      return shortCall;
   }

   public void setShortCall(String shortCall) {
      this.shortCall = shortCall;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getTypeCode() {
      return typeCode;
   }

   public void setTypeCode(String typeCode) {
      this.typeCode = typeCode;
   }

   public String getRemark() {
      return remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public String getTypeShortCall() {
      return typeShortCall;
   }

   public void setTypeShortCall(String typeShortCall) {
      this.typeShortCall = typeShortCall;
   }

   public String getStandard() {
      return standard;
   }

   public void setStandard(String standard) {
      this.standard = standard;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   @Override
   public String toString() {
      return "LineAllocationInfo{" +
              "id=" + id +
              ", name='" + name + '\'' +
              ", width='" + width + '\'' +
              ", colorName='" + colorName + '\'' +
              ", color='" + color + '\'' +
              ", symbolID='" + symbolID + '\'' +
              ", shortCall='" + shortCall + '\'' +
              ", type='" + type + '\'' +
              ", typeCode='" + typeCode + '\'' +
              ", remark='" + remark + '\'' +
              ", typeShortCall='" + typeShortCall + '\'' +
              ", standard='" + standard + '\'' +
              ", city='" + city + '\'' +
              '}';
   }
}
