# SLGain VST Audio Plugin

![License](https://img.shields.io/badge/license-Visual%20Review%20Only-red) ![Platform](https://img.shields.io/badge/platform-Android-green) ![Language](https://img.shields.io/badge/language-Java-orange)

## About This Repository

This repository contains the **logic-only source code** of the Sinewave Breathing Android application. It is provided for **demonstration, educational review, and professional evaluation purposes only**.

⚠️ **This code cannot be compiled or executed** as all graphical assets, audio files, build configurations, and resource definitions have been intentionally excluded.

## What is Sinewave Breathing?

Sinewave Breathing is an Android application that guides users through a **mathematically-optimized breathing technique**.

The app helps users:

- Practice sinusoidal breathing patterns at specific frequencies (0.02 Hz - 0.135 Hz)
- Target their personal **baroreflex resonant frequency** (typically ~0.1 Hz)
- Achieve **heart rate variability coherence** and **phase alignment**
- Manage stress, anxiety, and improve sleep quality
- Visualize their breathing with animated sine wave graphics

### The Science Behind It

The technique synchronizes breathing, heart rate, and blood pressure into coherent sine wave patterns. When practiced at the user's resonant frequency (~0.1 Hz or 6 breaths per minute), it:

- Maximizes heart rate variability amplitude
- Stimulates the baroreflex response
- Creates phase alignment between respiratory, cardiac, and blood pressure rhythms
- Activates parasympathetic nervous system for relaxation

For the full theoretical background, see: [Mathematical Spirituality: Sinewave Breathing](https://sinewavelab.com/sonofields/mathematical-spirituality-sinewave-breathing/)

## App Features

Based on the source code, the app includes:

### Core Functionality

- **Adjustable breathing frequency** (0.02 Hz - 0.135 Hz range)
  - Fine adjustment buttons (±0.001 Hz / ±0.005 Hz)
  - Coarse adjustment buttons (±0.025 Hz)
- **Lottie-based sine wave animation** that synchronizes with breathing cycles
- **Session timer** with configurable duration
- **Audio guidance system** with multiple sound cue options:
  - 8 clicks per cycle (at 8 breath phases)
  - 2 clicks per cycle (at inhalation/exhalation transitions)
  - Custom configurations
- **Haptic feedback** (vibration) synchronized with audio cues
- **Volume control** integrated with system audio
- **Session completion notifications**

### Technical Implementation

- SharedPreferences for persistent user settings
- CountDownTimer for session management
- SoundPool for low-latency audio playback
- Lottie animations for smooth, scalable graphics
- PreferenceFragment for settings management
- Keep-screen-on during active sessions

## Repository Contents

### Included Files

- `MainActivity.java` - Main application logic and breathing session control
- `SettingsActivity.java` - Settings screen container
- `SettingsFragment.java` - Preference fragment for user settings
- `LICENSE.md` - Visual Review License

### Intentionally Excluded

As stated in the LICENSE, the following are **not included** and remain proprietary:

#### Visual Assets

- Lottie animation files (sine wave visualization)
- UI/UX layouts (XML files)
- Icons and drawable resources
- Animation definitions
- App theme and styling

#### Audio Assets

- Click sound effects (`click_in.raw`)
- Transition sound effects (`change.raw`)
- Session completion sound (`ending.raw`)

#### Build Configuration

- `AndroidManifest.xml`
- Gradle build files
- Resource XML files (`preferences.xml`, `menu_main.xml`, layouts)
- ProGuard/R8 rules

#### Dependencies Used (Not Included)

The app relies on the following libraries (visible in code imports):

- AndroidX AppCompat
- AndroidX Preference
- Airbnb Lottie for Android
- Standard Android SDK components (SoundPool, Vibrator, etc.)

## Technical Highlights

### Architecture Patterns

- Activity-Fragment architecture for settings
- SharedPreferences for data persistence
- Listener-based UI event handling
- Animation lifecycle management

### Key Implementation Details

- **Frequency calculations** with precise rounding to 3 decimal places
- **Animation speed scaling** relative to base frequency (0.08 Hz)
- **Progress-based sound triggering** at specific animation points (0.05, 0.15, 0.25, 0.35, 0.45, 0.55, 0.65, 0.75, 0.85, 0.95)
- **Volume normalization** between 0.0 and 1.0 for cross-device consistency
- **Vibration compatibility** for both legacy (pre-O) and modern Android versions
- **Fade animations** for session start/stop transitions

## License

This source code is provided under a **Visual Review License**.

See [LICENSE.md](https://claude.ai/chat/LICENSE.md) for complete terms.

## About the Developer

**Sinewave Lab** (Micael Nobre)
 Creator of Mathematical Spirituality philosophy and SonoFields research project

- Website: [sinewavelab.com/sonofields](https://sinewavelab.com/sonofields/)
- Research: Frequency-based wellness applications
- Focus: Intersection of mathematics, spirituality, and human physiology

## Contact

For inquiries about the complete application:

- Visit: [SonoFields](https://sinewavelab.com/sonofields/)