package com.se360.UIT_Go.api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "jwt.public-key=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs/yJcHARVAGn1xzVbndvyuj1spOinx4IXLP+r73zRpd9Aqjq8gKDnJolEPHXM55wdLxkvjXMEv3p665tbcRXUyHUWI6/p/UIeB6pEZyNUqUj6BiiE0lhv3nINmlAXeXibigMaOzUXBAIl7fuw29oG5bCWjjoUBWgVMFRkGAJfOBZDnqG1OgX1ibjCP4qDI5RgBuv32xAP3/n3eUmNel3+kxAj/ETVkmRbJfiJh61qc4n3bUxXXpJkPnAdGyNxcVPXArASLkKDlb1PSQOJ1x83s75yvXgLMxzt1hgThArM4vlFZRx8IjTbWIpJAjAqil0PLRIX6L3FzoRW6xuHRmMGwIDAQAB"
})
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
    }

}
