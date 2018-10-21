/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency, transparency,
 *    accountability and the service delivery of the government organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.config.core;

import org.egov.infra.filestore.service.FileStoreService;
import org.egov.infra.reporting.engine.ReportService;
import org.egov.infra.reporting.engine.jasper.JasperReportService;
import org.egov.infra.web.rest.handler.RestErrorHandler;
import org.egov.infra.web.rest.handler.RestTemplateLoggerInterceptor;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.egov.infra.config.core.LocalizationSettings.DEFAULT_COUNTRY_CODE_KEY;
import static org.egov.infra.config.core.LocalizationSettings.DEFAULT_CURRENCY_CODE_KEY;
import static org.egov.infra.config.core.LocalizationSettings.DEFAULT_CURRENCY_NAME_KEY;
import static org.egov.infra.config.core.LocalizationSettings.DEFAULT_CURRENCY_NAME_SHORT_KEY;
import static org.egov.infra.config.core.LocalizationSettings.DEFAULT_CURRENCY_SYMBOL_HEX_KEY;
import static org.egov.infra.config.core.LocalizationSettings.DEFAULT_CURRENCY_SYMBOL_UTF8_KEY;
import static org.egov.infra.config.core.LocalizationSettings.DEFAULT_DATE_PATTERN_KEY;
import static org.egov.infra.config.core.LocalizationSettings.DEFAULT_DATE_TIME_PATTERN_KEY;
import static org.egov.infra.config.core.LocalizationSettings.DEFAULT_ENCODING_KEY;
import static org.egov.infra.config.core.LocalizationSettings.DEFAULT_LOCALE_KEY;
import static org.egov.infra.config.core.LocalizationSettings.DEFAULT_TIME_ZONE_KEY;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ApplicationConfiguration {

    @Resource(name = "tenants")
    private List<String> tenants;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EnvironmentSettings environmentSettings;

    @Value("${filestoreservice.beanname}")
    private String fileStoreServiceBeanName;

    @Bean
    public FileStoreService fileStoreService() {
        return (FileStoreService) context.getBean(fileStoreServiceBeanName);
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

    @Bean(name = "cities", autowire = Autowire.BY_NAME)
    @DependsOn(value = "tenants")
    public List<String> cities() {
        final List<String> cities = new ArrayList<>(tenants);
        if (!environmentSettings.devMode())
            cities.remove(environmentSettings.statewideSchemaName());
        return cities;
    }

    @Bean
    public LocalValidatorFactoryBean entityValidator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public ReportService reportService() {
        return new JasperReportService(10, 30);
    }

    @PostConstruct
    public void enhanceSystemProperties() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetObject(System.getProperties());
        methodInvokingFactoryBean.setTargetMethod("putAll");
        Properties properties = new Properties();
        properties.setProperty(DEFAULT_TIME_ZONE_KEY, environmentSettings.getProperty(DEFAULT_TIME_ZONE_KEY));
        properties.setProperty(DEFAULT_COUNTRY_CODE_KEY, environmentSettings.getProperty(DEFAULT_COUNTRY_CODE_KEY));
        properties.setProperty(DEFAULT_CURRENCY_CODE_KEY, environmentSettings.getProperty(DEFAULT_CURRENCY_CODE_KEY));
        properties.setProperty(DEFAULT_CURRENCY_NAME_KEY, environmentSettings.getProperty(DEFAULT_CURRENCY_NAME_KEY));
        properties.setProperty(DEFAULT_CURRENCY_NAME_SHORT_KEY, environmentSettings.getProperty(DEFAULT_CURRENCY_NAME_SHORT_KEY));
        properties.setProperty(DEFAULT_CURRENCY_SYMBOL_UTF8_KEY, environmentSettings.getProperty(DEFAULT_CURRENCY_SYMBOL_UTF8_KEY));
        properties.setProperty(DEFAULT_CURRENCY_SYMBOL_HEX_KEY, environmentSettings.getProperty(DEFAULT_CURRENCY_SYMBOL_HEX_KEY));
        properties.setProperty(DEFAULT_LOCALE_KEY, environmentSettings.getProperty(DEFAULT_LOCALE_KEY));
        properties.setProperty(DEFAULT_ENCODING_KEY, environmentSettings.getProperty(DEFAULT_ENCODING_KEY));
        properties.setProperty(DEFAULT_DATE_PATTERN_KEY, environmentSettings.getProperty(DEFAULT_DATE_PATTERN_KEY));
        properties.setProperty(DEFAULT_DATE_TIME_PATTERN_KEY, environmentSettings.getProperty(DEFAULT_DATE_TIME_PATTERN_KEY));
        methodInvokingFactoryBean.setArguments(new Object[]{properties});
        methodInvokingFactoryBean.prepare();
        methodInvokingFactoryBean.invoke();
    }

    @Bean
    public RestTemplate restTemplate(){
        System.out.println("************************* RestTemplate object created*********************");

        SimpleClientHttpRequestFactory simpleCFactory = new  SimpleClientHttpRequestFactory();
        simpleCFactory.setOutputStreaming(false);

        ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(simpleCFactory);
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateLoggerInterceptor()));
        restTemplate.setErrorHandler(new RestErrorHandler());
        return restTemplate;
    }
}
