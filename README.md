# TAntiGuiDupe 

TAntiGuiDupe, Minecraft sunucuları için özel olarak geliştirilmiş, **sıfır işlemci yükü (zero-loop)** mantığıyla çalışan gelişmiş bir GUI ve Dupe (Eşya Kopyalama) korumasıdır. UIUtils, Meteor, Wurst ve Clumsy gibi programlarla yapılan tüm kopyalama açıklarını %100 oranında engeller.

**Geliştirici:** fuseheisen

## Özellikler

* **Optimizasyon (Zero-Loop):** Arka planda sunucuyu yoran döngüler (runTaskTimer) kullanmaz. Sadece hile tespiti yapıldığı o milisaniyede çalışır. Sunucunuzun TPS ve MSPT değerlerini asla etkilemez.
* **Delay Packets / Burst Koruması:** Paketleri bilgisayarda biriktirip aniden sunucuya göndererek yapılan eşya kopyalama girişimlerini (100ms içinde >25 paket) tespit eder ve işlemi felç eder.
* **Send Packets False (Dondurma) Koruması:** Oyuncu sunucuya paket göndermeyi 2.5 saniyeden fazla keserse, sistem bunu "Zaman Bükme/Dondurma" olarak algılar ve menüyü zorla kapatır.
* **Desync (Hayalet Eşya) Engellemesi:** İptal edilen her paketten sonra sistem oyuncunun envanterini anında zorla eşitler (Force Sync). Oyuncu sahte eşyayı faresinin ucunda tutamaz.
* **Fiziksel Kilit:** Korunan bir menü açıkken (Örn: Plugin menüleri), "Q" ile eşya atma veya "F" ile el değiştirme gibi arka plan fiziksel eylemlerini tamamen reddeder.

## Bağımlılıklar (Dependencies)
Bu eklenti paket yakalamak için **PacketEvents** kütüphanesini kullanır. Sunucunuzda PacketEvents'in güncel bir sürümünün yüklü olduğundan emin olun.

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

# TAntiGuiDupe 

TAntiGuiDupe is an advanced GUI and Dupe (Item Duplication) protection developed specifically for Minecraft servers, operating on a **zero-CPU load (zero-loop)** logic. It 100% blocks all duplication exploits performed via programs such as UIUtils, Meteor, Wurst, and Clumsy.

**Developer:** fuseheisen

## Features

* **Optimization (Zero-Loop):** Does not use background loops (runTaskTimer) that strain the server. It only runs in the exact millisecond a cheat is detected. It never affects your server's TPS and MSPT values.
* **Delay Packets / Burst Protection:** Detects item duplication attempts made by accumulating packets on the computer and suddenly sending them to the server (>25 packets within 100ms) and paralyzes the action.
* **Send Packets False (Freeze) Protection:** If a player stops sending packets to the server for more than 2.5 seconds, the system detects this as "Time Bending/Freezing" and forcefully closes the menu.
* **Desync (Ghost Item) Prevention:** After every canceled packet, the system instantly and forcefully synchronizes the player's inventory (Force Sync). The player cannot hold the fake item on their cursor.
* **Physical Lock:** While a protected menu is open (e.g., Plugin menus), it completely rejects background physical actions such as dropping items with "Q" or swapping hands with "F".

## Dependencies
This plugin uses the **PacketEvents** library to intercept packets. Ensure that an up-to-date version of PacketEvents is installed on your server.
