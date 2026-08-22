# Resolve Build Script Issues

Check and fix issues in `build.gradle.kts` files, primarily focusing on `shared/build.gradle.kts` which has syntax errors and redundant configurations related to the new KMP Android Library plugin (AGP 9.0+).

## User Review Required

> [!IMPORTANT]
> The `shared/build.gradle.kts` file currently contains manual source set wiring for iOS which is incompatible with the new KMP DSL and default hierarchy. I will be removing these manual `dependsOn` calls.

> [!NOTE]
> I will remove the `sqldelight` configuration from the `shared` module as it is a duplicate of the one in `core:database`, where the `.sq` files actually reside.

## Proposed Changes

### [shared module](file:///D:/mak/android-proj/Podcast-KMM/shared/build.gradle.kts)

#### [MODIFY] [shared/build.gradle.kts](file:///D:/mak/android-proj/Podcast-KMM/shared/build.gradle.kts)
- Remove `alias(libs.plugins.sqldelight)` from `plugins` block as it's not used in this module.
- Fix `iosMain` and `iosTest` blocks to remove erroneous manual `dependsOn` calls and direct target source set access.
- Remove duplicate `sqldelight { ... }` configuration.

### [core:database module](file:///D:/mak/android-proj/Podcast-KMM/core/database/build.gradle.kts)

#### [MODIFY] [core/database/build.gradle.kts](file:///D:/mak/android-proj/Podcast-KMM/core/database/build.gradle.kts)
- (Optional/Minor) Ensure test configuration is idiomatic, though the current `withDeviceTestBuilder` seems to be the project's chosen pattern for this AGP version.

## Verification Plan

### Automated Tests
- Run `./gradlew :shared:assemble` to verify build script correctness.
- Run `./gradlew :core:database:assemble` to verify database module build.
- Run `./gradlew :androidApp:assembleDebug` to ensure top-level app still builds.

### Manual Verification
- Verify that the IDE no longer reports errors in `shared/build.gradle.kts`.
