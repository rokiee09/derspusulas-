# DersPusulası 📚

Öğrencilere yönelik, görseli güçlü, offline çalışan, veri toplamayan "odak + ders takip" uygulaması.

## 🚀 Hızlı Başlangıç

### Yöntem 1: Android Studio ile
1. Android Studio'yu açın
2. "Open an Existing Project" seçin
3. Bu klasörü seçin
4. Gradle sync tamamlanana kadar bekleyin
5. Run butonuna tıklayın

### Yöntem 2: Komut Satırından APK Oluşturma
```bash
# Windows
gradlew.bat assembleDebug

# Linux/Mac
./gradlew assembleDebug
```

APK dosyası: `app/build/outputs/apk/debug/app-debug.apk`

### Yöntem 3: Batch Script ile
Windows'ta `build-apk.bat` dosyasını çift tıklayın (Debug APK için).

## 🚀 Production Release APK Oluşturma (Play Store için)

### 1. Release Key Store Oluşturma
```bash
keytool -genkey -v -keystore derspusulasi-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias derspusulasi
```

### 2. Release APK Oluşturma
```bash
# Windows
gradlew.bat assembleRelease

# Linux/Mac
./gradlew assembleRelease
```

**VEYA** Windows'ta `build-release-apk.bat` dosyasını çift tıklayın.

APK dosyası: `app/build/outputs/apk/release/app-release.apk`

### 3. Play Store'a Yükleme
1. Google Play Console'a giriş yapın
2. Yeni uygulama oluşturun
3. Release APK'yi yükleyin
4. Store listing bilgilerini doldurun
5. Yayınlayın

**ÖNEMLİ**: Release APK için signing key store gereklidir. Key store dosyasını güvenli bir yerde saklayın!

## 📱 Özellikler

- ✅ **Pomodoro**: Odak ve mola zamanlayıcısı
- ✅ **Yapılacaklar**: Görev listesi yönetimi
- ✅ **Geri Sayım**: Özelleştirilebilir geri sayım
- ✅ **Kronometre**: Tur (lap) özellikli kronometre
- ✅ **Soru Takibi**: Ders bazlı soru çözüm kaydı ve net hesaplama
- ✅ **Motivasyon Sözleri**: 20 adet motivasyon sözü
- ✅ **Offline Çalışma**: İnternet bağlantısı gerektirmez
- ✅ **Veri Gizliliği**: Veriler sadece cihazda saklanır

## 🔧 Gereksinimler

- **compileSdk**: 35 (Android 15)
- **minSdk**: 35 (Android 15)
- **targetSdk**: 35 (Android 15)

**Production**: minSdk 35 (Android 15+) - Play Store için hazır.

## 📦 Teknolojiler

- Kotlin
- Jetpack Compose
- Material3
- MVVM Architecture
- StateFlow
- Kotlinx Serialization (JSON)
- Local File Storage (NO Database)

## 📝 Notlar

- Uygulama tamamen offline çalışır
- İnternet izni yoktur
- Analytics/Ads/Tracking yoktur
- Veriler cihaz içinde JSON dosyalarında saklanır

## 🐛 Sorun Giderme

### Gradle Sync Hatası
```
File → Invalidate Caches → Invalidate and Restart
```

### Build Hatası
```
Build → Clean Project
Build → Rebuild Project
```

### SDK Hatası
Android SDK 35 yüklü olmalı. Android Studio → SDK Manager'dan kontrol edin.

## 📄 Lisans

Bu proje öğrenciler için ücretsizdir.
