package com.farmafene.cas;

import org.jasig.cas.authentication.principal.WebApplicationService;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

public class RegisteredServiceThemeBasedViewResolver extends InternalResourceViewResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisteredServiceThemeBasedViewResolver.class);
    private static final String THEME_LOCATION_PATTERN = "%s/%s/ui/";

    private String themeDefault;
    private final ServicesManager servicesManager;

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * The {@link RegisteredServiceThemeBasedViewResolver} constructor.
     *
     * @param servicesManager the serviceManager implementation
     * @see #setCache(boolean)
     */
    public RegisteredServiceThemeBasedViewResolver(final ServicesManager servicesManager) {
        super();
        super.setCache(false);

        this.servicesManager = servicesManager;
    }

    /**
     * Uses the viewName and the theme associated with the service.
     * being requested and returns the appropriate view.
     *
     * @param viewName the name of the view to be resolved
     * @return a theme-based UrlBasedView
     * @throws Exception an exception
     */
    @Override
    protected AbstractUrlBasedView buildView(final String viewName) throws Exception {
        final RequestContext requestContext = RequestContextHolder.getRequestContext();
        final WebApplicationService service = WebUtils.getService(requestContext);
        final RegisteredService registeredService = this.servicesManager.findServiceBy(service);

        final InternalResourceView view = (InternalResourceView) BeanUtils.instantiateClass(getViewClass());

		final String defaultThemePrefix = String.format(THEME_LOCATION_PATTERN, getPrefix(), themeDefault);
        final String defaultViewUrl = defaultThemePrefix + viewName + getSuffix();
        view.setUrl(defaultViewUrl);

        if (service != null && registeredService != null
            && registeredService.getAccessStrategy().isServiceAccessAllowed()
            && StringUtils.hasText(registeredService.getTheme())) {

            LOGGER.debug("Attempting to locate views for service [{}] with theme [{}]",
                registeredService.getServiceId(), registeredService.getTheme());

            final String themePrefix = String.format(THEME_LOCATION_PATTERN, getPrefix(), registeredService.getTheme());
            LOGGER.debug("Prefix [{}] set for service [{}] with theme [{}]", themePrefix, service,
                registeredService.getTheme());
            final String viewUrl = themePrefix + viewName + getSuffix();

            final Resource resource = this.resourceLoader.getResource(viewUrl);
            if (resource.exists()) {
                view.setUrl(viewUrl);
            }

        }

        final String contentType = getContentType();
        if (contentType != null) {
            view.setContentType(contentType);
        }
        view.setRequestContextAttribute(getRequestContextAttribute());
        view.setAttributesMap(getAttributesMap());

        //From InternalResourceViewResolver.buildView
        view.setAlwaysInclude(false);
        view.setExposeContextBeansAsAttributes(false);
        view.setPreventDispatchLoop(true);

        LOGGER.debug("View resolved: {}", view.getUrl());

        return view;
    }

    /**
     * setCache is not supported in the {@link RegisteredServiceThemeBasedViewResolver} because each
     * request must be independently evaluated.
     *
     * @param cache a value indicating whether the view should cache results.
     */
    @Override
    public void setCache(final boolean cache) {
        LOGGER.warn("The {} does not support caching. Turned off caching forcefully.", this.getClass().getSimpleName());
        super.setCache(false);
    }

    public void setResourceLoader(final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

	/**
	 * @return the themeDefault
	 */
	public String getThemeDefault() {
		return themeDefault;
	}

	/**
	 * @param themeDefault the themeDefault to set
	 */
	public void setThemeDefault(String themeDefault) {
		this.themeDefault = themeDefault;
	}
}
