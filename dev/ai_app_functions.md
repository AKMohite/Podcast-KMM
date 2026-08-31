# AI App Functions - Developer Guide

This document lists all the AppFunctions implemented in the **Pocket Notes** application that are
exposed to system AI agents. These functions allow for discovery, playback, and control of podcasts
via natural language.

## Package Info

- **Target Package**: `com.mak.pocketnotes.android.dev` (Development Build)
- **Service Class**: `com.mak.pocketnotes.android.ai.PodcastAppFunctionService`

---

## 1. Discovery Functions

### `searchPodcasts`

Search for podcasts by topic, title, or keyword.

- **Arguments**: `query` (String)
- **ADB Command**:
  ```bash

# adb shell cmd app_functions invoke-function --package com.mak.pocketnotes.android.dev --function searchPodcasts --args '{"query": "wander"}'

adb shell cmd app_function execute-app-function --package com.mak.pocketnotes.android.dev
--function 'com.mak.pocketnotes.android.ai.BasePodcastAppFunctionService#searchPodcasts'
--parameters '{"query": "wander"}'

  ```

### `searchEpisodes`
Search for specific podcast episodes by title or topic.
- **Arguments**: `query` (String)
- **ADB Command**:
  ```bash
  adb shell cmd app_functions invoke-function --package com.mak.pocketnotes.android.dev --function searchEpisodes --args '{"query": "Mars mission"}'
  ```

---

## 2. Playback Functions

### `playPodcastEpisode`

Play a specific podcast episode by its unique identifier.

- **Arguments**: `episodeId` (String)
- **ADB Command**:
  ```bash
  adb shell cmd app_functions invoke-function --package com.mak.pocketnotes.android.dev --function playPodcastEpisode --args '{"episodeId": "podcast-123-episode-456"}'
  ```

### `playLatestEpisode`

Find and play the most recent episode of a podcast by its name.

- **Arguments**: `podcastName` (String)
- **ADB Command**:
  ```bash
  adb shell cmd app_functions invoke-function --package com.mak.pocketnotes.android.dev --function playLatestEpisode --args '{"podcastName": "The Daily"}'
  ```

### `addToQueue`

Add a specific podcast episode to the end of the current playback queue.

- **Arguments**: `episodeId` (String)
- **ADB Command**:
  ```bash
  adb shell cmd app_functions invoke-function --package com.mak.pocketnotes.android.dev --function addToQueue --args '{"episodeId": "podcast-123-episode-456"}'
  ```

---

## 3. Playback Control & Status

### `getPlaybackState`

Get the current playback state including the playing episode, position, and speed.

- **Arguments**: None
- **ADB Command**:
  ```bash
  adb shell cmd app_functions invoke-function --package com.mak.pocketnotes.android.dev --function getPlaybackState --args '{}'
  ```

### `pausePlayback`

Pause the current podcast playback.

- **ADB Command**:
  ```bash
  adb shell cmd app_functions invoke-function --package com.mak.pocketnotes.android.dev --function pausePlayback --args '{}'
  ```

### `resumePlayback`

Resume the current podcast playback.

- **ADB Command**:
  ```bash
  adb shell cmd app_functions invoke-function --package com.mak.pocketnotes.android.dev --function resumePlayback --args '{}'
  ```

### `skipForward` / `skipBackward`

Navigate within the current episode (Forward 30s / Backward 10s).

- **ADB Commands**:
  ```bash
  adb shell cmd app_functions invoke-function --package com.mak.pocketnotes.android.dev --function skipForward --args '{}'
  adb shell cmd app_functions invoke-function --package com.mak.pocketnotes.android.dev --function skipBackward --args '{}'
  ```

### `skipToNext` / `skipToPrevious`

Navigate between episodes in the queue.

- **ADB Commands**:
  ```bash
  adb shell cmd app_functions invoke-function --package com.mak.pocketnotes.android.dev --function skipToNext --args '{}'
  adb shell cmd app_functions invoke-function --package com.mak.pocketnotes.android.dev --function skipToPrevious --args '{}'
  ```

---

## Verification & Debugging

To list all functions currently registered by the system for the app:

```bash
adb shell cmd app_functions list-functions --package com.mak.pocketnotes.android.dev
```
