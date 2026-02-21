# 🌐 Gizlilik Politikası URL'si Oluşturma Rehberi

GitHub Pages kullanarak ücretsiz gizlilik politikası URL'si oluşturabilirsiniz. İşte adım adım rehber:

## Yöntem 1: GitHub Pages (Önerilen - Ücretsiz)

### Adım 1: GitHub Hesabı Oluşturun
1. https://github.com adresine gidin
2. "Sign up" ile ücretsiz hesap oluşturun

### Adım 2: Yeni Repository Oluşturun
1. GitHub'da sağ üstten **"+"** → **"New repository"** tıklayın
2. Repository adı: `derspusulasi-privacy` (veya istediğiniz isim)
3. **Public** seçin (ücretsiz GitHub Pages için gerekli)
4. **"Create repository"** tıklayın

### Adım 3: HTML Dosyasını Yükleyin
1. Oluşturduğunuz repository'de **"Add file"** → **"Upload files"** tıklayın
2. `privacy-policy.html` dosyasını sürükleyip bırakın
3. Dosyayı `index.html` olarak yeniden adlandırın (veya `privacy-policy.html` olarak bırakın)
4. **"Commit changes"** tıklayın

### Adım 4: GitHub Pages'i Aktifleştirin
1. Repository sayfasında **"Settings"** sekmesine gidin
2. Sol menüden **"Pages"** seçin
3. **Source** altında **"Deploy from a branch"** seçin
4. **Branch**: `main` (veya `master`)
5. **Folder**: `/ (root)`
6. **Save** tıklayın

### Adım 5: URL'inizi Alın
Birkaç dakika sonra URL hazır olacak:
```
https://[kullanıcı-adınız].github.io/derspusulasi-privacy/
```
veya eğer dosyayı `index.html` olarak yüklediyseniz:
```
https://[kullanıcı-adınız].github.io/derspusulasi-privacy/index.html
```

**Örnek:** `https://burak.github.io/derspusulasi-privacy/privacy-policy.html`

---

## Yöntem 2: Google Sites (Alternatif - Ücretsiz)

### Adım 1: Google Sites Oluşturun
1. https://sites.google.com adresine gidin
2. Google hesabınızla giriş yapın
3. **"Boş"** şablon seçin

### Adım 2: İçeriği Ekleyin
1. `GIZLILIK_POLITIKASI.md` dosyasındaki içeriği kopyalayın
2. Google Sites editörüne yapıştırın
3. Başlık: "Gizlilik Politikası - DersPusulası"

### Adım 3: Yayınlayın
1. Sağ üstten **"Yayınla"** butonuna tıklayın
2. Site adresi: `derspusulasi-privacy` (veya istediğiniz isim)
3. **"Yayınla"** tıklayın

### Adım 4: URL'inizi Alın
URL şu formatta olacak:
```
https://sites.google.com/view/derspusulasi-privacy
```

---

## Yöntem 3: Netlify Drop (En Kolay - Ücretsiz)

### Adım 1: Netlify Drop'a Gidin
1. https://app.netlify.com/drop adresine gidin
2. Hesap oluşturun (ücretsiz)

### Adım 2: Dosyayı Yükleyin
1. `privacy-policy.html` dosyasını sürükleyip bırakın
2. Otomatik olarak yayınlanır

### Adım 3: URL'inizi Alın
URL şu formatta olacak:
```
https://[rastgele-isim].netlify.app/privacy-policy.html
```

---

## 📝 Önemli Notlar

### HTML Dosyasını Düzenleme
`privacy-policy.html` dosyasını açıp şu bilgileri güncelleyin:
1. **Son Güncelleme Tarihi**: `[Tarih buraya]` kısmını bugünün tarihi ile değiştirin
2. **E-posta Adresi**: `[E-posta adresinizi buraya ekleyin]` kısmını gerçek e-posta adresiniz ile değiştirin

### Örnek Güncelleme:
```html
<p class="last-updated"><strong>Son Güncelleme:</strong> 8 Şubat 2026</p>
...
<p><strong>E-posta:</strong> info@derspusulasi.com</p>
```

---

## ✅ Play Console'da Kullanım

1. Google Play Console'a gidin
2. Uygulamanızı seçin
3. **"Mağaza listesi"** → **"Gizlilik politikası"** bölümüne gidin
4. Oluşturduğunuz URL'i yapıştırın
5. Kaydedin

---

## 🔗 Hızlı Başlangıç (GitHub Pages)

1. GitHub'da yeni repository oluşturun: `derspusulasi-privacy`
2. `privacy-policy.html` dosyasını yükleyin
3. Settings → Pages → Source: `main` branch seçin
4. URL'iniz hazır: `https://[kullanıcı-adınız].github.io/derspusulasi-privacy/privacy-policy.html`

---

## 📧 İletişim Bilgileri

Gizlilik politikası sayfasında mutlaka bir iletişim e-posta adresi ekleyin. Play Store gereksinimidir.

---

**Başarılar! 🎉**
