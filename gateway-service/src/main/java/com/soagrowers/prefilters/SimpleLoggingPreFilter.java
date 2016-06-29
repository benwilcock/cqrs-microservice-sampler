package com.soagrowers.prefilters;

import com.netflix.zuul.ZuulFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import com.netflix.zuul.context.RequestContext;

/**
 * Created by ben on 29/06/16.
 */
public class SimpleLoggingPreFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(SimpleLoggingPreFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.debug("{} request to {}", request.getMethod(), request.getRequestURL().toString());
        return null;
    }
}
