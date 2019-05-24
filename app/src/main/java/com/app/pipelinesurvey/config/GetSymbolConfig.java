package com.app.pipelinesurvey.config;

import com.app.pipelinesurvey.R;
import com.app.pipelinesurvey.bean.Symbolbean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HaiRun
 * @time 2019/4/28.14:55
 * 获取专题图点符号库配置
 */

public class GetSymbolConfig {

    private static GetSymbolConfig get_symConfig = null;
    private  List<Symbolbean> m_symbolInfos  = null;
    public  synchronized static GetSymbolConfig ins() {
        if (get_symConfig == null) {
            get_symConfig = new GetSymbolConfig();
        }
        return get_symConfig;
    }

    public List<Symbolbean> getSymbolConfig(){
        //给水
        m_symbolInfos = new ArrayList<>();
        m_symbolInfos.add(new Symbolbean("J-放水口",11, R.mipmap.ic_symbol_j_fangshuikou_11));
        m_symbolInfos.add(new Symbolbean("J-变径",10, R.mipmap.ic_symbol_j_bianjin_10));
        m_symbolInfos.add(new Symbolbean("J-入户",9, R.mipmap.ic_symbol_j_ruhu_9));
        m_symbolInfos.add(new Symbolbean("J-出地",8, R.mipmap.ic_symbol_j_chudi_8));
        m_symbolInfos.add(new Symbolbean("J-出测区",7, R.mipmap.ic_symbol_j_chucequ_7));
        m_symbolInfos.add(new Symbolbean("J-水表",6, R.mipmap.ic_symbol_j_shuibiao_6));
        m_symbolInfos.add(new Symbolbean("J-阀门井",2, R.mipmap.ic_symbol_j_famenjin_2));
        m_symbolInfos.add(new Symbolbean("J-消防栓",3, R.mipmap.ic_symbol_j_xiaofangxuan_3));
        m_symbolInfos.add(new Symbolbean("J-阀门",4, R.mipmap.ic_symbol_j_famen_4));
        m_symbolInfos.add(new Symbolbean("J-预留口",5, R.mipmap.ic_symbol_j_yuliukou_5));
        m_symbolInfos.add(new Symbolbean("J-探测点",1, R.mipmap.ic_symbol_j_tancedian_1));
        //雨、污、排
        m_symbolInfos.add(new Symbolbean("YPW-立管点",491, R.mipmap.ic_symbol_ywp_point_491));
        m_symbolInfos.add(new Symbolbean("YPW-户出/入户",38, R.mipmap.ic_symbol_ywp_inlet_pipe_38));
        m_symbolInfos.add(new Symbolbean("YPW-化粪池",40, R.mipmap.ic_symbol_ywp_huafenchi_40));
        m_symbolInfos.add(new Symbolbean("YPW-进/出水口",39, R.mipmap.ic_symbol_ywp_jinchushuikou_39));
        m_symbolInfos.add(new Symbolbean("YPW-出测区",37, R.mipmap.ic_symbol_ywp_chucequ_37));
        m_symbolInfos.add(new Symbolbean("YPW-雨/污水篦",36, R.mipmap.ic_symbol_ywp_yuwushuibi_36));
        m_symbolInfos.add(new Symbolbean("YPW-预留口",35, R.mipmap.ic_symbol_ywp_yuliukou_34));
        m_symbolInfos.add(new Symbolbean("YPW-流水放向",34, R.mipmap.ic_symbol_ywp_liushuifangxiang_34));
        m_symbolInfos.add(new Symbolbean("YPW-窨井",33, R.mipmap.ic_symbol_ywp_yinjing_33));
        m_symbolInfos.add(new Symbolbean("YPW-探测点",32, R.mipmap.ic_symbol_ywp_tancedian_32));
        //电信
        m_symbolInfos.add(new Symbolbean("D-入户",47, R.mipmap.ic_symbol_d_ruhu_47));
        m_symbolInfos.add(new Symbolbean("D-电杆",50, R.mipmap.ic_symbol_d_diangan_50));
        m_symbolInfos.add(new Symbolbean("D-电话亭",49, R.mipmap.ic_symbol_d_dainhuating_49));
        m_symbolInfos.add(new Symbolbean("D-出测区",48, R.mipmap.ic_symbol_d_chucequ_48));
        m_symbolInfos.add(new Symbolbean("D-预留口",46, R.mipmap.ic_symbol_d_yuliukou_46));
        m_symbolInfos.add(new Symbolbean("D-上杆",45, R.mipmap.ic_symbol_d_shanggan_45));
        m_symbolInfos.add(new Symbolbean("D-接线箱",44, R.mipmap.ic_symbol_d_jiexianxiang_47));
        m_symbolInfos.add(new Symbolbean("D-手孔井",43, R.mipmap.ic_symbol_d_shoukongjin_43));
        m_symbolInfos.add(new Symbolbean("D-人孔井",42, R.mipmap.ic_symbol_d_renkongjin_42));
        m_symbolInfos.add(new Symbolbean("D-探测点",41, R.mipmap.ic_symbol_d_tancedian_41));
        m_symbolInfos.add(new Symbolbean("D-出地",525, R.mipmap.ic_symbol_d_chudi_525));
        //电力
        m_symbolInfos.add(new Symbolbean("L-高压电杆",31, R.mipmap.ic_symbol_l_ganyadiangan_31));
        m_symbolInfos.add(new Symbolbean("L-低压电杆",30, R.mipmap.ic_symbol_l_diyadiangan_30));
        m_symbolInfos.add(new Symbolbean("L-接线箱",488, R.mipmap.ic_symbol_l_jiexianxiang_488));
        m_symbolInfos.add(new Symbolbean("L-上杆",487, R.mipmap.ic_symbol_l_shanggan_487));
        m_symbolInfos.add(new Symbolbean("L-变电箱",486, R.mipmap.ic_symbol_l_biandianxiang486));
        m_symbolInfos.add(new Symbolbean("L-窨井",476, R.mipmap.ic_symbol_l_yinjin_476));
        m_symbolInfos.add(new Symbolbean("L-预留口",475, R.mipmap.ic_symbol_l_yuliukohg_475));
        m_symbolInfos.add(new Symbolbean("L-入户",474, R.mipmap.ic_symbol_l_ruhu_474));
        m_symbolInfos.add(new Symbolbean("L-出测区",29, R.mipmap.ic_symbol_l_chucequ_29));
        m_symbolInfos.add(new Symbolbean("L-探测点",472, R.mipmap.ic_symbol_l_tancedian_472));
        m_symbolInfos.add(new Symbolbean("L-路灯杆",517, R.mipmap.ic_symbol_l_ludenggan_517));
        m_symbolInfos.add(new Symbolbean("L-出地",518, R.mipmap.ic_symbol_l_chudi_518));
        //工业
        m_symbolInfos.add(new Symbolbean("G-出地",63, R.mipmap.ic_symbol_g_out_group_63));
        m_symbolInfos.add(new Symbolbean("G-出测区",62, R.mipmap.ic_symbol_g_out_check_62));
        m_symbolInfos.add(new Symbolbean("G-探测点",61, R.mipmap.ic_symbol_g_probe_point_61));
        m_symbolInfos.add(new Symbolbean("G-阀门井",490, R.mipmap.ic_symbol_g_valve_well_490));
        m_symbolInfos.add(new Symbolbean("G-预留口",489, R.mipmap.ic_symbol_g_reserved_489));
        m_symbolInfos.add(new Symbolbean("G-阀门",519, R.mipmap.ic_symbol_g_valve_519));
        m_symbolInfos.add(new Symbolbean("G-调压缸",520, R.mipmap.ic_symbol_g_tiaoyagang_520));
        m_symbolInfos.add(new Symbolbean("G-入户",521, R.mipmap.ic_symbol_g_ruhu_521));
        //煤气 燃气
        m_symbolInfos.add(new Symbolbean("MR-预留口",12, R.mipmap.ic_symbol_m_yuliukou_12));
        m_symbolInfos.add(new Symbolbean("MR-阀门井",485, R.mipmap.ic_symbol_m_famenjing_485));
        m_symbolInfos.add(new Symbolbean("MR-探测点",484, R.mipmap.ic_symbol_m_tancedian_484));
        m_symbolInfos.add(new Symbolbean("MR-出测区",483, R.mipmap.ic_symbol_m_chucequ_483));
        m_symbolInfos.add(new Symbolbean("MR-出地",482, R.mipmap.ic_symbol_m_chudi482));
        m_symbolInfos.add(new Symbolbean("MR-变径",481, R.mipmap.ic_symbol_m_bainjing_481));
        m_symbolInfos.add(new Symbolbean("MR-入户",480, R.mipmap.ic_symbol_m_ruhu480));
        m_symbolInfos.add(new Symbolbean("MR-阀门",479, R.mipmap.ic_symbol_m_famen_479));
        m_symbolInfos.add(new Symbolbean("MR-凝水缸",478, R.mipmap.ic_symbol_m_nishuikang_478));
        m_symbolInfos.add(new Symbolbean("MR-调压箱",477, R.mipmap.ic_symbol_m_diaoyaxiang_477));
        //军队
        m_symbolInfos.add(new Symbolbean("B-探测点",499, R.mipmap.ic_symbol_b_tanchedian_499));
        m_symbolInfos.add(new Symbolbean("B-人孔井",500, R.mipmap.ic_symbol_b_renkongjin_500));
        m_symbolInfos.add(new Symbolbean("B-手孔井",501, R.mipmap.ic_symbol_b_shoukongjin_501));
        m_symbolInfos.add(new Symbolbean("B-接线箱",502, R.mipmap.ic_symbol_b_jiexianxiang_502));
        m_symbolInfos.add(new Symbolbean("B-预留口",503, R.mipmap.ic_symbol_b_yuliukon_503));
        m_symbolInfos.add(new Symbolbean("B-出测区",504, R.mipmap.ic_symbol_b_chucequ_504));
        m_symbolInfos.add(new Symbolbean("B-上杆",505, R.mipmap.ic_symbol_b_shanggan_505));
        m_symbolInfos.add(new Symbolbean("B-出地",506, R.mipmap.ic_symbol_b_chudi_506));
        m_symbolInfos.add(new Symbolbean("B-入户",507, R.mipmap.ic_symbol_b_ruhu_507));
        //有视
        m_symbolInfos.add(new Symbolbean("T-探测点",508, R.mipmap.ic_symbol_t_tancedian_508));
        m_symbolInfos.add(new Symbolbean("T-人孔井",509, R.mipmap.ic_symbol_t_renkongjin_509));
        m_symbolInfos.add(new Symbolbean("T-手孔井",510, R.mipmap.ic_symbol_t_shoukongjin_510));
        m_symbolInfos.add(new Symbolbean("T-接线箱",511, R.mipmap.ic_symbol_t_jiexianxiang_511));
        m_symbolInfos.add(new Symbolbean("T-预留口",512, R.mipmap.ic_symbol_t_yuliukou_512));
        m_symbolInfos.add(new Symbolbean("T-出测区",513, R.mipmap.ic_symbol_t_chucequ_513));
        m_symbolInfos.add(new Symbolbean("T-上杆",514, R.mipmap.ic_symbol_t_shanggan_514));
        m_symbolInfos.add(new Symbolbean("T-出地",515, R.mipmap.ic_symbol_t_chudi_515));
        m_symbolInfos.add(new Symbolbean("T-入户",516, R.mipmap.ic_symbol_t_ruhu516));
        //路灯
        m_symbolInfos.add(new Symbolbean("S-地灯",492, R.mipmap.ic_symbol_s_lamp_492));
        m_symbolInfos.add(new Symbolbean("S-手孔井",52, R.mipmap.ic_symbol_s_shoukongjin_52));
        m_symbolInfos.add(new Symbolbean("S-路灯杆",473, R.mipmap.ic_symbol_s_ludenggan_473));
        m_symbolInfos.add(new Symbolbean("S-上杆",471, R.mipmap.ic_symbol_s_shanggan_471));
        m_symbolInfos.add(new Symbolbean("S-预留口",470, R.mipmap.ic_symbol_s_yuliukou_470));
        m_symbolInfos.add(new Symbolbean("S-接线箱",469, R.mipmap.ic_symbol_s_jiexianxiang_469));
        m_symbolInfos.add(new Symbolbean("S-入户",468, R.mipmap.ic_symbol_s_ruhu_468));
        m_symbolInfos.add(new Symbolbean("S-出测区",467, R.mipmap.ic_symbol_s_chucequ467));
        m_symbolInfos.add(new Symbolbean("S-探测点",498, R.mipmap.ic_symbol_s_tancedian_498));
        m_symbolInfos.add(new Symbolbean("S-电杆",522, R.mipmap.ic_symbol_s_diangan_522));
        m_symbolInfos.add(new Symbolbean("S-人孔井",523, R.mipmap.ic_symbol_s_renkongjin_523));
        m_symbolInfos.add(new Symbolbean("S-出地",524, R.mipmap.ic_symbol_s_chudi_524));
        //交通
        m_symbolInfos.add(new Symbolbean("X-探测点",526, R.mipmap.ic_symbol_x_tancedian_526));
        m_symbolInfos.add(new Symbolbean("X-电杆",527, R.mipmap.ic_symbol_x_diangan_527));
        m_symbolInfos.add(new Symbolbean("X-人孔井",528, R.mipmap.ic_symbol_x_shoukongjin528));
        m_symbolInfos.add(new Symbolbean("X-手孔井",529, R.mipmap.ic_symbol_x_shoukongjin_529));
        m_symbolInfos.add(new Symbolbean("X-接线箱",530, R.mipmap.ic_symbol_x_jiexianxiang_530));
        m_symbolInfos.add(new Symbolbean("X-预留口",531, R.mipmap.ic_symbol_x_yuliukou_531));
        m_symbolInfos.add(new Symbolbean("X-出测区",532, R.mipmap.ic_symbol_x_chucequ_532));
        m_symbolInfos.add(new Symbolbean("X-上杆",533, R.mipmap.ic_symbol_x_shanggan_533));
        m_symbolInfos.add(new Symbolbean("X-入户",534, R.mipmap.ic_symbol_x_ruhu_534));
        m_symbolInfos.add(new Symbolbean("X-出地",535, R.mipmap.ic_symbol_x_chudi535));

        return m_symbolInfos;
    }


}
