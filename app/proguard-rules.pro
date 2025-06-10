# Keep your data models
-keep class com.emmanuelcastillo.livingdextracker.utils.data_classes.** { *; }
-keep class com.emmanuelcastillo.livingdextracker.utils.database.entity_classes.** { *; }

# Keep Gson annotations and generic type info
-keepattributes Signature
-keepattributes *Annotation*

# Keep members annotated with SerializedName
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Gson and converters
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-dontwarn com.google.gson.**

# Retrofit Gson Converter
-keep class retrofit2.converter.gson.** { *; }
-dontwarn retrofit2.converter.gson.**
