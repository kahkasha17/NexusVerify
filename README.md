# NEXUS VERIFY
### A Rule-Based Digital Content Verification System

NEXUS VERIFY is a web-based application designed to analyze digital content and identify potentially suspicious information using rule-based verification techniques. It helps users assess the risk associated with news content, SMS/email messages, and URLs.

## Features

- **Fake News Detection:** Analyzes news content using suspicious keywords and predefined rules.
- **Spam SMS/Email Detection:** Identifies potentially fraudulent or spam messages.
- **Fake Link Detection:** Examines URLs for suspicious patterns, shortened links, IP-based URLs, and other risk indicators.
- **Risk Score:** Generates a risk score between 0 and 100.
- **Risk Classification:** Categorizes results into Low, Medium, and High risk.
- **Verification History:** Stores previous verification records.
- **Interactive Dashboard:** Provides a simple interface for submitting content and viewing results.

## Technology Stack

### Frontend
- React.js
- Vite
- HTML5
- CSS3
- JavaScript

### Backend
- Java
- Java HTTP Server
- REST API

### Development Tools
- Visual Studio Code
- JDK
- Node.js and npm
- Git and GitHub

## Project Structure

```text
NexusVerifiy/
│
├── backend/
│   ├── src/
│   │   └── com/
│   │       └── nexusverify/
│   │           ├── api/
│   │           │   └── ApiServer.java
│   │           ├── checker/
│   │           │   ├── FakeNewsChecker.java
│   │           │   ├── SpamChecker.java
│   │           │   └── LinkChecker.java
│   │           ├── HistoryManager.java
│   │           ├── RiskCalculator.java
│   │           ├── InputValidator.java
│   │           └── ...
│   ├── bin/
│   └── data/
│
├── frontend/
│   ├── src/
│   │   ├── services/
│   │   │   └── api.js
│   │   ├── App.jsx
│   │   ├── App.css
│   │   └── index.css
│   ├── package.json
│   └── ...
│
└── README.md
```

## Risk Assessment

The application calculates a risk score based on predefined rules and suspicious patterns.

| Risk Score | Risk Level |
|---|---|
| 0–29 | Low Risk |
| 30–59 | Medium Risk |
| 60–100 | High Risk |

The final score is limited to a maximum of 100.

**Note:** The risk score represents rule-based assessment, not the statistical probability that content is fake, fraudulent, or malicious.

## Installation and Setup

### Prerequisites

Make sure the following software is installed:

- Java JDK
- Node.js and npm
- Visual Studio Code (recommended)

### 1. Clone the Repository

```bash
git clone https://github.com/kahkasha17/NexusVerify.git
cd NexusVerifiy
```

### 2. Start the Backend

Open a terminal and execute:

```bash
cd backend
java -cp bin com.nexusverify.api.ApiServer
```

The backend API will start on:

```text
http://localhost:8080
```

### 3. Start the Frontend

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Open the local URL displayed by Vite, usually:

```text
http://localhost:5173
```

**Important:** Keep both backend and frontend terminals running while using the application locally.

## API Endpoints

The backend provides the following REST API endpoints:

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/verify` | Verifies submitted content |
| GET | `/api/history` | Retrieves verification history |
| DELETE | `/api/history` | Clears verification history |

### Verification Request Example

```json
{
  "type": "news",
  "input": "Breaking news: Share this information immediately!"
}
```

Supported verification types:

- `news`
- `spam`
- `link`

## Working Methodology

1. The user selects a verification category.
2. The user submits news text, an SMS/email message, or a URL.
3. The frontend sends the input to the Java backend through a REST API.
4. The backend validates and analyzes the submitted content.
5. Predefined rules and suspicious patterns are used to calculate the risk score.
6. The result is classified as Low, Medium, or High Risk.
7. The frontend displays the verification result.
8. The verification record is stored in the history.

## Data Storage

Verification history is stored in a text file:

```text
backend/data/history.txt
```

The file is used to maintain previously submitted verification records.

## Limitations

- The system uses predefined rules and keyword-based analysis.
- It does not use a trained machine learning or deep learning model.
- It does not independently verify information against trusted news sources.
- A low-risk result does not guarantee that content is authentic or safe.
- A high-risk result indicates suspicious patterns and does not prove malicious intent.

## Future Enhancements

- Integration with trusted fact-checking APIs.
- Machine learning-based content classification.
- Advanced URL and domain reputation analysis.
- Database integration for persistent history management.
- User authentication and personalized verification history.
- Cloud deployment for public access.

## Author

**Kahkasha Khan**

GitHub: [@Kahkasha17](https://github.com/Kahkasha17)

Developed as an academic project using Java, React, and rule-based analysis.

## Contributions

Contributions are welcome! If you would like to contribute to NEXUS VERIFY, follow these steps:

1. Fork this repository.
2. Clone your forked repository:

   ```bash
   git clone https://github.com/kahkasha17/NexusVerify.git
