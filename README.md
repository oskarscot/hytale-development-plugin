# Hytale Development Plugin

![Build](https://github.com/oskarscot/hytale-development-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)

<!-- Plugin description -->
First-hand support for Hytale plugin development in IntelliJ IDEA.

Project scaffolding, build setup, and editor support for writing Hytale server plugins.
<!-- Plugin description end -->

## Requirements

- IntelliJ IDEA 2026.1 or newer (Ultimate or Community).
- Generated projects target JDK 25 via Gradle toolchain. The IDE will offer to download one if you don't have it.

## Installation

**From the marketplace**: <kbd>Settings</kbd> → <kbd>Plugins</kbd> → <kbd>Marketplace</kbd>, search "Hytale Development", install.
Or open [the listing](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) directly.

**From a release**: grab the zip from [releases](https://github.com/oskarscot/hytale-development-plugin/releases/latest) and use <kbd>Settings</kbd> → <kbd>Plugins</kbd> → <kbd>⚙️</kbd> → <kbd>Install plugin from disk</kbd>.

## Usage

### New project

<kbd>File</kbd> → <kbd>New</kbd> → <kbd>Project</kbd>, pick **Hytale Plugin**.

Wizard fields:

- Plugin name and package.
- Patchline (`release`, `pre-release`) - selects the Maven repo.
- Hytale version, fetched from the chosen patchline.
- Language: Java or Kotlin.

### manifest.json schema

Active on any `manifest.json` under `src/main/resources/` when Hytale Server is on the project classpath. Provides:

- Key autocomplete (`Group`, `Name`, `Main`, `ServerVersion`, `Authors`, `Dependencies`, …)
- Hover docs sourced from the [Hytale manifest spec](https://doctale.dev/getting-started/plugin-manifest/).
- Type and format validation (semver `Version`, `Email`, `Url`).
- Errors for missing required fields and unknown keys.

The status bar shows **Hytale Plugin Manifest** as the active schema.

## Build

```sh
./gradlew buildPlugin     # zip lands in build/distributions/
./gradlew runIde          # sandbox IDE with the plugin loaded
./gradlew verifyPlugin    # checks against the targeted IDE range
```

---
Built on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
