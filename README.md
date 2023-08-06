# PowerHouse-Task

It is a weather forecasting application based on the MVVM architecture that provides users with the current weather forecast for their device's current location. The app is designed to offer a seamless experience while adhering to the specified requirements.

# Key Features:

Current Weather Display: The app fetches the current weather forecast by sending an HTTP request to the OpenWeatherMap API. It then parses the response to extract relevant weather information such as temperature, humidity, wind speed, and more.

User-Friendly Interface: The weather information is presented in a visually appealing and user-friendly format using card views. The location's name, current temperature, weather description, and other details are displayed on the card.

Offline Support: The app ensures a smooth user experience even when there is no internet connection. It stores the previously fetched weather data locally, and on subsequent app launches, this cached data is displayed. The timestamp of the cached data is also provided to indicate when the data was last updated.

Background Data Refresh: In addition to displaying cached data, the app performs background network calls to fetch the latest weather data. This ensures that users receive up-to-date weather information even if the app is running in the background.

Screenshots:
<img src="https://github.com/vishal719/PowerHouse-Task/assets/73362847/8833481e-7f6b-43da-9fe5-40b3debd1b29" width="180" height="400">
<img src="https://github.com/vishal719/PowerHouse-Task/assets/73362847/75eda992-42e6-4f6e-8d1b-4f202aa8e3bd" width="180" height="400"> 
