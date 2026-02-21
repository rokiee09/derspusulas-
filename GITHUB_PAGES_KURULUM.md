# 🔧 GitHub Pages Kurulum - Adım Adım

## ✅ Kontrol Listesi

Repository oluştururken şunlara dikkat edin:

### 1. Repository Oluşturma
- ✅ Repository adı: `derspusulasi-privacy` (tam olarak bu isim)
- ✅ Public olmalı (Private repository'lerde GitHub Pages ücretli)
- ✅ README dosyası eklemeyin (boş repository oluşturun)

### 2. Dosya Yükleme
- ✅ Dosya adı: `privacy-policy.html` (tam olarak bu isim)
- ✅ Dosya repository'nin kök dizininde olmalı (alt klasörde değil)

### 3. GitHub Pages Ayarları
- ✅ Settings → Pages → Source: `main` branch
- ✅ Folder: `/ (root)` seçilmeli

---

## 📝 Detaylı Adımlar

### Adım 1: Repository Oluşturma
1. https://github.com/new adresine gidin
2. **Owner**: `rokiee09` seçili olmalı
3. **Repository name**: `derspusulasi-privacy` (tam olarak)
4. **Description**: (opsiyonel) "DersPusulası Gizlilik Politikası"
5. **Public** seçin (önemli!)
6. **Add a README file** işaretini KALDIRIN
7. **Add .gitignore** seçmeyin
8. **Choose a license** seçmeyin
9. **Create repository** tıklayın

### Adım 2: Dosyayı Yükleme
1. Repository sayfasında **"uploading an existing file"** linkine tıklayın
   VEYA
   **"Add file"** → **"Upload files"** tıklayın
2. `privacy-policy.html` dosyasını sürükleyip bırakın
3. **Commit changes** butonuna tıklayın
4. **"Commit directly to the main branch"** seçili olsun
5. **"Commit changes"** tıklayın

### Adım 3: GitHub Pages Aktifleştirme
1. Repository sayfasında üst menüden **"Settings"** sekmesine gidin
2. Sol menüden **"Pages"** seçin
3. **"Source"** bölümünde:
   - **"Deploy from a branch"** seçin
   - **Branch**: `main` seçin
   - **Folder**: `/ (root)` seçin
4. **"Save"** butonuna tıklayın
5. Birkaç dakika bekleyin (genellikle 1-2 dakika)

### Adım 4: URL Kontrolü
URL şu formatta olmalı:
```
https://rokiee09.github.io/derspusulasi-privacy/privacy-policy.html
```

**Not:** İlk yayınlama 1-5 dakika sürebilir.

---

## 🔍 Sorun Giderme

### Sorun: "404 Not Found" hatası
**Çözüm:**
- Repository adını kontrol edin: `derspusulasi-privacy` (tam olarak)
- Dosya adını kontrol edin: `privacy-policy.html` (tam olarak)
- GitHub Pages'in aktif olduğundan emin olun (Settings → Pages)

### Sorun: Repository bulunamıyor
**Çözüm:**
- GitHub'da `rokiee09` kullanıcı adıyla giriş yaptığınızdan emin olun
- Repository'nin Public olduğundan emin olun

### Sorun: GitHub Pages aktif değil
**Çözüm:**
1. Settings → Pages'e gidin
2. Source'u `main` branch olarak ayarlayın
3. Save'e tıklayın
4. Birkaç dakika bekleyin

### Sorun: Dosya görünmüyor
**Çözüm:**
- Dosyanın repository'nin kök dizininde olduğundan emin olun
- Dosya adının `privacy-policy.html` olduğundan emin olun
- Commit yaptığınızdan emin olun

---

## 🚀 Alternatif: index.html Kullanımı

Eğer yukarıdaki yöntem çalışmazsa, dosyayı `index.html` olarak adlandırın:

1. `privacy-policy.html` dosyasını `index.html` olarak yeniden adlandırın
2. Repository'ye `index.html` olarak yükleyin
3. URL şu şekilde olacak:
```
https://rokiee09.github.io/derspusulasi-privacy/
```

Bu daha kısa ve temiz bir URL'dir.

---

## ✅ Başarı Kontrolü

URL çalışıyorsa şunları görmelisiniz:
- ✅ "Gizlilik Politikası - DersPusulası" başlığı
- ✅ Mavi renkli başlıklar
- ✅ Tüm içerik düzgün görünüyor
- ✅ Mobilde de düzgün görünüyor

---

## 📞 Yardım

Hala çalışmıyorsa:
1. Repository URL'ini paylaşın: `https://github.com/rokiee09/derspusulasi-privacy`
2. Hata mesajını kontrol edin
3. GitHub Pages durumunu kontrol edin (Settings → Pages)
