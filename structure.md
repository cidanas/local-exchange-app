# Project Structure

vscode_projects
├── local-exchange-app
│   ├── .github
│   │   └── workflows
│   │       └── ci-cd.yml
│   ├── backend
│   │   ├── src
│   │   │   ├── main
│   │   │   │   ├── java
│   │   │   │   │   └── com
│   │   │   │   │       └── localexchange
│   │   │   │   │           ├── config
│   │   │   │   │           │   ├── CorsConfig.java
│   │   │   │   │           │   └── SecurityConfig.java
│   │   │   │   │           ├── controller
│   │   │   │   │           │   ├── AuthController.java
│   │   │   │   │           │   ├── ExchangeRequestController.java
│   │   │   │   │           │   ├── ItemListingController.java
│   │   │   │   │           │   ├── MessageController.java
│   │   │   │   │           │   ├── NotificationController.java
│   │   │   │   │           │   ├── ReviewController.java
│   │   │   │   │           │   ├── SkillListingController.java
│   │   │   │   │           │   └── UploadController.java
│   │   │   │   │           ├── dto
│   │   │   │   │           │   ├── ExchangeRequestDTO.java
│   │   │   │   │           │   ├── ItemDetailDTO.java
│   │   │   │   │           │   ├── ItemListingDTO.java
│   │   │   │   │           │   ├── LoginDTO.java
│   │   │   │   │           │   ├── LoginResponseDTO.java
│   │   │   │   │           │   ├── MessageDTO.java
│   │   │   │   │           │   ├── NotificationDTO.java
│   │   │   │   │           │   ├── ProfileUpdateDTO.java
│   │   │   │   │           │   ├── RegisterDTO.java
│   │   │   │   │           │   ├── ReviewDTO.java
│   │   │   │   │           │   ├── SkillDetailDTO.java
│   │   │   │   │           │   ├── SkillListingDTO.java
│   │   │   │   │           │   └── UserDTO.java
│   │   │   │   │           ├── exception
│   │   │   │   │           │   ├── DuplicateEmailException.java
│   │   │   │   │           │   ├── GlobalExceptionHandler.java
│   │   │   │   │           │   ├── InvalidCredentialsException.java
│   │   │   │   │           │   ├── ResourceNotFoundException.java
│   │   │   │   │           │   └── UnauthorizedException.java
│   │   │   │   │           ├── model
│   │   │   │   │           │   ├── ExchangeRequest.java
│   │   │   │   │           │   ├── ExchangeStatus.java
│   │   │   │   │           │   ├── ItemListing.java
│   │   │   │   │           │   ├── Message.java
│   │   │   │   │           │   ├── Notification.java
│   │   │   │   │           │   ├── NotificationType.java
│   │   │   │   │           │   ├── Review.java
│   │   │   │   │           │   ├── SkillListing.java
│   │   │   │   │           │   └── User.java
│   │   │   │   │           ├── repository
│   │   │   │   │           │   ├── ExchangeRequestRepository.java
│   │   │   │   │           │   ├── ItemListingRepository.java
│   │   │   │   │           │   ├── MessageRepository.java
│   │   │   │   │           │   ├── NotificationRepository.java
│   │   │   │   │           │   ├── ReviewRepository.java
│   │   │   │   │           │   ├── SkillListingRepository.java
│   │   │   │   │           │   └── UserRepository.java
│   │   │   │   │           ├── security
│   │   │   │   │           │   ├── CustomUserDetailsService.java
│   │   │   │   │           │   ├── JwtAuthenticationFilter.java
│   │   │   │   │           │   └── JwtTokenProvider.java
│   │   │   │   │           ├── service
│   │   │   │   │           │   ├── AuthService.java
│   │   │   │   │           │   ├── ExchangeRequestService.java
│   │   │   │   │           │   ├── FileStorageService.java
│   │   │   │   │           │   ├── ItemListingService.java
│   │   │   │   │           │   ├── MessageService.java
│   │   │   │   │           │   ├── NotificationService.java
│   │   │   │   │           │   ├── ReviewService.java
│   │   │   │   │           │   └── SkillListingService.java
│   │   │   │   │           └── LocalExchangeApplication.java
│   │   │   │   └── resources
│   │   │   │       ├── application.yml
│   │   │   │       └── data.sql
│   │   │   └── test
│   │   │       └── java
│   │   │           └── com
│   │   │               └── localexchange
│   │   │                   └── service
│   │   │                       ├── AuthServiceTest.java
│   │   │                       ├── ExchangeRequestServiceTest.java
│   │   │                       └── ItemListingServiceTest.java
│   │   ├── target
│   │   │   ├── classes
│   │   │   │   ├── com
│   │   │   │   │   └── localexchange
│   │   │   │   │       ├── config
│   │   │   │   │       │   ├── CorsConfig.class
│   │   │   │   │       │   └── SecurityConfig.class
│   │   │   │   │       ├── controller
│   │   │   │   │       │   ├── AuthController.class
│   │   │   │   │       │   ├── ExchangeRequestController.class
│   │   │   │   │       │   ├── ItemListingController.class
│   │   │   │   │       │   ├── MessageController.class
│   │   │   │   │       │   ├── NotificationController.class
│   │   │   │   │       │   ├── ReviewController.class
│   │   │   │   │       │   ├── SkillListingController.class
│   │   │   │   │       │   └── UploadController.class
│   │   │   │   │       ├── dto
│   │   │   │   │       │   ├── ExchangeRequestDTO.class
│   │   │   │   │       │   ├── ItemDetailDTO.class
│   │   │   │   │       │   ├── ItemListingDTO.class
│   │   │   │   │       │   ├── LoginDTO.class
│   │   │   │   │       │   ├── LoginResponseDTO.class
│   │   │   │   │       │   ├── MessageDTO.class
│   │   │   │   │       │   ├── NotificationDTO.class
│   │   │   │   │       │   ├── ProfileUpdateDTO.class
│   │   │   │   │       │   ├── RegisterDTO.class
│   │   │   │   │       │   ├── ReviewDTO.class
│   │   │   │   │       │   ├── SkillDetailDTO.class
│   │   │   │   │       │   ├── SkillListingDTO.class
│   │   │   │   │       │   └── UserDTO.class
│   │   │   │   │       ├── exception
│   │   │   │   │       │   ├── DuplicateEmailException.class
│   │   │   │   │       │   ├── GlobalExceptionHandler.class
│   │   │   │   │       │   ├── GlobalExceptionHandler$ErrorResponse.class
│   │   │   │   │       │   ├── InvalidCredentialsException.class
│   │   │   │   │       │   ├── ResourceNotFoundException.class
│   │   │   │   │       │   └── UnauthorizedException.class
│   │   │   │   │       ├── model
│   │   │   │   │       │   ├── ExchangeRequest.class
│   │   │   │   │       │   ├── ExchangeStatus.class
│   │   │   │   │       │   ├── ItemListing.class
│   │   │   │   │       │   ├── Message.class
│   │   │   │   │       │   ├── Notification.class
│   │   │   │   │       │   ├── NotificationType.class
│   │   │   │   │       │   ├── Review.class
│   │   │   │   │       │   ├── SkillListing.class
│   │   │   │   │       │   └── User.class
│   │   │   │   │       ├── repository
│   │   │   │   │       │   ├── ExchangeRequestRepository.class
│   │   │   │   │       │   ├── ItemListingRepository.class
│   │   │   │   │       │   ├── MessageRepository.class
│   │   │   │   │       │   ├── NotificationRepository.class
│   │   │   │   │       │   ├── ReviewRepository.class
│   │   │   │   │       │   ├── SkillListingRepository.class
│   │   │   │   │       │   └── UserRepository.class
│   │   │   │   │       ├── security
│   │   │   │   │       │   ├── CustomUserDetailsService.class
│   │   │   │   │       │   ├── JwtAuthenticationFilter.class
│   │   │   │   │       │   └── JwtTokenProvider.class
│   │   │   │   │       ├── service
│   │   │   │   │       │   ├── AuthService.class
│   │   │   │   │       │   ├── ExchangeRequestService.class
│   │   │   │   │       │   ├── FileStorageService.class
│   │   │   │   │       │   ├── ItemListingService.class
│   │   │   │   │       │   ├── MessageService.class
│   │   │   │   │       │   ├── NotificationService.class
│   │   │   │   │       │   ├── ReviewService.class
│   │   │   │   │       │   └── SkillListingService.class
│   │   │   │   │       └── LocalExchangeApplication.class
│   │   │   │   ├── application.yml
│   │   │   │   └── data.sql
│   │   │   ├── generated-sources
│   │   │   │   └── annotations
│   │   │   ├── generated-test-sources
│   │   │   │   └── test-annotations
│   │   │   ├── maven-archiver
│   │   │   │   └── pom.properties
│   │   │   ├── maven-status
│   │   │   │   └── maven-compiler-plugin
│   │   │   │       ├── compile
│   │   │   │       │   └── default-compile
│   │   │   │       │       ├── createdFiles.lst
│   │   │   │       │       └── inputFiles.lst
│   │   │   │       └── testCompile
│   │   │   │           └── default-testCompile
│   │   │   │               ├── createdFiles.lst
│   │   │   │               └── inputFiles.lst
│   │   │   ├── test-classes
│   │   │   ├── local-exchange-app-1.0.0.jar
│   │   │   └── local-exchange-app-1.0.0.jar.original
│   │   ├── uploads
│   │   │   ├── 1764611929384-01e51d01-f2f5-4d4f-ba26-a52ae3ce98ea.jpg
│   │   │   ├── 1764611955526-ae5cfac6-e53b-4ecb-8564-b35aa1325749.jpg
│   │   │   ├── 1764616211649-5ab626bd-b8eb-4ab8-9cec-467f089a2a38.jpg
│   │   │   ├── 1764616496363-6d6baeb1-ec52-49aa-ac0c-e88951a477a2.jpg
│   │   │   ├── 1764617137903-55680499-a218-493c-9c39-dede2c7965a7.jpg
│   │   │   ├── 1764617152029-c4c6255d-bd36-4b7f-9da7-ae1ff0e98e42.jpg
│   │   │   ├── 1764617157425-2d88f007-7a4c-4aed-8442-881f6047a18c.jpg
│   │   │   ├── 1764617162316-afb65183-7e9a-4780-9a22-9cf476f8edf6.jpg
│   │   │   ├── 1764619517666-bf82a41f-2067-462b-ba08-4464a436b6c8.jpg
│   │   │   ├── 1764619530693-5fa67e1e-c9f1-43df-9bfa-cd41f816caf5.jpg
│   │   │   ├── 1764626092658-5f0a1118-a3d7-4cf8-9de2-877bcbecbdf5.jpg
│   │   │   ├── 1764627218983-a679f76a-1922-44b1-8992-16da0683f100.jpg
│   │   │   ├── 1764628016616-3f0bc141-aeb9-4081-84f9-c2779f97bdc7.jpg
│   │   │   ├── 1764752406801-bd77d252-f255-4c54-b223-b640bb7fa551.jpg
│   │   │   ├── 1765467758021-db42b370-e46f-4e8f-b712-e7f7bb8ec390.jpg
│   │   │   └── 1765467780613-830acd94-db44-403c-ba93-8b756bac11dc.jpg
│   │   └── pom.xml
│   ├── docs
│   │   ├── API_DOCUMENTATION.md
│   │   ├── ARCHITECTURE.md
│   │   └── SRS.md
│   ├── frontend
│   │   ├── public
│   │   │   └── vite.svg
│   │   ├── src
│   │   │   ├── components
│   │   │   │   ├── common
│   │   │   │   │   ├── Alert.jsx
│   │   │   │   │   ├── Badge.jsx
│   │   │   │   │   ├── Button.jsx
│   │   │   │   │   ├── Card.jsx
│   │   │   │   │   ├── ImagePicker.jsx
│   │   │   │   │   ├── Input.jsx
│   │   │   │   │   ├── LoadingSpinner.jsx
│   │   │   │   │   ├── Modal.jsx
│   │   │   │   │   ├── Rating.jsx
│   │   │   │   │   ├── Select.jsx
│   │   │   │   │   └── TextArea.jsx
│   │   │   │   └── layout
│   │   │   │       ├── Footer.jsx
│   │   │   │       └── Navbar.jsx
│   │   │   ├── context
│   │   │   │   ├── AuthContext.jsx
│   │   │   │   └── NotificationContext.jsx
│   │   │   ├── pages
│   │   │   │   ├── ExchangesPage.jsx
│   │   │   │   ├── HomePage.jsx
│   │   │   │   ├── ItemCreatePage.jsx
│   │   │   │   ├── ItemDetailPage.jsx
│   │   │   │   ├── ItemEditPage.jsx
│   │   │   │   ├── ItemsPage.jsx
│   │   │   │   ├── LoginPage.jsx
│   │   │   │   ├── MessagesPage.jsx
│   │   │   │   ├── NotificationsPage.jsx
│   │   │   │   ├── ProfilePage.jsx
│   │   │   │   ├── RegisterPage.jsx
│   │   │   │   ├── ReviewCreatePage.jsx
│   │   │   │   ├── SkillCreatePage.jsx
│   │   │   │   ├── SkillDetailPage_updated.jsx
│   │   │   │   ├── SkillDetailPage.jsx
│   │   │   │   ├── SkillEditPage.jsx
│   │   │   │   └── SkillsPage.jsx
│   │   │   ├── services
│   │   │   │   ├── api.js
│   │   │   │   ├── authService.js
│   │   │   │   ├── exchangeService.js
│   │   │   │   ├── itemService.js
│   │   │   │   ├── messageService.js
│   │   │   │   ├── notificationService.js
│   │   │   │   ├── reviewService.js
│   │   │   │   ├── skillService.js
│   │   │   │   └── uploadService.js
│   │   │   ├── utils
│   │   │   │   ├── constants.js
│   │   │   │   └── validators.js
│   │   │   ├── App.jsx
│   │   │   ├── index.css
│   │   │   └── main.jsx
│   │   ├── .eslintrc.cjs
│   │   ├── index.html
│   │   ├── package-lock.json
│   │   ├── package.json
│   │   ├── postcss.config.js
│   │   ├── tailwind.config.js
│   │   └── vite.config.js
│   ├── .gitignore
│   ├── INSTALLATION.md
│   └── README.md
└── uploads
