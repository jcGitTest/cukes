package cs.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class TestConf {

    private static final Logger LOGGER = Logger.getLogger(TestConf.class.getName());
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    private static final ObjectMapper MAPPER = buildObjectMapper();
    private static final Config BASE_CONFIG = ConfigFactory.load();
    private static final TestConf CONF_INSTANCE = buildTestConfSingleton(BASE_CONFIG);


    @Valid
    @Min(1)
    private Integer ajaxWaitSeconds;

    @Valid
    @NotNull
    private String searchUrl;

    public String getSearchUrl() {
        return searchUrl;
    }

    public Integer getAjaxWaitSeconds() {
        return ajaxWaitSeconds;
    }

    public static TestConf getConfInstance() {
        return CONF_INSTANCE;
    }

    private static ObjectMapper buildObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return mapper;
    }

    private static TestConf buildTestConfSingleton(Config config){
        Map<String, Object> unwrapped = config.root().unwrapped();
        TestConf testConf = MAPPER.convertValue(unwrapped, TestConf.class);
        Set<ConstraintViolation<TestConf>> violations = VALIDATOR.validate(testConf);
        if(!violations.isEmpty()){
            StringBuilder message = new StringBuilder();
            message.append("Configuration is invalid. The following problems need to be corrected: \n");
            violations.forEach((violation -> {
                message.append(" ");
                message.append(violation.getPropertyPath());
                message.append(":");
                message.append(violation.getMessage());
                message.append("\n");
            }));
            LOGGER.info(message.toString());
            throw new IllegalStateException("Application's test config invalid. ");
        }
        return testConf;
    }


}
