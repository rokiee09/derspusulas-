package com.derspusulasi.data

object MotivationQuotes {
    val quotes = listOf(
        "Bugün 1 adım, yarın 10 net.",
        "Odaklan: Gelecek \"şimdi\" yazılıyor.",
        "Başlamak, bitirmenin yarısıdır.",
        "Netler tesadüf değil, birikimdir.",
        "25 dakika sabır, 1 ömür avantaj.",
        "Yorulmak normal, vazgeçmek değil.",
        "Küçük tekrarlar büyük sonuç doğurur.",
        "Her soru bir basamak. Çıkıyorsun.",
        "Bugün çalış, yarın rahat et.",
        "Başarı, disiplinin sessiz ödülüdür.",
        "Hedefe giden yol: bugün.",
        "Zor olan, gelişimin işaretidir.",
        "Bir pomodoro daha: fark orada.",
        "Net artışı, moral artışı demektir.",
        "Kendi rekorunu kır: dünkü sen rakibin.",
        "Kısa mola, uzun zafer.",
        "Yapabildiğin kadar değil; sürdürebildiğin kadar.",
        "Bugün 0 değil, 1 olsun. Yeter.",
        "Sınav bir gün; emek her gün.",
        "Düzen kur, motivasyon sonra gelir."
    )

    fun getRandomQuote(): String {
        return quotes.random()
    }
}
