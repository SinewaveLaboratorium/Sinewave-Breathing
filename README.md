# Sinewave Breathing Android App

![License](https://img.shields.io/badge/license-Open%20Consultation%20Only-red) ![Platform](https://img.shields.io/badge/platform-Android-green) ![Language](https://img.shields.io/badge/language-Java-orange)

> ⚠️ **Licensing Notice:** This project is **Source Available** for evaluation and portfolio review purposes only. **It is not an Open Source project**. Graphical assets, compiled binaries, and third-party framework dependencies have been excluded from this repository. Please refer to the `LICENSE` file for full terms.

## What is Sinewave Breathing?

An Android application designed to guide users through **mathematically-optimized breathing** (0.02 Hz - 0.135 Hz). The goal is to reach the **baroreflex resonant frequency** (~0.1 Hz) to maximize heart rate variability (HRV) and induce deep relaxation.

![](https://sinewavelab.com/sonofields/wp-content/uploads/2026/02/Sinewave-Breathing-app.webp)

### Key Features (Logic Included)

* **Precision Controls:** Frequency adjustment down to  increments.
* **Sync Engine:** Logic for synchronizing Lottie animations, Haptic feedback, and SoundPool audio cues.
* **Session Management:** Configurable timers and persistent user preferences.
* **Phase Alignment:** Calculations for 8-phase breath cycle triggers.

## Repository Structure

| File | Description |
| --- | --- |
| `MainActivity.java` | Core breathing logic and session state management. |
| `SettingsFragment.java` | Configuration logic for user preferences. |
| `SettingsActivity.java` | Container for the settings UI. |

**Excluded Proprietary Assets:** XML Layouts, Lottie JSON files, `.raw` audio files, and Gradle build scripts.

## Technical Context

The code demonstrates implementation of:

* **SoundPool** for low-latency rhythmic cues.
* **SharedPreferences** for session persistence.
* **CountDownTimer** for precise breathing intervals.

## License & Contact

**License:** Open Review Only (See `LICENSE.md`).

**Developer:** [Sinewave Lab](https://sinewavelab.com/sonofields/) (Micael Nobre).

