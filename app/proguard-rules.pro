# Keep your data models
-keep class com.emmanuelcastillo.livingdextracker.utils.api.** { *; }

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

# Retrofit
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

# Retrofit Gson Converter
-keep class retrofit2.converter.gson.** { *; }
-dontwarn retrofit2.converter.gson.**

# OkHttp
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
