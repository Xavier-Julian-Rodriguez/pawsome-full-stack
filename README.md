# Pawsome
## Xavier Rodriguez
To view the production application, visit: http://pawsome-frontend-bucket.s3-website.us-east-2.amazonaws.com/
## Description
Make your pet happy and build a deeper bond with them by using our custom homemade pet treat recipes to make pet snacks. Create a user account, pet profiles, and begin generating custom ingredient treat recipes for your pet.

## Local Development Setup

### Starting the Back End (see front-end README for front-end set up)
1. This application requires you to have an OpenAI api key to make api calls to the OpenAI GPT recipe generator. You can sign up for an account and obtain a secret api key at: https://platform.openai.com/signup
2. This application stores data on a PostgreSQL database. Enter your credentials in the properties below. To download PostgreSQL, visit: https://www.postgresql.org/download/
3. The application integrates JWT Tokens for authorization. The 'SECRET_KEY' property is whatever value you choose. By default, it is set to 'your_secret_key4*'
4. Before running the back end server, set the environment variables for each of the properties listed below. These environment variables can be found in the application.properties file. Add the values and copy and paste in your terminal, or set them in the application.properties file.

- export OPENAI_SECRET_KEY=
- export DB_NAME=
- export AWS_DB_ENDPOINT=(set your database endpoint here 'localhost' for example)
- export DB_USERNAME=
- export DB_PASSWORD=
- export SECRET_KEY=your_secret_key4*
- export CORS_ALLOWED_ORIGIN=http://localhost:4200

5. From the root directory where the pom.xml is, run: 'mvn clean install'
6. Then run: 'mvn spring-boot:run'
7. The app will run unit tests and begin on port 8080.
