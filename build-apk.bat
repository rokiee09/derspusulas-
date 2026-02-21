@echo off
echo DersPusulasi APK Build Script
echo =============================
echo.

REM Gradle wrapper ile debug APK oluştur
echo Building APK...
call gradlew.bat assembleDebug

if %ERRORLEVEL% EQU 0 (
    echo.
    echo =============================
    echo BUILD BASARILI!
    echo =============================
    echo.
    echo APK dosyasi: app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo Bu dosyayi telefonunuza aktarip yukleyebilirsiniz.
    echo.
    pause
) else (
    echo.
    echo =============================
    echo BUILD HATASI!
    echo =============================
    echo.
    echo Hata mesajini kontrol edin.
    echo.
    pause
)
