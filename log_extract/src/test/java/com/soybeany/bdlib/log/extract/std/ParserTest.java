package com.soybeany.bdlib.log.extract.std;

import com.soybeany.bdlib.log.extract.model.TimeIndex;
import com.soybeany.bdlib.log.extract.std.model.StdRow;
import com.soybeany.bdlib.log.extract.std.parser.StdRowParser;
import com.soybeany.bdlib.log.extract.std.parser.StdTimeParser;

import org.junit.Test;

/**
 * <br>Created by Soybeany on 2019/6/2.
 */
public class ParserTest {

    @Test
    public void rowParser() {
        StdRowParser parser = new StdRowParser();
        StdRow row = parser.toOutput("[19-02-14 08:36:47] [INFO] [http-nio-8080-exec-1] {AuthController:26}-成功");
        assert null != row;
    }

    @Test
    public void timeParser() {
        StdTimeParser parser = new StdTimeParser();
        TimeIndex index = parser.toOutput("19-02-14 08:36:47");
        assert null != index;
    }

}