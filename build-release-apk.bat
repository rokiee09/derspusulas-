@echo off
echo DersPusulasi RELEASE APK Build Script
echo ======================================
echo.
echo NOT: Release APK icin signing key store gereklidir!
echo Key store olusturmak icin:
echo   keytool -genkey -v -keystore derspusulasi-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias derspusulasi
echo.
echo Key store dosyasini proje kok klasorune koyun ve build.gradle'da signingConfigs'i aktif edin.
echo.
pause
echo.
echo Building RELEASE APK...
call gradlew.bat assembleRelease

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ======================================
    echo BUILD BASARILI!
    echo ======================================
    echo.
    echo APK dosyasi: app\build\outputs\apk\release\app-release.apk
    echo.
    echo Bu APK'yi Play Store'a yukleyebilirsiniz.
    echo.
    pause
) else (
    echo.
    echo ======================================
    echo BUILD HATASI!
    echo ======================================
    echo.
    echo Hata mesajini kontrol edin.
    echo Eger signing hatasi aliyorsaniz, build.gradle'da signingConfigs'i yapilandirin.
    echo.
    pause
)
