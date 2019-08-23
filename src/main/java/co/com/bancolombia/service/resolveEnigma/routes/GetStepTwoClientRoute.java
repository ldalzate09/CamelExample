package co.com.bancolombia.service.resolveEnigma.routes;

import co.com.bancolombia.service.resolveEnigma.model.client.ClientJsonApiBodyResponseSuccess;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class GetStepTwoClientRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:get-step-two")
            .routeId("stepTwo")
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        .to("freemarker:templates/GetStepTwoClientTemplate.ftl")
        .hystrix()
        .hystrixConfiguration().executionTimeoutInMilliseconds(2000).end()
        .to("http4://localhost:8090/EnigmaSteps/getStep")
            .convertBodyTo(String.class)
            .unmarshal().json(JsonLibrary.Jackson, ClientJsonApiBodyResponseSuccess.class)
        .process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                ClientJsonApiBodyResponseSuccess stepOneResponse = (ClientJsonApiBodyResponseSuccess) exchange.getIn().getBody();
                if (stepOneResponse.getData().get(0).getStep().equalsIgnoreCase("2")) {
                    exchange.setProperty("Step2", stepOneResponse.getData().get(0).getStepDescription());
                }
                else {
                    exchange.setProperty("Error", "0001");
                    exchange.setProperty("descError", "Step two is invalid");
                }
            }
        })
        .endHystrix()
        .onFallback()
        .process(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.setProperty("Error", "0002");
                exchange.setProperty("descError", "Error consulting the step two");
            }
        })
        .end();
    }
}
