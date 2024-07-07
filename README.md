![stockapp](https://github.com/Rohit-554/TrackDown/assets/48874687/ce4e4844-846a-4d44-afbc-122d66a77f5b)# TrackDown

A application built over kotlin to which lets you explore various stocks, etfs etc.

![App logo](https://github.com/Rohit-554/TrackDown/assets/48874687/27027894-53b3-4b62-b26d-da65579c965d)

## About the Project

This Android application provides users with real-time data and insights into stocks and ETFs. It includes two main screens and utilizes the Alpha Vantage API for data fetching.
![stockapp](https://github.com/Rohit-554/TrackDown/assets/48874687/0599ff91-9106-4fef-aeaa-c5d0520e55e8)
## Demo
https://github.com/Rohit-554/TrackDown/assets/48874687/689306da-d8f5-4b67-b4da-f2951f21e092

https://github.com/Rohit-554/TrackDown/assets/48874687/31892580-c2e9-48ad-ba49-2a167cdd4e3b

### Explore Screen
- Displays Top Gainers and Losers in separate tabs.
- Each tab features a grid of cards with detailed stock/ETF information.






<img src="https://github.com/Rohit-554/TrackDown/assets/48874687/47b3d7b5-4e0c-4fd8-afd0-a6d2948c168d" width="200" height="450">

### Details Screen
- Shows basic information and a price line graph for selected stocks/ETFs.
  <br>
<img src="https://github.com/Rohit-554/TrackDown/assets/48874687/5cc3366e-b574-4a32-b533-1d763e4b6c98" width="200" height="450">
<img src="https://github.com/Rohit-554/TrackDown/assets/48874687/58839dca-5471-4f57-a1f9-4c847aeed6a0" width="200" height="450">

## Features

- **API Integration**: Utilizes [Alpha Vantage](https://www.alphavantage.co/support/#) for data endpoints and [Api Ninjas](https://api-ninjas.com/api/logo) For logo .
- **Error Handling**: Manages loading, error, and empty states effectively.
- **Caching**: Implements API response caching using [Room](https://developer.android.com/training/data-storage/room) with expiration.
- **Dependency Injection**: Uses dependency injection for efficient management (Kotlin + Android).
- **UI/UX**: Includes a basic yet intuitive user interface.

## Additional Enhancements

- **Search Suggestions**: Displays recently searched items as suggestions.
- **UI Customization**: Offers light/dark mode theme switch.
- **API Exploration**: Explores additional Alpha Vantage endpoints for added functionality.

## Technologies Used
- Kotlin (Android)
- Jetpack compose
- Graph [MpAndroidChart](https://github.com/PhilJay/MPAndroidChart)
- Alpha Vantage API
- [Api Ninjas](https://api-ninjas.com/api/logo) For logo 

## Getting Started

To run the app:

1. Clone the repository.
2. Generate an API key and replace it in the app. Place it in `local.properties` like this:
```
sdk.dir= ...
STOCK_API_KEY= ...
LOGO_API_KEY= ...
```
3. Apis can be generated from here [STOCK_API_key](https://www.alphavantage.co/support/#) and [LOGO_API_KEY](https://api-ninjas.com/profile)
4. Build and run the app on an Android device or emulator.

## Tech Stack

- **Kotlin:** For development
- **Android Studio:** The integrated development environment (IDE) for Android app development.
- **Compose:** an open-source, Kotlin-based toolkit for building native Android user interfaces (UIs)
- **Dependency Injection:** Employed for efficient management of app dependencies.
- **MVVM (Model-View-ViewModel):** Architecture pattern for separating UI from business logic.





