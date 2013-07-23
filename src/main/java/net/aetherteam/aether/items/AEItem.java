package net.aetherteam.aether.items;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target( {java.lang.annotation.ElementType.FIELD})
public @interface AEItem
{
    public abstract String name();

    public abstract String[] names();
}

