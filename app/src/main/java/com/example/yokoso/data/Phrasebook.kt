package com.example.yokoso.data

data class Phrase(val ru: String, val en: String, val jp: String, val romaji: String)

data class PhraseGroup(val ru: String, val en: String, val phrases: List<Phrase>)

// фразы сгруппированы по ситуациям
val PHRASEBOOK = listOf(
    PhraseGroup(
        "Основное", "Basics", listOf(
            Phrase("Здравствуйте", "Hello", "こんにちは", "Konnichiwa"),
            Phrase("Спасибо", "Thank you", "ありがとうございます", "Arigatō gozaimasu"),
            Phrase("Извините", "Excuse me / Sorry", "すみません", "Sumimasen"),
            Phrase("Вы говорите по-английски?", "Do you speak English?", "英語を話せますか", "Eigo o hanasemasu ka"),
            Phrase("Где туалет?", "Where is the toilet?", "トイレはどこですか", "Toire wa doko desu ka")
        )
    ),
    PhraseGroup(
        "В кафе и ресторане", "At a café or restaurant", listOf(
            Phrase("Меню, пожалуйста", "Menu, please", "メニューをお願いします", "Menyū o onegai shimasu"),
            Phrase("Что вы порекомендуете?", "What do you recommend?", "おすすめは何ですか", "Osusume wa nan desu ka"),
            Phrase("Воду, пожалуйста", "Water, please", "お水をください", "Omizu o kudasai"),
            Phrase("У меня аллергия на орехи", "I am allergic to nuts", "ナッツアレルギーがあります", "Nattsu arerugī ga arimasu"),
            Phrase("Очень вкусно!", "It is delicious!", "とても美味しいです", "Totemo oishii desu"),
            Phrase("Счёт, пожалуйста", "The check, please", "お会計をお願いします", "Okaikei o onegai shimasu")
        )
    ),
    PhraseGroup(
        "В транспорте", "Transport", listOf(
            Phrase("Где станция?", "Where is the station?", "駅はどこですか", "Eki wa doko desu ka"),
            Phrase("Один билет до Киото", "One ticket to Kyoto", "京都まで一枚お願いします", "Kyōto made ichimai onegai shimasu"),
            Phrase("С какой платформы отправляется поезд?", "Which platform does the train leave from?", "何番線ですか", "Nan-ban-sen desu ka"),
            Phrase("Сколько стоит проезд?", "How much is the fare?", "運賃はいくらですか", "Unchin wa ikura desu ka"),
            Phrase("Остановите здесь, пожалуйста", "Please stop here", "ここで止めてください", "Koko de tomete kudasai")
        )
    ),
    PhraseGroup(
        "В отеле", "At the hotel", listOf(
            Phrase("У меня есть бронь", "I have a reservation", "予約しています", "Yoyaku shite imasu"),
            Phrase("Во сколько завтрак?", "What time is breakfast?", "朝食は何時ですか", "Chōshoku wa nanji desu ka"),
            Phrase("Можно оставить багаж?", "Can I leave my luggage?", "荷物を預けてもいいですか", "Nimotsu o azukete mo ii desu ka"),
            Phrase("Есть ли здесь Wi-Fi?", "Is there Wi-Fi?", "Wi-Fiはありますか", "Wi-Fi wa arimasu ka")
        )
    ),
    PhraseGroup(
        "Экстренные случаи", "Emergencies", listOf(
            Phrase("Помогите!", "Help!", "助けてください", "Tasukete kudasai"),
            Phrase("Мне нужен врач", "I need a doctor", "医者が必要です", "Isha ga hitsuyō desu"),
            Phrase("Вызовите скорую помощь", "Call an ambulance", "救急車を呼んでください", "Kyūkyūsha o yonde kudasai"),
            Phrase("Я потерял паспорт", "I lost my passport", "パスポートをなくしました", "Pasupōto o nakushimashita")
        )
    )
)