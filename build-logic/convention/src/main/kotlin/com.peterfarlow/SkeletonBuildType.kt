package com.peterfarlow

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class SkeletonBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
    BENCHMARK(".benchmark")
}
