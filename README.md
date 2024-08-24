# Food Planner Application

## Overview
The **Food Planner Application** is an Android mobile application designed to help users plan their weekly meals. The app provides the ability to view meal categories, suggest meals, and search for specific meals based on various criteria. Users can save their favorite meals for later access, even when offline. The application leverages the [TheMealDB API](https://themealdb.com/api.php) for meal data and includes functionalities like user authentication, data synchronization, and local storage.

## Features
- **Meal of the Day**: View an arbitrary meal for inspiration.
- **Search Meals**: Search for meals based on country, ingredient, or category.
- **Categories and Countries**: Browse available categories and countries to discover popular meals.
- **Favorites Management**: Add or remove meals from favorites using Room for local storage.
- **Data Synchronization**: Synchronize and backup user data with Firebase to retrieve upon login.
- **Meal Planning**: Add meals to the current week’s plan and view them offline.
- **Offline Access**: View favorite meals and weekly plans even without network connectivity.
- **User Authentication**:
  - Simple login and sign-up with social media authentication (Google, Facebook, Twitter).
  - Persistent login using SharedPreferences.
  - Guest mode for limited access.
- **Meal Details**:
  - View meal name, image, origin country, ingredients (with images if possible), steps, and an embedded video.
  - Add or remove meals from favorites.
- **Splash Screen**: Animated splash screen using Lottie.
- **Design**: The application follows Material Design principles.

## Technical Specifications
- **Language**: Kotlin/Java
- **Architecture**: MVP (Model-View-Presenter)
- **API**: [TheMealDB API](https://themealdb.com/api.php)
- **Local Storage**: Room Database
- **Network**: Retrofit for API calls
- **Authentication**: Firebase Authentication (Email/Password, Social Login)
- **Data Synchronization**: Firebase Firestore
- **UI Design**: Material Design with custom views and animations (Lottie)
- **Offline Functionality**: Room for storing favorite meals and weekly plans

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/MahmooudDarwish/meal.git



