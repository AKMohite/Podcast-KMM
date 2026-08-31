# PocketNote

KMM project where all podcasts are listed

## Android
| Home                              | Details                                      | Episodes                                  |
|-----------------------------------|----------------------------------------------|-------------------------------------------|
| ![](art/android/android-home.png) | ![](art/android/android-podcast-details.png) | ![](art/android/android-episode-list.png) |

## iOS
| Home                      | Details                              | Episodes                          |
|---------------------------|--------------------------------------|-----------------------------------|
| ![](art/ios/ios-home.png) | ![](art/ios/ios-podcast-details.png) | ![](art/ios/ios-episode-list.png) |

## Responsive
| Mobile                           | Foldable                           | Tablet                        | Tablet Curated                        |
|----------------------------------|------------------------------------|-------------------------------|---------------------------------------|
| ![](art/android/mobile_home.png) | ![](art/android/foldable_home.png) | ![](art/android/tab_home.png) | ![](art/android/tab_home_curated.png) |

## Wear OS

| Podcasts                        | Details (Top header)                    | Details (Episode List)                  |
|---------------------------------|-----------------------------------------|-----------------------------------------|
| ![](art/wear/wear-podcasts.png) | ![](art/wear/wear-podcast-detail-1.png) | ![](art/wear/wear-podcast-detail-2.png) |

## Other Platforms

- **Android TV**: 🚧 Work In Progress (WIP)
- **Android Auto**: 🚧 Work In Progress (WIP)

## Development Tools

This project uses several tools to ensure code quality and documentation consistency.

### Code Formatting (Spotless)

To fix code formatting across the entire project:

```bash
./gradlew spotlessApply
```

### Static Analysis (Detekt)

To run code smell analysis:

```bash
./gradlew detekt
```

### Test Coverage (Kover)

To generate an HTML test coverage report:

```bash
./gradlew koverHtmlReport
```

Reports can be found in `[module]/build/reports/kover/html`.

### API Documentation (Dokka)

To generate the API documentation:

```bash
./gradlew dokkaGenerate
```

The documentation will be generated in `build/dokka/html`.

### AI & Agentic Capabilities

This project implements **Android AppFunctions**, allowing system AI agents to interact with podcast
discovery and playback.

- [AI App Functions Developer Guide](dev/ai_app_functions.md): A complete list of exposed functions
  and ADB commands for testing.

### References
- App icon from [Icon kitchen](https://icon.kitchen/)
