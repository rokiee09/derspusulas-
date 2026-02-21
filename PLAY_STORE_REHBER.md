# 🚀 Play Store Yayınlama Rehberi - DersPusulası

## 📋 Ön Hazırlık Kontrol Listesi

### ✅ Teknik Gereksinimler (Tamamlandı)
- [x] AAB dosyası oluşturuldu (`app/build/outputs/bundle/release/app-release.aab`)
- [x] Key store oluşturuldu ve güvenli yerde saklanıyor
- [x] `targetSdk` 35 (Play Store gereksinimi karşılandı)
- [x] `compileSdk` 35
- [x] `minSdk` 24 (Android 7.0+)
- [x] Uygulama imzalandı (signed)

### 📝 Hazırlanması Gerekenler

#### 1. Google Play Console Hesabı
- [ ] Google Play Console hesabı oluşturun: https://play.google.com/console
- [ ] Geliştirici kayıt ücreti ödeyin ($25, tek seferlik)
- [ ] Hesap bilgilerinizi tamamlayın

#### 2. Uygulama Bilgileri
- [ ] **Uygulama Adı**: DersPusulası (veya tercih ettiğiniz isim)
- [ ] **Kısa Açıklama** (80 karakter): "Öğrenciler için odak ve ders takip uygulaması. Offline çalışır, veri toplamaz."
- [ ] **Uzun Açıklama** (4000 karakter): Detaylı açıklama hazırlayın
- [ ] **Kategori**: Eğitim / Productivity
- [ ] **İçerik Derecelendirmesi**: PEGI 3 / Everyone

#### 3. Görseller (Zorunlu)
- [ ] **Uygulama İkonu**: 512x512 px (PNG, 32-bit)
- [ ] **Feature Graphic**: 1024x500 px (Play Store'da üstte görünen banner)
- [ ] **Ekran Görüntüleri**: 
  - En az 2 adet (telefon için)
  - Boyut: 16:9 veya 9:16 oranında
  - Minimum: 320px, Maksimum: 3840px
  - Önerilen: 1080x1920 px (portrait) veya 1920x1080 px (landscape)

#### 4. Gizlilik Politikası (Zorunlu)
- [ ] Gizlilik politikası URL'i hazırlayın (GitHub Pages, Google Sites, vb.)
- [ ] Uygulama veri toplamadığı için basit bir politika yeterli

#### 5. Diğer Bilgiler
- [ ] **Fiyatlandırma**: Ücretsiz
- [ ] **Hedef Kitle**: Öğrenciler, gençler
- [ ] **İletişim Bilgileri**: E-posta adresiniz

---

## 🎯 Adım Adım Yayınlama Süreci

### Adım 1: Google Play Console'a Giriş
1. https://play.google.com/console adresine gidin
2. Google hesabınızla giriş yapın
3. Geliştirici kaydınızı tamamlayın (ilk kez ise)

### Adım 2: Yeni Uygulama Oluşturma
1. Sol menüden **"Tüm uygulamalar"** → **"Uygulama oluştur"**
2. **Uygulama adı** girin: `DersPusulası`
3. **Varsayılan dil**: Türkçe
4. **Uygulama türü**: Uygulama
5. **Ücretsiz** veya **Ücretli** seçin (önerilen: Ücretsiz)
6. **Gizlilik politikası** URL'i ekleyin
7. **Oluştur** butonuna tıklayın

### Adım 3: Uygulama Bilgilerini Doldurma

#### 3.1 Store Listing (Mağaza Listesi)
1. Sol menüden **"Mağaza listesi"** sekmesine gidin
2. **Uygulama adı**: DersPusulası
3. **Kısa açıklama** (80 karakter):
   ```
   Öğrenciler için odak ve ders takip uygulaması. Offline çalışır, veri toplamaz.
   ```
4. **Tam açıklama** (4000 karakter):
   ```
   DersPusulası, öğrencilere yönelik tasarlanmış kapsamlı bir odak ve ders takip uygulamasıdır.

   🎯 Özellikler:
   • Pomodoro Tekniği: Odaklanma ve mola zamanlayıcısı ile verimli çalışma
   • Yapılacaklar Listesi: Görevlerinizi organize edin ve takip edin
   • Geri Sayım: Özelleştirilebilir geri sayım zamanlayıcısı
   • Kronometre: Tur (lap) özellikli profesyonel kronometre
   • Soru Takibi: Ders bazlı soru çözüm kaydı ve net hesaplama
   • Motivasyon Sözleri: 20 adet ilham verici motivasyon sözü

   🔒 Gizlilik ve Güvenlik:
   • Tamamen offline çalışır - internet bağlantısı gerektirmez
   • Veri toplamaz - hiçbir analitik veya reklam servisi kullanılmaz
   • Veriler sadece cihazınızda saklanır - gizliliğiniz korunur
   • İnternet izni yoktur - tamamen güvenli

   📚 Kimler İçin:
   • Sınavlara hazırlanan öğrenciler
   • Verimli çalışma teknikleri arayanlar
   • Ders takibi yapmak isteyenler
   • Odaklanma sorunu yaşayan öğrenciler

   DersPusulası ile çalışmalarınızı organize edin, hedeflerinize ulaşın!
   ```

5. **Görselleri yükleyin**:
   - Uygulama ikonu (512x512 px)
   - Feature graphic (1024x500 px)
   - En az 2 ekran görüntüsü

6. **Kategori**: Eğitim veya Verimlilik
7. **İletişim bilgileri**: E-posta adresiniz
8. **Kaydet** butonuna tıklayın

#### 3.2 İçerik Derecelendirmesi
1. Sol menüden **"İçerik derecelendirmesi"** sekmesine gidin
2. Anketi doldurun:
   - Şiddet içerik: Hayır
   - Cinsel içerik: Hayır
   - Küfür: Hayır
   - Uyuşturucu: Hayır
   - Korku: Hayır
   - Kumar: Hayır
3. Sonuç: **PEGI 3** veya **Everyone** olmalı
4. Derecelendirmeyi kaydedin

#### 3.3 Hedef Kitle ve İçerik
1. **Hedef kitle**: 13+ yaş (veya uygun yaş grubu)
2. **Eğitim içeriği**: Evet (uygulama eğitim amaçlı)
3. **Primetime TV**: Hayır (reklam yok)

### Adım 4: AAB Dosyasını Yükleme

1. Sol menüden **"Üretim"** (Production) sekmesine gidin
2. **"Yeni sürüm oluştur"** butonuna tıklayın
3. **"Uygulama paketini yükle"** bölümüne gidin
4. **AAB dosyasını seçin**: `app/build/outputs/bundle/release/app-release.aab`
5. Dosya yüklendikten sonra:
   - **Sürüm adı**: 1.0 (veya istediğiniz versiyon)
   - **Sürüm notları** (Türkçe):
     ```
     İlk sürüm yayınlandı!
     
     • Pomodoro zamanlayıcısı
     • Yapılacaklar listesi
     • Geri sayım
     • Kronometre
     • Soru takibi
     • Motivasyon sözleri
     ```
6. **Kaydet** butonuna tıklayın

### Adım 5: Son Kontroller ve Yayınlama

1. **"Tüm kontrolleri göster"** butonuna tıklayın
2. Tüm gereksinimlerin tamamlandığını kontrol edin:
   - ✅ Store listing tamamlandı
   - ✅ İçerik derecelendirmesi tamamlandı
   - ✅ Hedef kitle ve içerik ayarlandı
   - ✅ Uygulama paketi yüklendi
   - ✅ Gizlilik politikası eklendi
   - ✅ Uygulama ikonu yüklendi
   - ✅ Ekran görüntüleri yüklendi

3. **"Gözden geçirme için gönder"** butonuna tıklayın
4. Google incelemesi genellikle **1-3 gün** sürer
5. Onaylandıktan sonra uygulama otomatik olarak yayınlanır

---

## 📝 Gizlilik Politikası Örneği

Uygulama veri toplamadığı için basit bir gizlilik politikası yeterlidir. GitHub Pages, Google Sites veya herhangi bir web hosting servisinde yayınlayabilirsiniz.

**Örnek Gizlilik Politikası:**

```
Gizlilik Politikası - DersPusulası

Son Güncelleme: [Tarih]

DersPusulası ("Biz", "Bizim", "Uygulama"), kullanıcı gizliliğine büyük önem verir.

VERİ TOPLAMA
DersPusulası hiçbir kişisel veri toplamaz, saklamaz veya paylaşmaz. Uygulama tamamen offline çalışır ve internet bağlantısı gerektirmez.

YEREL VERİLER
Uygulama içinde oluşturduğunuz tüm veriler (görevler, soru kayıtları, ayarlar) sadece cihazınızda saklanır. Bu veriler hiçbir zaman sunuculara gönderilmez veya üçüncü taraflarla paylaşılmaz.

ÜÇÜNCÜ TARAFLAR
Uygulama hiçbir üçüncü taraf servisi (analitik, reklam, sosyal medya) kullanmaz.

İZİNLER
Uygulama sadece bildirim ve titreşim izinleri kullanır. İnternet erişim izni yoktur.

İLETİŞİM
Sorularınız için: [E-posta adresiniz]

Bu gizlilik politikası değişiklik gösterebilir. Önemli değişikliklerde kullanıcılar bilgilendirilecektir.
```

---

## 🎨 Görsel Gereksinimleri Detaylı

### Uygulama İkonu
- **Boyut**: 512x512 px
- **Format**: PNG (32-bit)
- **Arka plan**: Şeffaf veya düz renk
- **Köşeler**: Yuvarlatılmış (Play Store otomatik yapar)

### Feature Graphic
- **Boyut**: 1024x500 px
- **Format**: PNG veya JPG
- **İçerik**: Uygulama adı ve temel özellikler

### Ekran Görüntüleri
- **Minimum**: 2 adet
- **Önerilen**: 4-8 adet
- **Boyut**: 
  - Portrait: 1080x1920 px (önerilen)
  - Landscape: 1920x1080 px
- **Format**: PNG veya JPG
- **İçerik**: Uygulamanın farklı ekranları

---

## ⚠️ Önemli Notlar

1. **İnceleme Süresi**: Google incelemesi genellikle 1-3 gün sürer, bazen daha uzun olabilir
2. **Güncellemeler**: Her güncellemede yeni AAB yüklemeniz gerekir
3. **Version Code**: Her yeni sürümde `versionCode`'u artırın (1, 2, 3...)
4. **Version Name**: Kullanıcıya görünen versiyon (1.0, 1.1, 2.0...)
5. **Key Store**: Key store dosyasını ve şifrelerini mutlaka güvenli yerde saklayın
6. **Test**: Yayınlamadan önce uygulamayı test edin

---

## 🔄 Güncelleme Süreci

Uygulamayı güncellemek için:

1. `app/build.gradle` dosyasında `versionCode` ve `versionName`'i artırın
2. Yeni AAB oluşturun: `gradlew.bat bundleRelease`
3. Play Console'da **"Yeni sürüm oluştur"** → AAB yükleyin
4. Sürüm notlarını güncelleyin
5. Gözden geçirme için gönderin

---

## 📞 Yardım ve Destek

- Google Play Console Yardım: https://support.google.com/googleplay/android-developer
- Play Console Dokümantasyon: https://developer.android.com/distribute/googleplay

---

**Başarılar! 🎉**
