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

## App Screenshots (Guest & User Views)

<table>
  <tr>
    <td><b>Guest Home</b></td>
    <td><b>Guest Settings</b></td>
    <td><b>Need to Login</b></td>
    <td><b>Sign Up</b></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/e322583c-0ce0-4f27-aed5-cb90969bef44" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/50c8827e-53bb-441f-af10-a8ec32b4f0ce" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/7a710513-4090-4cf5-98f8-4e7fc1bd432f" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/000044e8-831a-4346-8b86-c80ab5242fcf" width="200"/></td>
  </tr>
  <tr>
    <td><b>Sign In</b></td>
    <td><b>Home</b></td>
    <td><b>Search Categories</b></td>
    <td><b>Meal Details</b></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/f54eac10-0722-475e-a75a-6cc549f58cc4" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/e3762a45-4b98-4800-860b-1a6e1e124625" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/77bf1a1b-9f49-4515-92f3-007719898058" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/f8797c1f-bebd-4340-b43c-6663a9014f71" width="200"/></td>
  </tr>
  <tr>
    <td><b>Favourites</b></td>
    <td><b>Planner</b></td>
    <td><b>Settings</b></td>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/871e0f69-8522-4389-963f-1547460945f8" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/c475daad-77f6-4893-b46b-f135838744e2" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/ae869746-23fb-40d2-a51e-17704730bebc" width="200"/></td>
  </tr>
</table>


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



