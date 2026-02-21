@echo off
echo DersPusulasi Key Store Olusturma
echo =================================
echo.
echo Key store olusturuluyor...
echo.

keytool -genkey -v -keystore derspusulasi-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias derspusulasi

if %ERRORLEVEL% EQU 0 (
    echo.
    echo =================================
    echo KEY STORE BASARIYLA OLUSTURULDU!
    echo =================================
    echo.
    echo Dosya: derspusulasi-release.jks
    echo.
    echo ONEMLI: Bu dosyayi ve sifreleri guvenli bir yerde saklayin!
    echo Key store olmadan uygulamayi guncelleyemezsiniz.
    echo.
    pause
) else (
    echo.
    echo =================================
    echo HATA!
    echo =================================
    echo.
    echo Key store olusturulamadi.
    echo.
    pause
)
