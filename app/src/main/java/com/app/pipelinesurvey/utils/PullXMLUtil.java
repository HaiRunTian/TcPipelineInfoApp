package com.app.pipelinesurvey.utils;

import com.app.pipelinesurvey.bean.AppInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 2018-06-26.
 * Pull解析XML
 */

public class PullXMLUtil {
    public static List<String> parserXML2SqlList(InputStream inputStream, String parentNode,
                                                 String childNode) throws XmlPullParserException, IOException {
        List<String> _listSQL = null;
        XmlPullParserFactory _factory = XmlPullParserFactory.newInstance();
        XmlPullParser _parser = _factory.newPullParser();
        _parser.setInput(inputStream, "utf-8");
        int eventType = _parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if (_parser.getName().equals(parentNode)) {
                        _listSQL = new ArrayList<>();
                    } else if (_parser.getName().equals(childNode)) {
                        if (_listSQL != null) {
                            String sql = _parser.nextText();
                            if (!_listSQL.contains(sql))
                                _listSQL.add(sql);
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
                    default:break;
            }
            eventType = _parser.next();
        }
        return _listSQL;
    }

    public static AppInfo parserXMLOfAppInfo(InputStream inputStream) throws XmlPullParserException, IOException {
        AppInfo _appInfo = new AppInfo();
        XmlPullParserFactory _factory = XmlPullParserFactory.newInstance();
        XmlPullParser _parser = _factory.newPullParser();
        _parser.setInput(inputStream, "utf-8");
        int eventType = _parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if (_parser.getName().equals("app_name")) {
                        _appInfo.setApp_name(_parser.nextText());
                    } else if (_parser.getName().equals("version_code")) {
                        _appInfo.setVersion_code(_parser.nextText());
                    } else if (_parser.getName().equals("updated_date")) {
                        _appInfo.setUpdated_date(_parser.nextText());
                    } else if (_parser.getName().equals("dev_unit")) {
                        _appInfo.setDev_unit(_parser.nextText());
                    } else if (_parser.getName().equals("remark")) {
                        _appInfo.setRemark(_parser.nextText());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;

                    default:break;
            }
            eventType = _parser.next();
        }
        return _appInfo;
    }
}
