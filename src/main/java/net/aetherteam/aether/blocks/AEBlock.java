package net.aetherteam.aether.blocks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.minecraft.item.ItemBlock;

@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.FIELD})
public @interface AEBlock
{

Class itemBlock() default ItemBlock.class;

String name() default "";

String[] names() default {};
}
