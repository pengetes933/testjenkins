# Tixfest App
## Kontrak JSON
### Mobile Kontrak JSON
#### Auth

- Register Req `POST /auth/register`
    ```json
    {
        "email": "example@email.com",
        "password": "********"
    }
    ```
- Register Res
    ```json
    {
        "status": 200,
        "message": "Oke",
        "data": {
            "id": "asd879a9s8d7a9s7dasd798",
            "email": "example@email.com"
        } // or null
    }
    ```


- Login Req `POST /auth/login`
    ```json
    {
        "email": "example@email.com",
        "password": "**********"
    }
    ```
- Login Res
    ```json
    {
        "status": 200,
        "message": "Success response message",
        "data": {
            "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            "expiresIn": 86400,  // 1 hari dalam detik
            "refreshToken": "def50200e1b2...",
            "refreshExpiresIn": 604800  // 1 minggu dalam detik
        }
    }
    ```

- Refresh Token Req `POST /auth/refresh-token-mobile`
    ```json
    {
        "refreshToken": "def50200e1b2sdf99df9sd9ff9sdf9sd9f"
    }
    ```
- Refresh Token Res
    ```json
    {
        "status": 200,
        "message": "Success response message",
        "data": {
            "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            "expiresIn": 86400,  // 1 hari dalam detik
            "refreshToken": "def50200e1b2...",
            "refreshExpiresIn": 604800  // 1 minggu dalam detik
        }
    }
    ```
  
- Logout Req `POST /auth/logout` with Authorization Barear
    ```json
    {
        null
    } 
    ``` 
- Logout Res
    ```json
    {
        "status": 200,
        "message": "Success log out",
        "data": null
    }
    ```
  
- Forgot Password Req `POST /auth/forgot-password`
    ```json
    {
        "email": "example@email.com"
    }
    ```
- Forgot Password Res
    ```json
    {
        "status": 200,
        "message": "Password reset email sent"
    }
    ```
  
- Reset Password Req `/auth/reset-password`
    ```json
    {
        "token": "reset-token",
        "newPassword": "**********" // Regex untuk password
    }
    ```
- Reset Password Res
    ```json
    {
        "status": 200,
        "message": "Password reset successfully"
    }
    ```