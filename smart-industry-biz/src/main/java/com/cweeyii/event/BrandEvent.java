package com.cweeyii.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by wenyi on 17/1/8.
 * Email:caowenyi@meituan.com
 */
public class BrandEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    private Object context;

    public BrandEvent(Object source, Object context) {
        super(source);
        this.context=context;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }
}
