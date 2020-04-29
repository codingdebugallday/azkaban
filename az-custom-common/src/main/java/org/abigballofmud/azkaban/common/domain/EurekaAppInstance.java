package org.abigballofmud.azkaban.common.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * Query for all appID instances
 * </p>
 *
 * @author isacc 2020/4/29 9:57
 * @since 1.0
 */
@SuppressWarnings("unused")
public class EurekaAppInstance {


    /**
     * application : {"name":"HDSP-CORE","instance":[{"instanceId":"172.23.16.64:hdsp-core:8510","hostName":"172.23.16.64","app":"HDSP-CORE","ipAddr":"172.23.16.64","status":"UP","overriddenStatus":"UNKNOWN","port":{"$":8510,"@enabled":"true"},"securePort":{"$":443,"@enabled":"false"},"countryId":1,"dataCenterInfo":{"@class":"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo","name":"MyOwn"},"leaseInfo":{"renewalIntervalInSecs":10,"durationInSecs":30,"registrationTimestamp":1587974903933,"lastRenewalTimestamp":1588124753644,"evictionTimestamp":0,"serviceUpTimestamp":1587974903505},"metadata":{"SERVICE_VERSION_CODE":"DEFAULT","management.port":"8511","VERSION":"1.3.1","NODE_GROUP_ID":"0","PRODUCT_ENV_CODE":"DEFAULT","PRODUCT_VERSION_CODE":"DEFAULT","PRODUCT_CODE":"DEFAULT"},"homePageUrl":"http://172.23.16.64:8510/","statusPageUrl":"http://172.23.16.64:8511/actuator/info","healthCheckUrl":"http://172.23.16.64:8511/actuator/health","vipAddress":"hdsp-core","secureVipAddress":"hdsp-core","isCoordinatingDiscoveryServer":"false","lastUpdatedTimestamp":"1587974903934","lastDirtyTimestamp":"1587974903284","actionType":"ADDED"}]}
     */

    private ApplicationBean application;

    public ApplicationBean getApplication() {
        return application;
    }

    public void setApplication(ApplicationBean application) {
        this.application = application;
    }

    public static class ApplicationBean {
        /**
         * name : HDSP-CORE
         * instance : [{"instanceId":"172.23.16.64:hdsp-core:8510","hostName":"172.23.16.64","app":"HDSP-CORE","ipAddr":"172.23.16.64","status":"UP","overriddenStatus":"UNKNOWN","port":{"$":8510,"@enabled":"true"},"securePort":{"$":443,"@enabled":"false"},"countryId":1,"dataCenterInfo":{"@class":"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo","name":"MyOwn"},"leaseInfo":{"renewalIntervalInSecs":10,"durationInSecs":30,"registrationTimestamp":1587974903933,"lastRenewalTimestamp":1588124753644,"evictionTimestamp":0,"serviceUpTimestamp":1587974903505},"metadata":{"SERVICE_VERSION_CODE":"DEFAULT","management.port":"8511","VERSION":"1.3.1","NODE_GROUP_ID":"0","PRODUCT_ENV_CODE":"DEFAULT","PRODUCT_VERSION_CODE":"DEFAULT","PRODUCT_CODE":"DEFAULT"},"homePageUrl":"http://172.23.16.64:8510/","statusPageUrl":"http://172.23.16.64:8511/actuator/info","healthCheckUrl":"http://172.23.16.64:8511/actuator/health","vipAddress":"hdsp-core","secureVipAddress":"hdsp-core","isCoordinatingDiscoveryServer":"false","lastUpdatedTimestamp":"1587974903934","lastDirtyTimestamp":"1587974903284","actionType":"ADDED"}]
         */

        private String name;
        private List<InstanceBean> instance;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<InstanceBean> getInstance() {
            return instance;
        }

        public void setInstance(List<InstanceBean> instance) {
            this.instance = instance;
        }

        public static class InstanceBean {
            /**
             * instanceId : 172.23.16.64:hdsp-core:8510
             * hostName : 172.23.16.64
             * app : HDSP-CORE
             * ipAddr : 172.23.16.64
             * status : UP
             * overriddenStatus : UNKNOWN
             * port : {"$":8510,"@enabled":"true"}
             * securePort : {"$":443,"@enabled":"false"}
             * countryId : 1
             * dataCenterInfo : {"@class":"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo","name":"MyOwn"}
             * leaseInfo : {"renewalIntervalInSecs":10,"durationInSecs":30,"registrationTimestamp":1587974903933,"lastRenewalTimestamp":1588124753644,"evictionTimestamp":0,"serviceUpTimestamp":1587974903505}
             * metadata : {"SERVICE_VERSION_CODE":"DEFAULT","management.port":"8511","VERSION":"1.3.1","NODE_GROUP_ID":"0","PRODUCT_ENV_CODE":"DEFAULT","PRODUCT_VERSION_CODE":"DEFAULT","PRODUCT_CODE":"DEFAULT"}
             * homePageUrl : http://172.23.16.64:8510/
             * statusPageUrl : http://172.23.16.64:8511/actuator/info
             * healthCheckUrl : http://172.23.16.64:8511/actuator/health
             * vipAddress : hdsp-core
             * secureVipAddress : hdsp-core
             * isCoordinatingDiscoveryServer : false
             * lastUpdatedTimestamp : 1587974903934
             * lastDirtyTimestamp : 1587974903284
             * actionType : ADDED
             */

            private String instanceId;
            private String hostName;
            private String app;
            private String ipAddr;
            private String status;
            private String overriddenStatus;
            private PortBean port;
            private PortBean securePort;
            private int countryId;
            private DataCenterInfoBean dataCenterInfo;
            private LeaseInfoBean leaseInfo;
            private MetadataBean metadata;
            private String homePageUrl;
            private String statusPageUrl;
            private String healthCheckUrl;
            private String vipAddress;
            private String secureVipAddress;
            private String isCoordinatingDiscoveryServer;
            private String lastUpdatedTimestamp;
            private String lastDirtyTimestamp;
            private String actionType;

            public String getInstanceId() {
                return instanceId;
            }

            public void setInstanceId(String instanceId) {
                this.instanceId = instanceId;
            }

            public String getHostName() {
                return hostName;
            }

            public void setHostName(String hostName) {
                this.hostName = hostName;
            }

            public String getApp() {
                return app;
            }

            public void setApp(String app) {
                this.app = app;
            }

            public String getIpAddr() {
                return ipAddr;
            }

            public void setIpAddr(String ipAddr) {
                this.ipAddr = ipAddr;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getOverriddenStatus() {
                return overriddenStatus;
            }

            public void setOverriddenStatus(String overriddenStatus) {
                this.overriddenStatus = overriddenStatus;
            }

            public PortBean getPort() {
                return port;
            }

            public void setPort(PortBean port) {
                this.port = port;
            }

            public PortBean getSecurePort() {
                return securePort;
            }

            public void setSecurePort(PortBean securePort) {
                this.securePort = securePort;
            }

            public int getCountryId() {
                return countryId;
            }

            public void setCountryId(int countryId) {
                this.countryId = countryId;
            }

            public DataCenterInfoBean getDataCenterInfo() {
                return dataCenterInfo;
            }

            public void setDataCenterInfo(DataCenterInfoBean dataCenterInfo) {
                this.dataCenterInfo = dataCenterInfo;
            }

            public LeaseInfoBean getLeaseInfo() {
                return leaseInfo;
            }

            public void setLeaseInfo(LeaseInfoBean leaseInfo) {
                this.leaseInfo = leaseInfo;
            }

            public MetadataBean getMetadata() {
                return metadata;
            }

            public void setMetadata(MetadataBean metadata) {
                this.metadata = metadata;
            }

            public String getHomePageUrl() {
                return homePageUrl;
            }

            public void setHomePageUrl(String homePageUrl) {
                this.homePageUrl = homePageUrl;
            }

            public String getStatusPageUrl() {
                return statusPageUrl;
            }

            public void setStatusPageUrl(String statusPageUrl) {
                this.statusPageUrl = statusPageUrl;
            }

            public String getHealthCheckUrl() {
                return healthCheckUrl;
            }

            public void setHealthCheckUrl(String healthCheckUrl) {
                this.healthCheckUrl = healthCheckUrl;
            }

            public String getVipAddress() {
                return vipAddress;
            }

            public void setVipAddress(String vipAddress) {
                this.vipAddress = vipAddress;
            }

            public String getSecureVipAddress() {
                return secureVipAddress;
            }

            public void setSecureVipAddress(String secureVipAddress) {
                this.secureVipAddress = secureVipAddress;
            }

            public String getIsCoordinatingDiscoveryServer() {
                return isCoordinatingDiscoveryServer;
            }

            public void setIsCoordinatingDiscoveryServer(String isCoordinatingDiscoveryServer) {
                this.isCoordinatingDiscoveryServer = isCoordinatingDiscoveryServer;
            }

            public String getLastUpdatedTimestamp() {
                return lastUpdatedTimestamp;
            }

            public void setLastUpdatedTimestamp(String lastUpdatedTimestamp) {
                this.lastUpdatedTimestamp = lastUpdatedTimestamp;
            }

            public String getLastDirtyTimestamp() {
                return lastDirtyTimestamp;
            }

            public void setLastDirtyTimestamp(String lastDirtyTimestamp) {
                this.lastDirtyTimestamp = lastDirtyTimestamp;
            }

            public String getActionType() {
                return actionType;
            }

            public void setActionType(String actionType) {
                this.actionType = actionType;
            }

            public static class PortBean {
                /**
                 * {"$":443,"@enabled":"false"}
                 */
                @JsonAlias("$")
                private int port;
                @JsonAlias("@enabled")
                private String enabled;

                public int getPort() {
                    return port;
                }

                public void setPort(int port) {
                    this.port = port;
                }

                public String getEnabled() {
                    return enabled;
                }

                public void setEnabled(String enabled) {
                    this.enabled = enabled;
                }
            }

            public static class DataCenterInfoBean {
                @SerializedName("@class")
                private String classInfo;
                private String name;

                public String getClassInfo() {
                    return classInfo;
                }

                public void setClassInfo(String classInfo) {
                    this.classInfo = classInfo;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }

            public static class LeaseInfoBean {
                /**
                 * renewalIntervalInSecs : 10
                 * durationInSecs : 30
                 * registrationTimestamp : 1587974903933
                 * lastRenewalTimestamp : 1588124753644
                 * evictionTimestamp : 0
                 * serviceUpTimestamp : 1587974903505
                 */

                private int renewalIntervalInSecs;
                private int durationInSecs;
                private long registrationTimestamp;
                private long lastRenewalTimestamp;
                private int evictionTimestamp;
                private long serviceUpTimestamp;

                public int getRenewalIntervalInSecs() {
                    return renewalIntervalInSecs;
                }

                public void setRenewalIntervalInSecs(int renewalIntervalInSecs) {
                    this.renewalIntervalInSecs = renewalIntervalInSecs;
                }

                public int getDurationInSecs() {
                    return durationInSecs;
                }

                public void setDurationInSecs(int durationInSecs) {
                    this.durationInSecs = durationInSecs;
                }

                public long getRegistrationTimestamp() {
                    return registrationTimestamp;
                }

                public void setRegistrationTimestamp(long registrationTimestamp) {
                    this.registrationTimestamp = registrationTimestamp;
                }

                public long getLastRenewalTimestamp() {
                    return lastRenewalTimestamp;
                }

                public void setLastRenewalTimestamp(long lastRenewalTimestamp) {
                    this.lastRenewalTimestamp = lastRenewalTimestamp;
                }

                public int getEvictionTimestamp() {
                    return evictionTimestamp;
                }

                public void setEvictionTimestamp(int evictionTimestamp) {
                    this.evictionTimestamp = evictionTimestamp;
                }

                public long getServiceUpTimestamp() {
                    return serviceUpTimestamp;
                }

                public void setServiceUpTimestamp(long serviceUpTimestamp) {
                    this.serviceUpTimestamp = serviceUpTimestamp;
                }
            }

            public static class MetadataBean {
                /**
                 * SERVICE_VERSION_CODE : DEFAULT
                 * management.port : 8511
                 * VERSION : 1.3.1
                 * NODE_GROUP_ID : 0
                 * PRODUCT_ENV_CODE : DEFAULT
                 * PRODUCT_VERSION_CODE : DEFAULT
                 * PRODUCT_CODE : DEFAULT
                 */
                @JsonAlias("SERVICE_VERSION_CODE")
                private String serviceVersionCode;
                @JsonAlias("management.port")
                private String managementPort;
                @JsonAlias("VERSION")
                private String version;
                @JsonAlias("NODE_GROUP_ID")
                private String nodeGroupId;
                @JsonAlias("PRODUCT_ENV_CODE")
                private String productEnvCode;
                @JsonAlias("PRODUCT_VERSION_CODE")
                private String productVersionCode;
                @JsonAlias("PRODUCT_CODE")
                private String productCode;

                public String getServiceVersionCode() {
                    return serviceVersionCode;
                }

                public void setServiceVersionCode(String serviceVersionCode) {
                    this.serviceVersionCode = serviceVersionCode;
                }

                public String getManagementPort() {
                    return managementPort;
                }

                public void setManagementPort(String managementPort) {
                    this.managementPort = managementPort;
                }

                public String getVersion() {
                    return version;
                }

                public void setVersion(String version) {
                    this.version = version;
                }

                public String getNodeGroupId() {
                    return nodeGroupId;
                }

                public void setNodeGroupId(String nodeGroupId) {
                    this.nodeGroupId = nodeGroupId;
                }

                public String getProductEnvCode() {
                    return productEnvCode;
                }

                public void setProductEnvCode(String productEnvCode) {
                    this.productEnvCode = productEnvCode;
                }

                public String getProductVersionCode() {
                    return productVersionCode;
                }

                public void setProductVersionCode(String productVersionCode) {
                    this.productVersionCode = productVersionCode;
                }

                public String getProductCode() {
                    return productCode;
                }

                public void setProductCode(String productCode) {
                    this.productCode = productCode;
                }
            }
        }
    }
}
