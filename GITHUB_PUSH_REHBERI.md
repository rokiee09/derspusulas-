# 🚀 GitHub'a Push Etme Rehberi

## Adım Adım Talimatlar

### 1. GitHub'da Repository Oluşturun
1. https://github.com/new adresine gidin
2. Repository adı: `derspusulasi` (veya istediğiniz isim)
3. Public veya Private seçin
4. "Add a README file" seçmeyin
5. "Create repository" tıklayın

### 2. Terminal/Command Prompt'ta Komutları Çalıştırın

Proje klasörünüze gidin:
```bash
cd "C:\Users\burak\OneDrive\Masaüstü\derspusulası"
```

#### Git Repository Başlatın (eğer yoksa):
```bash
git init
```

#### Tüm Dosyaları Ekleyin:
```bash
git add .
```

#### İlk Commit:
```bash
git commit -m "Initial commit: DersPusulası Android app"
```

#### Branch'i main olarak ayarlayın:
```bash
git branch -M main
```

#### GitHub Repository'nizi Remote Olarak Ekleyin:
```bash
git remote add origin https://github.com/rokiee09/derspusulasi.git
```
**Not:** `rokiee09` yerine kendi GitHub kullanıcı adınızı, `derspusulasi` yerine oluşturduğunuz repository adını yazın.

#### Push Edin:
```bash
git push -u origin main
```

### 3. GitHub Kimlik Doğrulama

İlk push'ta GitHub kullanıcı adı ve şifre isteyebilir. Eğer 2FA (iki faktörlü doğrulama) aktifse, Personal Access Token kullanmanız gerekebilir.

#### Personal Access Token Oluşturma:
1. GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. "Generate new token" → "Generate new token (classic)"
3. Note: "DersPusulasi Push"
4. Expiration: İstediğiniz süre
5. Scopes: `repo` seçin
6. "Generate token" tıklayın
7. Token'ı kopyalayın (bir daha gösterilmeyecek!)

Push sırasında şifre yerine bu token'ı kullanın.

---

## ⚠️ ÖNEMLİ NOTLAR

### Key Store Dosyası
`.gitignore` dosyasında `*.jks` zaten var, bu yüzden key store dosyanız GitHub'a yüklenmeyecek. Bu güvenlik için iyi!

### Yüklenmeyecek Dosyalar (.gitignore sayesinde):
- `*.jks` (key store dosyaları)
- `*.apk`, `*.aab` (build dosyaları)
- `build/` klasörü
- `.gradle/` klasörü
- `local.properties` (SDK yolları)

### Yüklenecek Dosyalar:
- ✅ Tüm kaynak kodlar (`app/src/`)
- ✅ `build.gradle` dosyaları
- ✅ `AndroidManifest.xml`
- ✅ `README.md`
- ✅ Resim dosyaları (`app/src/main/res/`)
- ✅ Diğer proje dosyaları

---

## 🔄 Güncellemeler İçin

Daha sonra değişiklik yaptığınızda:

```bash
git add .
git commit -m "Açıklayıcı commit mesajı"
git push
```

---

## 📝 Alternatif: Android Studio'dan Push

1. Android Studio'da **VCS** → **Import into Version Control** → **Create Git Repository**
2. Proje klasörünü seçin
3. **VCS** → **Git** → **Add**
4. **VCS** → **Commit**
5. **VCS** → **Git** → **Push**
6. GitHub repository URL'ini ekleyin

---

## ✅ Kontrol

Push başarılı olduktan sonra:
- https://github.com/rokiee09/derspusulasi adresine gidin
- Tüm dosyaların göründüğünü kontrol edin

---

**Başarılar! 🎉**
