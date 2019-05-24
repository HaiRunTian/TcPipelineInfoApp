package com.app.pipelinesurvey.bean;

/**
 * Created by Kevin on 2018-06-15.
 * 管线类型
 */

public class PipeTypeInfo {
    private int id;
    private String serial_no;
    private String pipe_type;
    private String pipe_type_value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getPipe_type() {
        return pipe_type;
    }

    public void setPipe_type(String pipe_type) {
        this.pipe_type = pipe_type;
    }

    public String getPipe_type_value() {
        return pipe_type_value;
    }

    public void setPipe_type_value(String pipe_type_value) {
        this.pipe_type_value = pipe_type_value;
    }
}
