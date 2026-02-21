@echo off
echo ========================================
echo DersPusulasi - GitHub'a Push Etme
echo ========================================
echo.

echo [1/6] Git repository kontrol ediliyor...
if not exist .git (
    echo Git repository bulunamadi, baslatiliyor...
    git init
) else (
    echo Git repository zaten mevcut.
)
echo.

echo [2/6] Dosyalar ekleniyor...
git add .
echo.

echo [3/6] Commit yapiliyor...
git commit -m "Initial commit: DersPusulasi Android app"
echo.

echo [4/6] Branch main olarak ayarlaniyor...
git branch -M main
echo.

echo [5/6] Remote repository ekleniyor...
git remote remove origin 2>nul
git remote add origin https://github.com/rokiee09/derspusulas-.git
echo.

echo [6/6] GitHub'a push ediliyor...
echo.
echo NOT: GitHub kullanici adi ve sifre (veya Personal Access Token) istenebilir.
echo.
git push -u origin main

echo.
echo ========================================
if %ERRORLEVEL% EQU 0 (
    echo BASARILI! Proje GitHub'a yuklendi.
    echo.
    echo Repository URL: https://github.com/rokiee09/derspusulas-
) else (
    echo HATA! Push basarisiz oldu.
    echo.
    echo Kontrol edin:
    echo 1. GitHub'da repository olusturuldu mu?
    echo 2. GitHub kullanici adi ve sifre dogru mu?
    echo 3. Internet baglantisi var mi?
)
echo ========================================
echo.
pause
