# Stock Price Analytics Dashboard

This project is a Stock Price Analytics Dashboard consisting of three main components:
- MySQL database running in Docker
- Backend built with Java Spring Boot
- Frontend built with React

## Prerequisites

- Docker
- Node.js and npm
- Java JDK 17 or higher
- Maven

## Setup and Run

### MySQL Database

1. **Pull MySQL Docker Image**:
    ```sh
    docker pull mysql:latest
    ```

2. **Run MySQL Container**:
    ```sh
    docker run --name mysql-db -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=stockdb -p 3306:3306 -d mysql:latest
    ```

### Backend (Java Spring Boot)

1. **Navigate to the Backend Directory**:
    ```sh
    cd backend
    ```

2. **Configure MySQL Connection**:
   Update `application.properties` with your MySQL configurations:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/stockdb
    spring.datasource.username=root
    spring.datasource.password=root
   alphavantage.api.key=<ALPHADVANTAGE_KEY>
    ```

3. **Build and Run the Backend**:
    ```sh
    mvn clean install
    mvn spring-boot:run
    ```

### Frontend (React)

1. **Navigate to the Frontend Directory**:
    ```sh
    cd frontend
    ```

2. **Install Dependencies**:
    ```sh
    npm install
    ```

3. **Run the Frontend**:
    ```sh
    npm start
    ```

The React app should now be running on `http://localhost:3000` and will communicate with the backend at `http://localhost:8080`.

## Additional Information

### Backend API Endpoints

- **Get Stock Data**: `GET /api/stock_data/{symbol}/{start_date}`
    - Retrieves stock data for a given symbol and date.

### CORS Configuration

Ensure the backend is configured to allow CORS requests from the frontend. You can add this configuration globally or to specific endpoints in the Spring Boot application.

### Troubleshooting

- Ensure MySQL is running and accessible.
- Verify backend and frontend configurations match your environment settings.
- Check logs for detailed error messages and address them accordingly.

## Future Improvements

- Integrate `docker-compose` for easier setup and orchestration.
- Add more robust error handling and logging.
- Implement additional features and enhancements as needed.

## Contributing

Feel free to fork this repository and submit pull requests. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License.
