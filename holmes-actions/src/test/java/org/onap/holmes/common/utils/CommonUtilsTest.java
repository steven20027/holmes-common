/**
 * Copyright 2021 ZTE Corporation.
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

package org.onap.holmes.common.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CommonUtilsTest {
    @Test
    public void isHttpsEnabled_normal_true() throws Exception {
        System.setProperty("ENABLE_ENCRYPT", "true");
        assertThat(CommonUtils.isHttpsEnabled(), is(true));
    }

    @Test
    public void isHttpsEnabled_normal_false() throws Exception {
        System.setProperty("ENABLE_ENCRYPT", "false");
        assertThat(CommonUtils.isHttpsEnabled(), is(false));
    }

    @Test
    public void isHttpsEnabled_invalid_input() throws Exception {
        System.setProperty("ENABLE_ENCRYPT", "whatever");
        assertThat(CommonUtils.isHttpsEnabled(), is(false));
    }

    @Test
    public void getEnv() throws Exception {
        System.setProperty("TEST", "COMMON_UTILS");
        assertThat(CommonUtils.getEnv("TEST"), equalTo("COMMON_UTILS"));
    }

    @Test
    public void isValidIpAddress_with_port() throws Exception {
        boolean res = CommonUtils.isIpAddress("10.75.13.21:90");
        assertThat(res, is(true));
    }

    @Test
    public void isValidIpAddress_without_port() throws Exception {
        boolean res = CommonUtils.isIpAddress("10.75.13.21");
        assertThat(res, is(true));
    }

    @Test
    public void isValidIpAddress_with_port_with_http_prefix() throws Exception {
        boolean res = CommonUtils.isIpAddress("http://10.75.13.21:90");
        assertThat(res, is(true));
    }

    @Test
    public void isValidIpAddress_without_port_with_https_prefix() throws Exception {
        boolean res = CommonUtils.isIpAddress("https://10.75.13.21");
        assertThat(res, is(true));
    }

    @Test
    public void isValidIpAddress_invalid_ip_without_port() throws Exception {
        boolean res = CommonUtils.isIpAddress("holmes-rule-mgmt");
        assertThat(res, is(false));
    }

    @Test
    public void isValidIpAddress_invalid_ip_with_port() throws Exception {
        boolean res = CommonUtils.isIpAddress("holmes-rule-mgmt:443");
        assertThat(res, is(false));
    }

    @Test
    public void isValidIpAddress_invalid_ip_without_port_with_http_prefix() throws Exception {
        boolean res = CommonUtils.isIpAddress("http://holmes-rule-mgmt");
        assertThat(res, is(false));
    }

    @Test
    public void isValidIpAddress_invalid_ip_with_port_with_https_prefix() throws Exception {
        boolean res = CommonUtils.isIpAddress("https://holmes-rule-mgmt:443");
        assertThat(res, is(false));
    }
}