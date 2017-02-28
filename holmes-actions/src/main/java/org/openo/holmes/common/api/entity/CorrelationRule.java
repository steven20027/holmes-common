/**
 * Copyright 2017 ZTE Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openo.holmes.common.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorrelationRule {

    @JsonProperty(value = "ruleid")
    private String rid;
    @JsonProperty(value = "rulename")
    private String name;
    @JsonProperty
    private String description;
    @JsonProperty
    private int enabled;
    @JsonProperty
    private int templateID;
    private String engineID;
    @JsonProperty
    private String engineType;
    @JsonProperty
    private String creator;
    @JsonProperty
    private String modifier;
    @JsonProperty
    private Properties params;
    @JsonProperty
    private String content;
    @JsonProperty
    private String vendor;
    @JsonProperty(value = "createtime")
    private Date createTime;
    @JsonProperty(value = "updatetime")
    private Date updateTime;
    @JsonProperty(value = "package")
    private String packageName;
}
