/**
 * Copyright 2017 ZTE Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onap.holmes.common.aai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jvnet.hk2.annotations.Service;
import org.onap.holmes.common.aai.entity.RelationshipList;
import org.onap.holmes.common.aai.entity.RelationshipList.RelatedToProperty;
import org.onap.holmes.common.aai.entity.RelationshipList.Relationship;
import org.onap.holmes.common.aai.entity.RelationshipList.RelationshipData;
import org.onap.holmes.common.aai.entity.VmEntity;
import org.onap.holmes.common.aai.entity.VmResourceLink;
import org.onap.holmes.common.aai.entity.VnfEntity;

@Service
public class AaiResponseUtil {

    public static final String RELATIONSHIP_LIST = "relationship-list";

    public List<VmResourceLink> convertJsonToVmResourceLink(String responseJson)
            {
        List<VmResourceLink> vmResourceLinkList = new ArrayList<>();
        String resultDataKey = "result-data";
        JSONObject jsonNode = JSON.parseObject(responseJson);
        if (jsonNode.get(resultDataKey) != null) {
            JSONArray resultData = jsonNode.getJSONArray(resultDataKey);
            vmResourceLinkList = convertResultDataList(resultData);
        }
        return vmResourceLinkList;
    }

    public VmEntity convertJsonToVmEntity(String responseJson) {
        JSONObject jsonObject = JSON.parseObject(responseJson);
        if (jsonObject == null ||jsonObject.isEmpty()) {
            return null;
        }
        VmEntity vmEntity = new VmEntity();
        vmEntity.setInMaint(getBooleanElementByNode(jsonObject, "in-maint"));
        vmEntity.setClosedLoopDisable(
                getBooleanElementByNode(jsonObject, "is-closed-loop-disabled"));
        vmEntity.setProvStatus(getTextElementByNode(jsonObject, "prov-status"));
        vmEntity.setResourceVersion(getTextElementByNode(jsonObject, "resource-version"));
        vmEntity.setVserverId(getTextElementByNode(jsonObject, "vserver-id"));
        vmEntity.setVserverName(getTextElementByNode(jsonObject, "vserver-name"));
        vmEntity.setVserverName2(getTextElementByNode(jsonObject, "vserver-name2"));
        vmEntity.setVserverSelflink(getTextElementByNode(jsonObject, "vserver-selflink"));

        setRelationShips(jsonObject, vmEntity.getRelationshipList());
        if (vmEntity.getRelationshipList().getRelationships() == null) {
            vmEntity.getRelationshipList().setRelationships(Collections.emptyList());
        }
        return vmEntity;
    }

    public VnfEntity convertJsonToVnfEntity(String responseJson) throws IOException {
        JSONObject jsonObject = JSON.parseObject(responseJson);

        if (jsonObject.isEmpty()) {
            return null;
        }

        VnfEntity vnfEntity = new VnfEntity();
        vnfEntity.setInMaint(getBooleanElementByNode(jsonObject, "in-maint"));
        vnfEntity.setClosedLoopDisabled(
                getBooleanElementByNode(jsonObject, "is-closed-loop-disabled"));
        vnfEntity.setOrchestrationStatus(getTextElementByNode(jsonObject, "orchestration-status"));
        vnfEntity.setProvStatus(getTextElementByNode(jsonObject, "prov-status"));
        vnfEntity.setResourceVersion(getTextElementByNode(jsonObject, "resource-version"));
        vnfEntity.setServiceId(getTextElementByNode(jsonObject, "service-id"));
        vnfEntity.setVnfId(getTextElementByNode(jsonObject, "vnf-id"));
        vnfEntity.setVnfName(getTextElementByNode(jsonObject, "vnf-name"));
        vnfEntity.setVnfType(getTextElementByNode(jsonObject, "vnf-type"));

        setRelationShips(jsonObject, vnfEntity.getRelationshipList());
        if (vnfEntity.getRelationshipList().getRelationships() == null) {
            vnfEntity.getRelationshipList().setRelationships(Collections.emptyList());
        }
        return vnfEntity;
    }

    private void setRelationShips(JSONObject jsonObject, RelationshipList relationshipList) {
        if (jsonObject.get(RELATIONSHIP_LIST) != null) {
            JSONObject relationshipListNode = jsonObject.getJSONObject(RELATIONSHIP_LIST);
            String relationship = "relationship";
            if (relationshipListNode.get(relationship) != null) {
                JSONArray relationshipNode = relationshipListNode.getJSONArray(relationship);
                relationshipList
                        .setRelationships(convertRelationships(relationshipNode));
            }
        }
    }

    private List<VmResourceLink> convertResultDataList(JSONArray resultData) {
        List<VmResourceLink> vmResourceLinkList = new ArrayList<>();
        String resourceLink = "resource-link";
        String resourceType = "resource-type";
        for (int i = 0; i < resultData.size(); i++) {
            JSONObject jsonObject = resultData.getJSONObject(i);
            if (jsonObject.get(resourceLink) != null
                    && jsonObject.get(resourceType) != null) {
                VmResourceLink vmResourceLink = new VmResourceLink();
                vmResourceLink.setResourceLink(getTextElementByNode(jsonObject, resourceLink));
                vmResourceLink.setResourceType(getTextElementByNode(jsonObject, resourceType));
                vmResourceLinkList.add(vmResourceLink);
            }
        }
        return vmResourceLinkList;
    }

    private List<Relationship> convertRelationships(JSONArray relationshipNode) {
        List<Relationship> relationshipList = new ArrayList<>();
        for (int i = 0; i < relationshipNode.size(); i++) {
            Relationship relationship = new Relationship();
            JSONObject jsonObject = relationshipNode.getJSONObject(i);

            relationship.setRelatedLink(getTextElementByNode(jsonObject, "related-link"));
            relationship.setRelatedTo(getTextElementByNode(jsonObject, "related-to"));
            if (jsonObject.get("related-to-property") != null) {
                JSONArray relatedToPropertyNode = jsonObject.getJSONArray("related-to-property");
                relationship.setRelatedToPropertyList(
                        convertRelatedToProperty(relatedToPropertyNode));
            } else {
                relationship.setRelatedToPropertyList(Collections.emptyList());
            }
            if (jsonObject.get("relationship-data") != null) {
                JSONArray relationshipDataNode = jsonObject.getJSONArray("relationship-data");
                relationship
                        .setRelationshipDataList(convertRelationshipDate(relationshipDataNode));
            } else {
                relationship.setRelationshipDataList(Collections.emptyList());
            }
            relationshipList.add(relationship);
        }

        return relationshipList;
    }

    private List<RelationshipData> convertRelationshipDate(JSONArray relationshipDataNode) {
        List<RelationshipData> relationshipDataList = new ArrayList<>();
        for (int i = 0; i < relationshipDataNode.size(); i++) {
            JSONObject jsonObject = relationshipDataNode.getJSONObject(i);
            RelationshipData relationshipData = new RelationshipData();
            relationshipData.setRelationshipKey(
                    getTextElementByNode(jsonObject, "relationship-key"));
            relationshipData.setRelationshipValue(
                    getTextElementByNode(jsonObject, "relationship-value"));
            relationshipDataList.add(relationshipData);
            relationshipDataList.add(relationshipData);

        }
        return relationshipDataList;
    }

    private List<RelatedToProperty> convertRelatedToProperty(JSONArray relatedToPropertyNode) {
        List<RelatedToProperty> relatedToPropertyList = new ArrayList<>();
        for (int i = 0; i < relatedToPropertyNode.size(); i++) {
            JSONObject jsonObject = relatedToPropertyNode.getJSONObject(i);
            RelatedToProperty relatedToProperty = new RelatedToProperty();
            relatedToProperty
                    .setPropertyKey(getTextElementByNode(jsonObject, "property-key"));
            relatedToProperty.setPropertyValue(
                    getTextElementByNode(jsonObject, "property-value"));
            relatedToPropertyList.add(relatedToProperty);
        }
        return relatedToPropertyList;
    }

    private String getTextElementByNode(JSONObject jsonNode, String name) {
        if (jsonNode.get(name) != null) {
            return jsonNode.getString(name);
        }
        return null;
    }

    private Boolean getBooleanElementByNode(JSONObject jsonNode, String name) {
        if (jsonNode.get(name) != null) {
            return jsonNode.getBoolean(name);
        }
        return null;
    }
}
