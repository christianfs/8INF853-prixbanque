<a name="readme-top"></a>
# UQAC - 8INF853 - Prixbanque Microservices

[Link](https://drive.google.com/file/d/1skjqWD3hhMc1dK2ojQvMADW9w4IWQERz/view?usp=sharing) to access the presentation video.

<!-- ABOUT THE PROJECT -->
## About The Project

### Prixbanque Microservices

PrixBanque is a new "fintech" that seeks to develop simple, reliable, secure and 100% digital solutions for the financial life of its customers.

### Built With

* Java 11
* Spring Boot
* Lombok
* Spring Cloud Netflix
* Spring for Apache Kafka
* Spring Cloud Gateway
* MongoDB
* PostgreSQL
* Docker Compose

<!-- GETTING STARTED -->
## Getting Started

The project uses docker-compose. For the generation of containers, we use [docker hub](https://hub.docker.com/). To run the system in docker, a free account must be created on the site above. However, it is also possible to run through an IDE. We recommend using IntelliJ to identify and run the services directly through it.

### Installation

_Below is an example of how you can instruct your audience on installing and setting up your app. This template doesn't rely on any external dependencies or services._

1. Create a free account at [docker hub](https://hub.docker.com/) and get your user name
2. Clone the repo
  ```sh
  git clone git@github.com:christianfs/8INF853-prixbanque.git
  ```
3. Set the environment variables
  ```
  POSTGRES_USER
  POSTGRES_PASSWORD
  ```
4. In the docker-compose.yml file replace all occurrences of csegovia with your docker hub username. Unfortunately we couldn't get docker compose to recognize the environment variables.
5. Add your docker hub username and password in the settings.xml file between the servers tag. On Ubuntu it is located at ~/.m2/settings.xml
  ```xml
     <servers>
         <server>
             <id>registry.hub.docker.com</id>
             <username></username>
             <password></password>
         </server>
     </servers>
   ```
6. Navigate to the prixbanque folder. Compile and create the containers on the docker hub and run docker-compose through the command
  ```sh
  ./localdocker.sh
  ```
  Or just to run the containers
  ```sh
  docker compose up -d
  ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- Usage -->
## Usage

### Endpoints (in the Docker container)

- POST Account: http://localhost:8181/api/account
    - Body:
    ```json
    {
        "firstName": "FirstName",
        "lastName": "LastName",
        "email": "email@email.com",
        "phone": "3434434232",
        "address": "Address",
        "password":"xxxxxxx"
    }
    ```
- GET All Accounts: http://localhost:8181/api/account
- GET Account: http://localhost:8181/api/account/[accountNumber]
- PUT Account deposit: http://localhost:8181/api/core/transaction/deposit
    - Body:
    ```json
    {
        "accountNumber": "243320510",
        "amount": 20.00
    }
    ```
- PUT Account withdraw: http://localhost:8181/api/core/transaction/withdraw
    - Body:
    ```json
    {
        "accountNumber": "243320510",
        "amount": 20.00
    }
    ```
- PUT Transfer: http://localhost:8181/api/core/transaction/fund-transfer
    - Body:
    ```json
    {
        "accountNumber": "243320510",
        "recipientAccountNumber": "660335100",
        "amount": 1.45
    }
    ```
- GET All Balance Accounts: http://localhost:8181/api/core/balance-account
- GET All Statements: http://localhost:8181/api/statement
- GET Statement By Account Number And Between Dates: http://localhost:8181/api/statement/accountNumber/[accountNumber]/startDate/[startDate]/endDate/[endDate]
    - Date format: yyyy-MM-dd


<p align="right">(<a href="#readme-top">back to top</a>)</p>
