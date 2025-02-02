/**
 * Copyright 2017 - 2021 ZTE Corporation.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.onap.holmes.common.aai.config;

public class AaiConfig {

    private static final String AAI_API_VERSION = "v19";

    public static final String X_TRANSACTION_ID = "9999";

    public static final String X_FROMAPP_ID = "jimmy-postman";

    private static final String AAI_AUTHENTICATION_USER = "AAI";

    private static final String AAI_AUTHENTICATION_PWD = "AAI";

    private static final String AAI_PNF_URL_SUF = "/network/pnfs/pnf/{pnfName}/p-interfaces/p-interface/nodeId-{pnfName}-ltpId-{ifName}";

    private AaiConfig() {

    }

    public static String getAuthenticationCredentials() {
        String userCredentials = AAI_AUTHENTICATION_USER + ":"
                + AAI_AUTHENTICATION_PWD;
        return "Basic " + java.util.Base64.getEncoder().encodeToString(userCredentials.getBytes());
    }

    public static class AaiConsts {

        private AaiConsts() {

        }

        private static final String AAI_PREF = "/aai/";

        public static final String AAI_VNF_ADDR = AAI_PREF + "v11/network/generic-vnfs/generic-vnf";

        public static final String AAI_TP_UPDATE = AAI_PREF + AAI_API_VERSION + AAI_PNF_URL_SUF;

        public static final String AAI_LINK_QUERY = AAI_PREF + AAI_API_VERSION + AAI_PNF_URL_SUF;

        public static final String AAI_LINK_UPDATE = AAI_PREF + AAI_API_VERSION + "/network/logical-links/logical-link/{linkName}";

        public static final String AAI_TP_ADDR = AAI_PREF + AAI_API_VERSION + "/network/pnfs/pnf/{node-Id}/p-interfaces/p-interface/{tp-id}";

        public static final String AAI_VPN_ADDR = AAI_PREF + AAI_API_VERSION + AAI_PNF_URL_SUF;

        public static final String AAI_CONN_ADDR = AAI_PREF + AAI_API_VERSION + "/network/vpn-bindings/vpn-binding/{vpnId}";

        public static final String AAI_SERVICE_INSTANCE_ADDR_4_CCVPN = AAI_PREF + AAI_API_VERSION + "/network/connectivities/connectivity/{connectivityId}";

        public static final String AAI_SERVICE_INSTANCES_ADDR_4_CCVPN = AAI_PREF + AAI_API_VERSION + "/business/customers/customer/{global-customer-id}/service-subscriptions/service-subscription/{service-type}";

        public static final String AAI_VM_ADDR = AAI_PREF + "v11/search/nodes-query?search-node-type=vserver&filter=";

        public static final String AAI_SERVICE = AAI_SERVICE_INSTANCES_ADDR_4_CCVPN + "/service-instances/service-instance/{instance-id}";

        public static final String AAI_PNF = AAI_PREF + AAI_API_VERSION + "/network/pnfs/pnf/{pnfName}";

        public static final String AAI_PNF_VALUE = AAI_PNF + "?depth=all";

        public static final String AAI_PNF_ID = AAI_PREF + AAI_API_VERSION + "/network/pnfs?pnf-id={pnfId}";
    }


    public static class MsbConsts {

        private MsbConsts() {

        }

        private static final String AAI_MSB_PREF = "/api";

        private static final String AAI_NETWORK = "/aai-network/";

        private static final String AAI_BUSINESS = "/aai-business/";

        private static final String AAI_SEARCH = "/aai-search/";

        public static final String AAI_VNF_ADDR = AAI_NETWORK + AAI_API_VERSION + "/generic-vnfs/generic-vnf";

        public static final String AAI_TP_UPDATE = AAI_NETWORK + AAI_API_VERSION + AAI_PNF_URL_SUF;

        public static final String AAI_LINK_QUERY = AAI_NETWORK + AAI_API_VERSION + AAI_PNF_URL_SUF;

        public static final String AAI_LINK_UPDATE = AAI_NETWORK + AAI_API_VERSION + "/network/logical-links/logical-link/{linkName}";

        public static final String AAI_TP_ADDR = AAI_NETWORK + AAI_API_VERSION + "/pnfs/pnf/{node-Id}/p-interfaces/p-interface/{tp-id}";

        public static final String AAI_VPN_ADDR = AAI_NETWORK + AAI_API_VERSION + AAI_PNF_URL_SUF;

        public static final String AAI_CONN_ADDR = AAI_NETWORK + AAI_API_VERSION + "/network/vpn-bindings/vpn-binding/{vpnId}";

        public static final String AAI_SERVICE_INSTANCE_ADDR_4_CCVPN = AAI_NETWORK + AAI_API_VERSION + "/network/connectivities/connectivity/{connectivityId}";

        public static final String AAI_SERVICE_INSTANCES_ADDR_4_CCVPN = AAI_BUSINESS + AAI_API_VERSION + "/customers/customer/{global-customer-id}/service-subscriptions/service-subscription/{service-type}";

        public static final String AAI_VM_ADDR = AAI_SEARCH + AAI_API_VERSION + "/nodes-query?search-node-type=vserver&filter=";

        public static final String AAI_SITE_RESOURCES_QUERY = AAI_MSB_PREF + AAI_NETWORK + AAI_API_VERSION +
                "/site-resources";

        public static final String AAI_SITE_VNF_QUERY = AAI_MSB_PREF + AAI_NETWORK + AAI_API_VERSION +
                "/generic-vnfs/generic-vnf/{vnfId}";
    }
}
