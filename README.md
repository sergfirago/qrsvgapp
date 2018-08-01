# QR SVG конвертер
QR &lt;-> SVG

Приложение позволяет конвертировать небольшие файлы [масштабируемой векторной графики](https://ru.wikipedia.org/wiki/SVG) в [QR](https://ru.wikipedia.org/wiki/QR-%D0%BA%D0%BE%D0%B4) код и обратно.

![Imgur](https://i.imgur.com/KpPTgOil.png)

Кнопки и действия:
- Import - Позволяет загрузить svg-файл по ссылке из интернета или по имени файла из директории Picture
```java
Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
```
После импорта рисунок показывается на экране:

![Imgur](https://i.imgur.com/3iUuHssl.png)

- Export - Показывает QR код соответствующий svg-файлу

![Imgur](https://i.imgur.com/AIozQaql.png) ![Imgur](https://i.imgur.com/nz67OQGl.png)


- Save - Сохраняет QR код в директории Picture в альбоме "qr"
- Read - Читает QR код и конвертирует его в векторную графику.

## Структура пакетов:
- ui - Activity и DialogFragment
1. MainActivity - главный экран
2. ScanActivity - работа с камерой, чтение QR кода
3. InputDialog - диалог для четние ссылки или имени файла
- svgload - AsyncTask для загрузки svg файла (интернет или из устройства)
- util 

## Используемые библиотеки:
 - 'com.caverock:androidsvg:1.2.1' - работа с векторной графикой
 - 'me.dm7.barcodescanner:zxing:1.9.8' - работа с сканером QR кодов
 - 'com.github.kenglxn.QRGen:android:2.4.0' - генерация QR кода
 - 'gun0912.ted:tedpermission:2.2.0' - работа с permissions (камера, и хранилище)
 
