# Team 18
## Anir Saddik - Eva Reichel - Daria Jaroszuk - Erik Gerbreders - Rufina Chyhohidze

# KdG - Karel de Grote University
**Course**: Integration 4  
**Academic Year**: 2024/2025 - ACS202

---

### Setup Instructions

**Build and Run**
### 1. To set up the DataBase:

Run the `docker-compose.yml` file

### 2. Add .env file

```env
    WORKBENCH_API_KEY=YOUR-API-KEY
```

### 3. To execute the application, execute the gradlew bootRun command

   ```bash
   
    docker compose up -d
   ./gradlew bootRun
  ```


### 4. Populate the database:

Use `resources/static/data/data.sql`.

It will add four users to the database:

- Technician

  - Email: tech@example.com

  - Password: password123

  - Approved: true

- Workshop Admin

  - Email: admin@example.com

  - Password: admin123

  - Approved: false

- Customer

  - Email: customer@email.com

  - Password: customer123

- System Admin

  - Email: superadmin@email.com

  - Password: password

It is possible to create your own account, but if you want to create a Technician or Workshop Admin account, it must be approved by the Super Admin before logging in.

Additionally, the script adds two test benches and three workshops.

### 5. Starting a test

To start a test, log in as a Technician and click the Start Test button on the left side of the dashboard.

Enter the customer's email, select or create a bike, and start the test. After that, you can access the test report page or return to the dashboard.

