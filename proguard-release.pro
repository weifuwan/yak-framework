# Yak Framework release obfuscation
#
# First release-safe version: obfuscate names only. Do not shrink or optimize
# until dedicated Spring/MyBatis/Jackson compatibility tests are in place.

-dontshrink
-dontoptimize

# Preserve metadata used by Spring, Jackson, records and reflection.
# SourceFile / line tables / local-variable tables are intentionally omitted.
-keepattributes Exceptions,InnerClasses,EnclosingMethod,Signature,*Annotation*,AnnotationDefault,MethodParameters,Record
-keepparameternames
-useuniqueclassmembernames

# Rewrite exact class-name strings and framework metadata when internal classes
# are renamed.
-adaptclassstrings
-adaptresourcefilecontents META-INF/services/**,META-INF/spring/**

# ---------------------------------------------------------------------------
# Public Yak Framework API
# ---------------------------------------------------------------------------
#
# Downstream Maven consumers compile against public/protected symbols, so those
# symbols must remain source/binary compatible. Private and package-private
# implementation details are still eligible for obfuscation.

-keep public class io.yak.framework.** {
    public protected *;
}

-keep public interface io.yak.framework.** {
    public protected *;
}

-keep public enum io.yak.framework.** {
    public protected *;
}

-keep public @interface io.yak.framework.** {
    public protected *;
}

# ---------------------------------------------------------------------------
# Spring / reflection integration
# ---------------------------------------------------------------------------

-keep @org.springframework.context.annotation.Configuration class * {
    *;
}

-keep @org.springframework.boot.autoconfigure.AutoConfiguration class * {
    *;
}

-keep @org.springframework.boot.context.properties.ConfigurationProperties class * {
    *;
}

# Preserve implicit Spring bean names for stereotype components.
-keepnames @org.springframework.stereotype.Component class *
-keepnames @org.springframework.stereotype.Service class *
-keepnames @org.springframework.stereotype.Repository class *
-keepnames @org.springframework.stereotype.Controller class *
-keepnames @org.springframework.web.bind.annotation.RestController class *

# @Bean method names become bean names when no explicit name is supplied.
-keepclassmembers class * {
    @org.springframework.context.annotation.Bean <methods>;
}

# Jackson explicitly annotated members are reflection entry points.
-keepclassmembers class * {
    @com.fasterxml.jackson.annotation.JsonProperty <fields>;
    @com.fasterxml.jackson.annotation.JsonProperty <methods>;
}

# Java serialization relies on this exact special field name.
-keepclassmembers class * implements java.io.Serializable {
    private static final long serialVersionUID;
}

# Reflection/framework conversion commonly reaches enum helpers.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep public record component accessors stable for downstream callers.
-keep public class * extends java.lang.Record {
    public *;
}

# Do not add a global -dontwarn. A release build should surface unresolved
# dependencies so each optional/reflection dependency can be reviewed.
# Mapping and seed files are emitted under target/ by Maven and must not be
# published as Maven artifacts.
