# Getting Started

### Requirements

Java 17 

### Installation

from console

```./mvnw clean install```

### Cache configuration

application.properties

```spring.cache.caffeine.spec=maximumSize=500,expireAfterAccess=1m```

### Running the application

from console

```./mvnw spring-boot:run```

##### or else

from console

```docker login```

```docker build -t rate-service .```

```docker run -d -p 8080:8080 rate-service```

### Documentation

```http://localhost:8080/swagger-ui/index.html```

### Issue
```
Przygotuj serwis w Javie (z wykorzystaniem Spring Boot) będący middleware’em do dostępu do API
NBP (http://api.nbp.pl/). Serwis nie potrzebuje bazy danych, całość możesz trzymać w pamięci procesu,
ale postaraj się użyć odpowiednich wzorców projektowych które pozwolą na jej łatwe podpięcie. Staraj
się trzymać dobrych praktyk programistycznych, zwróć też uwagę na odpowiednią obsługę mogących
się pojawić błędów.
Powodzenia!
1. Serwis HTTP:
a. przygotuj endpoint /health który będzie sprawdzał stan aplikacji (jej dostępność)
b. przygotuj endpoint /rates który będzie zwracał listę kursów walut dla bieżącego dnia w
formacie json (encja Rate poniżej)
c. przygotuj endpoint /rates/:id który będzie zwracał kursy dla określonej waluty
d. rozbuduj endpoint /rates o obsługę daty, aby zwracać kursy na określony dzień
e. dodaj walidację przyjmowanych parametrów
2. Obsługa cache:
a. dodaj cache pobranych z API NBP kursów walut, tak aby nie musieć odpytywać
zewnętrznego API za każdym razem
b. zadbaj o cykliczne czyszczenie cache’a po konfigurowalnym czasie
c. dodaj endpoint /refresh-cache który odświeży cache dla wybranej waluty
3. Komunikacja AMQP:
a. dodaj emisję eventu na kolejkę RabbitMQ w momencie gdy nastąpiło odświeżenie
cache’a dla danej waluty
b. do endpointu /health dodaj stan połączenia z brokerem AMQP i ilość wyemitowanych
eventów

Encja obiektu Rate (dla USD):
{
"currency": "dolar amerykański",
"code": "USD",
"mid": 4.0648
"bid": 4.0275,
"ask": 4.1089,
"date": "2023-06-21"
}
```
