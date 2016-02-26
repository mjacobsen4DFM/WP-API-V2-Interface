package com.DFM.Controllers;

import javax.ws.rs.ApplicationPath;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mick on 2/3/2016.
 */
@ApplicationPath("/")
public class Application extends javax.ws.rs.core.Application {
    public static void start() {
        Map<String, String> conf = new HashMap<String, String>();
        conf.put("redisHost", "ec2-52-35-19-0.us-west-2.compute.amazonaws.com");
        conf.put("redisPort", "6379");
        conf.put("redisTimeout", "30000");
        conf.put("redisPassword", "bkT$esY9-VckUR*d");
        conf.put("publisherKey", "publishers:*");
        conf.put("xsltRootPath", "/etc/storm/transactstorm/conf/xslt/");
    }
}
