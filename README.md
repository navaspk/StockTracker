
**StockTracker**

StockTracker is an Android application for tracking stock prices and managing a watchlist. 
Built with Kotlin, Compose, and Gradle.


**Features**

View real-time stock prices and details

**Tech Stack**

Kotlin for Android development
Gradle for build automation
MVI with Clean architecture
Koin for dependency
Ktor for network requests


**Project Structure**

base module helps to provide default implementation for feature module like BaseViewModel etc

app/src/main/kotlin+java/ — Source code (Kotlin)
app/src/main/res/ — Resources (drawables, values)
app/src/main/AndroidManifest.xml — App manifest

network module help to provide common dependency for Ktor client to make the connection to websocket

**Getting Started**

Prerequisites
Android Studio (2025.1.1 or later)
JDK 17+

**Setup**

Clone the repository:
git clone https://github.com/navaspk/StockTracker.git
cd StockTracker
Open in Android Studio.
Build the project (Build > Make Project).
Run on an emulator or device.

**Usage**

Launch the app.
View real-time stock prices and details
Capable of controlling toggel for latest updated price of stocks

**License**

This project is licensed under the MIT License.





https://github.com/user-attachments/assets/d6e29db9-2025-4248-b098-251e4606200f


