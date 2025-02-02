/**
 * Copyright  2017-2021 ZTE Corporation.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.holmes.common.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.onap.holmes.common.constant.AlarmConst;
import org.onap.holmes.common.utils.JerseyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.onap.holmes.common.utils.CommonUtils.getEnv;
import static org.onap.holmes.common.utils.CommonUtils.isIpAddress;

public class MicroServiceConfig {

    final static public String CONSUL_ADDR_SUF = ":8500/v1/catalog/service/";
    final static public String CONSUL_HOST = "CONSUL_HOST";
    final static public String HOSTNAME = "HOSTNAME";
    final static public String POD_IP = "POD_IP";
    final static public String CONFIG_BINDING_SERVICE = "CONFIG_BINDING_SERVICE";
    final static public String MSB_ADDR = "MSB_ADDR";
    final static public String MSB_IAG_SERVICE_HOST = "MSB_IAG_SERVICE_HOST";
    final static public String MSB_IAG_SERVICE_PORT = "MSB_IAG_SERVICE_PORT";

    final static public String AAI_HOSTNAME = "aai.onap";

    final static public Logger log = LoggerFactory.getLogger(MicroServiceConfig.class);

    public static String getConsulAddrInfo() {
        return "http://" + getEnv(CONSUL_HOST) + CONSUL_ADDR_SUF;
    }

    public static String getServiceAddrInfoFromDcaeConsulByHostName(String hostname) {
        String ret = null;
        String queryString = getConsulAddrInfo() + hostname;
        log.info("Query the " + hostname + " address using the URL: " + queryString);
        try {
            JsonArray addrArray = JsonParser.parseString(execQuery(queryString)).getAsJsonArray();
            if (addrArray.size() > 0) {
                JsonObject addrJson = addrArray.get(0).getAsJsonObject();
                if (addrJson != null && addrJson.get("ServiceAddress") != null
                        && addrJson.get("ServicePort") != null) {
                    ret = "http://" + addrJson.get("ServiceAddress").getAsString() + ":" + addrJson
                            .get("ServicePort").getAsString();
                }
            } else {
                log.info("No service info is returned from DCAE Consul. Hostname: {}", hostname);
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        log.info("The " + hostname + " address is " + ret);
        return ret;
    }

    private static String execQuery(String queryString) {
        return JerseyClient.newInstance().get(queryString);
    }

    public static String getServiceConfigInfoFromCBS(String hostname) {
        String url = getServiceAddrInfoFromDcaeConsulByHostName(getEnv(CONFIG_BINDING_SERVICE)) + "/service_component/" + hostname;
        String ret = execQuery(url);
        log.info("The query url is: " + url + ". The corresponding configurations are " + ret);
        return ret;
    }

    public static String getMsbServerAddrWithHttpPrefix() {
        String[] addrInfo = getMsbIpAndPort();
        String ret = addrInfo[0] + ":" + addrInfo[1];
        if (!ret.startsWith(AlarmConst.HTTP) || !ret.startsWith(AlarmConst.HTTPS)) {
            ret = AlarmConst.HTTP + ret;
        }
        return ret;
    }

    public static String getAaiAddr() {
        return AlarmConst.HTTPS + AAI_HOSTNAME + ":8443";
    }

    public static String[] getMsbIpAndPort() {
        return new String[]{getEnv(MSB_IAG_SERVICE_HOST), getEnv(MSB_IAG_SERVICE_PORT)};
    }

    public static String[] getMicroServiceIpAndPort() {
        String[] serviceAddrInfo = null;
        String info = getServiceAddrInfoFromDcaeConsulByHostName(getEnv(HOSTNAME));
        log.info("Got the service information of \"" + getEnv(HOSTNAME) + "\" from Consul. The response is " + info + ".");

        if (info != null && !info.isEmpty()) {
            if (!isIpAddress(info)) {
                info = getEnv(POD_IP);
            }
            serviceAddrInfo = split(info);
        } else {
            serviceAddrInfo = split(getEnv(HOSTNAME));
        }
        return serviceAddrInfo;
    }


    private static String[] split(String addr) {
        String ip;
        String port = "80";
        if (addr.lastIndexOf(":") == -1) {
            ip = addr;
        } else if (addr.lastIndexOf(":") < 5 && addr.indexOf("://") != -1) {
            ip = addr.substring(addr.indexOf("//") + 2);    //remove the http(s):// prefix
        } else {
            ip = addr.substring(addr.indexOf("://") != -1 ? addr.indexOf("//") + 2 : 0, addr.lastIndexOf(":"));
            port = addr.substring(addr.lastIndexOf(":") + 1);
        }
        return new String[]{ip, port};
    }

}
