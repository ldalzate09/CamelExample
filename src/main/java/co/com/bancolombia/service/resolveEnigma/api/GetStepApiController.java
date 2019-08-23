package co.com.bancolombia.service.resolveEnigma.api;

import co.com.bancolombia.service.resolveEnigma.model.JsonApiBodyRequest;
import co.com.bancolombia.service.resolveEnigma.model.JsonApiBodyResponseErrors;
import co.com.bancolombia.service.resolveEnigma.model.JsonApiBodyResponseSuccess;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.apache.camel.EndpointInject;
import org.apache.camel.FluentProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-08-15T16:23:11.692-05:00[America/Bogota]")
@Controller
public class GetStepApiController implements GetStepApi {

    private static final Logger log = LoggerFactory.getLogger(GetStepApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private Object response;

    @EndpointInject(uri="direct:resolve-enigma")
    private FluentProducerTemplate producerTemplateResolveEnigma;

    @org.springframework.beans.factory.annotation.Autowired
    public GetStepApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<?> getStep(@ApiParam(value = "request body get enigma step" ,required=true )  @Valid @RequestBody JsonApiBodyRequest body) {
        try {
            //producerTemplateResolveEnigma.request();
            response = producerTemplateResolveEnigma.withBody(body).request();
            return new ResponseEntity<JsonApiBodyResponseSuccess>((JsonApiBodyResponseSuccess) response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Couldn't serialize response for content type application/json", e);
            return new ResponseEntity<JsonApiBodyResponseErrors>((JsonApiBodyResponseErrors)response, HttpStatus.FAILED_DEPENDENCY);
        }
    }
}
