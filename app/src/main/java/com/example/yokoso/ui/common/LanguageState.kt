package com.example.yokoso.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.content.Context
import android.content.SharedPreferences

object LanguageState {
    private var prefs: SharedPreferences? = null

    var current by mutableStateOf("ru")
        private set

    // вызывается один раз при старте приложения: читает сохранённый язык
    fun init(context: Context) {
        prefs = context.getSharedPreferences("yokoso", Context.MODE_PRIVATE)
        current = prefs?.getString("lang", "ru") ?: "ru"
    }

    // меняет язык и запоминает выбор
    fun set(lang: String) {
        current = lang
        prefs?.edit()?.putString("lang", lang)?.apply()
    }
}

enum class Str {
    Search, ChipTemples, ChipFood, ChipMuseums, ChipParks, Open,
    Tagline, TabLogin, TabRegister, FieldName, FieldEmail, FieldPassword,
    BtnLogin, BtnRegister, Forgot, Or, ContinueGoogle, NoAccount, SignUp,
    NavHome, NavRoute, NavFav, NavProfile, NoFavorites, NoRoute, AddToRoute, InRoute,
    Settings, InterfaceLang, Notifications, Logout, NothingFound, NothingFoundHint, ResetFilters,
    WriteReview, RatePlace, CommentLabel, CommentHint, PickRating, PublishReview, EventsTitle,
    NoEvents, PhrasebookTitle
}

private val ruDict = mapOf(
    Str.Search to "Поиск мест...",
    Str.ChipTemples to "Храмы", Str.ChipFood to "Рестораны",
    Str.ChipMuseums to "Музеи", Str.ChipParks to "Парки",
    Str.Open to "Открыто",
    Str.Tagline to "Гид по Японии для самостоятельных путешественников",
    Str.TabLogin to "Вход", Str.TabRegister to "Регистрация",
    Str.FieldName to "Имя", Str.FieldEmail to "Email", Str.FieldPassword to "Пароль",
    Str.BtnLogin to "Войти", Str.BtnRegister to "Зарегистрироваться",
    Str.Forgot to "Забыли пароль?", Str.Or to "или", Str.ContinueGoogle to "Продолжить с Google",
    Str.NoAccount to "Нет аккаунта?", Str.SignUp to "Зарегистрироваться",
    Str.NoFavorites to "Пока ничего не добавлено в избранное",
    Str.NoRoute to "В маршруте пока нет мест",
    Str.AddToRoute to "+ Добавить в маршрут",
    Str.InRoute to "✓ В маршруте",
    Str.Settings to "Настройки",
    Str.InterfaceLang to "Язык интерфейса",
    Str.Notifications to "Уведомления",
    Str.Logout to "Выйти",
    Str.NothingFound to "Ничего не найдено",
    Str.NothingFoundHint to "Попробуйте изменить запрос или сбросить фильтр по категории.",
    Str.ResetFilters to "Сбросить фильтры",
    Str.WriteReview to "Написать отзыв",
    Str.RatePlace to "Оцените место",
    Str.CommentLabel to "Комментарий",
    Str.CommentHint to "Расскажите, что понравилось или что стоит знать другим туристам...",
    Str.PickRating to "Выберите оценку",
    Str.PublishReview to "Опубликовать отзыв",
    Str.EventsTitle to "События и фестивали",
    Str.PhrasebookTitle to "Разговорник",
    Str.NoEvents to "Событий пока нет",
    Str.NavHome to "Главная", Str.NavRoute to "Маршрут", Str.NavFav to "Избранное", Str.NavProfile to "Профиль"
)

private val enDict = mapOf(
    Str.Search to "Search places...",
    Str.ChipTemples to "Temples", Str.ChipFood to "Restaurants",
    Str.ChipMuseums to "Museums", Str.ChipParks to "Parks",
    Str.Open to "Open",
    Str.Tagline to "A guide to Japan for independent travelers",
    Str.TabLogin to "Login", Str.TabRegister to "Sign up",
    Str.FieldName to "Name", Str.FieldEmail to "Email", Str.FieldPassword to "Password",
    Str.BtnLogin to "Log in", Str.BtnRegister to "Sign up",
    Str.NoFavorites to "No favorites yet",
    Str.Forgot to "Forgot password?", Str.Or to "or", Str.ContinueGoogle to "Continue with Google",
    Str.NoAccount to "No account?", Str.SignUp to "Sign up",
    Str.NoRoute to "Your route is empty",
    Str.AddToRoute to "+ Add to route",
    Str.Settings to "Settings",
    Str.InterfaceLang to "Interface language",
    Str.PhrasebookTitle to "Phrasebook",
    Str.Notifications to "Notifications",
    Str.Logout to "Log out",
    Str.InRoute to "✓ In route",
    Str.WriteReview to "Write a review",
    Str.RatePlace to "Rate this place",
    Str.CommentLabel to "Comment",
    Str.CommentHint to "Tell other travelers what you liked or what to know...",
    Str.PickRating to "Please choose a rating",
    Str.PublishReview to "Publish review",
    Str.NothingFound to "Nothing found",
    Str.NothingFoundHint to "Try changing your search or clearing the category filter.",
    Str.ResetFilters to "Reset filters",
    Str.EventsTitle to "Events and festivals",
    Str.NoEvents to "No events yet",
    Str.NavHome to "Home", Str.NavRoute to "Route", Str.NavFav to "Favorites", Str.NavProfile to "Profile"
)

@Composable
fun tr(key: Str): String {
    val dict = if (LanguageState.current == "ru") ruDict else enDict
    return dict[key] ?: key.name
}